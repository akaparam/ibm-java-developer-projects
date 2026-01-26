package com.example.grocerystore.ui;

import com.example.grocerystore.store.GroceryStore;

import java.util.Scanner;

public class ConsoleUI {

    private final Scanner sc = new Scanner(System.in);

    public void printAvailableCommands() {
        System.out.println("1. Checkout");
        System.out.println("2. Print Catalog");
        System.out.println("3. Search item");
        System.out.println("4. Calculate average price of catalog");
        System.out.println("5. Filter price with budget");
        System.out.println("99. Exit!!");
    }

    public void printCatalog(GroceryStore store) {
        String[] items = store.getGroceries();
        float[] prices = store.getPrices();
        int[] stock = store.getStock();

        for (int i = 0; i < items.length; i++) {
            System.out.println(
                (i + 1) + ". " + items[i] +
                " --- " + prices[i] +
                " (Stock: " + stock[i] + ")"
            );
        }
        System.out.println((items.length + 1) + ". Finish");
    }

    public String readInput(String message) {
        System.out.print(message);
        return sc.nextLine().trim().toLowerCase();
    }

    public float readFloat(String message) {
        System.out.print(message);
        return Float.parseFloat(sc.nextLine());
    }

    public void printBill(float total, float discount) {
        System.out.println("Total Price: " + total);

        if (discount > 0) {
            System.out.println("Discount: " + discount);
            System.out.println("Final Price: " + (total - discount));
        }
    }

    public void printAverage(float avg) {
        System.out.println("Average price of catalog: " + avg);
    }
}
