package com.devsuperior.dsmovie.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Test
    void authenticatedShouldReturnUserEntityWhenUserExists() {
    }

    @Test
    void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
    }

    @Test
    void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
    }

    @Test
    void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
    }
}
