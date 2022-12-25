package org.example.exam.repository.csv;

import org.example.exam.exception.LoadingTaskException;
import org.example.exam.localization.LocalizationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class CsvFileReader {

    private final Logger logger = LoggerFactory.getLogger(CSVTaskRepository.class);
    private final File file;

    public CsvFileReader(LocalizationHelper localizationHelper, @Value("${application.dataFilePath}") String filePath, @Value("${application.dataFileName}") String fileName) {
        file = buildFile(localizationHelper.getLocale(), filePath, fileName);
    }

    public List<String> read() {
        try (FileReader fileReader = new FileReader(file); BufferedReader reader = new BufferedReader(fileReader)) {
            return reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Reading file error:" + e.getMessage());
            throw new LoadingTaskException("Reading file error", e);
        }
    }

    private File buildFile(Locale locale, String filePath, String fileName) {
        String fullPath = filePath + "/" + locale.getLanguage() + "_" + locale.getCountry() + "/" + fileName;
        if (Files.exists(Paths.get(fullPath))) {
            return new File(fullPath);
        } else {
            // default file from resources
            URL resource = getClass().getClassLoader().getResource(filePath + "/" + fileName);
            if (null == resource) {
                throw new LoadingTaskException("Default task file not found");
            }
            try {
                return new File(resource.toURI());
            } catch (URISyntaxException e) {
                throw new LoadingTaskException("Error reading task questions from file", e);
            }
        }
    }

}
