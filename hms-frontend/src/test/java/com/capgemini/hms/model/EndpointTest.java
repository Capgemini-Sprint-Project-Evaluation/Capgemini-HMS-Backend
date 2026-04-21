package com.capgemini.hms.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EndpointTest {

    @Test
    void builderAndAccessors_shouldPopulateEndpointFields() {
        Endpoint endpoint = Endpoint.builder()
                .id("e1")
                .name("List")
                .method("GET")
                .path("/api/test")
                .description("desc")
                .access("ADMIN")
                .developerId("akash")
                .module("Auth")
                .expectedResponse("ok")
                .statusCode("200")
                .requestDtoStructure("req")
                .responseDtoStructure("res")
                .fields(Map.of("id", "number"))
                .build();

        assertEquals("e1", endpoint.getId());
        assertEquals("List", endpoint.getName());
        assertEquals("GET", endpoint.getMethod());
        assertEquals("/api/test", endpoint.getPath());
        assertEquals("desc", endpoint.getDescription());
        assertEquals("ADMIN", endpoint.getAccess());
        assertEquals("akash", endpoint.getDeveloperId());
        assertEquals("Auth", endpoint.getModule());
        assertEquals("ok", endpoint.getExpectedResponse());
        assertEquals("200", endpoint.getStatusCode());
        assertEquals("req", endpoint.getRequestDtoStructure());
        assertEquals("res", endpoint.getResponseDtoStructure());
        assertEquals("number", endpoint.getFields().get("id"));

        endpoint.setId("e2");
        endpoint.setMethod("POST");
        endpoint.setPath("/api/new");
        endpoint.setDescription("updated");
        endpoint.setAccess("USER");
        endpoint.setDeveloperId("dev2");
        endpoint.setModule("Updated");
        endpoint.setExpectedResponse("updated-ok");
        endpoint.setStatusCode("201");
        endpoint.setRequestDtoStructure("updated-req");
        endpoint.setResponseDtoStructure("updated-res");
        endpoint.setFields(Map.of("name", "string"));

        Endpoint empty = new Endpoint();
        empty.setName("Empty");

        assertEquals("e2", endpoint.getId());
        assertEquals("POST", endpoint.getMethod());
        assertEquals("Updated", endpoint.getModule());
    }
}
