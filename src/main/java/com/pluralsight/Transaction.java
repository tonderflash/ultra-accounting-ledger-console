package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Transaction {
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    // Formatter for parsing combined date and time if needed, or just date/time separately
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    // Combine date and time for sorting
    public LocalDateTime getDateTime() {
        return LocalDateTime.of(this.date, this.time);
    }

    @Override
    public String toString() {
        // A basic string representation, can be formatted nicer later
        return String.format("Date: %s, Time: %s, Desc: %s, Vendor: %s, Amount: %.2f",
                date.format(DATE_FORMATTER),
                time.format(TIME_FORMATTER),
                description, vendor, amount);
    }

    // Static method to parse from CSV line (optional, can be done in Main)
    public static Transaction fromCsvLine(String csvLine) {
        String[] fields = csvLine.split(Pattern.quote("|"));
        if (fields.length == 5) {
            try {
                LocalDate date = LocalDate.parse(fields[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(fields[1], TIME_FORMATTER);
                String description = fields[2];
                String vendor = fields[3];
                double amount = Double.parseDouble(fields[4]);
                return new Transaction(date, time, description, vendor, amount);
            } catch (Exception e) {
                System.err.println("Error parsing line: " + csvLine + " - " + e.getMessage());
                return null; // Or throw an exception
            }
        }
        return null; // Invalid format
    }
}
