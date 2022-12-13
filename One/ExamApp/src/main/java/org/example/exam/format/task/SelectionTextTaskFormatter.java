package org.example.exam.format.task;

import org.example.exam.model.task.SelectionTask;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

// Класс, реализующий форматирование задачи с несколькими вариантоми ответа.
// Реализует простое текстовое форматирование для вывода на консоль.
// По-хорошему надо делать форматирование произвольных видов, например, еще HTML и т.д.,
// но тогда надо добавить сюда Visitor, просто реализацией интерфейса
// не обойдешься.
@Component
public class SelectionTextTaskFormatter implements TaskFormatter<SelectionTask> {

    private final MessageSource messageSource;
    private final Locale locale;

    public SelectionTextTaskFormatter(MessageSource messageSource, Locale locale) {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    @Override
    public String format(SelectionTask task) {
        StringBuilder sb = new StringBuilder();
        sb.append(messageSource.getMessage("question", null, "Question", locale)).append(":")
                .append(task.getQuestion())
                .append("\n")
                .append(messageSource.getMessage("answers", null, "Chose one answer", locale)).append(":")
                .append("\n");
        for(String answer : task.getAnswers()) {
            sb.append(answer).append("\n");
        }
        sb.append(messageSource.getMessage("type_answer", null, "Type answer", locale));
        return sb.toString();
    }
}
