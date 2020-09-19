package pl.com.seremak.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.seremak.todoapp.model.Project;
import pl.com.seremak.todoapp.model.ProjectRepository;

public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {
}
