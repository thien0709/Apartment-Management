/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.pojo.Card;
import com.apartment_management.pojo.User;
import com.apartment_management.services.CardService;
import com.apartment_management.services.UserService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ADMIN
 */
@Controller
@RequestMapping("/card")
public class CardController {
    @Autowired
    private CardService cardSer;
    @Autowired
    private UserService userSer;

    // Hiển thị danh sách thẻ xe của người dùng
    @GetMapping("/list-card")
    public String listCards(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        // Lấy thông tin người dùng hiện tại
        User user = userSer.getUserByUserName(principal.getName());
        if (user == null) {
            model.addAttribute("error", "Không tìm thấy thông tin người dùng.");
            return "cards";
        }

        // Kiểm tra vai trò ADMIN
        if (!"ADMIN".equals(user.getRole())) {
            model.addAttribute("error", "Bạn không có quyền xem danh sách tất cả thẻ xe.");
            return "cards";
        }

        // Lấy tất cả thẻ xe
        List<Card> cards = cardSer.getAllCards();
        model.addAttribute("cards", cards != null ? cards : List.of());

        // Lấy danh sách người dùng cho form thêm thẻ
        List<User> users = userSer.getUsers(null);
        model.addAttribute("users", users != null ? users : List.of());
        model.addAttribute("newCard", new Card());

        return "cards";
    }
    // Thêm thẻ xe mới
    @PostMapping("/add")
    public String addCard(@ModelAttribute("newCard") Card card, BindingResult result, 
                         Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        // Lấy thông tin người dùng hiện tại
        User currentUser = userSer.getUserByUserName(principal.getName());
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin người dùng.");
            return "redirect:/card/list-card";
        }

        // Kiểm tra vai trò ADMIN
        if (!"ADMIN".equals(currentUser.getRole())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thêm thẻ xe.");
            return "redirect:/card/list";
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Dữ liệu nhập không hợp lệ.");
            return "redirect:/card/list-card";
        }

        // Kiểm tra userId
        User user = userSer.getUserById(card.getUserId().getId());
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại.");
            return "redirect:/card/list";
        }

        card.setUserId(user);
        Card createdCard = cardSer.addCard(card);
        if (createdCard != null) {
            redirectAttributes.addFlashAttribute("success", "Thêm thẻ xe thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không thể thêm thẻ xe. Vui lòng thử lại.");
        }

        return "redirect:/card/list-card";
    }
    // Xóa thẻ xe
    @GetMapping("/delete/{cardId}")
    public String deleteCard(@PathVariable("cardId") int cardId, Principal principal, 
                            RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        // Lấy thông tin người dùng hiện tại
        User currentUser = userSer.getUserByUserName(principal.getName());
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin người dùng.");
            return "redirect:/card/list-card";
        }

        // Kiểm tra vai trò ADMIN
        if (!"ADMIN".equals(currentUser.getRole())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền xóa thẻ xe.");
            return "redirect:/card/list-card";
        }

        // Xóa thẻ xe
        boolean deleted = cardSer.deleteCard(cardId);
        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "Xóa thẻ xe thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thẻ xe hoặc không thể xóa.");
        }

        return "redirect:/card/list-card";
    }
}
