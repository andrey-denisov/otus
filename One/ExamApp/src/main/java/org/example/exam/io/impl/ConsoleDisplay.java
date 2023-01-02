package org.example.exam.io.impl;

import org.example.exam.io.Display;
import org.springframework.stereotype.Service;

// реализация вывода в виде консоли
@Service
public class ConsoleDisplay implements Display {

    @Override
    public void display(String message) {
        System.out.println(message);
    }

}
