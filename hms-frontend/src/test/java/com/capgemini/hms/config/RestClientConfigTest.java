package com.capgemini.hms.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestClientConfigTest {

    @Test
    void restClient_shouldCreateBean() {
        RestClient restClient = new RestClientConfig().restClient(RestClient.builder());

        assertNotNull(restClient);
    }
}
