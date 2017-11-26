package com.floriantoenjes.tudo.core;

import com.floriantoenjes.tudo.todo.Todo;
import com.floriantoenjes.tudo.todo.TodoRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DatabaseLoader implements ApplicationRunner {

    private TodoRepository todoRepository;

    public DatabaseLoader(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Todo todo1 = new Todo();
        todo1.setName("Todo1");
        todo1.setCreatedAt(new Date());

        todoRepository.save(todo1);
    }
}
