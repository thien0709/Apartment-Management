/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.dto.request;

import java.util.List;

/**
 *
 * @author thien
 */
public class PaymentRequest {
    private List<Long> items;
    private int amount;

    public PaymentRequest(List<Long> items, int amount) {
        this.items = items;
        this.amount = amount;
    }

    public PaymentRequest() {
    }
    
    // getters and setters

    /**
     * @return the items
     */
    public List<Long> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<Long> items) {
        this.items = items;
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
