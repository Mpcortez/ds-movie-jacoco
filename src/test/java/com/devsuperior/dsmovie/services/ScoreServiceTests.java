package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ScoreServiceTests {

    @InjectMocks
    private ScoreService service;

    @Mock
    private UserService userService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ScoreRepository scoreRepository;

    private Long existingMovieId, nonExistingMovieId;

    private MovieEntity movie;

    private ScoreEntity score;
    private ScoreDTO scoreDTO;

    @BeforeEach
    void setUp() {
        existingMovieId = 1L;
        nonExistingMovieId = 2L;

        UserEntity user = UserFactory.createUserEntity();

        movie = MovieFactory.createMovieEntity();

        score = ScoreFactory.createScoreEntity();
        scoreDTO = ScoreFactory.createScoreDTO();

        when(userService.authenticated()).thenReturn(user);

        when(scoreRepository.saveAndFlush(any())).thenReturn(score);
        when(movieRepository.save(any())).thenReturn(movie);
    }

    @Test
    void saveScoreShouldReturnMovieDTO() {
        when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));

        MovieDTO result = service.saveScore(scoreDTO);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getScore());
        Assertions.assertEquals(score.getId().getMovie().getId(), result.getId());
    }

    @Test
    void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
        when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.saveScore(scoreDTO));
    }
}
