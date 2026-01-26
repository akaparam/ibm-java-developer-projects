package inventory;

/**
 * Strategy pattern implementation for calculating different types of discounts.
 */
public class DiscountCalculator {

    /**
     * Inner class to hold discount calculation results.
     */
    public static class DiscountResult {
        private double discountAmount;
        private String description;

        // Constructor
        public DiscountResult(double discountAmount, String description) {
            this.discountAmount = discountAmount;
            this.description = description;
        }

        // Getter methods
        public double getDiscountAmount() {
            return discountAmount;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Strategy method for calculating discounts
     * @param product - the product being purchased
     * @param quantity - quantity being purchased
     * @param discountType - type of discount to apply (STUDENT, BULK, NONE)
     * @return DiscountResult with amount and description
     */
    public static DiscountResult calculateDiscount(Product product, int quantity, String discountType) {

        // Initialize variables
        double discountAmount = 0.0;
        String description = "No discount applied";

        if (discountType == null) {
            return new DiscountResult(discountAmount, description);
        }

        switch (discountType) {
            case "STUDENT":
                // Student discount: 10% off books only
                if (isEligibleForStudentDiscount(product)) {
                    discountAmount = product.getPrice() * quantity * 0.10;
                    description = "Student discount (10% off books)";
                }
                break;

            case "BULK":
                // Bulk discount: 15% off when buying 5+ items
                if (isEligibleForBulkDiscount(quantity)) {
                    discountAmount = product.getPrice() * quantity * 0.15;
                    description = "Bulk discount (15% off for 5+ items)";
                }
                break;

            case "NONE":
            default:
                // No discount applied
                break;
        }

        return new DiscountResult(discountAmount, description);
    }

    // Helper methods
    private static boolean isEligibleForStudentDiscount(Product product) {
        return product != null && "BOOK".equals(product.getType());
    }

    private static boolean isEligibleForBulkDiscount(int quantity) {
        return quantity >= 5;
    }
}
