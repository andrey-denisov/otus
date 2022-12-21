package org.example.exam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class I18Config {

    @Value("${spring.messages.basename}")
    private String[] basenames;

    @Value("${spring.messages.encoding}")
    private String encoding;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(encoding);
        messageSource.setBasenames(basenames);
        return messageSource;
    }
}
