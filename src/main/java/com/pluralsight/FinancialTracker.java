package com.pluralsight;

import com.pluralsight.Transaction;

import javax.annotation.processing.Generated;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);

        for (Transaction transaction : transactions) {
            System.out.println("Date " + transaction.getDate() + "|" + "Time: " + transaction.getTime() + "|" + "Type" + transaction.getDescription() + "|" + "Vendor:" + transaction.getVendor() + "|" + "price" + transaction.getAmount() + "\n");
        }


        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }


    public static void loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>,<time>,<vendor>,<type>,<amount>
        // For example: 2023-04-29,13:45:00,Amazon,PAYMENT,29.99
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length == 5) {

                    LocalDate date = LocalDate.parse(parts[0].trim(), DATE_FORMATTER);
                    LocalTime time = LocalTime.parse(parts[1].trim(), TIME_FORMATTER);
                    String description = parts[2].trim();
                    String vendor = parts[3].trim();
                    double amount = Double.parseDouble(parts[4]);
                    transactions.add(new Transaction(date, time, description, vendor, amount));
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error Loading Inventory: " + e.getMessage());
        }
    }


    private static void addDeposit(Scanner scanner) {
        System.out.println("Add Deposit");
        System.out.println("Enter the date  (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        String description = scanner.nextLine();
        System.out.println("Enter the vendor: ");
        String vendor = scanner.nextLine();
        System.out.println("Enter your deposit amount");
        double amount = scanner.nextDouble();

        Transaction transaction = new Transaction(date, time, description, vendor, amount);
        transactions.add(transaction);

        String transactionString = String.format("%s|%s|%s|%s|%.2f", date.format(DATE_FORMATTER), time.format(TIME_FORMATTER),
                description, vendor, amount);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(transactionString);
            writer.newLine();
            System.out.println("Deposit added successfully");
        } catch (Exception ex) {
            System.out.println("Failed to add deposit");
        }


    }

    private static void addPayment(Scanner scanner) {
        System.out.println("Add Payment");
        System.out.println("Enter the date  (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);
        System.out.println("Enter the vendor: ");
        String vendor = scanner.nextLine();
        System.out.println("Enter the payment amount");
        double amount = scanner.nextDouble();

        String description = scanner.nextLine();


        Transaction transaction = new Transaction(date, time, description, vendor, amount);
        transactions.add(transaction);

        String transactionString = String.format("%s|%s|%s|%s|*-%.2f", date.format(DATE_FORMATTER), time.format(TIME_FORMATTER),
                description, vendor, amount);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(transactionString);
            writer.newLine();
            System.out.println("Payment added successfully");
        } catch (Exception ex) {
            System.out.println("Failed to add deposit");
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H": // line adds break statement
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, type, and amount.

        System.out.println("Date\t\tTime\tDescription\tVendor\t\tAmount");

        for (Transaction transaction : transactions) {

            System.out.printf("%s\t%s\t%s\t%s\t%.2f\n",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());

        }
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.

        System.out.println("Date\t\tTime\tDescription\tVendor\t\tAmount");

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.printf("%s\t%s\t%s\t%s\t%.2f\n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
    }


    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.

        System.out.println("Date\t\tTime\tDescription\tVendor\t\tAmount");

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.printf("%s\t%s\t%s\t%s\t%.2f\n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }

    }


    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    LocalDate currentDate = LocalDate.now();
                    Month currentMonth = currentDate.getMonth();


                    // Generate a report for all transactions within the current month,
                    // including the date, vendor, and amount for each transaction.
                case "2":

                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.
                case "3":

                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                case "5":

                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, vendor, and amount for each transaction.
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    public void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        boolean found = false;
        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.isAfter(startDate.minusDays(1)) && transactionDate.isBefore(endDate.plusDays(1))) {
                System.out.println(transaction);

                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found within the date range: " + startDate + " to " + endDate);
        }


    }


    public void filterTransactionsByVendor(String vendor) {
        boolean found = false;
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for vendor: " + vendor);
        }


    }
}




