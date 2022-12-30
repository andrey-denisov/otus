package org.example.exam.format.task;

import org.example.exam.localization.MessageLocalizationService;
import org.example.exam.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskFormatter {

    private final MessageLocalizationService messageLocalizationService;

    public TaskFormatter(MessageLocalizationService messageLocalizationService) {
        this.messageLocalizationService = messageLocalizationService;
    }

    public String format(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(messageLocalizationService.message("question", "Question")).append(":")
                .append(task.getQuestion())
                .append("\n")
                .append(messageLocalizationService.message("answers", "Chose one answer"))
                .append("\n");
        for(String answer : task.getAnswers()) {
            sb.append(answer).append("\n");
        }
        sb.append(messageLocalizationService.message("type_answer", "Type answer"));
        return sb.toString();
    }
}
