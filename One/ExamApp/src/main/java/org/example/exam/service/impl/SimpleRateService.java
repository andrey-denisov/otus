package org.example.exam.service.impl;

import org.example.exam.model.TaskResultDetail;
import org.example.exam.service.RateService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "application")
public class SimpleRateService implements RateService {

    private Map<String, Integer> incorrectAnswersCountToMark;

    public void setIncorrectAnswersCountToMark(Map<String, Integer> incorrectAnswersCountToMark) {
        this.incorrectAnswersCountToMark = incorrectAnswersCountToMark;
    }

    @Override
    public int rate(Collection<TaskResultDetail> taskResultDetails) {
        final long incorrect = taskResultDetails.stream().filter(r -> !r.isCorrect()).count();
        Integer mark = incorrectAnswersCountToMark.get(String.valueOf(incorrect));
        if (mark == null) {
            return incorrectAnswersCountToMark.get("default");
        }
        return mark;
    }

}
