/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.pojo.User;
import com.apartment_management.pojo.Package;
import com.apartment_management.services.EmailService;
import com.apartment_management.services.LockerService;
import com.apartment_management.services.PackageService;
import com.apartment_management.services.UserService;
import jakarta.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */
@Controller
@RequestMapping("/receive-package")
public class LockerController {

    @Autowired
    private UserService userService;

    @Autowired
    private LockerService lockerService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private EmailService emailService;

    // Hiển thị trang quản lý tủ đồ
    @GetMapping
    public String showLockerManagement(Model model) {
        Map<String, String> params = new HashMap<>();
        params.put("role", "RESIDENT");
        List<User> residents = userService.getUsers(params);
        model.addAttribute("residents", residents);
        return "manage_locker";
    }

    // Xử lý thêm gói hàng mới
    @PostMapping("/add-package")
    public String addPackage(@RequestParam("residentId") int residentId,
            @RequestParam("packageName") String packageName,
            Model model) {
        try {
            Package newPackage = packageService.createPackage(packageName, residentId);
            User resident = userService.getUserById(residentId);

            if (resident != null && resident.getEmail() != null && !resident.getEmail().isEmpty()) {
                emailService.sendNewPackageNotification(
                        resident.getEmail(),
                        packageName,
                        newPackage.getCreatedAt()
                );
                model.addAttribute("successMessage", "Thêm gói hàng và gửi email thông báo thành công!");
            } else {
                model.addAttribute("successMessage", "Thêm gói hàng thành công nhưng không gửi được email: Email cư dân không hợp lệ.");
            }
        } catch (MessagingException e) {
            model.addAttribute("errorMessage", "Thêm gói hàng thành công nhưng lỗi khi gửi email: " + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi thêm gói hàng: " + e.getMessage());
        }
        return showLockerManagement(model);
    }

    // Hiển thị danh sách gói hàng của cư dân
    @GetMapping("/packages/{userId}")
    public String showPackages(@PathVariable("userId") int userId,
            @RequestParam(value = "status", required = false) String status,
            Model model) {
        List<Package> packages;
        if (status != null && !status.isEmpty()) {
            // Chuyển đổi status từ tiếng Việt sang ENUM
            String enumStatus = status.equals("Chờ nhận") ? "PENDING" : "RECEIVED";
            packages = packageService.findByUserIdAndStatus(userId, enumStatus);
        } else {
            packages = packageService.findByUserId(userId);
        }

        User resident = userService.getUserById(userId);
        Map<String, String> params = new HashMap<>();
        params.put("role", "RESIDENT");
        List<User> residents = userService.getUsers(params);

        model.addAttribute("residents", residents);
        model.addAttribute("packages", packages);
        model.addAttribute("selectedResident", resident);
        model.addAttribute("selectedStatus", status);
        return "manage_locker";
    }

    // Cập nhật trạng thái gói hàng
    @PostMapping("/packages/{packageId}/update-status")
    public String updatePackageStatus(@PathVariable("packageId") int packageId,
            @RequestParam("status") String status,
            @RequestParam("userId") int userId,
            Model model) {
        try {
            // Chuyển đổi status từ tiếng Việt sang ENUM
            String enumStatus = status.equals("Chờ nhận") ? "PENDING" : "RECEIVED";
            packageService.updatePackageStatus(packageId, enumStatus);
            model.addAttribute("successMessage", "Cập nhật trạng thái gói hàng thành công!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }
        return "redirect:/receive-package/packages/" + userId;
    }

    // Xóa gói hàng
    @PostMapping("/packages/{packageId}/delete")
    public String deletePackage(@PathVariable("packageId") int packageId,
            @RequestParam("userId") int userId,
            Model model) {
        try {
            packageService.deletePackage(packageId);
            model.addAttribute("successMessage", "Xóa gói hàng thành công!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi xóa gói hàng: " + e.getMessage());
        }
        return "redirect:/receive-package/packages/" + userId;
    }
}
