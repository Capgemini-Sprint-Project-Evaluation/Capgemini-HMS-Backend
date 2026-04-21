package com.capgemini.hms;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class HmsFrontendApplicationTest {

    @Test
    void main_shouldDelegateToSpringApplicationRun() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            HmsFrontendApplication.main(new String[]{"arg1"});

            springApplication.verify(() -> SpringApplication.run(HmsFrontendApplication.class, new String[]{"arg1"}));
        }
    }
}
