package com.example.grocerystore.store;

import com.example.grocerystore.ui.ConsoleUI;

public class GroceryStore {

    private final String[] groceries = {
        "bread", "banana", "chicken breast", "peanut butter", "milk",
        "oats", "apple", "hide and seek", "kurkure", "takatak"
    };

    private final float[] prices = {
        59.0f, 45.0f, 249.5f, 312.0f, 27.0f,
        120.0f, 90.0f, 30.0f, 20.0f, 20.0f
    };

    private final int[] stock = {
        0, 10, 3, 4, 8, 6, 5, 7, 10, 10
    };

    public float checkout(ConsoleUI ui) {

        float totalBill = 0;

        while (true) {

            ui.printCatalog(this);
            String item = ui.readInput("Please enter the item name to add to cart: ");

            if (item.equals("finish") || item.equals("11")) {
                break;
            }

            int index = getItemIndex(item);

            if (index == -1) {
                System.out.println("Item not found!");
                continue;
            }

            if (stock[index] <= 0) {
                System.out.println("Sorry, " + groceries[index] + " is out of stock!");
                continue;
            }

            stock[index]--;
            totalBill += prices[index];

            System.out.println(groceries[index] + " added. Remaining stock: " + stock[index]);
        }

        return totalBill;
    }

    public float calculateDiscount(float totalBill) {
        if (totalBill > 100) {
            return totalBill * 0.1f;
        }
        
        return 0;
    }

    public void search(ConsoleUI ui) {
        String keyword = ui.readInput("Enter keyword to search: ");

        for (int i = 0; i < groceries.length; i++) {
            if (groceries[i].contains(keyword)) {
                System.out.println(groceries[i] + " --- " + prices[i]);
            }
        }
    }

    public void filterBelowPrice(float budget) {
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] <= budget) {
                System.out.println(groceries[i] + " --- " + prices[i]);
            }
        }
    }

    public float calculateAveragePrice() {
        float sum = 0;
        for (float p : prices) {
            sum += p;
        }
        return sum / prices.length;
    }

    private int getItemIndex(String item) {
        for (int i = 0; i < groceries.length; i++) {
            if (groceries[i].equalsIgnoreCase(item)) {
                return i;
            }
        }
        return -1;
    }

    public String[] getGroceries() { return groceries; }
    public float[] getPrices() { return prices; }
    public int[] getStock() { return stock; }
}
