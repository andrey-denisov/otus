package org.example.exam.service;

import org.example.exam.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> loadTaskList(int amount);
}
