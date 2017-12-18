package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.RoleRepository;
import com.floriantoenjes.tudo.user.User;
import com.floriantoenjes.tudo.user.UserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import static com.floriantoenjes.tudo.TestUtils.getJwtToken;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser
public class TodoListAndTodoIntegrationTest {
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DirtiesContext
    public void shouldAddTodoToTodoList() throws Exception {
        mockMvc.perform(get("http://localhost/api/v1/todoLists/1/todos")
                .header("Authorization", getJwtToken(mockMvc, "user", "password"))
        .contentType("application/hal+json;charset=UTF-8"))
                .andExpect(jsonPath("$._embedded.todos", hasSize(1)));

        mockMvc.perform(put("/api/v1/todos/3/todoList")
                .header("Authorization", getJwtToken(mockMvc, "user", "password"))
                .contentType("text/uri-list")
        .content("/api/v1/todoLists/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("http://localhost/api/v1/todoLists/1/todos")
                .header("Authorization", getJwtToken(mockMvc, "user", "password"))
                .contentType("application/hal+json;charset=UTF-8"))
                .andExpect(jsonPath("$._embedded.todos", hasSize(2)));
    }

    @Test
    @DirtiesContext
    public void shouldRemoveTodoFromTodoList() throws Exception {
        mockMvc.perform(get("/api/v1/todos/1/todoList")
                .header("Authorization", getJwtToken(mockMvc, "user", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json;charset=UTF-8"));

        mockMvc.perform(delete("/api/v1/todos/1/todoList")
                .header("Authorization", getJwtToken(mockMvc, "user", "password")))
        .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/todos/1/todoList")
                .header("Authorization", getJwtToken(mockMvc, "user", "password")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotAssignCreatorToTodo() throws Exception {
        mockMvc.perform(put("/api/v1/todos/1/assignedUsers")
                .header("Authorization", getJwtToken(mockMvc, "user", "password"))
                .contentType("text/uri-list")
                .content("/api/v1/users/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldAssignUserToTodo() throws Exception {
        mockMvc.perform(put("/api/v1/todos/1/assignedUsers")
                .header("Authorization", getJwtToken(mockMvc, "user", "password"))
                .contentType("text/uri-list")
                .content("/api/v1/users/3"))
                .andExpect(status().isNoContent());
    }

}
