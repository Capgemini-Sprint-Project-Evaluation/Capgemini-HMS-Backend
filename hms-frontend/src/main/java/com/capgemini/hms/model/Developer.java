package com.capgemini.hms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class Developer {
    private String id;
    private String name;
    private String role;
    private String domain;
    private String description;
    private String photoUrl;
    private java.util.List<String> responsibilities;

    public Developer() {}
    public Developer(String id, String name, String role, String domain, String description, String photoUrl, java.util.List<String> responsibilities) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.domain = domain;
        this.description = description;
        this.photoUrl = photoUrl;
        this.responsibilities = responsibilities;
    }

    // Manual Accessors
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public java.util.List<String> getResponsibilities() { return responsibilities; }
    public void setResponsibilities(java.util.List<String> responsibilities) { this.responsibilities = responsibilities; }

    // Manual Builder
    public static class DeveloperBuilder {
        private String id;
        private String name;
        private String role;
        private String domain;
        private String description;
        private String photoUrl;
        private java.util.List<String> responsibilities;
        public DeveloperBuilder id(String id) { this.id = id; return this; }
        public DeveloperBuilder name(String name) { this.name = name; return this; }
        public DeveloperBuilder role(String role) { this.role = role; return this; }
        public DeveloperBuilder domain(String domain) { this.domain = domain; return this; }
        public DeveloperBuilder description(String description) { this.description = description; return this; }
        public DeveloperBuilder photoUrl(String photoUrl) { this.photoUrl = photoUrl; return this; }
        public DeveloperBuilder responsibilities(java.util.List<String> responsibilities) { this.responsibilities = responsibilities; return this; }
        public Developer build() { return new Developer(id, name, role, domain, description, photoUrl, responsibilities); }
    }
    public static DeveloperBuilder builder() { return new DeveloperBuilder(); }
}
