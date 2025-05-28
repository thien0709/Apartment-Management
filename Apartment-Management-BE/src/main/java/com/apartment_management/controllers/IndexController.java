/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
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

    @RequestMapping("/")
    public String index(Locale locale) {
        String title = messageSource.getMessage("app.title", null, locale);
        System.out.println("Title: " + title + " | Locale: " + locale);
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
