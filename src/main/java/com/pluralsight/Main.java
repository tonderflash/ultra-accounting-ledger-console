package com.pluralsight;

import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.List; 
import java.util.stream.Collectors; 
import java.time.Month; 
import java.time.YearMonth; 

public class Main {
    private static final String TRANSACTIONS_FILE = "transactions.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nHome Screen");
            System.out.println("--------------------------------");
            System.out.println("Choose an option:");
            System.out.println("  D) Add Deposit");
            System.out.println("  P) Make Payment (Debit)");
            System.out.println("  L) Ledger");
            System.out.println("  X) Exit");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    makePayment(scanner);
                    break;
                case "L":
                    displayLedger(scanner);
                    break;
                case "X":
                    running = false;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
        scanner.close();
    }

    public static void addDeposit(Scanner scanner) {
        try {
            System.out.println("\nPlease enter the deposit details:");
            System.out.print("Description: ");
            String description = scanner.nextLine();
            System.out.print("Vendor: ");
            String vendor = scanner.nextLine();
            double amount = 0;
            boolean validInput = false;
            while (!validInput) {
                System.out.print("Amount: ");
                try {
                    amount = Double.parseDouble(scanner.nextLine());
                    if (amount <= 0) {
                        System.out.println("Deposit amount must be positive.");
                    } else {
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount format. Please enter a number.");
                }
            }

            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            String formattedEntry = String.format("%s|%s|%s|%s|%.2f%n",
                    date.format(DATE_FORMATTER),
                    time.format(TIME_FORMATTER),
                    description,
                    vendor,
                    amount);

            try (FileWriter fw = new FileWriter(TRANSACTIONS_FILE, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(formattedEntry);
                System.out.println("Deposit successfully recorded.");
            } catch (IOException e) {
                System.err.println("Error writing to transactions file: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("An unexpected error occurred during deposit: " + e.getMessage());
        }
    }

    public static void makePayment(Scanner scanner) {
        try {
            System.out.println("\nPlease enter the payment details:");
            System.out.print("Description: ");
            String description = scanner.nextLine();
            System.out.print("Vendor: ");
            String vendor = scanner.nextLine();
            double amount = 0;
            boolean validInput = false;
            while (!validInput) {
                System.out.print("Amount: ");
                try {
                    amount = Double.parseDouble(scanner.nextLine());
                    if (amount <= 0) {
                        System.out.println("Payment amount must be positive.");
                    } else {
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount format. Please enter a number.");
                }
            }

            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            String formattedEntry = String.format("%s|%s|%s|%s|%.2f%n",
                    date.format(DATE_FORMATTER),
                    time.format(TIME_FORMATTER),
                    description,
                    vendor,
                    -amount);

            try (FileWriter fw = new FileWriter(TRANSACTIONS_FILE, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(formattedEntry);
                System.out.println("Payment successfully recorded.");
            } catch (IOException e) {
                System.err.println("Error writing to transactions file: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("An unexpected error occurred during payment: " + e.getMessage());
        }
    }

    public static void displayLedger(Scanner scanner) {
        List<Transaction> transactions = loadTransactions();
        if (transactions.isEmpty()) {
            System.out.println("\nNo transactions found.");
            return;
        }

        boolean showLedger = true;
        while (showLedger) {
            System.out.println("\nLedger Menu");
            System.out.println("--------------------------------");
            System.out.println("  A) All");
            System.out.println("  D) Deposits");
            System.out.println("  P) Payments");
            System.out.println("  R) Reports");
            System.out.println("  H) Home");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A":
                    System.out.println("\n--- All Transactions ---");
                    displayTransactions(transactions);
                    break;
                case "D":
                    System.out.println("\n--- Deposits ---");
                    List<Transaction> deposits = transactions.stream()
                            .filter(t -> t.getAmount() > 0)
                            .collect(Collectors.toList());
                    displayTransactions(deposits);
                    break;
                case "P":
                    System.out.println("\n--- Payments ---");
                    List<Transaction> payments = transactions.stream()
                            .filter(t -> t.getAmount() < 0)
                            .collect(Collectors.toList());
                    displayTransactions(payments);
                    break;
                case "R":
                    displayReportsMenu(scanner, transactions);
                    break;
                case "H":
                    showLedger = false; 
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                Transaction transaction = Transaction.fromCsvLine(line);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            }
            Collections.sort(transactions, Comparator.comparing(Transaction::getDateTime).reversed());
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            if (!(e instanceof java.io.FileNotFoundException)) {
                 e.printStackTrace(); 
            }
        } catch (Exception e) { 
             System.err.println("An unexpected error occurred while loading transactions: " + e.getMessage());
             e.printStackTrace();
        }
        return transactions;
    }

    public static void displayTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }
        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("%-12s | %-10s | %-30s | %-20s | %-10s%n",
                          "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("---------------------------------------------------------------------------------");

        for (Transaction t : transactions) {
            System.out.printf("%-12s | %-10s | %-30s | %-20s | %10.2f%n",
                    t.getDate().format(DATE_FORMATTER),
                    t.getTime().format(TIME_FORMATTER),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
        System.out.println("---------------------------------------------------------------------------------");
    }

    public static void displayReportsMenu(Scanner scanner, List<Transaction> allTransactions) {
        boolean showReports = true;
        while(showReports) {
            System.out.println("\nReports Menu");
            System.out.println("--------------------------------");
            System.out.println("  1) Month To Date");
            System.out.println("  2) Previous Month");
            System.out.println("  3) Year To Date");
            System.out.println("  4) Previous Year");
            System.out.println("  5) Search by Vendor");
            System.out.println("  0) Back");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine();

            List<Transaction> filteredTransactions = new ArrayList<>();
            LocalDate today = LocalDate.now();

            switch (choice) {
                case "1": // Month To Date
                    System.out.println("\n--- Month To Date Transactions ---");
                    YearMonth currentMonth = YearMonth.from(today);
                    filteredTransactions = allTransactions.stream()
                        .filter(t -> YearMonth.from(t.getDate()).equals(currentMonth))
                        .collect(Collectors.toList());
                    displayTransactions(filteredTransactions);
                    break;
                case "2": // Previous Month
                    System.out.println("\n--- Previous Month Transactions ---");
                    YearMonth previousMonth = YearMonth.from(today).minusMonths(1);
                    filteredTransactions = allTransactions.stream()
                        .filter(t -> YearMonth.from(t.getDate()).equals(previousMonth))
                        .collect(Collectors.toList());
                    displayTransactions(filteredTransactions);
                    break;
                case "3": // Year To Date
                    System.out.println("\n--- Year To Date Transactions ---");
                    int currentYear = today.getYear();
                    filteredTransactions = allTransactions.stream()
                        .filter(t -> t.getDate().getYear() == currentYear)
                        .collect(Collectors.toList());
                    displayTransactions(filteredTransactions);
                    break;
                case "4": // Previous Year
                    System.out.println("\n--- Previous Year Transactions ---");
                    int previousYear = today.getYear() - 1;
                    filteredTransactions = allTransactions.stream()
                        .filter(t -> t.getDate().getYear() == previousYear)
                        .collect(Collectors.toList());
                    displayTransactions(filteredTransactions);
                    break;
                case "5": // Search by Vendor
                    System.out.print("Enter Vendor name: ");
                    String vendorName = scanner.nextLine();
                    System.out.println("\n--- Transactions for Vendor: " + vendorName + " ---");
                    filteredTransactions = allTransactions.stream()
                        .filter(t -> t.getVendor().equalsIgnoreCase(vendorName))
                        .collect(Collectors.toList());
                    displayTransactions(filteredTransactions);
                    break;
                case "0": // Back
                    showReports = false; // Exit reports menu
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
