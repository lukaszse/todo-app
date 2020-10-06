package pl.com.seremak.todoapp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.com.seremak.todoapp.TaskConfigurationProperties;
import pl.com.seremak.todoapp.model.*;
import pl.com.seremak.todoapp.model.projection.GroupReadModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just one group and the other undone group exist")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateException() {

        // given
        var mockGroupRepository = groupRepositoryReturning(true);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        // system under test
        var toTest = new ProjectService(null, mockGroupRepository, null, mockConfig);

        // when
        var exeption = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        // then
        assertThat(exeption)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");
    }


    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {

        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // And
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, null, null,mockConfig);

        // when
        var exeption = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        // then
        assertThat(exeption)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }


    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and no projects for a given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {

        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // And
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        // And
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, mockGroupRepository, null, mockConfig);

        // when
        var exeption = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        // then
        assertThat(exeption)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }


    @Test
    @DisplayName("should create new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup() {

        // given
        var today = LocalDate.now().atStartOfDay();
        // And
        var project = projectWith("bar", Set.of(-1, -2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        // And
        InMemoryGroupRepo inMemoryGroupRepo = inMemoryGroupRepository();
        var serviceWithInMemRepo = dummyGroupService(inMemoryGroupRepo);
        int countBeforeCall = inMemoryGroupRepo.count();
        // And
        TaskConfigurationProperties mockConfig = configurationReturning(true);

        // system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, serviceWithInMemRepo, mockConfig);

        // when
        GroupReadModel result = toTest.createGroup(today, 1);

        // then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());
    }

    private TaskGroupService dummyGroupService(InMemoryGroupRepo inMemoryGroupRepo) {
        return new TaskGroupService(inMemoryGroupRepo, null);
    }


    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps = daysToDeadline
                .stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(-1);
                    return step;
                })
                .collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        when(result.getGroups()).thenReturn(null);
        return result;
    }

    private TaskGroupRepository groupRepositoryReturning(boolean b) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(b);
        return mockGroupRepository;
    }


    private TaskConfigurationProperties configurationReturning(final boolean b) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(b);
        // and
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }


    private InMemoryGroupRepo inMemoryGroupRepository() {
        return new InMemoryGroupRepo();
    }

    private static class InMemoryGroupRepo implements TaskGroupRepository{

            private int index = 0;
            private Map<Integer, TaskGroup> map = new HashMap<>();

            public int count() {
                return map.values().size();
            }


            @Override
            public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
            }


            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(taskGroup -> !taskGroup.isDone())
                    .anyMatch(taskGroup ->
                            taskGroup.getProject() != null && taskGroup.getProject().getId() == projectId);
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
            }

            @Override
            public TaskGroup save(TaskGroup entity) {
                if(entity.getId() == 0) {
                    try {
                        var field = TaskGroup.class.getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity, ++index);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            map.put(entity.getId(), entity);
            return entity;
          }

        @Override
        public boolean existsByDescription(String description) {
            return map.values().stream()
                    .anyMatch(taskGroup -> taskGroup.getDescription().equals(description));
        }
    }

}