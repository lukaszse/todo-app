package pl.com.seremak.todoapp.model.projection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.com.seremak.todoapp.model.Task;
import pl.com.seremak.todoapp.model.TaskGroup;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {

    @Test
    @DisplayName("Should creatte null deadline group for group when no tast deadlines")
    void constructor_noDeadlines_createsNullDeadline() {

        // given
        var source = new TaskGroup();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar", null)));

        // when
        var result = new GroupReadModel(source);

        // then
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }
}