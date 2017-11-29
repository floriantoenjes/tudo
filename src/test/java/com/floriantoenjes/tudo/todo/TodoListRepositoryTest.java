package com.floriantoenjes.tudo.todo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser
public class TodoListRepositoryTest {
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

    @Test
    public void findOneWithWrongUserShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/todoLists/1").with(httpBasic("user2", "password")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void findOneWithCorrectUserShouldReturnTodo() throws Exception {
        mockMvc.perform(get("/api/v1/todoLists/1").with(httpBasic("user", "password")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json;charset=UTF-8"));
    }


    @Test
    public void findAllWithUserRoleShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/todoLists").with(httpBasic("user", "password")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void deleteWithWrongUserShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/todoLists/1").with(httpBasic("user2", "password")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void deleteWithCorrectUserShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/v1/todoLists/1").with(httpBasic("user", "password")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void findAllByCreatorWithoutUserShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/todoLists/search/findAllByCreator?creator=/api/v1/users/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void findAllByCreatorWithWrongUserShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/todoLists/search/findAllByCreator?creator=/api/v1/users/1")
                .with(httpBasic("user2", "password")))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void findAllByCreatorWithCorrectUserShouldReturnTodos() throws Exception {
        mockMvc.perform(get("/api/v1/todoLists/search/findAllByCreator?creator=/api/v1/users/1")
                .with(httpBasic("user", "password")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json;charset=UTF-8"));
    }

}