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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
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

        Role roleUser = new Role("ROLE_USER");
        roleRepository.save(roleUser);

        User user = new User("user", "email@email.com", "password");
        user.addRole(roleUser);
        userRepository.save(user);

        User user2 = new User("user2", "email2@email.com", "password");
        user2.addRole(roleUser);
        userRepository.save(user2);

        TodoList todoList1 = new TodoList("TodoList1");
        todoList1.setCreator(user);
        todoListRepository.save(todoList1);

        Todo todo1 = new Todo("Todo1");
        todo1.setCreator(user);
        todo1.addTag("tag");

        Todo todo2 = new Todo("Todo2");
        todo2.setCreator(user2);

        Todo todo3 = new Todo("Todo3");
        todo3.setCreator(user);
        todo3.addTag("tag");

        todoList1.addTodo(todo1);

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);
    }
}
