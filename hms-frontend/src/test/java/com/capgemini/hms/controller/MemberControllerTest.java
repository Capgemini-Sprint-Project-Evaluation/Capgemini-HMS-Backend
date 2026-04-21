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
class MemberControllerTest {

    @Mock
    private DeveloperService developerService;

    @Mock
    private EndpointService endpointService;

    @InjectMocks
    private MemberController memberController;

    @Test
    void memberDetail_shouldRedirectWhenDeveloperMissing() {
        when(developerService.getDeveloperById("missing")).thenReturn(null);

        assertEquals("redirect:/", memberController.memberDetail("missing", new ExtendedModelMap()));
    }

    @Test
    void memberDetail_shouldPopulateModelWhenDeveloperExists() {
        ExtendedModelMap model = new ExtendedModelMap();
        Developer developer = Developer.builder().id("akash").name("Akash").build();
        List<Endpoint> endpoints = List.of(Endpoint.builder().id("e1").build());
        when(developerService.getDeveloperById("akash")).thenReturn(developer);
        when(endpointService.getEndpointsByDeveloper("akash")).thenReturn(endpoints);

        String view = memberController.memberDetail("akash", model);

        assertEquals("member", view);
        assertEquals(developer, model.get("developer"));
        assertEquals(endpoints, model.get("endpoints"));
    }

    @Test
    void endpointDetail_shouldRedirectWhenEndpointMissing() {
        when(endpointService.getEndpointById("missing")).thenReturn(null);

        assertEquals("redirect:/", memberController.endpointDetail("missing", new ExtendedModelMap()));
    }

    @Test
    void endpointDetail_shouldRedirectWhenDeveloperMissing() {
        Endpoint endpoint = Endpoint.builder().id("e1").developerId("akash").build();
        when(endpointService.getEndpointById("e1")).thenReturn(endpoint);
        when(developerService.getDeveloperById("akash")).thenReturn(null);

        assertEquals("redirect:/", memberController.endpointDetail("e1", new ExtendedModelMap()));
    }

    @Test
    void endpointDetail_shouldPopulateModel() {
        ExtendedModelMap model = new ExtendedModelMap();
        Endpoint endpoint = Endpoint.builder().id("e1").name("Endpoint").developerId("akash").build();
        Developer developer = Developer.builder().id("akash").name("Akash").build();
        when(endpointService.getEndpointById("e1")).thenReturn(endpoint);
        when(developerService.getDeveloperById("akash")).thenReturn(developer);

        String view = memberController.endpointDetail("e1", model);

        assertEquals("endpoint", view);
        assertEquals(endpoint, model.get("endpoint"));
        assertEquals(developer, model.get("developer"));
    }
}
