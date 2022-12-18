package org.example.exam.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocalizationService {

    private final MessageSource messageSource;

    private final Locale locale;

    public LocalizationService(MessageSource messageSource, @Value("${locale:en}") String localeName) {
        this.messageSource = messageSource;
        this.locale = Locale.forLanguageTag(localeName);
    }

    public String message(String code, String defaultMessage) {
        return messageSource.getMessage(code, null, defaultMessage, locale);
    }
}
