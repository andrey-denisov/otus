package org.example.exam;

import org.example.exam.service.ExamService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        ExamService examService = context.getBean("examService", ExamService.class);
        examService.executeExam();
    }
}
