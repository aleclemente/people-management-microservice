package com.aleclemente.peoplemanagementmicroservice;

import com.aleclemente.peoplemanagementmicroservice.PeopleManagementMicroserviceApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = PeopleManagementMicroserviceApplication.class)
public abstract class IntegrationTest {
}
