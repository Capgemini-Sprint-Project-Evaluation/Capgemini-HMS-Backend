package com.capgemini.hms.service;

import com.capgemini.hms.model.Endpoint;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EndpointServiceTest {

    private final EndpointService endpointService = new EndpointService();

    @Test
    void getAllEndpoints_shouldReturnSeededCatalog() {
        List<Endpoint> endpoints = endpointService.getAllEndpoints();

        assertFalse(endpoints.isEmpty());
        assertTrue(endpoints.size() > 10);
    }

    @Test
    void getEndpointsByDeveloper_shouldFilterCaseInsensitively() {
        List<Endpoint> endpoints = endpointService.getEndpointsByDeveloper("AKASH");

        assertFalse(endpoints.isEmpty());
        assertTrue(endpoints.stream().allMatch(endpoint -> endpoint.getDeveloperId().equalsIgnoreCase("akash")));
    }

    @Test
    void getEndpointById_shouldReturnMatchingEndpoint() {
        Endpoint endpoint = endpointService.getEndpointById("akash-auth-01");

        assertNotNull(endpoint);
        assertEquals("System Login", endpoint.getName());
        assertEquals("/api/v1/auth/signin", endpoint.getPath());
        assertEquals("string", endpoint.getFields().get("username"));
    }

    @Test
    void getEndpointById_shouldReturnNullForUnknownId() {
        assertNull(endpointService.getEndpointById("missing"));
    }
}
