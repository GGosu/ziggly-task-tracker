package com.ziggly.model.utils;

import jakarta.servlet.http.HttpServletRequest;

public class Utils {
    public static Integer getUserId(HttpServletRequest request) {
        String userId = request.getHeader("X-USER-ID");
        try {
            return Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
