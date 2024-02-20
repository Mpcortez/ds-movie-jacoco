package com.devsuperior.dsmovie.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class MovieServiceTests {

    @InjectMocks
    private MovieService service;

    @Test
    void findAllShouldReturnPagedMovieDTO() {
    }

    @Test
    void findByIdShouldReturnMovieDTOWhenIdExists() {
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    }

    @Test
    void insertShouldReturnMovieDTO() {
    }

    @Test
    void updateShouldReturnMovieDTOWhenIdExists() {
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    }

    @Test
    void deleteShouldDoNothingWhenIdExists() {
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenDependentId() {
    }
}
