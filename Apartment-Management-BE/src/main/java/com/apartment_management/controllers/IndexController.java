/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.services.FeedBackService;
import com.apartment_management.services.InvoiceService;
import com.apartment_management.services.LockerService;
import com.apartment_management.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ADMIN
 */
@Controller
@ControllerAdvice
public class IndexController {

    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private LockerService lockSer;
    
        @Autowired
    private InvoiceService invoiceSer;
            
        @Autowired
    private FeedBackService feedbackSer;
    
    @Autowired
    private UserService userSer;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("totalResidents", userSer.findByRole("RESIDENT").size());
         model.addAttribute("totalLocker", lockSer.findAllLockers().size());
           model.addAttribute("totalInvoice", invoiceSer.findAll().size());
           model.addAttribute("totalFeedback", feedbackSer.getAllFeedbacks().size());
           
        return "index";
    }

    @ModelAttribute("pageUrl")
    public String populatePageUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        if (uri.startsWith(contextPath)) {
            return uri.substring(contextPath.length());
        }
        return uri;
    }

}
