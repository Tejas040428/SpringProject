package com.tnqsoftware.customer_mapping_to_user.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler
{
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse httpServletResponse,
                                        AuthenticationException ex) throws IOException, ServletException
    {
        Map<String,Object> response = new HashMap<>();
        response.put("status",HttpStatus.UNAUTHORIZED);
        response.put("message","Unauthorized access");

        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(out, response);
        out.flush();
    }
}
