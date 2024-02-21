package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MovieServiceTests {

    @InjectMocks
    private MovieService service;

    @Mock
    private MovieRepository repository;

    private long existingId, nonExistingId, dependentId;

    private String expectedTitle;

    private MovieEntity movie;
    private MovieDTO movieDTO;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        expectedTitle = "Test Movie";

        movie = MovieFactory.createMovieEntity();
        movieDTO = MovieFactory.createMovieDTO();

        PageImpl<MovieEntity> moviePage = new PageImpl<>(List.of(movie));

        when(repository.searchByTitle(any(), any())).thenReturn(moviePage);

        when(repository.findById(existingId)).thenReturn(Optional.of(movie));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(repository.save(any())).thenReturn(movie);

        when(repository.getReferenceById(existingId)).thenReturn(movie);
        doThrow(EntityNotFoundException.class).when(repository).getReferenceById(nonExistingId);

        when(repository.existsById(existingId)).thenReturn(true);
        when(repository.existsById(dependentId)).thenReturn(true);
        when(repository.existsById(nonExistingId)).thenReturn(false);

        doNothing().when(repository).deleteById(existingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    void findAllShouldReturnPagedMovieDTO() {
        PageRequest pageRequest = PageRequest.of(0, 12);

        Page<MovieDTO> result = service.findAll("Test Movie", pageRequest);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getContent().isEmpty());
        Assertions.assertEquals(1, result.getTotalPages());
    }

    @Test
    void findByIdShouldReturnMovieDTOWhenIdExists() {
        MovieDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());

        verify(repository, times(1)).findById(existingId);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));

        verify(repository, times(1)).findById(nonExistingId);
    }

    @Test
    void insertShouldReturnMovieDTO() {
        MovieDTO result = service.insert(movieDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedTitle, result.getTitle());
    }

    @Test
    void updateShouldReturnMovieDTOWhenIdExists() {
        MovieDTO result = service.update(existingId, movieDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals(expectedTitle, result.getTitle());

        verify(repository, times(1)).save(movie);
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistingId, movieDTO));

        verify(repository, times(0)).save(movie);
    }

    @Test
    void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> service.delete(existingId));

        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));

        verify(repository, times(0)).deleteById(nonExistingId);
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Assertions.assertThrows(DatabaseException.class, () -> service.delete(dependentId));

        verify(repository, times(1)).deleteById(dependentId);
    }
}
