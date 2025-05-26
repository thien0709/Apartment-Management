/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.DetailInvoice;
import com.apartment_management.pojo.Invoice;
import com.apartment_management.repositories.DetailInvoiceRepository;
import com.apartment_management.repositories.InvoiceRepository;
import com.apartment_management.services.InvoiceService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private DetailInvoiceRepository detailInvoiceRepository;

    @Override
    public Invoice createInvoice(Invoice invoice, Set<DetailInvoice> detailInvoices) {
        // Đặt ngày phát hành là ngày hiện tại nếu chưa có
        if (invoice.getIssuedDate() == null) {
            invoice.setIssuedDate(new Date());
        }

        // Đặt trạng thái mặc định là UNPAID
        invoice.setStatus("UNPAID");
        invoice.setPaymentMethod(null);

        // Lưu hóa đơn
        Invoice savedInvoice = invoiceRepository.createInvoice(invoice);

        // Lưu chi tiết hóa đơn
        for (DetailInvoice detail : detailInvoices) {
            detail.setInvoiceId(savedInvoice);
            detailInvoiceRepository.createDetailInvoice(detail);
        }

        savedInvoice.setDetailInvoiceSet(detailInvoices);
        return savedInvoice;
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice findById(Integer id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public boolean deleteInvoice(Integer id) {
        Invoice invoice = invoiceRepository.findById(id);
        if (invoice != null) {
            if ("PAID".equals(invoice.getStatus())) {
                System.out.println("Cannot delete paid invoice ID: " + id);
                return false;
            }
            invoiceRepository.deleteInvoice(id);
            System.out.println("Deleted invoice ID: " + id);
            return true;
        }
        System.out.println("Invoice ID: " + id + " not found");
        return false;
    }

    @Override
    public List<Invoice> getInvoicesByUserId(int userId) {
        return invoiceRepository.getInvoicesByUserId(userId);
    }

    @Override
    public void updatePaymentInfo(List<Integer> invoiceIds, String method, MultipartFile proofImage) {
        String proofUrl = null;

        // Upload ảnh nếu có
        if (proofImage != null && !proofImage.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(proofImage.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                proofUrl = res.get("secure_url").toString();
            } catch (IOException ex) {
                throw new RuntimeException("Lỗi upload ảnh thanh toán");
            }
        }

        // Gọi repository để cập nhật
        for (Integer id : invoiceIds) {
            Invoice invoice = invoiceRepository.findById(id);
            if (invoice != null) {
                invoiceRepository.updatePaymentInfo(invoice, method, proofUrl);
            }
        }
    }

}
