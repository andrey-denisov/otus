package org.example.exam.service;

import org.example.exam.format.task.TaskFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.example.exam.model.Task;
import org.example.exam.model.TaskResult;
import org.example.exam.io.Display;
import org.example.exam.io.UserInput;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ExamService {

    private final TaskService taskService;
    private final Display display;
    private final UserInput userInput;
    private final RateService rateSerivce;
    private final TaskFormatter formatter;
    private final MessageSource messageSource;

    @Value("${application.locale}")
    private String locale;

    public ExamService(TaskService taskService, Display display, UserInput userInput, RateService rateSerivce, TaskFormatter formatter, MessageSource messageSource) {
        this.taskService = taskService;
        this.display = display;
        this.userInput = userInput;
        this.rateSerivce = rateSerivce;
        this.formatter = formatter;
        this.messageSource = messageSource;
    }

    // реализует алгоритм проведения экзамена: запрашивает имя, задает вопросы, оценивает результат
    public int passExam(int taskAmount) {
        try {
            String studentName = inputStudentName();
            displayGreeting(studentName);

            Map<Task, TaskResult> result = askExamQuestions(loadTaskList(taskAmount));

            int rate = rateSerivce.rate(result.values());
            long correct = calcCorrectAnswers(result);

            display.display("\n\n");
            displayResult(studentName, rate, correct, result.size());

            return rate;
        } catch (Exception e) {
            String msg = messageSource.getMessage("something_went_wrong", null, "Something went wrong", Locale.forLanguageTag(locale));
            display.display(msg);
            return 0;
        }
    }

    private Map<Task, TaskResult> askExamQuestions(List<Task> taskList) {
        Map<Task, TaskResult> result = new HashMap<>();
        taskList.forEach(task -> {
            display.display(formatter.format(task));
            String answer = userInput.getUserInput();
            TaskResult taskResult = task.checkAnswer(answer);
            result.put(task, taskResult);
        });
        return result;
    }

    private void displayResult(String studentName, int rate, long correct, int total) {
        String msg = MessageFormat.format(
                messageSource.getMessage("test_result", null, null, Locale.forLanguageTag(locale)),
                studentName, rate, correct, total);
        display.display(msg);
    }

    private long calcCorrectAnswers(Map<Task, TaskResult> result) {
        return result.values().stream().filter(TaskResult::isCorrect).count();
    }

    private void displayGreeting(String studentName) {
        String greeting_message = MessageFormat.format(
                messageSource.getMessage("greeting_message", null, "Hello", Locale.forLanguageTag(locale)),
                studentName);

        display.display(greeting_message);
        display.display("\n");
    }

    private String inputStudentName() {
        display.display(messageSource.getMessage("type_your_name", null, null, Locale.forLanguageTag(locale)));
        return userInput.getUserInput();
    }

    private List<Task> loadTaskList(int taskAmount) throws IOException {
        List<Task> taskList = taskService.loadTaskList(taskAmount);
        if(taskList.isEmpty()) {
            throw new RuntimeException(messageSource.getMessage("no_task_found", null, "No task found", Locale.forLanguageTag(locale)));
        }
        return taskList;
    }
}
