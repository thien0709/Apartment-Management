/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.pojo.Invoice;
import com.apartment_management.services.InvoiceService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author thien
 */
@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class ApiPaymentsController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/banking")
    public ResponseEntity<?> createPayment(
            @RequestParam("invoiceId") List<Integer> invoiceIds,
            @RequestParam("method") String method,
            @RequestParam(value = "file", required = false) MultipartFile proofImage
    ) {
        try {
            if (invoiceIds == null || invoiceIds.isEmpty() || method == null || method.isEmpty()) {
                return ResponseEntity.badRequest().body("Thiếu dữ liệu hóa đơn hoặc phương thức thanh toán.");
            }

            invoiceService.updatePaymentInfo(invoiceIds, method, proofImage);

            return ResponseEntity.ok("Cập nhật phương thức thanh toán thành công.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xử lý thanh toán.");
        }
    }

}
