package com.sazzad.restApi.services;

import com.sazzad.restApi.domain.entities.AuthorEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthorService {

    AuthorEntity createAuthorEntity(AuthorEntity authorEntity);
}
