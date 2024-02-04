package com.ziggly.model.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.mockito.Mockito;

public class Utils {
    public static Integer getUserId(HttpServletRequest request) {
        String userId = request.getHeader("X-USER-ID");
        try {
            return Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static HttpServletRequest getMockupRequest(){
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("X-USER-ID")).thenReturn("123");
        return request;
    }
    public static HttpServletRequest getMockupRequest(String id){
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("X-USER-ID")).thenReturn(id);
        return request;
    }

}
