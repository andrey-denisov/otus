package org.example.exam.config;

import org.example.exam.config.I18Config;
import org.example.exam.format.task.SelectionTextTaskFormatter;
import org.example.exam.format.task.TaskFormatter;
import org.example.exam.model.task.SelectionTask;
import org.example.exam.repository.TaskRepository;
import org.example.exam.repository.csv.CSVTaskRepository;
import org.example.exam.service.ExamService;
import org.example.exam.service.RateService;
import org.example.exam.service.SimpleRateSerivce;
import org.example.exam.service.TaskService;
import org.example.exam.io.impl.ConsoleDisplay;
import org.example.exam.io.impl.ConsoleUserInput;
import org.example.exam.io.Display;
import org.example.exam.io.UserInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.util.Locale;

@Configuration("test")
@Import(I18Config.class)
public class TestConfig {

    @Value("tasks.csv")
    private String fileName;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private Locale locale;

    @Bean
    public TaskRepository repository() {
        return new CSVTaskRepository(SelectionTask.class, new File(fileName), messageSource , locale);
    }

    @Bean
    public TaskService taskService() {
        return new TaskService(repository());
    }

    @Bean
    public TaskFormatter<SelectionTask> taskFormatter() {
        return new SelectionTextTaskFormatter(messageSource, locale);
    }

    @Bean
    public Display taskDisplay() {
        return new ConsoleDisplay();
    }

    @Bean
    public UserInput userInput() {
        return new ConsoleUserInput();
    }

    @Bean
    RateService rateService() {
        return new SimpleRateSerivce();
    }

    @Bean
    public ExamService examService() {
        return new ExamService(taskService(), taskDisplay(), userInput(), rateService(), taskFormatter(), messageSource);
    }
}
