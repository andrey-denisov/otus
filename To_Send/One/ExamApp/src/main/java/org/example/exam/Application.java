package org.example.exam;

import org.example.exam.config.AppConfig;
import org.example.exam.service.ExamService;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class Application{

    private static Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        ExamService examService = context.getBean("examService", ExamService.class);
        examService.passExam(5);
    }
}
