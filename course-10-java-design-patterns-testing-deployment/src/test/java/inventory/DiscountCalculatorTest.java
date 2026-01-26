package inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DiscountCalculator class.
 */
class DiscountCalculatorTest {

    // Test products
    private Product book;
    private Product electronics;

    /**
     * Set up test data before each test
     */
    @BeforeEach
    void setUp() {
        book = ProductFactory.createProduct("B001", "Test Book", "BOOK", 20.0, 10);
        electronics = ProductFactory.createProduct("E001", "Test Electronics", "ELECTRONICS", 100.0, 10);
    }

    /**
     * Test student discount on books
     */
    @Test
    void testStudentDiscountOnBooks() {
        DiscountCalculator.DiscountResult result =
                DiscountCalculator.calculateDiscount(book, 2, "STUDENT");

        assertEquals(4.0, result.getDiscountAmount(), 0.001);
        assertTrue(result.getDescription().contains("Student"));
    }

    /**
     * Test student discount on electronics (should not apply)
     */
    @Test
    void testStudentDiscountOnElectronics() {
        DiscountCalculator.DiscountResult result =
                DiscountCalculator.calculateDiscount(electronics, 2, "STUDENT");

        assertEquals(0.0, result.getDiscountAmount(), 0.001);
        assertEquals("No discount applied", result.getDescription());
    }

    /**
     * Test bulk discount when quantity >= 5
     */
    @Test
    void testBulkDiscountValid() {
        DiscountCalculator.DiscountResult bookResult =
                DiscountCalculator.calculateDiscount(book, 5, "BULK");

        assertEquals(15.0, bookResult.getDiscountAmount(), 0.001); // 20 * 5 * 0.15

        DiscountCalculator.DiscountResult electronicsResult =
                DiscountCalculator.calculateDiscount(electronics, 6, "BULK");

        assertEquals(90.0, electronicsResult.getDiscountAmount(), 0.001); // 100 * 6 * 0.15
    }

    /**
     * Test bulk discount when quantity < 5
     */
    @Test
    void testBulkDiscountInvalid() {
        DiscountCalculator.DiscountResult result =
                DiscountCalculator.calculateDiscount(book, 3, "BULK");

        assertEquals(0.0, result.getDiscountAmount(), 0.001);
        assertEquals("No discount applied", result.getDescription());
    }

    /**
     * Test no discount option
     */
    @Test
    void testNoDiscount() {
        DiscountCalculator.DiscountResult result =
                DiscountCalculator.calculateDiscount(book, 5, "NONE");

        assertEquals(0.0, result.getDiscountAmount(), 0.001);
        assertEquals("No discount applied", result.getDescription());
    }

    /**
     * Test boundary conditions
     */
    @Test
    void testBoundaryConditions() {
        // Exactly 5 items for bulk discount
        DiscountCalculator.DiscountResult bulkFive =
                DiscountCalculator.calculateDiscount(book, 5, "BULK");
        assertEquals(15.0, bulkFive.getDiscountAmount(), 0.001);

        // Quantity = 1
        DiscountCalculator.DiscountResult single =
                DiscountCalculator.calculateDiscount(book, 1, "BULK");
        assertEquals(0.0, single.getDiscountAmount(), 0.001);

        // Large quantities
        DiscountCalculator.DiscountResult large =
                DiscountCalculator.calculateDiscount(electronics, 100, "BULK");
        assertEquals(1500.0, large.getDiscountAmount(), 0.001);
    }

    /**
     * Test discount calculation accuracy
     */
    @Test
    void testDiscountCalculationAccuracy() {
        // $20 book, qty 2, student discount = $4.00
        DiscountCalculator.DiscountResult student =
                DiscountCalculator.calculateDiscount(book, 2, "STUDENT");
        assertEquals(4.0, student.getDiscountAmount(), 0.001);

        // $100 electronics, qty 5, bulk discount = $75.00
        DiscountCalculator.DiscountResult bulk =
                DiscountCalculator.calculateDiscount(electronics, 5, "BULK");
        assertEquals(75.0, bulk.getDiscountAmount(), 0.001);
    }
}