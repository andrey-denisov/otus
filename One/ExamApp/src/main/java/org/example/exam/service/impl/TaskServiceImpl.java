package org.example.exam.service.impl;

import org.example.exam.model.Task;
import org.example.exam.repository.TaskRepository;
import org.example.exam.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    @Autowired
    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    // получает из базы все задания, перемешивает и выбирает из них требуемое количество
    @Override
    public List<Task> loadTaskList(int amount) {
        List<Task> all = repository.getAll();
        if(null == all || all.isEmpty()) {
            return Collections.emptyList();
        }
        Collections.shuffle(all);
        if(all.size() > amount) {
            return all.subList(0, amount);
        }
        return all;
    }
}
