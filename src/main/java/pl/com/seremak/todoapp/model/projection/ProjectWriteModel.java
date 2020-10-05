package pl.com.seremak.todoapp.model.projection;

import pl.com.seremak.todoapp.model.Project;
import pl.com.seremak.todoapp.model.ProjectStep;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {

    @NotBlank(message = "Project's description mustn't be empty")
    private String description;

    @Valid
    private List<ProjectStep> steps = new ArrayList<>();


    public ProjectWriteModel() {
        steps.add(new ProjectStep());
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ProjectStep> steps) {
        this.steps = steps;
    }

    public Project toProject() {
        var result = new Project();
        result.setDescription(description);
        steps.forEach(projectStep -> projectStep.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }
}
