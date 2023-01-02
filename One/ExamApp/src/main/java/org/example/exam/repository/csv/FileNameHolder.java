package org.example.exam.repository.csv;

import org.example.exam.localization.LocaleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileNameHolder {

    private final LocaleProvider localeProvider;
    private final String filePath;
    private final String fileName;


    public FileNameHolder(@Value("${application.dataFilePath}") String filePath, @Value("${application.dataFileName}") String fileName, LocaleProvider localeProvider) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.localeProvider = localeProvider;
    }

    public String getFullPath() {
        return filePath + "/" + localeProvider.getLocale().getLanguage() + "_" + localeProvider.getLocale().getCountry() + "/" + fileName;
    }

    public String getResourceFilePath() {
        return filePath + "/" + fileName;
    }
}
