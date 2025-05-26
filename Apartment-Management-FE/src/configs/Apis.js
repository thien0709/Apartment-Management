import axios from "axios";
import cookie from 'react-cookies'

const BASE_URL = 'http://localhost:8080/apartment_management/api/';

export const endpoints = {
    'login': '/login',
    'admins': '/admins',
    'current-user': '/secure/profile',
    'change-password': (userId) => `users/${userId}/change_password`,
    'updateAvatar': (userId) => `/users/${userId}/update_avatar`,
    'invoices': (userId) => `/invoices/${userId}`,
    'payment-vnpay': '/payment/vnpay',
    'payment-banking': '/payment/banking',
    'feedback': '/feedback',
    'feedbacks': (userId) => `/feedback/${userId}`,
    'edit-feedback': (feedbackId) => `/feedback/${feedbackId}`,
    'surveys': '/surveys',
    'survey-detail': (surveyId) => `/survey/${surveyId}`,

}

export const authApis = (token = null) => {
    if (!token) {
        token = cookie.load('token');
    }

    return axios.create({
        baseURL: BASE_URL,
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
};

export default axios.create({
    baseURL: BASE_URL
});