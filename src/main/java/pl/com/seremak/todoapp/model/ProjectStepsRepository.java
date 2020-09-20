package pl.com.seremak.todoapp.model;

import java.util.List;
import java.util.Optional;

public interface ProjectStepsRepository {

    List<Project> findAll();

    Optional<Project> findById(Integer id);

    Project save(Project entity);
}
