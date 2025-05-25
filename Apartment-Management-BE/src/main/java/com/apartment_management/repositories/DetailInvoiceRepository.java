/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repositories;

import com.apartment_management.pojo.DetailInvoice;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface DetailInvoiceRepository {
    void createDetailInvoice(DetailInvoice detailInvoice);
    List<DetailInvoice> findAll();
    List<DetailInvoice> findByInvoiceId(Integer invoiceId);
    
}
