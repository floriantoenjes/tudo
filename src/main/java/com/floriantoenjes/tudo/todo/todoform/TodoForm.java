package com.floriantoenjes.tudo.todo.todoform;

import com.floriantoenjes.tudo.todo.Todo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class TodoForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long progress;

    private boolean completed;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @OneToOne
    private Todo todo;

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = new Date();
    }
}
