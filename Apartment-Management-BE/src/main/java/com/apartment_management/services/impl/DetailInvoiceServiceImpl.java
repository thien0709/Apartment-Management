/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.DetailInvoice;
import com.apartment_management.repositories.DetailInvoiceRepository;
import com.apartment_management.services.DetailInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class DetailInvoiceServiceImpl implements DetailInvoiceService{
    @Autowired
    private DetailInvoiceRepository detailInvoiceRepository;

    @Override
    public void createDetailInvoice(DetailInvoice detailInvoice) {
        detailInvoiceRepository.createDetailInvoice(detailInvoice);
    }
}
