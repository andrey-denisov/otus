package org.example.exam.service;

import org.example.exam.model.TaskResultDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;

// простейшая реализация сервиса выставления оценок
@Component
@Qualifier("SimpleRateSerivce")
public class SimpleRateService implements RateService{

    @Override
    public int rate(Collection<TaskResultDetail> taskResultDetails) {
        final long incorrect = taskResultDetails.stream().filter(r -> !r.isCorrect()).count();
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
