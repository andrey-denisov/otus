package org.example.exam.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class Task {
    private long id;
    private String question;
    private String rightAnswer;
    private List<String> answers;

    // нужен для маршалера
    protected Task() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public Task(long id, String question, String rightAnswer, List<String> answers) {
        this.id = id;
        this.question = question;
        this.rightAnswer = rightAnswer;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public TaskResultDetail checkAnswer(String answer) {
        boolean correct = rightAnswer.equalsIgnoreCase(answer);
        if(correct) {
            return new TaskResultDetail(true, new ArrayList<>());
        }
        return  new TaskResultDetail(false, Collections.singletonList(answer));
    }
}
