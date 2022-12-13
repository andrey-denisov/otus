package org.example.exam.service;

import org.example.exam.model.TaskResult;

import java.util.Collection;

// сервис выставления оценок по результатам экзамена
public interface RateService {
    int rate(Collection<TaskResult> taskResults);
}
