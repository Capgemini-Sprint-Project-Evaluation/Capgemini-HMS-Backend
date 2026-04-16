package com.capgemini.hms.auth.config;

import com.capgemini.hms.auth.entity.ERole;
import com.capgemini.hms.auth.entity.Role;
import com.capgemini.hms.auth.entity.User;
import com.capgemini.hms.room.entity.Block;
import com.capgemini.hms.room.entity.BlockId;
import com.capgemini.hms.room.repository.BlockRepository;
import com.capgemini.hms.auth.repository.RoleRepository;
import com.capgemini.hms.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SystemInitializer is responsible for ensuring the core infrastructure 
 * (Roles and Primary Admin) is present in the database on every startup.
 */
@Component
@Order(1)
public class SystemInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder encoder;
    @Autowired private BlockRepository blockRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- HMS SYSTEM INITIALIZATION: STARTING ---");

        // 1. Efficiently Ensure All Security Roles Exist (Single Query)
        List<Role> existingRoles = roleRepository.findAll();
        Set<ERole> existingEnumRoles = existingRoles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Map<ERole, Role> rolesMap = new HashMap<>();
        // Add existing ones to the map for quick lookup during Admin creation
        existingRoles.forEach(r -> rolesMap.put(r.getName(), r));

        for (ERole erole : ERole.values()) {
            if (!existingEnumRoles.contains(erole)) {
                System.out.println("Initializing missing role: " + erole);
                Role newRole = roleRepository.save(new Role(erole));
                rolesMap.put(erole, newRole);
            }
        }

        // 2. Ensure Primary Admin Account Exists (Akash Gaikwad)
        if (!userRepository.existsByUsername("teamtechie14")) {
            User admin = new User("teamtechie14", "team14@gmail.com", encoder.encode("capg1234"));
            admin.setRoles(Collections.singleton(rolesMap.get(ERole.ROLE_ADMIN)));
            userRepository.save(admin);
            System.out.println("✅ Primary Admin account 'teamtechie14' initialized successfully.");
        } else {
            System.out.println("ℹ️ Primary Admin account already exists. Skipping.");
        }

        // 3. Ensure Physical Block infrastructure exists
        initializeBlocks();

        System.out.println("--- HMS SYSTEM INITIALIZATION: COMPLETED ---");
    }

    private void initializeBlocks() {
        BlockId blockId = new BlockId(2, 1);
        if (!blockRepository.existsById(blockId)) {
            System.out.println("Initializing required clinical block: Floor 2, Code 1");
            Block block = new Block();
            block.setId(blockId);
            blockRepository.save(block);
            System.out.println("✅ Clinical block initialized successfully.");
        }
    }
}
