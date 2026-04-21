package com.capgemini.hms.controller;

import com.capgemini.hms.model.Developer;
import com.capgemini.hms.model.Endpoint;
import com.capgemini.hms.service.DeveloperService;
import com.capgemini.hms.service.EndpointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    private DeveloperService developerService;

    @Mock
    private EndpointService endpointService;

    @InjectMocks
    private HomeController homeController;

    @Test
    void index_shouldPopulateModel() {
        ExtendedModelMap model = new ExtendedModelMap();
        when(developerService.getAllDevelopers()).thenReturn(List.of(Developer.builder().id("akash").build()));
        when(endpointService.getAllEndpoints()).thenReturn(List.of(Endpoint.builder().id("e1").build(), Endpoint.builder().id("e2").build()));

        String view = homeController.index(model);

        assertEquals("index", view);
        assertEquals("Capgemini HMS - Distributed System Showcase", model.get("title"));
        assertEquals(2, model.get("endpointCount"));
    }
}
