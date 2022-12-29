package org.example.exam.localization;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceWrapper {

    private final MessageSource messageSource;

    private final LocaleProvider localeProvider;

    public MessageSourceWrapper(MessageSource messageSource, LocaleProvider localeProvider) {
        this.messageSource = messageSource;
        this.localeProvider = localeProvider;
    }

    public String message(String code, String defaultMessage) {
        return messageSource.getMessage(code, null, defaultMessage, localeProvider.getLocale());
    }
}
