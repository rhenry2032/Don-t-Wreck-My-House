package learn.mastery.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleIO {

    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public void println(String message) {
        System.out.println(message);
    }

    public void print(String message) {
        System.out.print(message);
    }

    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    public String readString(String prompt) {
        print(prompt);
        return scanner.nextLine();
    }

    public String readRequiredString(String prompt) {
        while (true) {
            String input = readString(prompt);
            if (!input.trim().isEmpty()) {
                return input.trim();
            }

            println("Value is required.");
        }
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            String input = readRequiredString(prompt);
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                println(String.format("Value must be between %d and %d.", min, max));
            } catch (NumberFormatException ex) {
                println("Invalid number.");
            }
        }
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;            // Accept blank entry
            }

            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ex) {
                println("Invalid date format. Please use MM/dd/yyyy.");
            }
        }
    }


    public LocalDate readDate(String prompt, LocalDate defaultValue) {
        print(prompt);
        String input = scanner.nextLine();
        if (input.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return LocalDate.parse(input, formatter);
        } catch (DateTimeParseException ex) {
            println("Invalid date format. Please use MM/dd/yyyy.");
            return readDate(prompt, defaultValue); // retry
        }
    }

    public boolean readBoolean(String prompt) {
        while (true) {
            String input = readRequiredString(prompt + " (y/n): ");
            if (input.equalsIgnoreCase("y")) return true;
            if (input.equalsIgnoreCase("n")) return false;
            println("Please enter 'y' or 'n'.");
        }
    }

    public String formatDate(LocalDate date) {
        return date.format(formatter);
    }
}
