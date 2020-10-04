package pl.com.seremak.todoapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import pl.com.seremak.todoapp.model.Task;
import pl.com.seremak.todoapp.model.TaskRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTestE2E {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TaskRepository taskRepo;

    @Test
    void httpGet_returnsAllTasks() {

        // given
        var initialSize = taskRepo.findAll().size();
        taskRepo.save(new Task("example description 1", LocalDateTime.now()));
        taskRepo.save(new Task("example description 2", LocalDateTime.now()));

        // when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);


        // then
        assertThat(result).hasSize(initialSize + 2);
    }

    @Test
    void httpGet_returnsOneTask() {

        // given
        var initialSize = taskRepo.findAll().size();
        String testString = "test string";
        Task task = new Task(testString, LocalDateTime.now());
        taskRepo.save(task);
        int id = task.getId();

        // when
        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/" + id, Task.class);


        // then
        assertThat(result.getDescription()).isEqualTo(testString);
    }

}