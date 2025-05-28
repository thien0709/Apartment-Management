/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.controllers;

import com.apartment_management.dto.reponse.AnswerResponse;
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

    @GetMapping("/surveys")
    public ResponseEntity<List<Survey>> getAllSurvey() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<Question>> getQuestionsBySurveyId(@PathVariable("surveyId") int surveyId) {
        return ResponseEntity.ok(questionService.getQuestionsBySurveyId(surveyId));
    }

    @PostMapping("/survey/{surveyId}")
    public ResponseEntity<String> submitSurveyResponses(
            @PathVariable("surveyId") int surveyId,
            @RequestBody List<AnswerResponse> responses) {
        try {
            for (AnswerResponse r : responses) {
                responseService.submitResponse(r.getAnswer(), r.getQuestionId(), r.getUserId());
            }
            return ResponseEntity.ok("Responses submitted successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to submit responses.");
        }
    }

}
