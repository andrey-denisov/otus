package org.example.exam.io;

import org.springframework.stereotype.Service;

@Service
public class IOService {

    private final Display display;
    private final UserInput input;

    public IOService(Display display, UserInput input) {
        this.display = display;
        this.input = input;
    }

    public void display(String message) {
        display.display(message);
    }

    public String getUserInput() {
        return input.getUserInput();
    }
}
