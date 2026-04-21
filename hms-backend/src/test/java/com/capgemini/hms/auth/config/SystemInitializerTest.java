package com.capgemini.hms.auth.config;

import com.capgemini.hms.auth.entity.ERole;
import com.capgemini.hms.auth.entity.Role;
import com.capgemini.hms.auth.entity.User;
import com.capgemini.hms.auth.repository.DepartmentRepository;
import com.capgemini.hms.auth.repository.RoleRepository;
import com.capgemini.hms.auth.repository.UserRepository;
import com.capgemini.hms.infrastructure.repository.BlockRepository;
import com.capgemini.hms.infrastructure.repository.RoomRepository;
import com.capgemini.hms.scheduling.repository.PhysicianRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SystemInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PhysicianRepository physicianRepository;

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private RoomRepository roomRepository;

    private SystemInitializer systemInitializer = new SystemInitializer(new MockEnvironment());

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void initDatabase_shouldSkipAdminCreationWhenCredentialsAreMissingOutsideDev() throws Exception {
        ReflectionTestUtils.setField(systemInitializer, "adminUsername", "");
        ReflectionTestUtils.setField(systemInitializer, "adminPassword", "");

        when(departmentRepository.count()).thenReturn(1L);
        when(physicianRepository.count()).thenReturn(1L);
        when(blockRepository.count()).thenReturn(1L);

        systemInitializer.initDatabase(
                userRepository,
                roleRepository,
                departmentRepository,
                physicianRepository,
                blockRepository,
                roomRepository,
                passwordEncoder
        ).run();

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void initDatabase_shouldUseDevFallbackCredentialsWhenDevProfileIsActive() throws Exception {
        MockEnvironment environment = new MockEnvironment().withProperty("spring.profiles.active", "dev");
        SystemInitializer initializer = new SystemInitializer(environment);
        ReflectionTestUtils.setField(initializer, "adminUsername", "");
        ReflectionTestUtils.setField(initializer, "adminPassword", "");
        ReflectionTestUtils.setField(initializer, "adminEmail", "admin@local.test");
        ReflectionTestUtils.setField(initializer, "devFallbackUsername", "dev-admin");
        ReflectionTestUtils.setField(initializer, "devFallbackPassword", "dev-secret");

        when(userRepository.existsByUsername("dev-admin")).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(java.util.Optional.of(new Role(1, ERole.ROLE_ADMIN)));
        when(departmentRepository.count()).thenReturn(1L);
        when(physicianRepository.count()).thenReturn(1L);
        when(blockRepository.count()).thenReturn(1L);

        initializer.initDatabase(
                userRepository,
                roleRepository,
                departmentRepository,
                physicianRepository,
                blockRepository,
                roomRepository,
                passwordEncoder
        ).run();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertTrue(passwordEncoder.matches("dev-secret", savedUser.getPassword()));
        assertNotEquals("dev-secret", savedUser.getPassword());
    }

    @Test
    void initDatabase_shouldSyncExistingAdminCredentialsFromConfiguration() throws Exception {
        ReflectionTestUtils.setField(systemInitializer, "adminUsername", "acashtech28");
        ReflectionTestUtils.setField(systemInitializer, "adminPassword", "acash@9945");
        ReflectionTestUtils.setField(systemInitializer, "adminEmail", "admin@local.test");

        User existingUser = User.builder()
                .id(1L)
                .username("acashtech28")
                .email("old@local.test")
                .password(passwordEncoder.encode("old-password"))
                .roles(new java.util.HashSet<>())
                .build();

        when(userRepository.existsByUsername("acashtech28")).thenReturn(true);
        when(userRepository.findByUsername("acashtech28")).thenReturn(java.util.Optional.of(existingUser));
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(java.util.Optional.of(new Role(1, ERole.ROLE_ADMIN)));
        when(departmentRepository.count()).thenReturn(1L);
        when(physicianRepository.count()).thenReturn(1L);
        when(blockRepository.count()).thenReturn(1L);

        systemInitializer.initDatabase(
                userRepository,
                roleRepository,
                departmentRepository,
                physicianRepository,
                blockRepository,
                roomRepository,
                passwordEncoder
        ).run();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertTrue(passwordEncoder.matches("acash@9945", savedUser.getPassword()));
        assertTrue(savedUser.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN));
        org.junit.jupiter.api.Assertions.assertEquals("admin@local.test", savedUser.getEmail());
    }

    @Test
    void initDatabase_shouldNotResaveExistingAdminWhenAlreadySynchronized() throws Exception {
        ReflectionTestUtils.setField(systemInitializer, "adminUsername", "aligned-admin");
        ReflectionTestUtils.setField(systemInitializer, "adminPassword", "aligned-secret");
        ReflectionTestUtils.setField(systemInitializer, "adminEmail", "aligned@local.test");

        Role adminRole = new Role(1, ERole.ROLE_ADMIN);
        java.util.Set<Role> roles = new java.util.HashSet<>();
        roles.add(adminRole);
        User existingUser = User.builder()
                .id(2L)
                .username("aligned-admin")
                .email("aligned@local.test")
                .password(passwordEncoder.encode("aligned-secret"))
                .roles(roles)
                .build();

        when(userRepository.existsByUsername("aligned-admin")).thenReturn(true);
        when(userRepository.findByUsername("aligned-admin")).thenReturn(java.util.Optional.of(existingUser));
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(java.util.Optional.of(adminRole));
        when(roleRepository.findByName(eq(ERole.ROLE_DOCTOR))).thenReturn(java.util.Optional.empty());
        when(roleRepository.findByName(eq(ERole.ROLE_NURSE))).thenReturn(java.util.Optional.empty());
        when(roleRepository.findByName(eq(ERole.ROLE_PATIENT))).thenReturn(java.util.Optional.empty());
        when(departmentRepository.count()).thenReturn(1L);
        when(physicianRepository.count()).thenReturn(1L);
        when(blockRepository.count()).thenReturn(1L);

        systemInitializer.initDatabase(
                userRepository,
                roleRepository,
                departmentRepository,
                physicianRepository,
                blockRepository,
                roomRepository,
                passwordEncoder
        ).run();

        verify(userRepository, never()).save(argThat(user -> "aligned-admin".equals(user.getUsername())));
        verify(roleRepository, times(5)).findByName(any(ERole.class));
    }

    @Test
    void initDatabase_shouldUseConfiguredCredentialsOutsideDevWhenProvided() throws Exception {
        ReflectionTestUtils.setField(systemInitializer, "adminUsername", "prod-admin");
        ReflectionTestUtils.setField(systemInitializer, "adminPassword", "prod-secret");
        ReflectionTestUtils.setField(systemInitializer, "adminEmail", "prod@local.test");

        when(userRepository.existsByUsername("prod-admin")).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(java.util.Optional.of(new Role(1, ERole.ROLE_ADMIN)));
        when(roleRepository.findByName(eq(ERole.ROLE_DOCTOR))).thenReturn(java.util.Optional.empty());
        when(roleRepository.findByName(eq(ERole.ROLE_NURSE))).thenReturn(java.util.Optional.empty());
        when(roleRepository.findByName(eq(ERole.ROLE_PATIENT))).thenReturn(java.util.Optional.empty());
        when(departmentRepository.count()).thenReturn(1L);
        when(physicianRepository.count()).thenReturn(1L);
        when(blockRepository.count()).thenReturn(1L);

        systemInitializer.initDatabase(
                userRepository,
                roleRepository,
                departmentRepository,
                physicianRepository,
                blockRepository,
                roomRepository,
                passwordEncoder
        ).run();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertTrue(passwordEncoder.matches("prod-secret", userCaptor.getValue().getPassword()));
        assertFalse(userCaptor.getValue().getPassword().isBlank());
    }
}
