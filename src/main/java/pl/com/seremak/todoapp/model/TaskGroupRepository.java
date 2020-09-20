package pl.com.seremak.todoapp.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {

    List<TaskGroup> findAll();

    List<TaskGroup> findByDoneIsFalsePAndProject_Id(Integer projectId);

    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);

    Optional<TaskGroup> findById(Integer id);

    TaskGroup save(TaskGroup entity);

}
