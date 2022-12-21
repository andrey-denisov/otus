package org.example.exam.repository.csv;

import org.example.exam.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CSVTaskRepositoryTest {

    private CsvFileReader fileReader = Mockito.mock(CsvFileReader.class);

    @BeforeEach
    void setUp() {
        when(fileReader.read(Mockito.any())).thenReturn(
                Arrays.asList(
                        "id,question,rightAnswer,answers",
                        "1,Name of the first cosmonaut,Gagarin,Gagarin;Ivanov;Batareikin",
                        "2,Capital of France,Paris,London;Berlin;Paris",
                        "3,How many teeth does a person have?,32,44;32;18",
                        "4,How many grams in a kilogram?,1000,100;10;1000",
                        "5,Which planet has rings?,Saturn,Venus;Saturn;Jupiter",
                        "6,Who is Einstein?,Physicist,General;Doctor;Physicist"
                )
        );
    }

    @Test
    void getAll() {
        List<Task> result = new CSVTaskRepository(fileReader, "tasks.csv").getAll();
        assertNotNull(result);
        assertEquals(6, result.size());
    }
}