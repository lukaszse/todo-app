package pl.com.seremak.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.com.seremak.todoapp.logic.TaskGroupService;
import pl.com.seremak.todoapp.model.Task;
import pl.com.seremak.todoapp.model.TaskGroupRepository;
import pl.com.seremak.todoapp.model.TaskRepository;
import pl.com.seremak.todoapp.model.projection.GroupReadModel;
import pl.com.seremak.todoapp.model.projection.GroupWriteModel;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class TaskGroupController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);

    private TaskGroupService taskGroupService;
    private TaskRepository taskRepository;
    private TaskGroupRepository taskGroupRepository;

    TaskGroupController(TaskGroupService taskGroupService, TaskGroupRepository taskGroupRepository) {
        this.taskGroupService = taskGroupService;
        this.taskGroupRepository = taskGroupRepository;
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.info("readAllGroups() method invoked");
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTaskFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup (@RequestBody @Valid GroupWriteModel newGroupWriteModel) {
        var group = taskGroupService.createGroup(newGroupWriteModel);
        return ResponseEntity.created(URI.create("/" + group.getId())).body(group);
    }

    @Transactional
    @PatchMapping("/{id}")
    ResponseEntity<?> toggleTask(@PathVariable int id) {
        taskGroupService.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgumeny(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
