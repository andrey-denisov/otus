package org.example.exam.service;

import org.example.exam.exception.ExamExecutionException;
import org.example.exam.exception.LoadingTaskException;
import org.example.exam.format.task.TaskFormatter;
import org.example.exam.io.IOFacade;
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

    private final LocalizationService localizationService;

    private final TaskService taskService;
    private final IOFacade ioFacade;
    private final RateService rateSerivce;
    private final TaskFormatter formatter;

    private final int taskAmount;

    public ExamService(TaskService taskService, IOFacade ioFacade, RateService rateSerivce, TaskFormatter formatter/*, MessageSource messageSource*/, LocalizationService localizationService, @Value("${taskAmount:5}") int taskAmount) {
        this.taskService = taskService;
        this.rateSerivce = rateSerivce;
        this.formatter = formatter;
        this.localizationService = localizationService;
        this.taskAmount = taskAmount;
        this.ioFacade = ioFacade;
    }

    public void executeExam() {
        try {
            String studentName = inputStudentName();
            displayGreeting(studentName);
            TaskResult result = passExam(loadTaskList(taskAmount));
            ioFacade.display("\n\n");
            displayResult(studentName, result.getRate(), result.getCorrect(), result.getDetails().size());
        } catch (ExamExecutionException e) {
            logger.error(e.getMessage());
            String msg = localizationService.message("something_went_wrong", "Sonething went wrong");
            ioFacade.display(msg);
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
            ioFacade.display(formatter.format(task));
            String answer = ioFacade.getUserInput();
            TaskResultDetail taskResultDetail = task.checkAnswer(answer);
            result.put(task, taskResultDetail);
        });
        return result;
    }

    private void displayResult(String studentName, int rate, long correct, int total) {
        String msg = MessageFormat.format(
                localizationService.message("test_result", "{0}, Your rate is: {1}. Correct done {2} tasks of {3}"),
                studentName, rate, correct, total);
        ioFacade.display(msg);
    }

    private int calcCorrectAnswers(Collection<TaskResultDetail> answers) {
        return (int)answers.stream().filter(TaskResultDetail::isCorrect).count();
    }

    private void displayGreeting(String studentName) {
        String greeting_message = MessageFormat.format(localizationService.message("greeting_message", "Hello {0}"), studentName);
        ioFacade.display(greeting_message);
        ioFacade.display("\n");
    }

    private String inputStudentName() {
        ioFacade.display(localizationService.message("type_your_name", "Type your name"));
        return ioFacade.getUserInput();
    }

    private List<Task> loadTaskList(int taskAmount) {
        List<Task> taskList = taskService.loadTaskList(taskAmount);
        if (taskList.isEmpty()) {
            throw new LoadingTaskException("No task found");
        }
        return taskList;
    }
}
