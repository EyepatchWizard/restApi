package com.sazzad.restApi.controllers;

import com.sazzad.restApi.domain.dto.BookDto;
import com.sazzad.restApi.domain.entities.BookEntity;
import com.sazzad.restApi.mappers.Mapper;
import com.sazzad.restApi.services.AuthorService;
import com.sazzad.restApi.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class BookController {

    private BookService bookService;

    private Mapper<BookEntity,BookDto> bookMapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDto> bookMapper) {

        this.bookService = bookService;

        this.bookMapper = bookMapper;
    }

    @PutMapping("/book/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto) {

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        boolean isExists = bookService.isExists(isbn);
        BookEntity savedBookEntity = bookService.save(isbn, bookEntity);
        BookDto savedBookDto = bookMapper.mapTo(savedBookEntity);

        if(isExists){
            return new ResponseEntity<>(savedBookDto, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED);
        }
    }

    @GetMapping("/book")
    public Page<BookDto> getAllBooks (Pageable pageable){

        Page<BookEntity> books = bookService.findAll(pageable);
        return books
                .map(bookMapper::mapTo);
    }

    @GetMapping("/book/{isbn}")
    public ResponseEntity<BookDto> findBook(@PathVariable("isbn") String isbn){

        Optional<BookEntity> foundBook = bookService.findOne(isbn);
        return foundBook.map(bookEntity -> {
            BookDto bookDto = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/book/{isbn}")
    public ResponseEntity<BookDto> partialUpdate(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto) {

        if(!bookService.isExists(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity updatedBook = bookService.partialUpdate(isbn, bookEntity);
        return new ResponseEntity<>(
                bookMapper.mapTo(updatedBook),HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/book/{isbn}")
    public ResponseEntity deleteBook(@PathVariable("isbn") String isbn) {
        bookService.delete(isbn);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
