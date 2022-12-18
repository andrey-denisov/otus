package org.example.exam.format.task;

import org.example.exam.model.task.SelectionTask;
import org.example.exam.service.LocalizationService;
import org.springframework.stereotype.Component;

@Component
public class SelectionTextTaskFormatter implements TaskFormatter<SelectionTask> {

    private final LocalizationService localizationService;

    public SelectionTextTaskFormatter(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    @Override
    public String format(SelectionTask task) {
        StringBuilder sb = new StringBuilder();
        sb.append(localizationService.message("question", "Question")).append(":")
                .append(task.getQuestion())
                .append("\n")
                .append(localizationService.message("answers", "Chose one answer"))
                .append("\n");
        for(String answer : task.getAnswers()) {
            sb.append(answer).append("\n");
        }
        sb.append(localizationService.message("type_answer", "Type answer"));
        return sb.toString();
    }
}
