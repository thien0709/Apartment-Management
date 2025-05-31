/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.services;

import com.apartment_management.pojo.Question;
import java.util.List;

/**
 *
 * @author thien
 */
public interface QuestionService {

    List<Question> getQuestionsBySurveyId(int surveyId);

    Question getQuestionById(int id);

    void addQuestion(String content, int surveyId);

    void updateQuestion(int questionId, String content);

    void deleteQuestion(int id);
}
