package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {

    private static final String RESOURCE_NOT_FOUND = "Recurso não encontrado";

    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<MovieDTO> findAll(String title, Pageable pageable) {
        Page<MovieEntity> result = repository.searchByTitle(title, pageable);
        return result.map(MovieDTO::new);
    }

    @Transactional(readOnly = true)
    public MovieDTO findById(Long id) {
        MovieEntity result = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND));
        return new MovieDTO(result);
    }

    @Transactional
    public MovieDTO insert(MovieDTO dto) {
        MovieEntity entity = new MovieEntity();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new MovieDTO(entity);
    }

    @Transactional
    public MovieDTO update(Long id, MovieDTO dto) {
        try {
            MovieEntity entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new MovieDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }
    }

    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(MovieDTO dto, MovieEntity entity) {
        entity.setTitle(dto.getTitle());
        entity.setScore(dto.getScore());
        entity.setCount(dto.getCount());
        entity.setImage(dto.getImage());
    }
}