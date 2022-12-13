package org.example.exam.config;

import org.example.exam.format.task.SelectionTextTaskFormatter;
import org.example.exam.format.task.TaskFormatter;
import org.example.exam.io.Display;
import org.example.exam.io.impl.ConsoleDisplay;
import org.example.exam.repository.csv.CSVTaskRepository;
import org.example.exam.service.RateService;
import org.example.exam.service.SimpleRateSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

@ConfigurationProperties(prefix = "application")
@Import(I18Config.class)
public class AppConfig {

    @Autowired
    private MessageSource messageSource;

    private String dataFilePath;
    private String entityClassName;

    @Autowired
    private Locale locale;

    public String getDataFilePath() {
        return dataFilePath;
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Bean
    public CSVTaskRepository repository() throws ClassNotFoundException, IOException {
        File dataFile = file(dataFilePath);
        Class<?> aClass = Class.forName(entityClassName);
        return new CSVTaskRepository(aClass, dataFile, messageSource, locale);
    }

    File file(String path) throws IOException {
        File dataFile = new File(path);
        if(dataFile.exists()) {
            return dataFile;
        }
        return ResourceUtils.getFile("classpath:" + path);
    }

    @Bean
    public RateService rateService() {
        return new SimpleRateSerivce();
    }

    @Bean
    public Display display() {
        return new ConsoleDisplay();
    }

    @Bean
    public TaskFormatter formatter() {
        return new SelectionTextTaskFormatter(messageSource, locale);
    }


}

