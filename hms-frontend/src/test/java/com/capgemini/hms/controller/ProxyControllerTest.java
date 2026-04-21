package com.capgemini.hms.controller;

import com.capgemini.hms.service.ApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProxyControllerTest {

    @Mock
    private ApiService apiService;

    @InjectMocks
    private ProxyController proxyController;

    @Test
    void fetchCsrfToken_shouldDelegateToBackendCsrfEndpoint() {
        ResponseEntity<Object> backendResponse = ResponseEntity.ok(Map.of("token", "abc"));
        when(apiService.proxyRequest("GET", "/api/v1/auth/csrf", java.util.Collections.emptyMap()))
                .thenReturn(backendResponse);

        ResponseEntity<Object> response = proxyController.fetchCsrfToken();

        assertEquals(backendResponse, response);
    }

    @Test
    void proxyRequest_shouldSubstitutePathVariablesAndNormalizeRole() {
        when(apiService.proxyRequest(eq("POST"), eq("/api/v1/management/departments/10"), anyMap()))
                .thenReturn(ResponseEntity.ok(Map.of("ok", true)));

        Map<String, Object> request = Map.of(
                "method", "POST",
                "path", "/api/v1/management/departments/{id}",
                "id", 10,
                "role", "ADMIN",
                "name", "Dept"
        );

        ResponseEntity<Object> response = proxyController.proxyRequest(request);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(apiService).proxyRequest(eq("POST"), eq("/api/v1/management/departments/10"), captor.capture());
        assertEquals(ResponseEntity.ok(Map.of("ok", true)), response);
        assertEquals("Dept", captor.getValue().get("name"));
        assertEquals(java.util.Collections.singletonList("ADMIN"), captor.getValue().get("role"));
    }
}
