import java.util.*;
import java.io.*;
import java.time.*;

public class ExpenseTracker {
    private static List<Transaction> transactions = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static double currentAmount = 0.0;

    public static void main(String[] args) {
        while (true) {
            System.out.println(" Expense Tracker ");
            System.out.println("--------------------");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Save to File");
            System.out.println("5. Load from File");
            System.out.println("6. Exit");
            System.out.println();
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addTransaction("Income");
                case 2 -> addTransaction("Expense");
                case 3 -> viewSummary();
                case 4 -> saveToFile();
                case 5 -> loadFromFile();
                case 6 -> {
                    System.out.println("Exiting... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addTransaction(String type) {
        System.out.print("Enter category (e.g., Salary, Food, Rent): ");
        String category = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        LocalDate date = LocalDate.now();

        if (type.equalsIgnoreCase("Income")) {
            currentAmount += amount;
        } else if (type.equalsIgnoreCase("Expense")) {
            currentAmount -= amount;
        }

        Transaction t = new Transaction(type, category, amount, date);
        transactions.add(t);
        System.out.println(type + " added.");
        viewSummary();
    }

    private static void viewSummary() {
        Map<String, Double> summary = new HashMap<>();
        for (Transaction t : transactions) {
            String key = t.getDate().getMonth() + " - " + t.getType() + " - " + t.getCategory();
            summary.put(key, summary.getOrDefault(key, 0.0) + t.getAmount());
        }

        System.out.println("Monthly Summary");
        System.out.println("-------------------");
        for (String key : summary.keySet()) {
            System.out.println(key + ": " + summary.get(key) + "rs");
        }
        System.out.println("Current Amount : " + currentAmount);
    }

    private static void saveToFile() {
        System.out.print("Give a File Name:");
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine();
        try (PrintWriter writer = new PrintWriter(new File(fileName + ".txt"))) {
            for (Transaction t : transactions) {
                writer.println(t.toString());
            }
            System.out.println("Data saved to " + fileName + ".txt");
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    private static void loadFromFile() {
        System.out.print("Enter filename to load (e.g., transactions): ");
        String filename = scanner.nextLine();
        transactions.clear();
        currentAmount = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction t = Transaction.fromString(line);
                transactions.add(t);
                if (t.getType().equalsIgnoreCase("Income")) {
                    currentAmount += t.getAmount();
                } else if (t.getType().equalsIgnoreCase("Expense")) {
                    currentAmount -= t.getAmount();
                }
            }
            System.out.println("Data loaded from " + filename);
            viewSummary();
        } catch (IOException e) {
            System.out.println("Error loading file.");
        }
    }
}
