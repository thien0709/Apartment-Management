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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

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
    public void updateStatusToPaid(List<Integer> invoiceIds) {
        for (Integer id : invoiceIds) {
            Invoice invoice = invoiceRepository.findById(id); 
            if (invoice != null) {
                invoiceRepository.updateStatusToPaid(invoice); 
            }
        }
    }

}
