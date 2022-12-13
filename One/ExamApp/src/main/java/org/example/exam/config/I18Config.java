package org.example.exam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class I18Config {

    @Value("${application.locale:en}")
    private String localeName;

    @Bean
    public Locale locale() {
      return Locale.forLanguageTag(localeName);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames("classpath:/i18/messages", "classpath:/i18/content");
        return messageSource;
    }
}
