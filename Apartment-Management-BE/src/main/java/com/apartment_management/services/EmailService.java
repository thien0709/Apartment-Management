/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.services;

import jakarta.mail.MessagingException;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public interface EmailService {
    void sendNewPackageNotification(String recipientEmail, String packageName, Date createdAt) throws MessagingException;
}
