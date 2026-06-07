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

        // STEP 1: Buka & Login
        periplus.open();
        waitForPreloader();
        
        periplus.login(EMAIL, PASSWORD);
        waitForPreloader();
        System.out.println("LOGIN OK -> " + driver.getCurrentUrl());

        // STEP 2: Cari produk
        periplus.searchProduct("Harry Potter");
        waitForPreloader();
        System.out.println("SEARCH OK -> " + driver.getCurrentUrl());

        // STEP 3: Pilih & masukkan ke keranjang
        WebElement firstProduct = periplus.getFirstProductFromSearch();
        String productName = firstProduct.getText().trim();
        System.out.println("ADDING TO CART: " + productName);

        WebElement addToCartBtn = periplus.getAddToCartButtonFromProduct(firstProduct);
        scrollAndClick(addToCartBtn);
        waitForPreloader();

        // STEP 4: Buka cart & Verifikasi (Assertion)
        periplus.openCart();
        waitForPreloader();

        List<WebElement> cartItems = periplus.getCartItems();
        Assert.assertFalse(cartItems.isEmpty(), "FAIL: Halaman cart kosong!");

        String cartProductName = periplus.getProductNameFromCartItem(cartItems.get(0));
        Assert.assertTrue(
            cartProductName.toLowerCase().contains("harry potter"),
            "FAIL: Produk di cart bukan Harry Potter, tapi: " + cartProductName
        );

        System.out.println("VERIFIED: '" + cartProductName + "' in cart (" + cartItems.size() + " item)");
    }
}