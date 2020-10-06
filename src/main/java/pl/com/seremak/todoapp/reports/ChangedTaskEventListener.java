package pl.com.seremak.todoapp.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import pl.com.seremak.todoapp.controller.TaskController;
import pl.com.seremak.todoapp.event.TaskDone;
import pl.com.seremak.todoapp.event.TaskUndone;

public class ChangedTaskEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    @EventListener
    void on(TaskDone event) {
        logger.info("Got " + event);
    }

    @EventListener
    void on(TaskUndone event) {
        logger.info("Got " + event);
    }
}
