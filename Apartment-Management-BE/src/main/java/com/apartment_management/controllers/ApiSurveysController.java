/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.pojo.Question;
import com.apartment_management.pojo.Response;
import com.apartment_management.pojo.Survey;
import com.apartment_management.services.QuestionService;
import com.apartment_management.services.ResponseService;
import com.apartment_management.services.SurveyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author thien
 */
@RestController
@RequestMapping("/api")
public class ApiSurveysController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private ResponseService responseService;

    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<Question>> getQuestionsBySurveyId(@PathVariable int surveyId) {
        return ResponseEntity.ok(questionService.getQuestionsBySurveyId(surveyId));
    }

    @PostMapping("/survey/{surveyId}/submit")
    public ResponseEntity<String> submitSurveyResponses(
            @PathVariable int surveyId,
            @RequestBody List<Response> responses) {

        try {
            for (Response r : responses) {
                int questionId = r.getQuestionId().getId();
                int userId = r.getUserId().getId();
                String answer = r.getAnswer();
                responseService.submitResponse(answer, questionId, userId);
            }
            return ResponseEntity.ok("Responses submitted successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to submit responses.");
        }
    }

}
