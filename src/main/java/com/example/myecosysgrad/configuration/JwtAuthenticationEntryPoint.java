package com.example.myecosysgrad.configuration;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.example.myecosysgrad.dto.api.request.ApiResponse;
import com.example.myecosysgrad.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

// xử lý 401 Unauthorized
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException { // commence xảy ra khi có 1 exception (authen không thành
        // công,...) xảy ra trong
        // quá trình authentication

        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter()
                .write(objectMapper.writeValueAsString(apiResponse)); // Viết nội dung của apiResponse vào response

        response.flushBuffer();

        //        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
