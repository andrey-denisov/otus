package org.example.exam.service;

import org.example.exam.model.TaskResultDetail;

import java.util.Collection;

// сервис выставления оценок по результатам экзамена
public interface RateService {
    int rate(Collection<TaskResultDetail> taskResultDetails);
}
