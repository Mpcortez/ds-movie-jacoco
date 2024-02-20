package com.devsuperior.dsmovie.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ScoreServiceTests {

    @InjectMocks
    private ScoreService service;

    @Test
    void saveScoreShouldReturnMovieDTO() {
    }

    @Test
    void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
    }
}
