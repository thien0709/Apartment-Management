/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.pojo.Floor;
import com.apartment_management.pojo.Room;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.apartment_management.pojo.User;
import com.apartment_management.services.FloorService;
import com.apartment_management.services.RoomService;
import com.apartment_management.services.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author thien
 */
@Controller
public class UserController {

    @Autowired
    UserService userSer;
    @Autowired
    FloorService floorService;
    @Autowired
    RoomService roomService;
    @Autowired
    HttpSession session;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/register")
    public String register(
            @RequestParam(value = "floorId", required = false) Integer floorId,
            HttpSession session,
            Model model) {
        System.out.println("Processing /register with floorId: " + floorId);
        // Lấy danh sách tầng
        List<Floor> floors = floorService.getFloors();
        model.addAttribute("floors", floors);

        // Lấy danh sách phòng nếu có floorId
        if (floorId != null && floorId > 0) {
            try {
                List<Room> rooms = roomService.getRoomsByFloorId(floorId);
                model.addAttribute("rooms", rooms);
                model.addAttribute("selectedFloorId", floorId);
                System.out.println("Loaded " + rooms.size() + " rooms for floorId: " + floorId);
            } catch (Exception e) {
                System.err.println("Error loading rooms for floorId: " + floorId + ", Error: " + e.getMessage());
                model.addAttribute("rooms", Collections.emptyList());
            }
        } else {
            model.addAttribute("rooms", Collections.emptyList());
        }

        // Lấy trạng thái form từ session
        @SuppressWarnings("unchecked")
        Map<String, String> formData = (Map<String, String>) session.getAttribute("registerFormData");
        if (formData != null) {
            model.addAttribute("username", formData.getOrDefault("username", ""));
            model.addAttribute("fullName", formData.getOrDefault("fullName", ""));
            model.addAttribute("phone", formData.getOrDefault("phone", ""));
            model.addAttribute("email", formData.getOrDefault("email", ""));
            model.addAttribute("role", formData.getOrDefault("role", ""));
        } else {
            model.addAttribute("username", "");
            model.addAttribute("fullName", "");
            model.addAttribute("phone", "");
            model.addAttribute("email", "");
            model.addAttribute("role", "");
        }

        return "register";
    }

    @GetMapping(value = "/register/rooms", produces = MediaType.TEXT_HTML_VALUE)
    public String getRoomDropdown(@RequestParam("floorId") Integer floorId, Model model) {
        System.out.println("Fetching rooms for floorId: " + floorId);
        try {
            if (floorId == null || floorId <= 0) {
                System.out.println("Invalid floorId: " + floorId);
                model.addAttribute("rooms", Collections.emptyList());
            } else {
                List<Room> rooms = roomService.getRoomsByFloorId(floorId);
                model.addAttribute("rooms", rooms);
                System.out.println("Loaded " + rooms.size() + " rooms for floorId: " + floorId);
            }
        } catch (Exception e) {
            System.err.println("Error in getRoomDropdown for floorId: " + floorId + ", Error: " + e.getMessage());
            model.addAttribute("rooms", Collections.emptyList());
        }
        return "fragments :: roomDropdown";
    }

    @PostMapping("/register")
    public String registerSubmit(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("role") String role,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "roomId", required = false) Integer roomId,
            HttpSession session
    ) {
        System.out.println("Processing /register POST with username: " + username);
        // Lưu trạng thái form vào session
        Map<String, String> formData = new HashMap<>();
        formData.put("username", username);
        formData.put("fullName", fullName);
        formData.put("phone", phone);
        formData.put("email", email);
        formData.put("role", role);
        session.setAttribute("registerFormData", formData);

        // Xử lý đăng ký
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole(role.toUpperCase());
        user.setFile(file);

        if ("RESIDENT".equalsIgnoreCase(role) && roomId != null) {
            Room room = roomService.getRoomById(roomId);
            user.setRoomId(room);
        }

        userSer.addUser(user, file);

        // Xóa trạng thái form sau khi đăng ký thành công
        session.removeAttribute("registerFormData");
        return "redirect:/login";
    }

    @GetMapping("/manage-user")
    public String manageUserView(@RequestParam Map<String, String> params, Model model) {
        List<User> users = userSer.getUsers(params);
        model.addAttribute("users", users);
        model.addAttribute("params", params);
        return "manage_user";
    }

    @PostMapping("/manage-user/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        boolean temp = userSer.deleteUser(id);
        if (temp == true) {
            return "redirect:/manage-user";  // quay lại trang quản lý
        }
        return "Không thể xóa";
    }
    @PostMapping("/manage-user/block-user/{id}")
    public String blockUser(@PathVariable("id") int id) {
        User temp = userSer.blockUser(id);
        if (temp != null) {
            return "redirect:/manage-user";  // quay lại trang quản lý
        }
        return "Không thể khóa tài khoản";
    }
}
