package com.floriantoenjes.tudo.todo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser
public class TodoListAndTodoIntegrationTest {
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
    public void shouldAddTodoToTodoList() throws Exception {
        mockMvc.perform(get("http://localhost/api/v1/todoLists/1/todos").with(httpBasic("user", "password"))
        .contentType("application/hal+json;charset=UTF-8")).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$._embedded.todos", hasSize(0)));


        mockMvc.perform(put("/api/v1/todos/3/todoList").with(httpBasic("user", "password")).contentType("text/uri-list")
        .content("/api/v1/todoLists/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("http://localhost/api/v1/todoLists/1/todos").with(httpBasic("user", "password"))
                .contentType("application/hal+json;charset=UTF-8")).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$._embedded.todos", hasSize(1)));
    }
}
