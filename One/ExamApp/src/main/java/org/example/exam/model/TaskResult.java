package org.example.exam.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TaskResult {
    private final boolean correct;
    private final List<String> errorList = new ArrayList<>();

    public TaskResult(boolean correct, List<String> errorList) {
        this.correct = correct;
        this.errorList.addAll(errorList);
    }

    public boolean isCorrect() {
        return correct;
    }

    public List<String> getErrorList() {
        return errorList;
    }
}
