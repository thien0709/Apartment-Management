/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.dto.reponse.FeedbackDTO;
import com.apartment_management.pojo.Feedback;
import com.apartment_management.services.FeedBackService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author thien
 */
@RestController
@RequestMapping("/api")
public class ApiFeedbackController {

    @Autowired
    private FeedBackService feedbackService;

    @PostMapping("/feedback")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FeedbackDTO> createFeedback(@RequestBody Map<String, Object> payload) {
        int userId = (Integer) payload.get("userId");
        String content = (String) payload.get("content");
        Feedback feedback = this.feedbackService.createFeedback(userId, content);

        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setContent(feedback.getContent());
        dto.setCreatedAt(feedback.getCreatedAt());
        dto.setUsername(feedback.getUserId().getUsername());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/feedback/{userId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByUserId(@PathVariable("userId") int userId) {
        List<Feedback> feedbackList = feedbackService.getFeedbacksByUserId(userId);

        if (feedbackList != null && !feedbackList.isEmpty()) {
            List<FeedbackDTO> feedbackDTOList = feedbackList.stream().map(feedback -> {
                FeedbackDTO dto = new FeedbackDTO();
                dto.setId(feedback.getId());
                dto.setContent(feedback.getContent());
                dto.setCreatedAt(feedback.getCreatedAt());
                dto.setUsername(feedback.getUserId().getUsername());
                return dto;
            }).collect(Collectors.toList());

            return new ResponseEntity<>(feedbackDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/feedback/{feedbackId}")
    public Feedback updateFeedBack(
            @PathVariable(value = "feedbackId") int feedbackId,
            @RequestBody String content) {
        return this.feedbackService.updateFeedback(feedbackId, content);
    }
    
      @DeleteMapping("/feedback/{feedbackId}")
    public void deleteFeedBack(
            @PathVariable(value = "feedbackId") int feedbackId
            ) {
         this.feedbackService.deleteFeedback(feedbackId);
    }
}
