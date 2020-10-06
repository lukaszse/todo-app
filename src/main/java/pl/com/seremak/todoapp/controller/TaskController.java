package pl.com.seremak.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.com.seremak.todoapp.logic.TaskService;
import pl.com.seremak.todoapp.model.TaskRepository;
import pl.com.seremak.todoapp.model.Task;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final ApplicationEventPublisher eventPublisher;
    private final TaskRepository repository;
    private final TaskService service;

    TaskController(ApplicationEventPublisher eventPublisher, TaskRepository repository, TaskService service) {
        this.eventPublisher = eventPublisher;
        this.repository = repository;
        this.service = service;
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks() {
        logger.warn("exposing all the task");
        return service.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.warn("custom pagable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        logger.warn("put method invoked");

        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }


    @Transactional
    @PatchMapping ("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        logger.warn("patch method invoked");

        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .map(Task::toggle)
                .ifPresent(eventPublisher::publishEvent);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> getTask(@PathVariable int id) {

        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task newTask) {

        Task task = repository.save(newTask);
        return ResponseEntity.created(URI.create("/" + task.getId())).body(task);
    }

    @GetMapping(value = "/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }

}
