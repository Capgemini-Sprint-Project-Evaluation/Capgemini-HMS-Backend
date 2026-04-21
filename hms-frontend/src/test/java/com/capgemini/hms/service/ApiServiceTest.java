package com.capgemini.hms.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

class ApiServiceTest {

    private final RestClient.Builder restClientBuilder = RestClient.builder();
    private final MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
    private final ApiService apiService = new ApiService(restClientBuilder.build());

    ApiServiceTest() {
        ReflectionTestUtils.setField(apiService, "backendUrl", "http://localhost:8080");
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
        server.verify();
    }

    @Test
    void proxyRequest_shouldForwardCookiesAndQueryParamsForGetRequests() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.COOKIE, "JSESSIONID=test-session; theme=dark; XSRF-TOKEN=csrf-token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        server.expect(once(), requestTo("http://localhost:8080/api/test?q=abc"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.COOKIE, "JSESSIONID=test-session; XSRF-TOKEN=csrf-token"))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":\"SUCCESS\"}"));

        var response = apiService.proxyRequest("GET", "/api/test", Map.of("q", "abc", "blank", ""));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void proxyRequest_shouldForwardCsrfHeaderWhenPresent() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-XSRF-TOKEN", "csrf-token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        server.expect(once(), requestTo("http://localhost:8080/api/test"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-XSRF-TOKEN", "csrf-token"))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":\"SUCCESS\"}"));

        var response = apiService.proxyRequest("POST", "/api/test", Map.of("name", "demo"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void proxyRequest_shouldReturnBackendStatusBodyForHttpStatusExceptions() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        server.expect(once(), requestTo("http://localhost:8080/api/test"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\":\"invalid\"}"));

        var response = apiService.proxyRequest("POST", "/api/test", Map.of("name", "demo"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\":\"invalid\"}", response.getBody());
    }

    @Test
    void proxyRequest_shouldReturnInternalServerErrorForUnexpectedFailures() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ApiService failingApiService = new ApiService(RestClient.builder()
                .requestFactory(new FailingRequestFactory())
                .build());
        ReflectionTestUtils.setField(failingApiService, "backendUrl", "http://localhost:8080");

        var response = failingApiService.proxyRequest("POST", "/api/test", Map.of("name", "demo"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("ERROR", body.get("status"));
        assertEquals("Backend communication failed: connection down", body.get("message"));
    }

    @Test
    void proxyRequest_shouldWorkWithoutCookiesOrCsrfHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        server.expect(once(), requestTo("http://localhost:8080/api/test"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":\"SUCCESS\"}"));

        var response = apiService.proxyRequest("POST", "/api/test", Map.of("name", "demo"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void proxyRequest_shouldIgnoreBlankCsrfHeaderAndIrrelevantCookies() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.COOKIE, "theme=dark; locale=en");
        request.addHeader("X-XSRF-TOKEN", "   ");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        server.expect(once(), requestTo("http://localhost:8080/api/test"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":\"SUCCESS\"}"));

        var response = apiService.proxyRequest("POST", "/api/test", Map.of("name", "demo"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void proxyRequest_shouldTreatDeleteAsQueryOnlyRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        server.expect(once(), requestTo("http://localhost:8080/api/test?room=A"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":\"SUCCESS\"}"));

        var response = apiService.proxyRequest("DELETE", "/api/test", Map.of("room", "A", "blank", " "));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private static final class FailingRequestFactory implements ClientHttpRequestFactory {
        @Override
        public org.springframework.http.client.ClientHttpRequest createRequest(java.net.URI uri, HttpMethod httpMethod)
                throws IOException {
            throw new IOException("connection down");
        }
    }
}
