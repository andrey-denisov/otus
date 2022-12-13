package org.example.exam.io.impl;

import org.example.exam.io.UserInput;
import org.springframework.stereotype.Service;

import java.util.Scanner;

// реализация пользовательского ввода в виде консоли
@Service
public class ConsoleUserInput implements UserInput {

    @Override
    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }
}
