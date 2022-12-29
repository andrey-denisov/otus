package org.example.exam.io.impl;

import org.example.exam.io.UserInput;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.util.Scanner;

// реализация пользовательского ввода в виде консоли
@Service
public class ConsoleUserInput implements UserInput {
    @Override
    public String getUserInput() {
        Console console = System.console();
        String s;
        if(console != null) {
            s = console.readLine();
            console.flush();
        }
        else {
            s = new Scanner(System.in).nextLine();
        }
        return s;
    }
}
