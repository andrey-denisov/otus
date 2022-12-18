package org.example.exam.io.impl;

import org.example.exam.io.Display;
import org.example.exam.io.IOFacade;
import org.example.exam.io.UserInput;
import org.springframework.stereotype.Service;

@Service
public class ConsoleIOFacade implements IOFacade {

    private final Display display;
    private final UserInput input;

    public ConsoleIOFacade(Display display, UserInput input) {
        this.display = display;
        this.input = input;
    }

    @Override
    public void display(String message) {
        display.display(message);
    }

    @Override
    public String getUserInput() {
        return input.getUserInput();
    }
}
