package pl.com.seremak.todoapp.logic;

import pl.com.seremak.todoapp.TaskConfigurationProperties;
import pl.com.seremak.todoapp.model.*;
import pl.com.seremak.todoapp.model.projection.GroupReadModel;
import pl.com.seremak.todoapp.model.projection.GroupTaskWriteModel;
import pl.com.seremak.todoapp.model.projection.GroupWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {

    // == fields ==
    private ProjectRepository projectRepository;
    private TaskGroupRepository taskGroupRepository;
    private TaskGroupService taskGroupService;
    private TaskConfigurationProperties properties;


    public ProjectService(final ProjectRepository projectRepository, final TaskGroupRepository taskGroupRepository, final TaskGroupService taskGroupService, final TaskConfigurationProperties properties) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskGroupService = taskGroupService;
        this.properties = properties;
    }

    // == methods ==
    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public void create(Project project) {
        projectRepository.save(project);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!properties.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return (GroupReadModel) projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                                var task = new GroupTaskWriteModel();
                                                task.setDescription(projectStep.getDescription());
                                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                                return task;
                                            }
                                            ).collect(Collectors.toSet())
                    );
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}

