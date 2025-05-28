/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.apartment_management.repositories;

import com.apartment_management.pojo.Response;
import java.util.List;

/**
 *
 * @author thien
 */
public interface ResponseRepository {

    void submitResponse(Response response);

    List<Response> getResponses(Integer surveyId, Integer questionId, Integer userId);
      List<Response> findByQuestionId(Integer questionId);
}
