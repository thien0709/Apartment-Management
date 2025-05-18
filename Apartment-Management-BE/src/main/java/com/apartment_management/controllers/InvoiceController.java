/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.pojo.DetailInvoice;
import com.apartment_management.pojo.Feed;
import com.apartment_management.pojo.Invoice;
import com.apartment_management.pojo.User;
import com.apartment_management.services.DetailInvoiceService;
import com.apartment_management.services.FeedService;
import com.apartment_management.services.InvoiceService;
import com.apartment_management.services.UserService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ADMIN
 */
@Controller
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserService userService;

    @Autowired
    private FeedService feedService;

    @GetMapping("/invoice/create")
    public String showCreateInvoiceForm(Model model) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("users", userService.findByRole("RESIDENT"));
        model.addAttribute("fees", feedService.getAllFeeds());
        return "create-invoice";
    }

    @PostMapping("/invoice/create")
    public String createInvoice(
            @Valid Invoice invoice,
            BindingResult bindingResult,
            @RequestParam(value = "feedId", required = false) Integer feedId,
            @RequestParam(value = "amount", required = false) String amountStr,
            @RequestParam(value = "note", required = false) String note,
            RedirectAttributes redirectAttributes) {
        // Kiểm tra lỗi binding
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Dữ liệu không hợp lệ: " + bindingResult.getAllErrors());
            return "redirect:/invoice/create";
        }

        // Kiểm tra feedId
        if (feedId == null || feedId <= 0) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn một loại phí hợp lệ.");
            return "redirect:/invoice/create";
        }

        // Kiểm tra và chuyển đổi amount
        BigDecimal amount = null;
        try {
            if (amountStr != null && !amountStr.trim().isEmpty()) {
                amount = new BigDecimal(amountStr);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    redirectAttributes.addFlashAttribute("error", "Số tiền phải lớn hơn 0.");
                    return "redirect:/invoice/create";
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Vui lòng nhập số tiền.");
                return "redirect:/invoice/create";
            }
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Số tiền không hợp lệ. Vui lòng nhập số hợp lệ.");
            return "redirect:/invoice/create";
        }

        // Kiểm tra người dùng
        User user = userService.getUserById(invoice.getUserId().getId());
        if (user == null || !user.getRole().equals("RESIDENT")) {
            redirectAttributes.addFlashAttribute("error", "Người dùng không hợp lệ hoặc không phải cư dân.");
            return "redirect:/invoice/create";
        }
        invoice.setUserId(user);

        // Kiểm tra ngày hết hạn
        if (invoice.getDueDate() == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn ngày hết hạn.");
            return "redirect:/invoice/create";
        }

        // Thiết lập ngày phát hành
        invoice.setIssuedDate(new Date());

        // Tạo chi tiết hóa đơn
        Set<DetailInvoice> detailInvoices = new HashSet<>();
        DetailInvoice detail = new DetailInvoice();
        Feed feed = new Feed();
        feed.setId(feedId);
        detail.setFeedId(feed);
        detail.setAmount(amount);
        detail.setNote(note != null && !note.trim().isEmpty() ? note : null);
        detailInvoices.add(detail);

        // Thiết lập tổng số tiền
        invoice.setTotalAmount(amount);
        invoiceService.createInvoice(invoice, detailInvoices);

        redirectAttributes.addFlashAttribute("success", "Hóa đơn đã được tạo thành công!");
        return "redirect:/invoice/create";
    }
}
