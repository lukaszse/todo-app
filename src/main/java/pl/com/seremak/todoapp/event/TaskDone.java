package pl.com.seremak.todoapp.event;

import pl.com.seremak.todoapp.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent {
    public TaskDone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
