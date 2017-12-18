package com.floriantoenjes.tudo.todo.todoform;

import com.floriantoenjes.tudo.todo.Todo;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Data
@Entity
public class TodoForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(0)
    @Max(100)
    private Long progress = 0L;

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
