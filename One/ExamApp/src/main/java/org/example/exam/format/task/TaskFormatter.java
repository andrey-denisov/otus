package org.example.exam.format.task;

import org.example.exam.localization.MessageSourceWrapper;
import org.example.exam.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskFormatter {

    private final MessageSourceWrapper messageSourceWrapper;

    public TaskFormatter(MessageSourceWrapper messageSourceWrapper) {
        this.messageSourceWrapper = messageSourceWrapper;
    }

    public String format(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(messageSourceWrapper.message("question", "Question")).append(":")
                .append(task.getQuestion())
                .append("\n")
                .append(messageSourceWrapper.message("answers", "Chose one answer"))
                .append("\n");
        for(String answer : task.getAnswers()) {
            sb.append(answer).append("\n");
        }
        sb.append(messageSourceWrapper.message("type_answer", "Type answer"));
        return sb.toString();
    }
}
