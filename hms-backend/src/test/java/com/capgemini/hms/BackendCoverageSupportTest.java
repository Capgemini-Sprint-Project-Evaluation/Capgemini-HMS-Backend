package com.capgemini.hms;

import com.capgemini.hms.auth.entity.AffiliatedWith;
import com.capgemini.hms.auth.entity.AffiliatedWithId;
import com.capgemini.hms.auth.entity.AuditLog;
import com.capgemini.hms.auth.entity.Department;
import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.patient.dto.ProcedureDTO;
import com.capgemini.hms.patient.dto.StayDTO;
import com.capgemini.hms.patient.entity.Procedures;
import com.capgemini.hms.patient.entity.Stay;
import com.capgemini.hms.patient.entity.Undergoes;
import com.capgemini.hms.patient.entity.UndergoesId;
import com.capgemini.hms.scheduling.dto.TrainedInDTO;
import com.capgemini.hms.scheduling.entity.Physician;
import com.capgemini.hms.scheduling.entity.TrainedIn;
import com.capgemini.hms.scheduling.entity.TrainedInId;
import com.capgemini.hms.security.config.CsrfCookieFilter;
import com.capgemini.hms.security.service.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BackendCoverageSupportTest {

    @Test
    void apiResponse_shouldSupportConstructorsSettersAndHelpers() {
        ApiResponse<String> empty = new ApiResponse<>();
        empty.setStatus("SUCCESS");
        empty.setMessage("ok");
        empty.setData("data");
        empty.setErrors(Map.of("field", "required"));
        empty.setTimestamp(Instant.now());

        assertEquals("SUCCESS", empty.getStatus());
        assertEquals("ok", empty.getMessage());
        assertEquals("data", empty.getData());
        assertEquals("required", empty.getErrors().get("field"));
        assertNotNull(empty.getTimestamp());

        ApiResponse<String> success = ApiResponse.success("value", "done");
        ApiResponse<String> successDefault = ApiResponse.success("value");
        ApiResponse<String> error = ApiResponse.error("failed");
        ApiResponse<String> errorWithErrors = ApiResponse.error("failed", Map.of("field", "bad"));

        assertEquals("SUCCESS", success.getStatus());
        assertEquals("Operation successful", successDefault.getMessage());
        assertEquals("ERROR", error.getStatus());
        assertEquals("bad", errorWithErrors.getErrors().get("field"));
    }

    @Test
    void dtoAndEntityManualAccessors_shouldWork() {
        LocalDateTime now = LocalDateTime.now();

        ProcedureDTO procedureDTO = ProcedureDTO.builder().code(1).name("Procedure").cost(BigDecimal.TEN).build();
        procedureDTO.setCode(2);
        procedureDTO.setName("Updated");
        procedureDTO.setCost(BigDecimal.ONE);
        assertEquals(2, procedureDTO.getCode());
        assertEquals("Updated", procedureDTO.getName());
        assertEquals(BigDecimal.ONE, procedureDTO.getCost());

        StayDTO stayDTO = StayDTO.builder().stayId(1).patientSsn(2).roomNumber(3).startTime(now).endTime(now.plusDays(1)).build();
        stayDTO.setStayId(4);
        stayDTO.setPatientSsn(5);
        stayDTO.setRoomNumber(6);
        stayDTO.setStartTime(now.plusHours(1));
        stayDTO.setEndTime(now.plusHours(2));
        assertEquals(4, stayDTO.getStayId());
        assertEquals(5, stayDTO.getPatientSsn());
        assertEquals(6, stayDTO.getRoomNumber());

        TrainedInDTO trainedInDTO = TrainedInDTO.builder()
                .physicianId(1)
                .physicianName("Dr")
                .procedureCode(2)
                .procedureName("Proc")
                .treatmentDate(now)
                .certificationExpires(now.plusDays(1))
                .build();
        trainedInDTO.setPhysicianId(10);
        trainedInDTO.setPhysicianName("Updated");
        trainedInDTO.setProcedureCode(20);
        trainedInDTO.setProcedureName("Updated Proc");
        trainedInDTO.setTreatmentDate(now.plusDays(2));
        trainedInDTO.setCertificationExpires(now.plusDays(3));
        assertEquals(10, trainedInDTO.getPhysicianId());
        assertEquals("Updated", trainedInDTO.getPhysicianName());

        UndergoesId undergoesId = new UndergoesId(1, 2, 3, now);
        undergoesId.setPatient(4);
        undergoesId.setProcedures(5);
        undergoesId.setStay(6);
        undergoesId.setDate(now.plusDays(1));
        assertEquals(4, undergoesId.getPatient());
        assertEquals(5, undergoesId.getProcedures());
        assertEquals(6, undergoesId.getStay());

        TrainedInId trainedInId = new TrainedInId(1, 2);
        trainedInId.setPhysician(3);
        trainedInId.setProcedure(4);
        assertEquals(3, trainedInId.getPhysician());
        assertEquals(4, trainedInId.getProcedure());

        AuditLog auditLog = AuditLog.builder().id(1L).username("admin").action("CREATE").timestamp(now).details("details").build();
        auditLog.setId(2L);
        auditLog.setUsername("user");
        auditLog.setAction("UPDATE");
        auditLog.setTimestamp(now.plusMinutes(1));
        auditLog.setDetails("updated");
        assertEquals(2L, auditLog.getId());
        assertEquals("user", auditLog.getUsername());

        Physician physician = Physician.builder().employeeId(1).name("Dr").build();
        Department department = Department.builder().departmentId(1).name("Cardiology").build();
        AffiliatedWith affiliatedWith = AffiliatedWith.builder()
                .id(new AffiliatedWithId(1, 1))
                .physician(physician)
                .department(department)
                .primaryaffiliation(true)
                .build();
        affiliatedWith.setPrimaryaffiliation(false);
        affiliatedWith.setIsDeleted(true);
        assertFalse(affiliatedWith.getPrimaryaffiliation());
        assertEquals(true, affiliatedWith.getIsDeleted());

        Procedures procedures = Procedures.builder().code(7).name("Proc").cost(BigDecimal.TEN).isDeleted(false).build();
        Stay stay = Stay.builder().stayId(8).patient(null).room(null).stayStart(now).stayEnd(now.plusDays(1)).isDeleted(false).build();
        Undergoes undergoes = Undergoes.builder().id(new UndergoesId(1, 7, 8, now)).patient(null).procedure(procedures).stay(stay).physician(physician).build();
        undergoes.setIsDeleted(true);
        assertEquals(true, undergoes.getIsDeleted());

        TrainedIn trainedIn = TrainedIn.builder()
                .id(new TrainedInId(1, 7))
                .physician(physician)
                .procedure(procedures)
                .treatmentdate(now)
                .certificationexpires(now.plusDays(10))
                .build();
        trainedIn.setIsDeleted(true);
        assertEquals(true, trainedIn.getIsDeleted());
    }

    @Test
    void csrfCookieFilter_shouldHandleBothTokenStates() throws Exception {
        CsrfCookieFilter filter = new CsrfCookieFilter();
        MockHttpServletRequest requestWithoutToken = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        jakarta.servlet.FilterChain chain = mock(jakarta.servlet.FilterChain.class);

        filter.doFilter(requestWithoutToken, response, chain);
        verify(chain).doFilter(requestWithoutToken, response);

        MockHttpServletRequest requestWithToken = new MockHttpServletRequest();
        CsrfToken csrfToken = mock(CsrfToken.class);
        when(csrfToken.getToken()).thenReturn("csrf");
        requestWithToken.setAttribute(CsrfToken.class.getName(), csrfToken);

        filter.doFilter(requestWithToken, response, chain);
        verify(csrfToken).getToken();
    }

    @Test
    void userDetailsImpl_shouldExposeAllUserDetailsFlags() {
        com.capgemini.hms.auth.entity.User user = com.capgemini.hms.auth.entity.User.builder()
                .id(1L)
                .username("admin")
                .email("admin@test.com")
                .password("encoded")
                .roles(Set.of(new com.capgemini.hms.auth.entity.Role(1, com.capgemini.hms.auth.entity.ERole.ROLE_ADMIN)))
                .build();

        UserDetailsImpl details = UserDetailsImpl.build(user);

        assertEquals(1L, details.getId());
        assertEquals("admin@test.com", details.getEmail());
        assertEquals("admin", details.getUsername());
        assertEquals("encoded", details.getPassword());
        assertEquals(1, details.getAuthorities().size());
        assertEquals(true, details.isAccountNonExpired());
        assertEquals(true, details.isAccountNonLocked());
        assertEquals(true, details.isCredentialsNonExpired());
        assertEquals(true, details.isEnabled());
    }
}
