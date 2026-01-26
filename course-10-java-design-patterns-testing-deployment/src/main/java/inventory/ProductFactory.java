package inventory;

/**
 * Factory class for creating Product objects with business rules.
 */
public class ProductFactory {

    // Define product type constants
    public static final String BOOK_TYPE = "BOOK";
    public static final String ELECTRONICS_TYPE = "ELECTRONICS";

    /**
     * Factory method to create products with business rules
     * @param id - product identifier
     * @param name - product name
     * @param type - product type (must be BOOK or ELECTRONICS)
     * @param price - product price (must meet minimum requirements)
     * @param quantity - initial stock quantity
     * @return new Product instance
     * @throws IllegalArgumentException if validation fails
     */
    public static Product createProduct(String id, String name, String type, double price, int quantity) {

        // Step 1: Validate product type
        if (!isValidProductType(type)) {
            throw new IllegalArgumentException("Invalid product type: " + type);
        }

        // Step 2: Apply business rules based on product type
        validateBusinessRules(type, price);

        // Step 3: Create and return Product instance
        return new Product(id, name, type, price, quantity);
    }

    // Helper method for product type validation
    private static boolean isValidProductType(String type) {
        return BOOK_TYPE.equals(type) || ELECTRONICS_TYPE.equals(type);
    }

    // Helper method for business rule validation
    private static void validateBusinessRules(String type, double price) {
        if (BOOK_TYPE.equals(type) && price < 5.0) {
            throw new IllegalArgumentException("Books must have a minimum price of $5.00");
        }

        if (ELECTRONICS_TYPE.equals(type) && price < 10.0) {
            throw new IllegalArgumentException("Electronics must have a minimum price of $10.00");
        }
    }
}
