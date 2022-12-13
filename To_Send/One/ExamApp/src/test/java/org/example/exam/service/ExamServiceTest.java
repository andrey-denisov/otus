package org.example.exam.service;

import org.example.exam.config.TestConfig;
import org.example.exam.model.task.SelectionTask;
import org.example.exam.io.UserInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
class ExamServiceTest {

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserInput userInput;

    @Autowired
    private ExamService examService;

    @Test
    void passExam() throws IOException {
        when(taskService.loadTaskList(anyInt())).thenReturn(Arrays.asList(
                new SelectionTask(1, "q1", "a1", Arrays.asList("a1", "a2", "a3", "a4", "a5")),
                new SelectionTask(2, "q2", "a1", Arrays.asList("a1", "a2", "a3", "a4", "a5")),
                new SelectionTask(3, "q3", "a1", Arrays.asList("a1", "a2", "a3", "a4", "a5")),
                new SelectionTask(4, "q4", "a1", Arrays.asList("a1", "a2", "a3", "a4", "a5")),
                new SelectionTask(5, "q5", "a1", Arrays.asList("a1", "a2", "a3", "a4", "a5"))
        ));
        when(userInput.getUserInput()).thenReturn("a1");
        int result = examService.passExam(5);
        assertEquals(5, result);
    }
}