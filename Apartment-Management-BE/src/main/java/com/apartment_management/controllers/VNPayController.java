package com.apartment_management.controllers;

import com.apartment_management.services.InvoiceService;
import com.apartment_management.services.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/payment")
public class VNPayController {

    @Autowired
    private VNPayService vnpayService;

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/vnpay")
    public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestBody Map<String, Object> paymentRequest) {
        String returnUrl = "http://localhost:8080";
        String response = vnpayService.createOrder(request, paymentRequest, "Thanh toan don hang", returnUrl);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/pay-invoice/{invoiceId}")
//    public ResponseEntity<?> payInvoice(@PathVariable Long invoiceId, HttpServletRequest request) {
//        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
//        if (invoice == null || invoice.getStatus() == "UNPAID") {
//            return ResponseEntity.badRequest().body("Hóa đơn không hợp lệ hoặc đã thanh toán");
//        }
//
//        int amount = invoice.getTotalAmount().intValue();
//
//        String returnUrl = "http://localhost:8080";
//
//        String response = vnpayService.createOrder(request, amount, "Thanh toan hoa don " + invoiceId, returnUrl);
//
//        return ResponseEntity.ok(response);
//    }
    @GetMapping("/vnpay-payment-return")
    public String returnPayment(HttpServletRequest request) {
        int result = vnpayService.orderReturn(request);
        String orderId = request.getParameter("vnp_TxnRef");
        String responseCode = request.getParameter("vnp_ResponseCode");

        System.out.println("result: " + result);
        System.out.println("orderId: " + orderId);

        request.getParameterMap().forEach((k, v) -> System.out.println(k + ": " + Arrays.toString(v)));

        if ("00".equals(responseCode)) {
            try {
                List<Integer> invoiceIds = Arrays.stream(orderId.split("-"))
                        .map(Integer::parseInt)
                        .toList();

                invoiceService.updatePaymentInfo(invoiceIds, "CASH", null);
                System.out.println("sucess");

                return "redirect:http://localhost:3000/payment?status=success";
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:http://localhost:3000/payment?error=invalid_invoice_id";
            }
        } else if ("24".equals(responseCode)) {
            return "redirect:http://localhost:3000/payment?status=cancel";
        } else {
            return "redirect:http://localhost:3000/payment?status=fail";
        }
    }
}
