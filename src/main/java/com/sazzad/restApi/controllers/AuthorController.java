package com.sazzad.restApi.controllers;

import com.sazzad.restApi.domain.dto.AuthorDto;
import com.sazzad.restApi.domain.entities.AuthorEntity;
import com.sazzad.restApi.mappers.Mapper;
import com.sazzad.restApi.services.AuthorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorController {

    private AuthorService authorService;

    private Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity,AuthorDto> authorMapper) {
        this.authorService = authorService;

        this.authorMapper = authorMapper;
    }

    @PostMapping(path = "/author")
    public AuthorDto createAuthorEntity(@RequestBody AuthorDto authorDto){

        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity savedAuthorEntity = authorService.createAuthorEntity(authorEntity);
        return authorMapper.mapTo(savedAuthorEntity);
    }
}
