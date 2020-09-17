package pl.com.seremak.todoapp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "tasks")
public class Task {

    // == fields ==
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Task's description mustn't be empty")
    private String description;

    private boolean done;

    // == constructors ==
    public Task() {
    }

    // == methods ==
    // -- getters & setters --
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getId() {
        return id;
    }
}
