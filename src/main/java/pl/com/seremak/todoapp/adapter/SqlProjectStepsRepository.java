package pl.com.seremak.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.com.seremak.todoapp.model.ProjectStep;
import pl.com.seremak.todoapp.model.ProjectStepsRepository;

@Repository
public interface SqlProjectStepsRepository extends ProjectStepsRepository, JpaRepository<ProjectStep, Integer> {

}
