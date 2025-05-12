/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.services.impl;

import com.apartment_management.pojo.Survey;
import com.apartment_management.repositories.SurveyRepository;
import com.apartment_management.repositories.UserRepository;
import com.apartment_management.services.SurveyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author thien
 */
@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public List<Survey> getAllSurveys() {
        return surveyRepository.getAllSurveys();
    }

    @Override
    public Survey getSurveyById(int id) {
        return surveyRepository.getSurveyById(id);
    }

    @Override
    public void createSurvey() {
        Survey s = new Survey();
        thisurveyRepository.createSurvey(s);
    }

    @Override
    public void updateSurvey(Survey survey) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteSurvey(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
