package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.RoleRepository;
import com.floriantoenjes.tudo.user.User;
import com.floriantoenjes.tudo.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
@SpringBootTest
@WithMockUser
public class TodoListAndTodoIntegrationTest {
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    WebApplicationContext context;

    private User testUser;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        testUser = createTestUser("test_user");
    }

    // ToDo: Might dirty context
    @Test
    public void shouldAddTodoToTodoList() throws Exception {
        createTestTodoList("test_todo_list");
        createTestTodo("test_todo");

        mockMvc.perform(get("http://localhost/api/v1/todoLists/1/todos")
                .with(httpBasic("test_user", "password"))
        .contentType("application/hal+json;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$._embedded.todos", hasSize(0)));

        mockMvc.perform(put("/api/v1/todos/1/todoList")
                .with(httpBasic("test_user", "password"))
                .contentType("text/uri-list")
        .content("/api/v1/todoLists/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("http://localhost/api/v1/todoLists/1/todos").with(httpBasic("test_user", "password"))
                .contentType("application/hal+json;charset=UTF-8")).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$._embedded.todos", hasSize(1)));
    }

    @Test
    public void shouldRemoveTodoFromList() throws Exception {
        TodoList testTodoList = createTestTodoList("test_todo_list");
        Todo testTodo = createTestTodo("test_todo");
        testTodoList.addTodo(testTodo);
        todoRepository.save(testTodo);

        mockMvc.perform(get("/api/v1/todos/1/todoList").with(httpBasic("test_user", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json;charset=UTF-8"));

        mockMvc.perform(delete("/api/v1/todos/1/todoList").with(httpBasic("test_user", "password")))
        .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/todos/1/todoList").with(httpBasic("test_user", "password")))
                .andExpect(status().isNotFound());
    }


    private User createTestUser(String username) {
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setEmail("test@email.com");
        testUser.setPassword("password");
        testUser.addRole(roleRepository.findByName("ROLE_USER"));
        return userRepository.save(testUser);
    }

    private Todo createTestTodo(String name) {
        Todo testTodo = new Todo();
        testTodo.setCreator(testUser);
        testTodo.setName(name);
        return todoRepository.save(testTodo);
    }

    private TodoList createTestTodoList(String name) {
        TodoList testTodoList = new TodoList();
        testTodoList.setName(name);
        testTodoList.setCreator(testUser);
        return todoListRepository.save(testTodoList);
    }
}
