package com.capgemini.hms.auth.service;

import com.capgemini.hms.auth.dto.SignupRequest;
import com.capgemini.hms.auth.entity.ERole;
import com.capgemini.hms.auth.entity.Role;
import com.capgemini.hms.auth.entity.User;
import com.capgemini.hms.auth.repository.RoleRepository;
import com.capgemini.hms.auth.repository.UserRepository;
import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.exception.ConflictException;
import com.capgemini.hms.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerUser_shouldThrowWhenUsernameAlreadyExists() {
        SignupRequest request = SignupRequest.builder().username("taken").email("a@b.com").password("secret1").build();
        when(userRepository.existsByUsername("taken")).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.registerUser(request));
    }

    @Test
    void registerUser_shouldThrowWhenEmailAlreadyExists() {
        SignupRequest request = SignupRequest.builder().username("user").email("used@b.com").password("secret1").build();
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("used@b.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.registerUser(request));
    }

    @Test
    void registerUser_shouldAssignDefaultPatientRoleWhenRolesAreMissing() {
        SignupRequest request = SignupRequest.builder()
                .username("user")
                .email("user@test.com")
                .password("secret1")
                .build();
        Role patientRole = new Role(1, ERole.ROLE_PATIENT);

        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("user@test.com")).thenReturn(false);
        when(encoder.encode("secret1")).thenReturn("encoded");
        when(roleRepository.findByName(ERole.ROLE_PATIENT)).thenReturn(Optional.of(patientRole));

        ApiResponse<String> response = authService.registerUser(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertNotNull(captor.getValue().getRoles());
        assertEquals(1, captor.getValue().getRoles().size());
        assertEquals("User registered successfully!", response.getMessage());
    }

    @Test
    void registerUser_shouldAssignRequestedRoles() {
        SignupRequest request = SignupRequest.builder()
                .username("multi")
                .email("multi@test.com")
                .password("secret1")
                .role(Set.of("admin", "doctor", "nurse", "other"))
                .build();

        when(userRepository.existsByUsername("multi")).thenReturn(false);
        when(userRepository.existsByEmail("multi@test.com")).thenReturn(false);
        when(encoder.encode("secret1")).thenReturn("encoded");
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(new Role(1, ERole.ROLE_ADMIN)));
        when(roleRepository.findByName(ERole.ROLE_DOCTOR)).thenReturn(Optional.of(new Role(2, ERole.ROLE_DOCTOR)));
        when(roleRepository.findByName(ERole.ROLE_NURSE)).thenReturn(Optional.of(new Role(3, ERole.ROLE_NURSE)));
        when(roleRepository.findByName(ERole.ROLE_PATIENT)).thenReturn(Optional.of(new Role(4, ERole.ROLE_PATIENT)));

        authService.registerUser(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals(4, captor.getValue().getRoles().size());
    }

    @Test
    void registerUser_shouldThrowWhenConfiguredRoleIsMissing() {
        SignupRequest request = SignupRequest.builder()
                .username("user")
                .email("user@test.com")
                .password("secret1")
                .role(Set.of("admin"))
                .build();

        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("user@test.com")).thenReturn(false);
        when(encoder.encode("secret1")).thenReturn("encoded");
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.registerUser(request));
    }
}
