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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

    @Test
    public void findAllShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/todos").with(httpBasic("user", "password")))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    public void findAllByCreatorShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/todos/search/findAllByCreator?creator=/api/v1/users/2"))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    public void findAllByCreatorShouldReturnTodos() throws Exception {
        mockMvc.perform(get("/api/v1/todos/search/findAllByCreator?creator=/api/v1/users/1")
        .with(httpBasic("user", "password")))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

}