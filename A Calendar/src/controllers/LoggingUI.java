package controllers;

import useCases.CalendarManager;

import java.io.*;
import java.util.Scanner;



public class LoggingUI {
    /**
     * Give user with TEXT UI to log in and check the username and password if it exist.
     * Option 1: Create a new User.
     * Option 2: Log in to an existing user.
     * Option 3: Exit the program.
     */

    private LoggingIO logIO;

    public LoggingUI() {
        logIO = new LoggingIO();
    }

    public String start() {
        System.out.println("What do you want to do?");
        System.out.println("Option 1: Create a new user.");
        System.out.println("Option 2: Login into an existing user.");
        System.out.println("Option 3: Exit the program.");

        Scanner scanner = new Scanner(System.in);
        int input_option;

        // Keep looping until input is in {1,2,3}
        do {
            try {
                System.out.print("Enter your option number: ");
                input_option = scanner.nextInt();

                switch (input_option) {
                    // Call private method handling signing up.
                    case 1:
                        return this.signUp();

                    // Call private method for logging in.
                    case 2:
                        return this.logIn();

                    // Exit function and return to Main.
                    case 3:
                        System.out.println("Goodbye!");
                        return null;

                    // An invalid integer was inputted, ask for a correct input again.
                    default:
                        System.out.println("Error, please enter a valid option number.");
                }

            } catch (Exception InputMismatchException) {
                System.out.println("Error, please enter a valid option number.");
                scanner.next();
            }
        } while (true);
    }

    private String logIn() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String username, password;

        System.out.println("Enter your username and password.");
        System.out.print("username: ");
        username = scanner.nextLine();
        System.out.print("password: ");
        password = scanner.nextLine();

        if (logIO.userExists(username, password)) {
            // Log in successful
            return username;
        } else {
            System.out.println("No matching userdata found. Please check your both username and password enter correctly.");
            return this.start();
        }
    }

    private String signUp() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String username, password;

        System.out.println("Enter your username and password.");
        System.out.print("username: ");
        username = scanner.nextLine();
        System.out.print("password: ");
        password = scanner.nextLine();

        if (logIO.userExists(username, password)) {
            // Log in successful
            System.out.println("There already exists a same username.");
            return this.start();
        }

        logIO.createUser(username, password);
        System.out.println("User creation successful.");

        return this.start();
    }
}
