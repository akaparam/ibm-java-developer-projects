package inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for InventoryManager class.
 */
class InventoryManagerTest {

    // Test instance
    private InventoryManager manager;

    /**
     * Set up fresh instance before each test
     */
    @BeforeEach
    void setUp() {
        manager = new InventoryManager();
    }

    /**
     * Test adding products to inventory
     */
    @Test
    void testAddProduct() {
        manager.addProduct("B001", "Java Book", "BOOK", 20.00, 5);

        double value = manager.getInventoryValue();
        assertEquals(100.00, value);
    }

    /**
     * Test adding product with invalid parameters
     */
    @Test
    void testAddInvalidProduct() {
        manager.addProduct("B001", "Cheap Book", "BOOK", 2.00, 5);

        double value = manager.getInventoryValue();
        assertEquals(0.0, value);
    }

    /**
     * Test selling products
     */
    @Test
    void testSellProduct() {
        manager.addProduct("B001", "Java Book", "BOOK", 20.00, 10);

        manager.sellProduct("B001", 2, "NONE");
        assertEquals(160.00, manager.getInventoryValue());

        manager.sellProduct("B001", 2, "STUDENT");
        // 6 remaining * 20 = 120
        assertEquals(120.00, manager.getInventoryValue());
    }

    /**
     * Test selling more than available stock
     */
    @Test
    void testSellInsufficientStock() {
        manager.addProduct("E001", "Mouse", "ELECTRONICS", 25.00, 3);

        manager.sellProduct("E001", 5, "NONE");

        assertEquals(75.00, manager.getInventoryValue());
    }

    /**
     * Test adding stock to existing product
     */
    @Test
    void testAddStock() {
        manager.addProduct("E001", "Keyboard", "ELECTRONICS", 50.00, 2);

        manager.addStock("E001", 3);

        assertEquals(250.00, manager.getInventoryValue());
    }

    /**
     * Test inventory value calculation
     */
    @Test
    void testInventoryValue() {
        manager.addProduct("B001", "Book1", "BOOK", 10.00, 2);   // 20
        manager.addProduct("E001", "Gadget", "ELECTRONICS", 50.00, 3); // 150

        assertEquals(170.00, manager.getInventoryValue());
    }

    /**
     * Test low stock detection
     */
    @Test
    void testLowStockProducts() {
        manager.addProduct("B001", "Book1", "BOOK", 10.00, 2);
        manager.addProduct("B002", "Book2", "BOOK", 12.00, 10);
        manager.addProduct("E001", "Mouse", "ELECTRONICS", 25.00, 4);

        List<Product> lowStock = manager.getLowStockProducts(5);

        assertEquals(2, lowStock.size());
        assertTrue(lowStock.stream().anyMatch(p -> p.getId().equals("B001")));
        assertTrue(lowStock.stream().anyMatch(p -> p.getId().equals("E001")));
    }

    /**
     * Test operations on non-existent products
     */
    @Test
    void testNonExistentProduct() {
        manager.sellProduct("X001", 1, "NONE");
        manager.addStock("X001", 5);

        assertEquals(0.0, manager.getInventoryValue());
    }

    /**
     * Test complete workflow
     */
    @Test
    void testCompleteWorkflow() {
        manager.addProduct("B001", "Java Book", "BOOK", 20.00, 5);
        manager.addProduct("E001", "Mouse", "ELECTRONICS", 25.00, 4);

        assertEquals(200.00, manager.getInventoryValue());

        manager.sellProduct("B001", 2, "STUDENT"); // 3 left
        assertEquals(160.00, manager.getInventoryValue());

        manager.addStock("E001", 6); // now 10
        assertEquals(310.00, manager.getInventoryValue());

        List<Product> lowStock = manager.getLowStockProducts(5);
        assertEquals(1, lowStock.size());
        assertEquals("B001", lowStock.get(0).getId());
    }
}
