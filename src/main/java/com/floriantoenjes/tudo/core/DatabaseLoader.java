package com.floriantoenjes.tudo.core;

import com.floriantoenjes.tudo.category.Category;
import com.floriantoenjes.tudo.category.CategoryRepository;
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

    private CategoryRepository categoryRepository;

    private RoleRepository roleRepository;

    private TodoListRepository todoListRepository;

    private TodoRepository todoRepository;

    private UserRepository userRepository;

    public DatabaseLoader(CategoryRepository categoryRepository, RoleRepository roleRepository, TodoListRepository todoListRepository, TodoRepository todoRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
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

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("email2@email.com");
        user2.setPassword("password");

        user2.addRole(role_user);

        userRepository.save(user2);

        TodoList todoList1 = new TodoList();
        todoList1.setCreator(user);
        todoList1.setName("TodoList1");
        todoListRepository.save(todoList1);

        Category category = new Category();
        category.setName("Category1");


        Todo todo1 = new Todo();
        todo1.setName("Todo1");
        todo1.setCreatedAt(new Date());
        todo1.setCreator(user2);
        todo1.addCategory(category);

        todoList1.addTodo(todo1);
        todoRepository.save(todo1);
        categoryRepository.save(category);
    }
}
