package pl.com.seremak.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {

    // == fields ==
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "Project's description musn't be empty")
    String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "project")
    private Set<TaskGroup> groups;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> steps;

    // == constructors ==
    Project() {
    }

    // == methods ==
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    Set<TaskGroup> getGroups() {
        return groups;
    }

    void setGroups(Set<TaskGroup> groups) {
        this.groups = groups;
    }

    Set<ProjectStep> getSteps() {
        return steps;
    }

    void setSteps(Set<ProjectStep> steps) {
        this.steps = steps;
    }


}
