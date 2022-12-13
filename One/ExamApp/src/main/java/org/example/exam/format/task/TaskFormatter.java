package org.example.exam.format.task;

import org.example.exam.model.Task;

// Обобщенный интерфейс для форматированного вывода задания
public interface TaskFormatter<T extends Task> {
    String format(T task);
}
