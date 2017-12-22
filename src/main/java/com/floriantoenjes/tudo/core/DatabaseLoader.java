package com.floriantoenjes.tudo.core;

import com.floriantoenjes.tudo.contactrequest.ContactRequest;
import com.floriantoenjes.tudo.contactrequest.ContactRequestRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
//@Profile("!test")
public class DatabaseLoader implements ApplicationRunner {


    private ContactRequestRepository contactRequestRepository;

    private PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    private TodoListRepository todoListRepository;

    private TodoRepository todoRepository;

    private UserRepository userRepository;

    public DatabaseLoader(ContactRequestRepository contactRequestRepository, PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository, TodoListRepository todoListRepository,
                          TodoRepository todoRepository, UserRepository userRepository) {
        this.contactRequestRepository = contactRequestRepository;
        this.passwordEncoder = passwordEncoder;

        this.roleRepository = roleRepository;
        this.todoListRepository = todoListRepository;
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("load_user", "load_user",
                        AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN")));

        Role roleUser = new Role("ROLE_USER");
        roleRepository.save(roleUser);

        User user = new User("user", "email@email.com", passwordEncoder.encode("password"));
        user.addRole(roleUser);
        userRepository.save(user);

        User user2 = new User("user2", "email2@email.com", passwordEncoder.encode("password"));
        user2.addRole(roleUser);
        userRepository.save(user2);

        User user3 = new User("user3", "email3@email.com", passwordEncoder.encode("password"));
        user3.addRole(roleUser);
        userRepository.save(user3);

        User user4 = new User("user4", "email4@email.com", passwordEncoder.encode("password"));
        user4.addRole(roleUser);
        userRepository.save(user4);

        User user5 = new User("user5", "email5@email.com", passwordEncoder.encode("password"));
        user5.addRole(roleUser);
        userRepository.save(user5);

//        user.addContact(user3);
//        user.addContact(user4);
//        userRepository.save(user);

        TodoList todoList1 = new TodoList("TodoList1");
        todoList1.setCreator(user);
        todoListRepository.save(todoList1);

        Todo todo1 = new Todo("Todo1");
        todo1.setCreator(user);
        todo1.setDescription("Description of Todo1");
        todo1.addTag("tag");
        todo1.addTag("tag2");

        Todo todo2 = new Todo("Todo2");
        todo2.setCreator(user2);

        Todo todo3 = new Todo("Todo3");
        todo3.setCreator(user);
        todo3.addTag("tag");

        todoList1.addTodo(todo1);

//        todo1.assignToUser(user3);

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setSender(user);
        contactRequest.setReceiver(user2);
        contactRequestRepository.save(contactRequest);

        ContactRequest contactRequest2 = new ContactRequest();
        contactRequest2.setSender(user5);
        contactRequest2.setReceiver(user);
        contactRequestRepository.save(contactRequest2);
    }
}
