# Ultra Accounting Ledger

## Overview
Ultra Accounting Ledger is a Java console application for easily recording financial transactions (deposits and payments) using a CSV file as a database. It allows you to add, view, and filter transactions, as well as generate quick reports.

---

## Project Structure

```
/ultra-accounting-ledger
│
├── src/main/java/com/pluralsight/
│   ├── Main.java
│   └── Transaction.java
│
├── transactions.csv
└── README.md
```

---

## Main Files and Components

### 1. `transactions.csv` (Transaction Database)
- **Purpose:** Stores all transactions in plain text format.
- **Format:**
  ```
  date|time|description|vendor|amount
  2025-04-30|13:21:50|payment|luis|90.00
  ```
- **Notes:**
  - Each line represents a transaction.
  - Deposits are positive amounts, payments are negative.

### 2. `Transaction.java` (Model Class)
- **Purpose:** Represents an individual transaction.
- **Attributes:**
  - `date`, `time`, `description`, `vendor`, `amount`.
- **Key Methods:**
  - `fromCsvLine(String csvLine)`: Creates a Transaction instance from a CSV line.
  - Getters for each attribute.
  - `getDateTime()`: Returns a LocalDateTime object useful for sorting.
  - `toString()`: Human-readable representation of the transaction.
- **Location:** `src/main/java/com/pluralsight/Transaction.java`

### 3. `Main.java` (Main Logic and Menus)
- **Purpose:** Controls the application flow and displays menus.
- **Main Functions:**
  - **Main Menu (Home Screen):**
    - `D`: Add deposit
    - `P`: Register payment
    - `L`: View ledger
    - `X`: Exit
  - **Add Deposit:**
    - Requests description, vendor, and amount.
    - Saves the transaction as positive in the CSV.
  - **Register Payment:**
    - Requests description, vendor, and amount.
    - Saves the transaction as negative in the CSV.
  - **Ledger:**
    - Allows viewing all transactions, only deposits, only payments, or accessing reports.
    - Options: `A` (All), `D` (Deposits), `P` (Payments), `R` (Reports), `H` (Home)
  - **Reports:**
    - `1`: Current month
    - `2`: Previous month
    - `3`: Current year
    - `4`: Previous year
    - `5`: Search by vendor
    - `0`: Back
- **Location:** `src/main/java/com/pluralsight/Main.java`

---

## Application Flow

1. **Start:** The main menu is displayed.
2. **Add/Register:** The user can register deposits or payments, which are saved to the CSV.
3. **Ledger:** Allows viewing and filtering transactions.
4. **Reports:** Allows querying transactions by period or vendor.
5. **Exit:** Ends the application.

---

## How to Compile and Run?

```bash
# Compile
javac src/main/java/com/pluralsight/*.java -d out

# Run
java -cp out com.pluralsight.Main
```

---

## Key Names to Study
- **Main Class:** `Main`
- **Data Model:** `Transaction`
- **Data File:** `transactions.csv`
- **Main Menu:** Home Screen
- **Ledger:** Ledger
- **Reports:** Reports

---

## Tips for Studying this Project
- Review the menu flow in `Main.java`.
- Analyze how the CSV file is read and written.
- Observe how transactions are filtered and sorted.
- Practice adding and querying transactions.

---

Ready to practice and modify your own CLI accounting system in Java!
