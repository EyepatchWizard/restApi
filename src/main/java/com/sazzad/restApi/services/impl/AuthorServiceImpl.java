package com.sazzad.restApi.services.impl;

import com.sazzad.restApi.domain.entities.AuthorEntity;
import com.sazzad.restApi.repositories.AuthorRepository;
import com.sazzad.restApi.services.AuthorService;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity createAuthorEntity(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }
}
