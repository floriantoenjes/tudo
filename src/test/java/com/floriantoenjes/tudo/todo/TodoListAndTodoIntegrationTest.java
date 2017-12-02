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

import static org.hamcrest.Matchers.hasSize;
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
        User testUser = createTestUser("test_user");
        createTestTodoList("test_todo_list", testUser);
        createTestTodo("test_todo", testUser);

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
        User testUser = createTestUser("test_user");
        TodoList testTodoList = createTestTodoList("test_todo_list", testUser);
        Todo testTodo = createTestTodo("test_todo", testUser);
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

    @Test(expected = NestedServletException.class)
    public void shouldNotAssignUserToTodoButThrowException() throws Exception {
        User testUser = createTestUser("test_user");
        Todo todo = createTestTodo("test_todo", testUser);
        User testUser2 = createTestUser("test_user_2");

        mockMvc.perform(put("/api/v1/todos/1/assignedUsers")
                .with(httpBasic("test_user", "password"))
                .contentType("text/uri-list")
                .content("/api/v1/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldAssignUserToTodo() throws Exception {
        User testUser = createTestUser("test_user");
        Todo todo = createTestTodo("test_todo", testUser);
        testUser.addContact(createTestUser("test_user_2"));
        userRepository.save(testUser);

        mockMvc.perform(put("/api/v1/todos/1/assignedUsers")
                .with(httpBasic("test_user", "password"))
                .contentType("text/uri-list")
                .content("/api/v1/users/2"))
                .andExpect(status().isNoContent());
    }

    private User createTestUser(String username) {
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setEmail(username + "@email.com");
        testUser.setPassword("password");
        testUser.addRole(roleRepository.findByName("ROLE_USER"));
        return userRepository.save(testUser);
    }

    private Todo createTestTodo(String name, User user) {
        Todo testTodo = new Todo();
        testTodo.setCreator(user);
        testTodo.setName(name);
        return todoRepository.save(testTodo);
    }

    private TodoList createTestTodoList(String name, User user) {
        TodoList testTodoList = new TodoList();
        testTodoList.setName(name);
        testTodoList.setCreator(user);
        return todoListRepository.save(testTodoList);
    }
}
