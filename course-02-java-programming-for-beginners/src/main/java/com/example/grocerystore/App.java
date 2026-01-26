package com.example.grocerystore;

import com.example.grocerystore.store.GroceryStore;
import com.example.grocerystore.ui.ConsoleUI;

public class App {

    public static void main(String[] args) {

        GroceryStore store = new GroceryStore();
        ConsoleUI ui = new ConsoleUI();

        System.out.println("Hello World! Welcome to the GroceryShop");

        while (true) {

            ui.printAvailableCommands();
            String command = ui.readInput("Please enter a valid command: ");

            switch (command) {
                case "1":
                case "checkout":
                    float total = store.checkout(ui);
                    float discount = store.calculateDiscount(total);
                    ui.printBill(total, discount);
                    break;

                case "2":
                case "print catalog":
                    ui.printCatalog(store);
                    break;

                case "3":
                case "search":
                    store.search(ui);
                    break;

                case "4":
                case "average":
                    ui.printAverage(store.calculateAveragePrice());
                    break;

                case "5":
                case "filter price":
                    float budget = ui.readFloat("Mention your budget: ");
                    store.filterBelowPrice(budget);
                    break;

                case "99":
                case "exit":
                    System.out.println("Thanks for your time and shopping with us!");
                    System.exit(0);

                default:
                    System.out.println("Wrong Input! Please be mindful!");
            }

            System.out.println("-----------------------");
        }
    }
}
