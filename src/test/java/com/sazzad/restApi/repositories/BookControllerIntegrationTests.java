package com.sazzad.restApi.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sazzad.restApi.TestDataUtil;
import com.sazzad.restApi.domain.entities.AuthorEntity;
import com.sazzad.restApi.domain.entities.BookEntity;
import com.sazzad.restApi.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private BookService bookService;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateBookReturnsHttpStatus201Created() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);
        String bookJson = objectMapper.writeValueAsString(bookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/book/" + bookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateBookReturnsCreateBook() throws Exception {

        BookEntity bookEntityA = TestDataUtil.createTestBookA(null);
        String bookJson = objectMapper.writeValueAsString(bookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/book/" + bookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn ").value(bookEntityA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookEntityA.getTitle())
        );
    }

    @Test
    public void testThatGetAllBooksReturnsHttp200Ok() throws Exception {

        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        bookService.save(testBookA.getIsbn(),testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/book")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn")
                        .value("978-1-2345-6789-0")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title")
                        .value("The Shadow In The Attic")
        );
    }
    @Test
    public void testThatFinOneIsOk() throws Exception {

        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        bookService.save(testBookA.getIsbn(),testBookA);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/book/" + testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateIsOk() throws Exception {

        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        BookEntity savedBook = bookService.save(bookEntity.getIsbn(), bookEntity);

        BookEntity TestBookEntity = TestDataUtil.createTestBookA(null);
        String bookJson = objectMapper.writeValueAsString(TestBookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/book/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateIsOk() throws Exception {

        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        BookEntity savedBook = bookService.save(bookEntity.getIsbn(), bookEntity);

        BookEntity testBookEntity = TestDataUtil.createTestBookA(null);
        testBookEntity.setTitle("Updated");
        String bookJson = objectMapper.writeValueAsString(testBookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/book/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn ").value(testBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookEntity.getTitle())
        );
    }

    @Test
    public void testThatNonExistingBookDeleteIsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/book/eerew")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatExistingBookDeleteIsOk() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        BookEntity savedBook = bookService.save(bookEntity.getIsbn(), bookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/book/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
