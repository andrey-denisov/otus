package org.example.exam;

import org.example.exam.exception.ExamExecutionException;
import org.example.exam.service.ExamService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
            ExamService examService = context.getBean("examService", ExamService.class);
            examService.executeExam();
        } catch (ExamExecutionException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
