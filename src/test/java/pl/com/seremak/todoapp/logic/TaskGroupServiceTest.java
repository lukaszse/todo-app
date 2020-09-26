package pl.com.seremak.todoapp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.com.seremak.todoapp.model.TaskGroupRepository;
import pl.com.seremak.todoapp.model.TaskRepository;

import java.util.Optional;

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
}