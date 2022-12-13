package org.example.exam.service;

import org.example.exam.config.TestConfig;
import org.example.exam.model.Task;
import org.example.exam.model.task.SelectionTask;
import org.example.exam.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
class TaskServiceTest {

    @MockBean
    private TaskRepository repository;

    @Autowired
    private TaskService taskService;

    @Test
    void loadTaskListEnoughData() throws IOException {
        when(repository.getAll()).thenReturn(Arrays.asList(
                new SelectionTask(1, null, null, null),
                new SelectionTask(1, null, null, null),
                new SelectionTask(1, null, null, null),
                new SelectionTask(1, null, null, null),
                new SelectionTask(1, null, null, null),
                new SelectionTask(1, null, null, null)
                ));

        List<Task> result = taskService.loadTaskList(5);
        assertEquals(5, result.size());
    }

    @Test
    void loadTaskListNotEnoughData() throws IOException {
        when(repository.getAll()).thenReturn(Arrays.asList(
                new SelectionTask(1, null, null, null),
                new SelectionTask(1, null, null, null),
                new SelectionTask(1, null, null, null)
                ));

        List<Task> result = taskService.loadTaskList(5);
        assertEquals(3, result.size());
    }
}