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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/register")
    public String register(
            @RequestParam(value = "floorId", required = false) Integer floorId,
            @RequestParam(value = "ajax", required = false) Boolean ajax,
            Model model) {

        if (floorId != null) {
            List<Room> rooms = roomService.getRoomsByFloorId(floorId);
            model.addAttribute("rooms", rooms);

            // Trả về fragment khi là AJAX
            if (ajax != null && ajax) {
                return "fragments :: roomDropdown";
            }
        }

        List<Floor> floors = floorService.getFloors();
        model.addAttribute("floors", floors);
        return "register";
    }

    @GetMapping(value = "/register/rooms", produces = "text/html")
    public String getRoomDropdown(@RequestParam("floorId") Integer floorId, Model model) {
        List<Room> rooms = roomService.getRoomsByFloorId(floorId);
        model.addAttribute("rooms", rooms);
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
            @RequestParam(value = "roomId", required = false) Integer roomId
    ) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole(role.toUpperCase());
        user.setFile(file);

        // Nếu là cư dân thì gán room
        if ("RESIDENT".equalsIgnoreCase(role) && roomId != null) {
            Room room = roomService.getRoomById(roomId);
            user.setRoomId(room);
        }

        userSer.addUser(user, file);
        return "redirect:/login";
    }

    @GetMapping("/manage-user")
    public String manageUserView(@RequestParam Map<String, String> params, Model model) {
        List<User> users = userSer.getUsers(params);
        model.addAttribute("users", users);
        model.addAttribute("params", params);
        return "manage_user";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        boolean temp = userSer.deleteUser(id);
        if (temp == true) {
            return "redirect:/manage-user";  // quay lại trang quản lý
        }
        return "Không thể xóa";
    }
}
