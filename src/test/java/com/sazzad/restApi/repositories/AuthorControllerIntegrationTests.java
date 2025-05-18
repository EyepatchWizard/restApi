package com.sazzad.restApi.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sazzad.restApi.TestDataUtil;
import com.sazzad.restApi.domain.entities.AuthorEntity;
import com.sazzad.restApi.domain.entities.BookEntity;
import com.sazzad.restApi.services.AuthorService;
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
//@ExtendWith(Extension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private AuthorService authorService;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {

        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorA);

         mockMvc.perform(
                 MockMvcRequestBuilders.post("/author")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(authorJson)
         ).andExpect(
                 MockMvcResultMatchers.status().isCreated()
         );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {

        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("80")
        );

    }

    @Test
    public void testThatGetAllAuthorReturnsHttpStatus200Ok() throws Exception {

        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        authorService.save(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/author")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name")
                        .value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age")
                        .value(80)
        );
    }

    @Test
    public void testThatFindOneIsOk() throws Exception {

        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        authorService.save(authorEntity);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/author/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateIsOk() throws Exception {

        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntity);

        AuthorEntity TestAuthorEntity = TestDataUtil.createTestAuthorA();
        String authorJson = objectMapper.writeValueAsString(TestAuthorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/author/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateIsOk() throws Exception {

        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntity);

        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        testAuthorEntity.setName("Updated");
        String authorJson = objectMapper.writeValueAsString(testAuthorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/author/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthorEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorEntity.getAge())
        );
    }

    @Test
    public void testThatNonExistingAuthorDeleteIsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/author/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatExistingAuthorDeleteIsOk() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/author/" + authorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
