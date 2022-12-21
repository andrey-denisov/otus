package org.example.exam.localization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocalizationHelper {

    private final MessageSource messageSource;

    private final Locale locale;

    public LocalizationHelper(MessageSource messageSource, @Value("${locale:en}") String localeName) {
        this.messageSource = messageSource;
        this.locale = Locale.forLanguageTag(localeName);
    }

    public String message(String code, String defaultMessage) {
        return messageSource.getMessage(code, null, defaultMessage, locale);
    }
}
