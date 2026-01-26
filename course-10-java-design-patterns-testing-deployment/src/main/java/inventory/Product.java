package inventory;

/**
 * Product class representing items in the inventory.
 */
public class Product {

    // Declare private fields
    private String id;
    private String name;
    private String type;
    private double price;
    private int quantity;

    /**
     * Constructor with validation
     * @param id - unique product identifier
     * @param name - product name
     * @param type - product category (BOOK or ELECTRONICS)
     * @param price - product price (must be non-negative)
     * @param quantity - stock quantity (must be non-negative)
     * @throws IllegalArgumentException if price or quantity is negative
     */
    public Product(String id, String name, String type, double price, int quantity) {
        if (price < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }

        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setter methods with validation
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }
        this.quantity = quantity;
    }

    /**
     * Sell method
     * @param amount - quantity to sell
     * @return true if sale successful, false if insufficient stock
     */
    public boolean sell(int amount) {
        if (amount <= 0) {
            return false;
        }
        if (quantity >= amount) {
            quantity -= amount;
            return true;
        }
        return false;
    }

    /**
     * Add stock method
     * @param amount - quantity to add to stock
     */
    public void addStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to add must be positive");
        }
        quantity += amount;
    }

    /**
     * Check if in stock
     * @return true if product has stock available
     */
    public boolean isInStock() {
        return quantity > 0;
    }

    /**
     * String representation
     * @return formatted string representation of the product
     */
    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "', type='" + type +
               "', price=" + price + ", quantity=" + quantity + "}";
    }
}
