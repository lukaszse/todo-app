package pl.com.seremak.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.com.seremak.todoapp.model.TaskGroup;
import pl.com.seremak.todoapp.model.TaskGroupRepository;

import java.util.List;

public interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {

    @Override
    @Query("select distinct g from TaskGroup g join fetch  g.tasks")
    List<TaskGroup> findAll();


    @Override
    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
}
