package com.floriantoenjes.tudo.todo.todoform;

import com.floriantoenjes.tudo.todo.Todo;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class TodoForm {

    @Min(0)
    @Max(100)
    private Long progress = 0L;

    private boolean completed;

    private Date completedAt;

    private Date lastUpdated;

}
