package pl.com.seremak.todoapp.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {

    List<TaskGroup> findAll();

    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);

    Optional<TaskGroup> findById(Integer id);

    TaskGroup save(TaskGroup entity);

    boolean existsByDescription(String description);

}
