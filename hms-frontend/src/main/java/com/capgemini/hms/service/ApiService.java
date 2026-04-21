package com.capgemini.hms.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApiService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ApiService.class);
    private static final List<String> FORWARDED_COOKIE_NAMES = Arrays.asList("JSESSIONID", "XSRF-TOKEN");

    private final RestClient restClient;

    public ApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Value("${hms.backend.url}")
    private String backendUrl;

    /**
     * Executes a proxied request to the backend with automatic cookie forwarding.
     */
    public ResponseEntity<Object> proxyRequest(String method, String path, Map<String, Object> body) {
        boolean queryOnlyRequest = HttpMethod.GET.matches(method) || HttpMethod.DELETE.matches(method);
        String targetUrl = buildTargetUrl(path, queryOnlyRequest ? body : null);
        log.info("Executing Proxied {} request to: {}", method, targetUrl);

        // 1. Get current HttpServletRequest from Context
        HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 2. Extract Cookie header
        String cookies = extractProxyCookies(currentRequest.getHeader(HttpHeaders.COOKIE));

        // 3. Prepare Backend Request Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (cookies != null) {
            headers.set(HttpHeaders.COOKIE, cookies);
            log.debug("Forwarding Cookies to backend: {}", cookies);
        }
        String csrfHeader = currentRequest.getHeader("X-XSRF-TOKEN");
        if (csrfHeader != null && !csrfHeader.isBlank()) {
            headers.set("X-XSRF-TOKEN", csrfHeader);
        }

        try {
            return executeRequest(targetUrl, HttpMethod.valueOf(method.toUpperCase()), headers, queryOnlyRequest ? null : body);
        } catch (RestClientResponseException e) {
            log.error("Backend error ({}): {}", e.getStatusCode(), e.getResponseBodyAsString());

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);

            return ResponseEntity.status(e.getStatusCode())
                    .headers(responseHeaders)
                    .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Critical Communication Failure: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "ERROR", "message", "Backend communication failed: " + resolveFailureMessage(e)));
        }
    }

    private ResponseEntity<Object> executeRequest(String targetUrl, HttpMethod httpMethod, HttpHeaders headers, Object body) {
        RestClient.RequestBodySpec requestSpec = restClient.method(httpMethod)
                .uri(targetUrl)
                .headers(httpHeaders -> httpHeaders.addAll(headers));

        if (body != null) {
            return requestSpec.body(body).retrieve().toEntity(Object.class);
        }

        return requestSpec.retrieve().toEntity(Object.class);
    }

    private String resolveFailureMessage(Exception exception) {
        Throwable cause = exception.getCause();
        if (cause != null && cause.getMessage() != null && !cause.getMessage().isBlank()) {
            return cause.getMessage();
        }
        return exception.getMessage();
    }

    private String buildTargetUrl(String path, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendUrl + path);
        if (queryParams != null) {
            queryParams.forEach((key, value) -> {
                if (value != null && !String.valueOf(value).isBlank()) {
                    builder.queryParam(key, value);
                }
            });
        }
        return builder.toUriString();
    }

    private String extractProxyCookies(String rawCookieHeader) {
        if (rawCookieHeader == null || rawCookieHeader.isBlank()) {
            return null;
        }

        String filteredCookies = Arrays.stream(rawCookieHeader.split(";"))
                .map(String::trim)
                .filter(cookie -> FORWARDED_COOKIE_NAMES.stream().anyMatch(name -> cookie.startsWith(name + "=")))
                .collect(Collectors.joining("; "));

        return filteredCookies.isBlank() ? null : filteredCookies;
    }
}
