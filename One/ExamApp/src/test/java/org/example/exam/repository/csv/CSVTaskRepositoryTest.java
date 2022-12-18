package org.example.exam.repository.csv;

import org.example.exam.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CSVTaskRepositoryTest {

    @Autowired
    MessageSource messageSource;

    @Autowired
    Locale locale;

    @Test
    void getAll() throws IOException {
        List<Task> result = new CSVTaskRepository(new PlainCsvFileReader(), "tasks.csv").getAll();
        assertNotNull(result);
        assertEquals(6, result.size());
    }
}