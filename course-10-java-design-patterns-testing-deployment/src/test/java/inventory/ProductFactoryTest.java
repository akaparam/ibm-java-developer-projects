package inventory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProductFactory class.
 */
class ProductFactoryTest {

    /**
     * Test creating a valid book product
     */
    @Test
    void testCreateValidBook() {
        Product product = ProductFactory.createProduct(
                "B100", "Clean Code", ProductFactory.BOOK_TYPE, 25.99, 10);

        assertNotNull(product);
        assertEquals("B100", product.getId());
        assertEquals("Clean Code", product.getName());
        assertEquals(ProductFactory.BOOK_TYPE, product.getType());
        assertEquals(25.99, product.getPrice());
        assertEquals(10, product.getQuantity());
    }

    /**
     * Test creating a valid electronics product
     */
    @Test
    void testCreateValidElectronics() {
        Product product = ProductFactory.createProduct(
                "E100", "Keyboard", ProductFactory.ELECTRONICS_TYPE, 49.99, 5);

        assertNotNull(product);
        assertEquals("E100", product.getId());
        assertEquals("Keyboard", product.getName());
        assertEquals(ProductFactory.ELECTRONICS_TYPE, product.getType());
        assertEquals(49.99, product.getPrice());
        assertEquals(5, product.getQuantity());
    }

    /**
     * Test book minimum price validation
     */
    @Test
    void testBookMinimumPriceValidation() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ProductFactory.createProduct(
                        "B200", "Cheap Book", ProductFactory.BOOK_TYPE, 4.99, 3)
        );

        assertTrue(ex.getMessage().contains("minimum price"));
    }

    /**
     * Test electronics minimum price validation
     */
    @Test
    void testElectronicsMinimumPriceValidation() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ProductFactory.createProduct(
                        "E200", "Cheap Gadget", ProductFactory.ELECTRONICS_TYPE, 9.99, 2)
        );

        assertTrue(ex.getMessage().contains("minimum price"));
    }

    /**
     * Test invalid product type
     */
    @Test
    void testInvalidProductType() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ProductFactory.createProduct(
                        "X001", "Unknown", "FOOD", 12.00, 1)
        );
    }

    /**
     * Test boundary conditions
     */
    @Test
    void testBoundaryConditions() {
        Product book = ProductFactory.createProduct(
                "B300", "Boundary Book", ProductFactory.BOOK_TYPE, 5.00, 1);

        Product electronics = ProductFactory.createProduct(
                "E300", "Boundary Device", ProductFactory.ELECTRONICS_TYPE, 10.00, 1);

        assertNotNull(book);
        assertEquals(5.00, book.getPrice());

        assertNotNull(electronics);
        assertEquals(10.00, electronics.getPrice());
    }
}
