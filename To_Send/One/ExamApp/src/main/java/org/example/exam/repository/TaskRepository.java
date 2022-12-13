package org.example.exam.repository;

import org.example.exam.model.Task;

import java.io.IOException;
import java.util.List;

public interface TaskRepository {
    List<Task> getAll() throws IOException;
}
