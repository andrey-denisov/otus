package org.example.exam.repository.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.example.exam.exception.LoadingTaskException;
import org.example.exam.model.Task;
import org.example.exam.model.task.SelectionTask;
import org.example.exam.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.List;

@Repository
public class CSVTaskRepository implements TaskRepository {

    private final Logger logger = LoggerFactory.getLogger(CSVTaskRepository.class);

    private final CsvFileReader cvsFile;
    private final File file;

    public CSVTaskRepository(@Qualifier("LocalizedCsvFileReader") CsvFileReader cvsFile, @Value("${application.dataFilePath}") String fileName) {
        this.cvsFile = cvsFile;
        this.file = file(fileName);
    }

    @Override
    public List<Task> getAll() {
        byte[] content = String.join("\n", cvsFile.read(file)).getBytes();

        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            MappingIterator<Task> readValues = mapper.reader(SelectionTask.class).with(bootstrapSchema).readValues(content);
            return readValues.readAll();
        }
        catch (IOException e) {
            logger.error("Error during loading data from file:" + e.getMessage());
            throw new LoadingTaskException("Error during loading data from file", e);
        }
    }

    private File file(String path)  {
        File dataFile = new File(path);
        if(dataFile.exists()) {
            return dataFile;
        }
        try {
            return ResourceUtils.getFile("classpath:" + path);
        } catch (IOException e) {
            logger.error("Error during open data file:" + e.getMessage());
            throw new LoadingTaskException("Error during open data file", e);
        }
    }
}
