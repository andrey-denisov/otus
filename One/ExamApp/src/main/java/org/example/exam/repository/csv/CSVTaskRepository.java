package org.example.exam.repository.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.example.exam.exception.LoadingTaskException;
import org.example.exam.model.Task;
import org.example.exam.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.List;

@Repository
public class CSVTaskRepository implements TaskRepository {

    private final Logger logger = LoggerFactory.getLogger(CSVTaskRepository.class);

    private final CsvFileReader cvsFileReader;

    public CSVTaskRepository(CsvFileReader cvsFileReader) {
        this.cvsFileReader = cvsFileReader;
    }

    @Override
    public List<Task> getAll() {
        byte[] content = String.join("\n", cvsFileReader.read()).getBytes();

        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            MappingIterator<Task> readValues = mapper.reader(Task.class).with(bootstrapSchema).readValues(content);
            return readValues.readAll();
        }
        catch (IOException e) {
            logger.error("Error during loading data from file:" + e.getMessage());
            throw new LoadingTaskException("Error during loading data from file", e);
        }
    }
}
