package com.floriantoenjes.tudo.todo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser
public class TodoRepositoryTest {
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

//  ToDo: Write tests for PUT method

    private String getJwtToken(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andReturn();

        return result.getResponse().getHeader("Authorization");
    }

    @Test
    public void findOneWithWrongUserShouldReturnUnauthorized() throws Exception {
        String user2jwt = getJwtToken("user2", "password");

        mockMvc.perform(get("/api/v1/todos/1").header("Authorization", user2jwt))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void findOneWithCorrectUserShouldReturnTodo() throws Exception {
        String userJwt = getJwtToken("user", "password");

        mockMvc.perform(get("/api/v1/todos/1").header("Authorization", userJwt))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json;charset=UTF-8"));
    }


    @Test
    public void deleteWithWrongUserShouldReturnUnauthorized() throws Exception {
        String user2jwt = getJwtToken("user2", "password");

        mockMvc.perform(delete("/api/v1/todos/1").header("Authorization", user2jwt))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteWithCorrectUserShouldReturnOk() throws Exception {
        String userJwt = getJwtToken("user", "password");

        mockMvc.perform(delete("/api/v1/todos/1").header("Authorization", userJwt))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    public void findAllWithUserRoleShouldReturnUnauthorized() throws Exception {
        String userJwt = getJwtToken("user", "password");

        mockMvc.perform(get("/api/v1/todos").header("Authorization", userJwt))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    public void findAllByCreatorWithoutUserShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/todos/search/findAllByCreator?creator=/api/v1/users/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void findAllByCreatorWithWrongUserShouldReturnUnauthorized() throws Exception {
        String user2jwt = getJwtToken("user2", "password");

        mockMvc.perform(get("/api/v1/todos/search/findAllByCreator?creator=/api/v1/users/1")
                .header("Authorization", user2jwt))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void findAllByCreatorWithCorrectUserShouldReturnTodos() throws Exception {
        String userJwt = getJwtToken("user", "password");

        mockMvc.perform(get("/api/v1/todos/search/findAllByCreator?creator=/api/v1/users/1")
                .header("Authorization", userJwt))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json;charset=UTF-8"));
    }


    @Test
    public void findAllByCreatorAndTagsWithWrongUserAndExistingTagShouldReturnUnauthorized() throws Exception {
        String user2jwt = getJwtToken("user2", "password");

        mockMvc.perform(get("/api/v1/todos/search/findAllByCreatorAndTags?creator=/api/v1/users/1&tag=tag")
                .header("Authorization", user2jwt))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void findAllByCreatorAndTagsWithCorrectUserAndNotExistingTagShouldReturnEmpty() throws Exception {
        String userJwt = getJwtToken("user", "password");

        mockMvc.perform(get("/api/v1/todos/search/findAllByCreatorAndTags?creator=/api/v1/users/1&tag=invalid")
                .header("Authorization", userJwt))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$._embedded.todos", hasSize(0)));
    }

    @Test
    public void findAllByCreatorAndTagsWithCorrectUserAndExistingTagShouldReturnTodos() throws Exception {
        String userJwt = getJwtToken("user", "password");

        mockMvc.perform(get("/api/v1/todos/search/findAllByCreatorAndTags?creator=/api/v1/users/1&tag=tag")
                .header("Authorization", userJwt))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json;charset=UTF-8"));
    }

}