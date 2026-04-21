package com.capgemini.hms.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeveloperTest {

    @Test
    void builderAndAccessors_shouldPopulateDeveloperFields() {
        Developer developer = Developer.builder()
                .id("akash")
                .name("Akash")
                .role("Lead")
                .domain("Security")
                .description("Owns auth")
                .photoUrl("/img/a.png")
                .responsibilities(List.of("Auth"))
                .build();

        assertEquals("akash", developer.getId());
        assertEquals("Akash", developer.getName());
        assertEquals("Lead", developer.getRole());
        assertEquals("Security", developer.getDomain());
        assertEquals("Owns auth", developer.getDescription());
        assertEquals("/img/a.png", developer.getPhotoUrl());
        assertEquals(List.of("Auth"), developer.getResponsibilities());

        developer.setName("Updated");
        developer.setId("updated-id");
        developer.setRole("Updated Role");
        developer.setDomain("Updated Domain");
        developer.setDescription("Updated Description");
        developer.setPhotoUrl("/img/b.png");
        developer.setResponsibilities(List.of("Security"));
        Developer empty = new Developer();
        empty.setName("Empty");

        assertEquals("updated-id", developer.getId());
        assertEquals("Updated", developer.getName());
    }
}
