package com.capgemini.hms.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String remoteAddr = request.getRemoteAddr();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();

            logger.info("INCOMING_REQUEST: method={}, uri={}, status={}, duration={}ms, client_ip={}",
                    method, uri, status, duration, remoteAddr);
            

            if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE")) {
                logger.warn("AUDIT_LOG: Sensitive action performed on URI: {} | Status: {}", uri, status);
            }
        }
    }
}
