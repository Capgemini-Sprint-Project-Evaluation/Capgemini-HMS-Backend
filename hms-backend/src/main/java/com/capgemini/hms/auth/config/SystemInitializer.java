package com.capgemini.hms.auth.config;

import com.capgemini.hms.auth.entity.Department;
import com.capgemini.hms.auth.entity.ERole;
import com.capgemini.hms.auth.entity.Role;
import com.capgemini.hms.auth.entity.User;
import com.capgemini.hms.auth.repository.DepartmentRepository;
import com.capgemini.hms.auth.repository.RoleRepository;
import com.capgemini.hms.auth.repository.UserRepository;
import com.capgemini.hms.infrastructure.entity.Block;
import com.capgemini.hms.infrastructure.entity.BlockId;
import com.capgemini.hms.infrastructure.entity.Room;
import com.capgemini.hms.infrastructure.repository.BlockRepository;
import com.capgemini.hms.infrastructure.repository.RoomRepository;
import com.capgemini.hms.scheduling.entity.Physician;
import com.capgemini.hms.scheduling.repository.PhysicianRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@lombok.RequiredArgsConstructor
public class SystemInitializer {
    private static final Logger log = LoggerFactory.getLogger(SystemInitializer.class);

    private final Environment environment;

    @Value("${admin.username:}")
    private String adminUsername;

    @Value("${admin.password:}")
    private String adminPassword;

    @Value("${admin.email:admin@local.test}")
    private String adminEmail;

    @Value("${admin.dev-fallback.username:dev-admin}")
    private String devFallbackUsername;

    @Value("${admin.dev-fallback.password:}")
    private String devFallbackPassword;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, 
                                   RoleRepository roleRepository, 
                                   DepartmentRepository departmentRepository,
                                   PhysicianRepository physicianRepository,
                                   BlockRepository blockRepository,
                                   RoomRepository roomRepository,
                                   BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            log.info("--- HMS SYSTEM INITIALIZATION: STARTING ---");
            seedRoles(roleRepository);
            seedFirstAdmin(userRepository, roleRepository, passwordEncoder);
            seedDepartments(departmentRepository);
            seedPhysicians(physicianRepository);
            seedBlocksAndRooms(blockRepository, roomRepository);
            log.info("--- HMS SYSTEM INITIALIZATION: COMPLETED ---");
        };
    }

    private void seedRoles(RoleRepository roleRepository) {
        for (ERole erole : ERole.values()) {
            if (roleRepository.findByName(erole).isEmpty()) {
                roleRepository.save(new Role(null, erole));
                log.info("Seeded role: {}", erole);
            }
        }
    }

    private void seedFirstAdmin(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        String resolvedUsername = resolveAdminUsername();
        String resolvedPassword = resolveAdminPassword();
        if (resolvedUsername.isBlank() || resolvedPassword.isBlank()) {
            log.warn("Admin seeding skipped because credentials are not configured for the active environment.");
            return;
        }

        if (!userRepository.existsByUsername(resolvedUsername)) {
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new IllegalStateException("Error: ADMIN role not found."));

            User admin = User.builder()
                    .username(resolvedUsername)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(resolvedPassword))
                    .roles(Collections.singleton(adminRole))
                    .build();

            userRepository.save(admin);
            log.info("Created first ADMIN account: {}", resolvedUsername);
        } else {
            userRepository.findByUsername(resolvedUsername).ifPresent(admin -> {
                Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
                boolean updated = false;

                if (!resolvedPassword.isBlank() && !passwordEncoder.matches(resolvedPassword, admin.getPassword())) {
                    admin.setPassword(passwordEncoder.encode(resolvedPassword));
                    updated = true;
                }

                if (!adminEmail.equals(admin.getEmail())) {
                    admin.setEmail(adminEmail);
                    updated = true;
                }

                if (!admin.getRoles().contains(adminRole)) {
                    admin.getRoles().add(adminRole);
                    updated = true;
                }

                if (updated) {
                    userRepository.save(admin);
                    log.info("Synchronized existing ADMIN account: {}", resolvedUsername);
                }
            });
        }
    }

    private String resolveAdminUsername() {
        if (!adminUsername.isBlank()) {
            return adminUsername;
        }
        return isDevProfileActive() ? devFallbackUsername : "";
    }

    private String resolveAdminPassword() {
        if (!adminPassword.isBlank()) {
            return adminPassword;
        }
        return isDevProfileActive() ? devFallbackPassword : "";
    }

    private boolean isDevProfileActive() {
        return environment.acceptsProfiles(Profiles.of("dev"));
    }

    private void seedDepartments(DepartmentRepository departmentRepository) {
        if (departmentRepository.count() == 0) {
            departmentRepository.saveAll(Arrays.asList(
                Department.builder().departmentId(1).name("Cardiology").build(),
                Department.builder().departmentId(2).name("Radiology").build(),
                Department.builder().departmentId(3).name("Geriatrics").build(),
                Department.builder().departmentId(4).name("Emergency").build()
            ));
            log.info("Seeded Hospital Departments.");
        }
    }

    private void seedPhysicians(PhysicianRepository physicianRepository) {
        if (physicianRepository.count() == 0) {
            physicianRepository.saveAll(Arrays.asList(
                Physician.builder().employeeId(101).name("Dr. Rahul Sharma").position("Senior Cardiologist").ssn(111222).build(),
                Physician.builder().employeeId(102).name("Dr. John Doe").position("Radiology Head").ssn(333444).build(),
                Physician.builder().employeeId(103).name("Dr. Jane Smith").position("General Physician").ssn(555666).build()
            ));
            log.info("Seeded Physicians.");
        }
    }

    private void seedBlocksAndRooms(BlockRepository blockRepository, RoomRepository roomRepository) {
        if (blockRepository.count() == 0) {
            Block b1 = blockRepository.save(new Block(new BlockId(1, 1)));
            Block b2 = blockRepository.save(new Block(new BlockId(1, 2)));
            
            roomRepository.saveAll(Arrays.asList(
                Room.builder().roomNumber(101).roomType("ICU").block(b1).unavailable(false).build(),
                Room.builder().roomNumber(102).roomType("Standard").block(b1).unavailable(false).build(),
                Room.builder().roomNumber(201).roomType("Deluxe").block(b2).unavailable(false).build()
            ));
            log.info("Seeded Blocks and Rooms.");
        }
    }
}
