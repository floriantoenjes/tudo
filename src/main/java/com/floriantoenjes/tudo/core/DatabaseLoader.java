package com.floriantoenjes.tudo.core;

import com.floriantoenjes.tudo.todo.Todo;
import com.floriantoenjes.tudo.todo.TodoList;
import com.floriantoenjes.tudo.todo.TodoListRepository;
import com.floriantoenjes.tudo.todo.TodoRepository;
import com.floriantoenjes.tudo.user.Role;
import com.floriantoenjes.tudo.user.RoleRepository;
import com.floriantoenjes.tudo.user.User;
import com.floriantoenjes.tudo.user.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DatabaseLoader implements ApplicationRunner {

    private RoleRepository roleRepository;

    private TodoListRepository todoListRepository;

    private TodoRepository todoRepository;

    private UserRepository userRepository;

    public DatabaseLoader(RoleRepository roleRepository, TodoListRepository todoListRepository, TodoRepository todoRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.todoListRepository = todoListRepository;
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Role role_user = new Role("ROLE_USER");
        roleRepository.save(role_user);

        User user = new User();
        user.setUsername("user");
        user.setEmail("email@email.com");
        user.setPassword("password");

        user.addRole(role_user);

        userRepository.save(user);

        TodoList todoList1 = new TodoList();
        todoList1.setCreator(user);
        todoList1.setName("TodoList1");
        todoListRepository.save(todoList1);

        Todo todo1 = new Todo();
        todo1.setName("Todo1");
        todo1.setCreatedAt(new Date());
        todo1.setCreator(user);

        todoList1.addTodo(todo1);
        todoRepository.save(todo1);
    }
}
