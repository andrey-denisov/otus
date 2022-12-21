package org.example.exam.format.task;

import org.example.exam.model.task.SelectionTask;
import org.example.exam.localization.LocalizationHelper;
import org.springframework.stereotype.Component;

@Component
public class SelectionTextTaskFormatter implements TaskFormatter<SelectionTask> {

    private final LocalizationHelper localizationHelper;

    public SelectionTextTaskFormatter(LocalizationHelper localizationHelper) {
        this.localizationHelper = localizationHelper;
    }

    @Override
    public String format(SelectionTask task) {
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
