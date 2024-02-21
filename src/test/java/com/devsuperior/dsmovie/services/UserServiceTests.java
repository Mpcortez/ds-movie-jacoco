package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private CustomUserUtil userUtil;

    private String existingUserName, nonExistingUsername;

    @BeforeEach
    void setUp() {
        existingUserName = "maria@gmail.com";
        nonExistingUsername = "nonExisting@gmail.com";

        UserEntity user = UserFactory.createUserEntity();
        List<UserDetailsProjection> userDetails = UserDetailsFactory.createCustomClientUser(existingUserName);

        when(repository.findByUsername(existingUserName)).thenReturn(Optional.of(user));
        when(repository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        when(repository.searchUserAndRolesByUsername(existingUserName)).thenReturn(userDetails);
        when(repository.searchUserAndRolesByUsername(nonExistingUsername)).thenReturn(new ArrayList<>());
    }

    @Test
    void authenticatedShouldReturnUserEntityWhenUserExists() {
        when(userUtil.getLoggedUsername()).thenReturn(existingUserName);

        UserEntity result = service.authenticated();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingUserName, result.getUsername());
    }

    @Test
    void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
        doThrow(ClassCastException.class).when(userUtil).getLoggedUsername();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> service.authenticated());
    }

    @Test
    void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
        UserDetails result = service.loadUserByUsername(existingUserName);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingUserName, result.getUsername());
    }

    @Test
    void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(nonExistingUsername));
    }
}
