package inventory;

import java.util.Scanner;

/**
 * Main class providing command-line interface for the inventory system.
 */
public class Main {

    // Class-level variables
    private static InventoryManager manager;
    private static Scanner scanner;

    /**
     * Main method
     */
    public static void main(String[] args) {
        // Initialize manager and scanner
        manager = new InventoryManager();
        scanner = new Scanner(System.in);

        System.out.println("Welcome to Inventory Management System!");

        // Load sample data
        loadSampleData();

        // Main application loop
        while (true) {
            showMenu();
            int choice = getChoice();
            handleChoice(choice);
        }
    }

    /**
     * Load sample data for testing
     */
    private static void loadSampleData() {
        manager.addProduct("B001", "Java Programming", "BOOK", 29.99, 10);
        manager.addProduct("B002", "Design Patterns", "BOOK", 35.50, 8);
        manager.addProduct("E001", "Laptop", "ELECTRONICS", 999.99, 5);
        manager.addProduct("E002", "Mouse", "ELECTRONICS", 25.99, 15);
    }

    /**
     * Display main menu options
     */
    private static void showMenu() {
        System.out.println();
        System.out.println("===== Main Menu =====");
        System.out.println("1. Add Product");
        System.out.println("2. View Inventory");
        System.out.println("3. Sell Product");
        System.out.println("4. Add Stock");
        System.out.println("5. View Statistics");
        System.out.println("6. Exit");
        System.out.println("=====================");
    }

    /**
     * Get user choice with input validation
     */
    private static int getChoice() {
        while (true) {
            System.out.print("Enter choice (1-6): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 6) {
                    return choice;
                }
                System.out.println("Please enter a number between 1 and 6.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Handle user menu choice
     */
    private static void handleChoice(int choice) {
        switch (choice) {
            case 1:
                addProduct();
                break;
            case 2:
                manager.viewInventory();
                break;
            case 3:
                sellProduct();
                break;
            case 4:
                addStock();
                break;
            case 5:
                manager.viewStatistics();
                break;
            case 6:
                System.out.println("Exiting application.");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Handle adding new product
     */
    private static void addProduct() {
        String id = getStringInput("Enter product ID: ");
        String name = getStringInput("Enter product name: ");
        String type = getStringInput("Enter product type (BOOK/ELECTRONICS): ");
        double price = getDoubleInput("Enter product price: ");
        int quantity = getIntInput("Enter initial quantity: ");

        manager.addProduct(id, name, type, price, quantity);
    }

    /**
     * Handle selling product
     */
    private static void sellProduct() {
        String id = getStringInput("Enter product ID: ");
        int quantity = getIntInput("Enter quantity to sell: ");
        String discountType = getStringInput("Enter discount type (STUDENT/BULK/NONE): ");

        manager.sellProduct(id, quantity, discountType);
    }

    /**
     * Handle adding stock
     */
    private static void addStock() {
        String id = getStringInput("Enter product ID: ");
        int quantity = getIntInput("Enter quantity to add: ");

        manager.addStock(id, quantity);
    }

    // Helper methods for input validation

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }
}
