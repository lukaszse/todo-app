package pl.com.seremak.todoapp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.com.seremak.todoapp.model.TaskGroup;
import pl.com.seremak.todoapp.model.TaskGroupRepository;
import pl.com.seremak.todoapp.model.TaskRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("when existsByDoneIsFalseAndGroup_Id return value of true")
    void toggleGroup_when_existsByDoneIsFalseAndGroup_Id_return_true_and_throws_IllegalStateException() {

        // given
        var mockTaskRepo = mock(TaskRepository.class);
        when(mockTaskRepo.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);

        // System under test
        var toTest = new TaskGroupService(null, mockTaskRepo);

        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(3));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone task");
    }

    @Test
    @DisplayName("when findById return empty Optional and throws illegalArgumentException")
    void toggleGroup_findById_return_empty_Optional_and_Throws_IllegarArgumentException() {

        // given
        var mockRepo = mock(TaskGroupRepository.class);
        when(mockRepo.findById(anyInt())).thenReturn(Optional.empty());
        // and
        var mockTaskRepo = mock(TaskRepository.class);
        when(mockTaskRepo.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);

        // System under test
        var toTest = new TaskGroupService(mockRepo, mockTaskRepo);

        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(3));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("when everything is ok, and state is changing")
    void toggleGroup_when_isOK_and_change_state_to_Done() {

        // given
        TaskGroup taskGroup = new TaskGroup();
        taskGroup.setDone(false);
        // and
        InMemoryGroupRepo inMemoryGroupRepo = inMemoryGroupRepository();
        inMemoryGroupRepo.save(taskGroup);
        // and
        var mockTaskRepo = mock(TaskRepository.class);
        when(mockTaskRepo.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);

        // System under test
        var toTest = new TaskGroupService(inMemoryGroupRepo, mockTaskRepo);

        // when
        toTest.toggleGroup(1);

        // then
        assertThat(taskGroup.isDone()).isEqualTo(true);
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
    }

}

