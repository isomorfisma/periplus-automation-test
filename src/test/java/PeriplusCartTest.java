import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class PeriplusCartTest extends BaseTest {

    private final String EMAIL    = "michaeltotooo11@gmail.com";
    private final String PASSWORD = "automationtest123";

    @Test
    public void testAddToCart() {
        PeriplusApp periplus = new PeriplusApp(driver, wait);

        periplus.open();
        waitForPreloader();

        periplus.login(EMAIL, PASSWORD);
        waitForPreloader();
        System.out.println("login ok");

        periplus.searchProduct("Harry Potter");
        waitForPreloader();
        System.out.println("search ok");

        WebElement firstProduct = periplus.getFirstProductFromSearch();
        String productName = firstProduct.getText().trim();
        System.out.println("adding to cart: " + productName);

        WebElement addToCartBtn = periplus.getAddToCartButtonFromProduct(firstProduct);
        scrollAndClick(addToCartBtn);
        waitForPreloader();

        periplus.openCart();
        waitForPreloader();

        List<WebElement> cartItems = periplus.getCartItems();
        Assert.assertFalse(cartItems.isEmpty(), "cart is empty");

        String cartProductName = periplus.getProductNameFromCartItem(cartItems.get(0));
        Assert.assertTrue(
            cartProductName.toLowerCase().contains("harry potter"),
            "unexpected product in cart: " + cartProductName
        );

        System.out.println("verified: '" + cartProductName + "' in cart (" + cartItems.size() + " item)");
    }
}