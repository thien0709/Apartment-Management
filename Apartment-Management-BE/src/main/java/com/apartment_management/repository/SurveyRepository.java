/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repository;

import com.apartment_management.pojo.Question;
import com.apartment_management.pojo.Response;
import com.apartment_management.pojo.Survey;
import java.util.List;
import java.util.Map;

/**
 *
 * @author thien
 */
public interface SurveyRepository {
   List<Survey> getAllSurveys();
    Survey getSurveyById(int id);
    void createSurvey(Survey survey);
    void updateSurvey(Survey survey);
    void deleteSurvey(int id);
}
