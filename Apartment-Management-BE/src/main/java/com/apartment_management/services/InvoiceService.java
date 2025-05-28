/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.services;

import com.apartment_management.pojo.DetailInvoice;
import com.apartment_management.pojo.Invoice;
import java.util.List;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
public interface InvoiceService {

    Invoice createInvoice(Invoice invoice, Set<DetailInvoice> detailInvoices);

    List<Invoice> findAll();

    Invoice findById(Integer id);

    List<Invoice> getInvoicesByUserId(int userId);

    void updatePaymentInfo(List<Integer> invoiceIds, String method, MultipartFile proofImage);

    boolean deleteInvoice(Integer id);

}
