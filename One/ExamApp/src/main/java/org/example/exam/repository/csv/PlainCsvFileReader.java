package org.example.exam.repository.csv;

import org.example.exam.exception.LoadingTaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("PlainCsvFileReader")
public class PlainCsvFileReader implements CsvFileReader {

    private final Logger logger = LoggerFactory.getLogger(CSVTaskRepository.class);

    @Override
    public List<String> read(File file) {
        try (FileReader fileReader = new FileReader(file);
             BufferedReader reader = new BufferedReader(fileReader)) {
             return reader.lines().collect(Collectors.toList());
        }
        catch (Exception e) {
            logger.error("Reading file error:" + e.getMessage());
            throw new LoadingTaskException("Reading file error", e);
        }
    }
}

