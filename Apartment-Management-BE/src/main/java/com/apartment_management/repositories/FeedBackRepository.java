/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repositories;
import com.apartment_management.pojo.Feedback;
import com.apartment_management.pojo.User;
import java.util.List;

/**
 *
 * @author thien
 */
public interface FeedBackRepository {

    List<Feedback> getAllFeedbacks();

    List<Feedback> getFeedbacksByUserId(User u);

    Feedback getFeedBackById(int feedbackId);

    Feedback createFeedback(Feedback feedback);

    Feedback updateFeedback(Feedback feedback);

    Feedback updateFeedbackStatus(Feedback feedback);
    
    void deleteFeedback (Feedback feedback);
}
