package org.example.exam.service;

import org.example.exam.model.TaskResult;
import org.springframework.stereotype.Service;

import java.util.Collection;

// простейшая реализация сервиса выставления оценок
public class SimpleRateSerivce implements RateService{

    @Override
    public int rate(Collection<TaskResult> taskResults) {
        final long incorrect = taskResults.stream().filter(r -> !r.isCorrect()).count();
        if(incorrect == 0){
            return 5;
        }
        if(incorrect == 1) {
            return 4;
        }
        if(incorrect == 2) {
            return 3;
        }
        return 2;
    }
}
