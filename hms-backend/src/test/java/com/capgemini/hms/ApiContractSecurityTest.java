package com.capgemini.hms;

import com.capgemini.hms.auth.controller.AuthController;
import com.capgemini.hms.auth.controller.ManagementController;
import com.capgemini.hms.auth.entity.Department;
import com.capgemini.hms.auth.service.AuthService;
import com.capgemini.hms.auth.service.ManagementService;
import com.capgemini.hms.clinical.controller.ClinicalController;
import com.capgemini.hms.clinical.dto.MedicationDTO;
import com.capgemini.hms.clinical.service.ClinicalService;
import com.capgemini.hms.exception.GlobalExceptionHandler;
import com.capgemini.hms.security.config.RestAuthenticationEntryPoint;
import com.capgemini.hms.security.config.WebSecurityConfig;
import com.capgemini.hms.security.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        AuthController.class,
        ManagementController.class,
        ClinicalController.class
})
@Import({
        WebSecurityConfig.class,
        RestAuthenticationEntryPoint.class,
        GlobalExceptionHandler.class
})
class ApiContractSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthService authService;

    @MockBean
    private ManagementService managementService;

    @MockBean
    private ClinicalService clinicalService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void protectedEndpointRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/management/departments"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Authentication required to access this resource."));
    }

    @Test
    void staffCanReadDepartmentsWithStandardSuccessEnvelope() throws Exception {
        when(managementService.getAllDepartments()).thenReturn(List.of(
                Department.builder().departmentId(1).name("Cardiology").headId(101).build()
        ));

        mockMvc.perform(get("/api/v1/management/departments")
                        .with(user("nurse").roles("NURSE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data[0].name").value("Cardiology"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void nonAdminCannotCreateDepartment() throws Exception {
        String body = """
                {
                  "departmentId": 5,
                  "name": "Oncology",
                  "headId": 101
                }
                """;

        mockMvc.perform(post("/api/v1/management/departments")
                        .with(user("doctor").roles("DOCTOR"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("ERROR"));
    }

    @Test
    void signinRejectsInvalidPayloadWithStructuredErrors() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "",
                                  "password": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.username").exists())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    void adminMedicationCreateReturnsCreatedEnvelope() throws Exception {
        MedicationDTO saved = MedicationDTO.builder()
                .code(11)
                .name("Paracetamol")
                .brand("Acme")
                .description("Pain relief")
                .build();
        when(clinicalService.addMedication(any(MedicationDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/clinical/medications")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Medication added successfully."))
                .andExpect(jsonPath("$.data.code").value(11));
    }

    @Test
    void signinCreatesSuccessResponse() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        "admin",
                        "password",
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));

        mockMvc.perform(post("/api/v1/auth/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "admin",
                                  "password": "password"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("User signed in successfully."));
    }
}
