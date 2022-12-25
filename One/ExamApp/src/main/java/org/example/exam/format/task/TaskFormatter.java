package org.example.exam.format.task;

import org.example.exam.localization.LocalizationHelper;
import org.example.exam.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskFormatter {

    private final LocalizationHelper localizationHelper;

    public TaskFormatter(LocalizationHelper localizationHelper) {
        this.localizationHelper = localizationHelper;
    }

    public String format(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(localizationHelper.message("question", "Question")).append(":")
                .append(task.getQuestion())
                .append("\n")
                .append(localizationHelper.message("answers", "Chose one answer"))
                .append("\n");
        for(String answer : task.getAnswers()) {
            sb.append(answer).append("\n");
        }
        sb.append(localizationHelper.message("type_answer", "Type answer"));
        return sb.toString();
    }
}
