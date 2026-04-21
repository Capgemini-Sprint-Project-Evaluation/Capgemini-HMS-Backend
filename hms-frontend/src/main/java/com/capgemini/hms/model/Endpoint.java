package com.capgemini.hms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

public class Endpoint {
    private String id;
    private String name;
    private String method; // GET, POST, etc
    private String path;
    private String description;
    private String access;
    private String developerId;
    private String module;
    
    // Documentation fields
    private String expectedResponse;
    private String statusCode;
    private String requestDtoStructure;
    private String responseDtoStructure;
    
    // For Dynamic Form Generation
    private java.util.Map<String, String> fields; // fieldName -> fieldType (string, number, date)

    public Endpoint() {}
    public Endpoint(String id, String name, String method, String path, String description, String access, String developerId, String module, String expectedResponse, String statusCode, String requestDtoStructure, String responseDtoStructure, java.util.Map<String, String> fields) {
        this.id = id;
        this.name = name;
        this.method = method;
        this.path = path;
        this.description = description;
        this.access = access;
        this.developerId = developerId;
        this.module = module;
        this.expectedResponse = expectedResponse;
        this.statusCode = statusCode;
        this.requestDtoStructure = requestDtoStructure;
        this.responseDtoStructure = responseDtoStructure;
        this.fields = fields;
    }

    // Manual Accessors
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAccess() { return access; }
    public void setAccess(String access) { this.access = access; }
    public String getDeveloperId() { return developerId; }
    public void setDeveloperId(String developerId) { this.developerId = developerId; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getExpectedResponse() { return expectedResponse; }
    public void setExpectedResponse(String expectedResponse) { this.expectedResponse = expectedResponse; }
    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }
    public String getRequestDtoStructure() { return requestDtoStructure; }
    public void setRequestDtoStructure(String requestDtoStructure) { this.requestDtoStructure = requestDtoStructure; }
    public String getResponseDtoStructure() { return responseDtoStructure; }
    public void setResponseDtoStructure(String responseDtoStructure) { this.responseDtoStructure = responseDtoStructure; }
    public java.util.Map<String, String> getFields() { return fields; }
    public void setFields(java.util.Map<String, String> fields) { this.fields = fields; }

    // Manual Builder
    public static class EndpointBuilder {
        private String id;
        private String name;
        private String method;
        private String path;
        private String description;
        private String access;
        private String developerId;
        private String module;
        private String expectedResponse;
        private String statusCode;
        private String requestDtoStructure;
        private String responseDtoStructure;
        private java.util.Map<String, String> fields;
        public EndpointBuilder id(String id) { this.id = id; return this; }
        public EndpointBuilder name(String name) { this.name = name; return this; }
        public EndpointBuilder method(String method) { this.method = method; return this; }
        public EndpointBuilder path(String path) { this.path = path; return this; }
        public EndpointBuilder description(String description) { this.description = description; return this; }
        public EndpointBuilder access(String access) { this.access = access; return this; }
        public EndpointBuilder developerId(String developerId) { this.developerId = developerId; return this; }
        public EndpointBuilder module(String module) { this.module = module; return this; }
        public EndpointBuilder expectedResponse(String expectedResponse) { this.expectedResponse = expectedResponse; return this; }
        public EndpointBuilder statusCode(String statusCode) { this.statusCode = statusCode; return this; }
        public EndpointBuilder requestDtoStructure(String requestDtoStructure) { this.requestDtoStructure = requestDtoStructure; return this; }
        public EndpointBuilder responseDtoStructure(String responseDtoStructure) { this.responseDtoStructure = responseDtoStructure; return this; }
        public EndpointBuilder fields(java.util.Map<String, String> fields) { this.fields = fields; return this; }
        public Endpoint build() { return new Endpoint(id, name, method, path, description, access, developerId, module, expectedResponse, statusCode, requestDtoStructure, responseDtoStructure, fields); }
    }
    public static EndpointBuilder builder() { return new EndpointBuilder(); }
}
