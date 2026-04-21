package com.capgemini.hms.service;

import com.capgemini.hms.model.Developer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DeveloperServiceTest {

    private final DeveloperService developerService = new DeveloperService();

    @Test
    void getAllDevelopers_shouldReturnSeededDevelopersInConfiguredOrder() {
        List<Developer> developers = developerService.getAllDevelopers();

        assertEquals(6, developers.size());
        assertEquals("akash", developers.get(0).getId());
        assertEquals("Akash Gaikwad", developers.get(0).getName());
    }

    @Test
    void getDeveloperById_shouldBeCaseInsensitive() {
        Developer developer = developerService.getDeveloperById("AKASH");

        assertNotNull(developer);
        assertEquals("Akash Gaikwad", developer.getName());
    }

    @Test
    void getDeveloperById_shouldReturnNullWhenMissing() {
        assertNull(developerService.getDeveloperById("missing"));
    }
}
