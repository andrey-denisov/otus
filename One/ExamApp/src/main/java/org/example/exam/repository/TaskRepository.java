package org.example.exam.repository;

import org.example.exam.model.Task;

import java.util.List;

public interface TaskRepository {
    List<Task> getAll();
}
