package pl.com.seremak.todoapp.model.projection;

import pl.com.seremak.todoapp.model.Task;

import java.time.LocalDateTime;

public class GroupTaskWriteModel {

    // == fields ==
    private String description;
    private LocalDateTime deadline;

    // == methods ==
    // -- getters & setters --
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Task toTask() {
        return  new Task(description, deadline);
    }
}
