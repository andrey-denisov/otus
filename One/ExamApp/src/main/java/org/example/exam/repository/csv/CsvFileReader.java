package org.example.exam.repository.csv;

import org.example.exam.exception.LoadingTaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CsvFileReader {

    private final Logger logger = LoggerFactory.getLogger(CSVTaskRepository.class);
    private final FileNameHolder fileNameHolder;

    public CsvFileReader(FileNameHolder fileNameHolder) {
        this.fileNameHolder = fileNameHolder;
    }

    public List<String> read() {
        try (InputStreamReader fileReader = new InputStreamReader(buildFile(fileNameHolder.getFullPath(), fileNameHolder.getResourceFilePath())); BufferedReader reader = new BufferedReader(fileReader)) {
            return reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Reading file error:" + e.getMessage());
            throw new LoadingTaskException("Reading file error", e);
        }
    }

    private InputStream buildFile(String fullPath, String resourceFilePath) {
        try {
            if (Files.exists(Paths.get(fullPath))) {
                logger.info("Read file from folder " + fullPath);
                return new FileInputStream(fullPath);
            } else {
                // default file from resources
                logger.warn("Data file not found at " + fullPath + ". Read default file from resources " + resourceFilePath);
                ClassPathResource classPathResource = new ClassPathResource(resourceFilePath);
                return classPathResource.getInputStream();
            }
        } catch (IOException e) {
            throw new LoadingTaskException("Error reading task questions from file", e);
        }
    }

}
