package org.example.exam.localization;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageLocalizationService {

    private final MessageSource messageSource;

    private final LocaleProvider localeProvider;

    public MessageLocalizationService(MessageSource messageSource, LocaleProvider localeProvider) {
        this.messageSource = messageSource;
        this.localeProvider = localeProvider;
    }

    public String message(String code, String defaultMessage, Object... args) {
        return messageSource.getMessage(code, args, defaultMessage, localeProvider.getLocale());
    }
}
