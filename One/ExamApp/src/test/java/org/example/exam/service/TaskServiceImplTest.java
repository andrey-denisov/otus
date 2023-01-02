package org.example.exam.service;

import org.example.exam.model.Task;
import org.example.exam.repository.TaskRepository;
import org.example.exam.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TaskServiceImplTest {

    private TaskRepository repository;
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TaskRepository.class);
        taskService = new TaskServiceImpl(repository);
    }

    @Test
    void loadTaskListEnoughData() throws IOException {
        when(repository.getAll()).thenReturn(Arrays.asList(
                new Task(1, null, null, null),
                new Task(1, null, null, null),
                new Task(1, null, null, null),
                new Task(1, null, null, null),
                new Task(1, null, null, null),
                new Task(1, null, null, null)
                ));

        List<Task> result = taskService.loadTaskList(5);
        assertEquals(5, result.size());
    }

    @Test
    void loadTaskListNotEnoughData() throws IOException {
        when(repository.getAll()).thenReturn(Arrays.asList(
                new Task(1, null, null, null),
                new Task(1, null, null, null),
                new Task(1, null, null, null)
                ));

        List<Task> result = taskService.loadTaskList(5);
        assertEquals(3, result.size());
    }
}