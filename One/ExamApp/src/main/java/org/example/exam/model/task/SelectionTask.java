package org.example.exam.model.task;

import org.example.exam.model.Task;
import org.example.exam.model.TaskResultDetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Реализация задачи с несколькими вариантами ответов
@SuppressWarnings("unused")
public class SelectionTask extends Task {

    private List<String> answers;

    protected SelectionTask() {
        super();
    }

    public SelectionTask(long id, String question, String rightAnswer, List<String> answers) {
        super(id, question, rightAnswer);
        this.answers = answers;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    // можно просто возвращать true/false, но собирать ошибки - задел на будущее расширение функционала.
    @Override
    public TaskResultDetail checkAnswer(String answer) {
        boolean correct = rightAnswer.equalsIgnoreCase(answer);
        if(correct) {
            return new TaskResultDetail(true, new ArrayList<>());
        }
        return  new TaskResultDetail(false, Collections.singletonList(answer));
    }
}
