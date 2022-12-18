package org.example.exam.model;

import java.util.Map;

public class TaskResult {
    private Map<Task, TaskResultDetail> details;
    private int rate;
    private int correct;

    public TaskResult(Map<Task, TaskResultDetail> details, int rate, int correct) {
        this.details = details;
        this.rate = rate;
        this.correct = correct;
    }

    public Map<Task, TaskResultDetail> getDetails() {
        return details;
    }

    public int getRate() {
        return rate;
    }

    public int getCorrect() {
        return correct;
    }
}
