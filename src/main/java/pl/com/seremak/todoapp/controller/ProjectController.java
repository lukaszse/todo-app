package pl.com.seremak.todoapp.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat.*;
import pl.com.seremak.todoapp.logic.ProjectService;
import pl.com.seremak.todoapp.model.Project;
import pl.com.seremak.todoapp.model.ProjectStep;
import pl.com.seremak.todoapp.model.projection.ProjectWriteModel;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    ProjectController(final ProjectService projectService) {
        this.projectService = projectService;
    }


    @GetMapping
    String showProjects(Model model) {
        ProjectWriteModel projectToEdit = new ProjectWriteModel();
        projectToEdit.setDescription("test");
        model.addAttribute("project", projectToEdit);
        return "projects";
    }

    @PostMapping
    String addProject(
            @ModelAttribute ("project") @Valid ProjectWriteModel newProject,
            BindingResult bindingResult,
            Model model
    ) {
        if(bindingResult.hasErrors()) {
            return "projects";
        }
        projectService.create(newProject);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Dodano projekt");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute ("project") ProjectWriteModel current) {
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @Timed(value = "project.create.group", histogram = true, percentiles = {0.5, 0.95, 0,.99})
    @PostMapping("/{id}")
    String createGroup(
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @PathVariable int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline
    ) {
        try {
            projectService.createGroup(deadline, id);
            model.addAttribute("message", "Dodano grupę!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message", "Błąd podczas tworzenia grupy!");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return projectService.readAll();
    }
}
