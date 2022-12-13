package org.example.exam.repository.csv;

import org.example.exam.config.TestConfig;
import org.example.exam.model.Task;
import org.example.exam.model.task.SelectionTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
class CSVTaskRepositoryTest {

    @Autowired
    MessageSource messageSource;

    @Autowired
    Locale locale;

    @Test
    void getAll() throws IOException {
        File file = new ClassPathResource("tasks.csv").getFile();
        List<Task> result = new CSVTaskRepository(SelectionTask.class, file, messageSource, locale).getAll();
        assertNotNull(result);
        assertEquals(6, result.size());
    }

}