/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.pojo.User;
import com.apartment_management.services.UserService;
import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */
@Controller
public class StatisticsController {
    @Autowired
    private UserService userService;

    // Hiển thị trang thống kê
    @GetMapping("/statistics")
    public String showStatistics(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.getUserByUserName(principal.getName());
        if (user == null) {
            model.addAttribute("error", "Không tìm thấy thông tin người dùng.");
            return "statistics";
        }

        if (!"ADMIN".equals(user.getRole())) {
            model.addAttribute("error", "Bạn không có quyền xem thống kê.");
            return "statistics";
        }

        return "statistics";
    }

    // API cung cấp dữ liệu cho Chart.js
    @GetMapping("/api/statistics/users")
    public ResponseEntity<Map<String, Long>> getUserStatistics(
            @RequestParam("period") String period,
            @RequestParam(value = "year", defaultValue = "2025") int year,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.getUserByUserName(principal.getName());
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).build();
        }

        try {
            Map<String, Long> stats = userService.getUserStatsByPeriod(period, year);
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
