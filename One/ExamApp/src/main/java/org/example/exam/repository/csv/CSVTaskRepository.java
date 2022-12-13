package org.example.exam.repository.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.example.exam.model.Task;
import org.example.exam.repository.TaskRepository;
import org.springframework.context.MessageSource;

import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// реализация репозитория для CSV
public class CSVTaskRepository implements TaskRepository {

    private static Logger logger = Logger.getLogger(CSVTaskRepository.class.getName());

    private final File file;
    private final Class entityClass;
    private final MessageSource messageSource;
    private final Locale locale;

    public CSVTaskRepository(Class entityClass, File file, MessageSource messageSource, Locale locale) {
        this.entityClass = entityClass;
        this.file = file;
        this.messageSource = messageSource;
        this.locale = locale;
    }

    // можно было сделать свой маппер, я взял уже готовый. В задании не оговаривалось.
    @Override
    public List<Task> getAll() throws IOException {
        byte[] content = readFile(file, messageSource, locale);
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            MappingIterator readValues = mapper.reader(entityClass).with(bootstrapSchema).readValues(content);
            return readValues.readAll();
        } catch (Exception e) {
            logger.log(Level.FINER, "Error occurred while loading object list from file " + file.getAbsolutePath(), e);
            throw e;
        }
    }

    byte[] readFile(File f, MessageSource ms, Locale loc) throws IOException {
        try(FileReader fileReader = new FileReader(f); BufferedReader reader = new BufferedReader(fileReader)){
            return reader.lines().map(
                    line ->
                    ms.getMessage(line, null, line, loc)).collect(Collectors.joining("\n")).getBytes();
        }
    }
}
