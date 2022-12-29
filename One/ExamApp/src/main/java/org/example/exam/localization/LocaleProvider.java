package org.example.exam.localization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocaleProvider {

    private final Locale locale;

    public LocaleProvider(@Value("${locale:en}") String localeName) {
        this.locale = Locale.forLanguageTag(localeName);
    }

    public Locale getLocale() {
        return locale;
    }
}
