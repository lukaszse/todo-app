package pl.com.seremak.todoapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.com.seremak.todoapp.model.Task;
import pl.com.seremak.todoapp.model.TaskGroup;
import pl.com.seremak.todoapp.model.TaskGroupRepository;

import java.util.Set;


@Component
public class WarmUp implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(WarmUp.class);

    private final TaskGroupRepository groupRepository;

    WarmUp(TaskGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("WarmUp: Applecation warmup after context refreshed");
        final String description = "czy taka grupa istnieje?";
        if(!groupRepository.existsByDescription(description)) {
            logger.info("No required group found! Adding it!");
            var group = new TaskGroup();
            group.setDescription(description);
            group.setTasks(Set.of(
                    new Task("ContextClosededEvent", null, group),
                    new Task("ContextRefreshedEvent", null, group),
                    new Task("ContextStoppedEvent", null, group),
                    new Task("ContextStartedEvent", null, group)
            ));
            groupRepository.save(group);
        } else {
            logger.info("WarmUp: group with requested description exist");
        }
    }
}
