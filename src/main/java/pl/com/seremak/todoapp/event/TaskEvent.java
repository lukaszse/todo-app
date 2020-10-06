package pl.com.seremak.todoapp.event;

import pl.com.seremak.todoapp.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {

    public static TaskEvent changed(Task source) {
        return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }

    private int taskId;
    private Instant occurrence;

    TaskEvent(int taskId, Clock clock) {
        this.taskId = taskId;
        this.occurrence = Instant.now(clock);
    }

    int getTaskId() {
        return taskId;
    }

    Instant getOccurrence() {
        return occurrence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "taskId=" + taskId +
                ", occurrence=" + occurrence +
                '}';
    }
}
