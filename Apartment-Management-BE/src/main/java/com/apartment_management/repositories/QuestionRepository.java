/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repositories;

import com.apartment_management.pojo.Question;
import java.util.List;

/**
 *
 * @author thien
 */
public interface QuestionRepository {
     List<Question> getQuestionsBySurveyId(int surveyId);
    Question getQuestionById(int id);
    void addQuestion(Question question);
    void updateQuestion(Question question);
    void deleteQuestion(int id);
}
