package com.capgemini.hms.controller;

import com.capgemini.hms.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/proxy")
public class ProxyController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProxyController.class);

    private final ApiService apiService;

    public ProxyController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/csrf")
    public ResponseEntity<Object> fetchCsrfToken() {
        return apiService.proxyRequest("GET", "/api/v1/auth/csrf", java.util.Collections.emptyMap());
    }

    @PostMapping
    public ResponseEntity<Object> proxyRequest(@RequestBody Map<String, Object> requestData) {
        String method = (String) requestData.get("method");
        String path = (String) requestData.get("path");

        log.info("Proxy Controller: Received proxy request for {} {}", method, path);

        // Prepare request body (copy all data)
        Map<String, Object> payload = new java.util.HashMap<>(requestData);
        
        // 1. Path Variable Substitution (e.g., /api/v1/management/departments/{id})
        // We look for any {key} in the path and replace it with the value from requestData
        for (Map.Entry<String, Object> entry : requestData.entrySet()) {
            String key = entry.getKey();
            String placeholder = "{" + key + "}";
            if (path.contains(placeholder)) {
                path = path.replace(placeholder, String.valueOf(entry.getValue()));
                payload.remove(key); // Remove from body if used in path
            }
        }

        // 2. Clean up proxy metadata from payload
        payload.remove("method");
        payload.remove("path");
        payload.remove("id"); // Remove generic 'id' if still present

        // Special handling for signup 'role' (Backend expects Set/List)
        if (payload.containsKey("role") && payload.get("role") instanceof String) {
            payload.put("role", java.util.Collections.singletonList(payload.get("role")));
        }

        return apiService.proxyRequest(method, path, payload);
    }
}
