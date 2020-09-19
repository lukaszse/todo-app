package pl.com.seremak.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.com.seremak.todoapp.model.TaskGroup;
import pl.com.seremak.todoapp.model.TaskGroupRepository;

import java.util.List;

public interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {

    @Override
    @Query("from TaskGroup g join fetch  g.tasks")
    List<TaskGroup> findAll();
}
