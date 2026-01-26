package inventory;

import java.util.*;

/**
 * Main business logic class that manages inventory operations.
 */
public class InventoryManager {

    // Declare collection to store products
    private Map<String, Product> products;

    /**
     * Constructor
     */
    public InventoryManager() {
        // Initialize the products collection
        this.products = new HashMap<>();
    }

    /**
     * Add product to inventory using Factory pattern
     */
    public void addProduct(String id, String name, String type, double price, int quantity) {
        try {
            if (products.containsKey(id)) {
                System.out.println("Product with ID " + id + " already exists.");
                return;
            }

            Product product = ProductFactory.createProduct(id, name, type, price, quantity);
            products.put(id, product);
            System.out.println("Product added: " + product);

        } catch (IllegalArgumentException e) {
            System.out.println("Failed to add product: " + e.getMessage());
        }
    }

    /**
     * Sell product with discount using Strategy pattern
     */
    public void sellProduct(String id, int quantity, String discountType) {
        Product product = products.get(id);

        if (product == null) {
            System.out.println("Product not found: " + id);
            return;
        }

        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (product.getQuantity() < quantity) {
            System.out.println("Insufficient stock for product: " + id);
            return;
        }

        DiscountCalculator.DiscountResult result =
                DiscountCalculator.calculateDiscount(product, quantity, discountType);

        double originalPrice = product.getPrice() * quantity;
        double finalPrice = originalPrice - result.getDiscountAmount();

        boolean success = product.sell(quantity);
        if (success) {
            displaySalesSummary(product, quantity, originalPrice, finalPrice, result.getDescription());
        } else {
            System.out.println("Sale could not be completed.");
        }
    }

    /**
     * Add stock to existing product
     */
    public void addStock(String id, int quantity) {
        Product product = products.get(id);

        if (product == null) {
            System.out.println("Product not found: " + id);
            return;
        }

        try {
            product.addStock(quantity);
            System.out.println("Stock added. New quantity for " + id + ": " + product.getQuantity());
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to add stock: " + e.getMessage());
        }
    }

    /**
     * Display all products in inventory
     */
    public void viewInventory() {
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        for (Product product : products.values()) {
            System.out.println(product);
        }
    }

    /**
     * Calculate total inventory value
     */
    public double getInventoryValue() {
        double total = 0.0;

        for (Product product : products.values()) {
            total += product.getPrice() * product.getQuantity();
        }

        return total;
    }

    /**
     * Get products with low stock
     */
    public List<Product> getLowStockProducts(int threshold) {
        List<Product> lowStock = new ArrayList<>();

        for (Product product : products.values()) {
            if (product.getQuantity() <= threshold) {
                lowStock.add(product);
            }
        }

        return lowStock;
    }

    /**
     * Display inventory statistics
     */
    public void viewStatistics() {
        System.out.println("Total products: " + products.size());
        System.out.println("Total inventory value: $" + getInventoryValue());

        List<Product> lowStock = getLowStockProducts(5);
        if (lowStock.isEmpty()) {
            System.out.println("No low stock products.");
        } else {
            System.out.println("Low stock products (<= 5):");
            for (Product product : lowStock) {
                System.out.println(product);
            }
        }
    }

    // Helper methods

    private boolean productExists(String id) {
        return products.containsKey(id);
    }

    private void displaySalesSummary(Product product, int quantity, double originalPrice,
                                     double finalPrice, String discountInfo) {
        System.out.println("Sale completed:");
        System.out.println("Product: " + product.getName());
        System.out.println("Quantity: " + quantity);
        System.out.println("Original price: $" + originalPrice);
        System.out.println("Discount: " + discountInfo);
        System.out.println("Final price: $" + finalPrice);
    }
}