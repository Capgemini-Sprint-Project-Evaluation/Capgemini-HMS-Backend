package com.capgemini.hms.security.service;

import com.capgemini.hms.auth.entity.ERole;
import com.capgemini.hms.auth.entity.Role;
import com.capgemini.hms.auth.entity.User;
import com.capgemini.hms.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_shouldMapUserToUserDetails() {
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@test.com")
                .password("encoded")
                .roles(Set.of(new Role(1, ERole.ROLE_ADMIN)))
                .build();
        when(userRepository.findByUsername("admin")).thenReturn(java.util.Optional.of(user));

        UserDetailsImpl details = (UserDetailsImpl) userDetailsService.loadUserByUsername("admin");

        assertEquals("admin", details.getUsername());
        assertEquals("admin@test.com", details.getEmail());
        assertEquals(1, details.getAuthorities().size());
    }

    @Test
    void loadUserByUsername_shouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByUsername("missing")).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("missing"));
    }
}
