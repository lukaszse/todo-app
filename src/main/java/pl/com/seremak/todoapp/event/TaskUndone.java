package pl.com.seremak.todoapp.event;

import pl.com.seremak.todoapp.model.Task;

import java.time.Clock;

public class TaskUndone extends TaskEvent {
    public TaskUndone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
