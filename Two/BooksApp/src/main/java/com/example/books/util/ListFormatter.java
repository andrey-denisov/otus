package com.example.books.util;

import java.util.List;
import java.util.stream.Collectors;

public class ListFormatter {
    public static <T, F extends Formatter<T>> String format(List<T> list, Formatter<T> formatter, String delimiter) {
        return list.stream().map(formatter::format).collect(Collectors.joining(delimiter));
    }
}
