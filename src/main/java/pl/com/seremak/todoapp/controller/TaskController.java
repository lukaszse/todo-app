package pl.com.seremak.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.seremak.todoapp.model.TaskRepository;
import pl.com.seremak.todoapp.model.Task;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
public class TaskController {

    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("exposing all the task");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.warn("custom pagable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @PutMapping("/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        logger.warn("put method invoked");

        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        toUpdate.setId(id);
        repository.save(toUpdate);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> getTask(@PathVariable int id) {

        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/tasks")
    ResponseEntity<Task> postTask(@RequestBody @Valid Task newTask) {

        Task task = repository.save(newTask);
        return ResponseEntity.created(URI.create("/" + task.getId())).body(task);
    }

}
