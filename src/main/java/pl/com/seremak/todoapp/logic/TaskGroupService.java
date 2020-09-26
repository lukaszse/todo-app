package pl.com.seremak.todoapp.logic;

import org.springframework.stereotype.Service;
import pl.com.seremak.todoapp.model.TaskGroup;
import pl.com.seremak.todoapp.model.TaskGroupRepository;
import pl.com.seremak.todoapp.model.TaskRepository;
import pl.com.seremak.todoapp.model.projection.GroupReadModel;
import pl.com.seremak.todoapp.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskGroupService {


    // == fields ==
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll()
                .stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone task. Done all task first");
        }

        TaskGroup result = repository.findById(groupId)
                .orElseThrow(()-> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}
