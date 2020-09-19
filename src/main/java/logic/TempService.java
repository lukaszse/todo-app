package logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.seremak.todoapp.model.Task;
import pl.com.seremak.todoapp.model.TaskGroupRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TempService {

    @Autowired
    List<String> temp(TaskGroupRepository repository) {

        // FIXME n+1 selects
        return repository.findAll().stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream())
                        .map(Task::getDescription)
                        .collect(Collectors.toList()));
    }
}
