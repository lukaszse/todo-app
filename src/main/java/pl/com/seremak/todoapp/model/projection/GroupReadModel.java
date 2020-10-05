package pl.com.seremak.todoapp.model.projection;

import pl.com.seremak.todoapp.model.Task;
import pl.com.seremak.todoapp.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {

    // == fields ==
    private int id;
    private String description;

    /*
    * Deadline from the latest task in group
    * */
    private LocalDateTime deadline;

    private Set<GroupTaskReadModel> tasks;

    // == constructors ==
    public GroupReadModel(TaskGroup source) {
        this.id = source.getId();
        this.description = source.getDescription();
        source.getTasks()
                .stream()
                .map(Task::getDeadline)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> deadline = date);
        this.tasks = source.getTasks()
                .stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toSet());
    }

    // == methods ==
    // -- getters & setters --
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
