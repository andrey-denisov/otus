package org.example.exam.service;

import org.example.exam.exception.ExamExecutionException;
import org.example.exam.exception.LoadingTaskException;
import org.example.exam.format.task.TaskFormatter;
import org.example.exam.io.IOService;
import org.example.exam.localization.LocalizationHelper;
import org.example.exam.model.TaskResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.example.exam.model.Task;
import org.example.exam.model.TaskResultDetail;

import java.text.MessageFormat;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ExamService {

    private final Logger logger = LoggerFactory.getLogger(ExamService.class);

    private final LocalizationHelper localizationHelper;

    private final TaskService taskService;
    private final IOService ioService;
    private final RateService rateSerivce;
    private final TaskFormatter formatter;

    private final int taskAmount;

    public ExamService(TaskService taskService, IOService ioService, RateService rateSerivce, TaskFormatter  formatter, LocalizationHelper localizationHelper, @Value("${taskAmount:5}") int taskAmount) {
        this.taskService = taskService;
        this.rateSerivce = rateSerivce;
        this.formatter = formatter;
        this.localizationHelper = localizationHelper;
        this.taskAmount = taskAmount;
        this.ioService = ioService;
    }

    public void executeExam() {
        try {
            String studentName = inputStudentName();
            displayGreeting(studentName);
            TaskResult result = passExam(loadTaskList(taskAmount));
            ioService.display("\n\n");
            displayResult(studentName, result.getRate(), result.getCorrect(), result.getDetails().size());
        } catch (ExamExecutionException e) {
            logger.error(e.getMessage());
            String msg = localizationHelper.message("something_went_wrong", "Sonething went wrong");
            ioService.display(msg);
        }
    }

    // реализует алгоритм проведения экзамена: запрашивает имя, задает вопросы, оценивает результат
    private TaskResult passExam(List<Task> taskList) {
        try {
        Map<Task, TaskResultDetail> details = askExamQuestions(taskList);
        return new TaskResult(
                details,
                rateSerivce.rate(details.values()),
                calcCorrectAnswers(details.values())
        );
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ExamExecutionException(e.getMessage(), e);
        }
    }

    private Map<Task, TaskResultDetail> askExamQuestions(List<Task> taskList) {
        Map<Task, TaskResultDetail> result = new HashMap<>();
        taskList.forEach(task -> {
            ioService.display(formatter.format(task));
            String answer = ioService.getUserInput();
            TaskResultDetail taskResultDetail = task.checkAnswer(answer);
            result.put(task, taskResultDetail);
        });
        return result;
    }

    private void displayResult(String studentName, int rate, long correct, int total) {
        String msg = MessageFormat.format(
                localizationHelper.message("test_result", "{0}, Your rate is: {1}. Correct done {2} tasks of {3}"),
                studentName, rate, correct, total);
        ioService.display(msg);
    }

    private int calcCorrectAnswers(Collection<TaskResultDetail> answers) {
        return (int)answers.stream().filter(TaskResultDetail::isCorrect).count();
    }

    private void displayGreeting(String studentName) {
        String greetingMessage = MessageFormat.format(localizationHelper.message("greeting_message", "Hello {0}"), studentName);
        ioService.display(greetingMessage);
        ioService.display("\n");
    }

    private String inputStudentName() {
        ioService.display(localizationHelper.message("type_your_name", "Type your name"));
        return ioService.getUserInput();
    }

    private List<Task> loadTaskList(int taskAmount) {
        List<Task> taskList = taskService.loadTaskList(taskAmount);
        if (taskList.isEmpty()) {
            throw new LoadingTaskException("No task found");
        }
        return taskList;
    }
}
