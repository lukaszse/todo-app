package pl.com.seremak.todoapp.model.projection;

import pl.com.seremak.todoapp.model.Task;

public class GroupTaskReadModel {

    // == fields ==
    private String description;
    private boolean done;

    // == constructors ==
    GroupTaskReadModel(Task source) {
        this.description = source.getDescription();
        this.done = source.isDone();
    }

    // == methods ==
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }
}
