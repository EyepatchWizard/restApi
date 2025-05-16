package com.sazzad.restApi.repositories;

import com.sazzad.restApi.domain.entities.BookEntity;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<BookEntity, String> {
}
