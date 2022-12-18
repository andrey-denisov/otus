package org.example.exam.repository.csv;

import org.example.exam.service.LocalizationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("LocalizedCsvFileReader")
public class LocalizedCsvFileReader implements CsvFileReader {

    private final LocalizationService localizationService;
    private final CsvFileReader decorated;

    public LocalizedCsvFileReader(@Qualifier("PlainCsvFileReader") CsvFileReader decorated, LocalizationService localizationService) {
        this.localizationService = localizationService;
        this.decorated = decorated;
    }

    @Override
    public List<String> read(File file) {
        return decorated
                .read(file)
                .stream()
                .map(line -> localizationService.message(line, line))
                .collect(Collectors.toList());
    }
}
