package com.sazzad.restApi.services.impl;

import com.sazzad.restApi.domain.entities.AuthorEntity;
import com.sazzad.restApi.repositories.AuthorRepository;
import com.sazzad.restApi.services.AuthorService;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }

    @Override
    public boolean isExists(Long id) {
        return authorRepository.existsById(id);
    }

    @Override
    public Optional<AuthorEntity> findOne(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<AuthorEntity> findAll() {

        return StreamSupport
                .stream(
                        authorRepository.findAll().spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity) {

        authorEntity.setId(id);
        return authorRepository.findById(id).map(existingAuthor -> {

            Optional.ofNullable(authorEntity.getAge()).ifPresent(existingAuthor::setAge);
            Optional.ofNullable(authorEntity.getName()).ifPresent(existingAuthor::setName);
            return authorRepository.save(existingAuthor);

        }).orElseThrow(() -> new RuntimeException("Author Does Not Exists"));
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
