import Model.RegisterResult;
import Model.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Robin on 2015-09-30.
 * <p>
 * Tests a logged on users cart.
 */

public class CartManagerTest {
    private static final int COUNT = 1;
    private static final String USERNAME = "000000000";
    private static final String PASS = "PASSWORD_TEST_PASSWORD";
    private static final String ZIP = "137 32 SE";
    private static final String STREET = "Testvägen 23";
    private static final int PRODUCT_ID = 1;
    private static Account account;
    private static Product product;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        product = new Product();
        product.setId(PRODUCT_ID);
        RegisterResult result = AccountManager.register(USERNAME, PASS, ZIP, STREET);
        account = result.getAccount();
    }

    @AfterClass
    public static void disposeAccount() {
        AccountManager.deRegister(account);
    }

    @After
    public void removeCart() {
        CartManager.clearCart(account);
    }

    @Test
    public void shouldAddItemsToCart() throws Exception {
        CartManager.addToCart(product, COUNT, account);
        int productCount = CartManager.getProductCount(account);

        if (productCount == 0) {
            throw new Exception("Product not added to cart.");
        }
    }

    @Test
    public void shouldAddCountItemsToCart() throws Exception {
        CartManager.addToCart(product, COUNT, account);
        CartManager.addToCart(product, COUNT, account);
        int productCount = CartManager.getProductCount(account);

        if (productCount != COUNT * 2)
            throw new Exception("Multiple adds does not accumulate.");
    }

    @Test
    public void shouldRemoveCountItemsFromCart() throws Exception {
        CartManager.addToCart(product, 2, account);
        CartManager.addToCart(product, -1, account);
        int productCount = CartManager.getProductCount(account);

        if (productCount != 1) {
            throw new Exception("Items not removed from cart.");
        }
    }

    @Test
    public void shouldRemoveCartItemWhenAllRemoved() throws Exception {
        CartManager.addToCart(product, 1, account);
        CartManager.addToCart(product, -2, account);
        int productCount = CartManager.getProductCount(account);

        if (productCount != 0) {
            throw new Exception("Negative count does not remove product from cart. " + productCount);
        }
    }

    @Test
    public void shouldClearCart() throws Exception {
        CartManager.clearCart(account);
        int productCount = CartManager.getProductCount(account);

        if (productCount != 0) {
            throw new Exception("Cart not cleared.");
        }
    }

    @Test
    public void shouldReturnCart() throws Exception {
        CartManager.addToCart(product, COUNT, account);
        Cart cart = CartManager.getCart(account);

        if (cart.getItems().size() == 0) {
            throw new Exception("The cart is empty.");
        }
    }
}
