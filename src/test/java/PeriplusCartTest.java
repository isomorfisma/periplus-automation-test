import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;

public class PeriplusCartTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String EMAIL    = "michaeltotooo11@gmail.com";
    private final String PASSWORD = "automationtest123";
    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");
        driver = new ChromeDriver(options);
        wait   = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    private void waitForPreloader() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".preloader, #loading")));
        } catch (Exception ignored) {}
    }

    private void scrollAndClick(WebElement el) {
        ((JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        el.click();
    }

    @Test
    public void testAddToCart() {

        driver.get("https://www.periplus.com/");
        waitForPreloader();

        wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".right-bar a[href*='Your-Account']"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("input[name='email'], #input-email")))
            .sendKeys(EMAIL);

        driver.findElement(
            By.cssSelector("input[name='password'], #input-password"))
            .sendKeys(PASSWORD);

        driver.findElement(
            By.cssSelector("button[type='submit'], #button-login"))
            .click();

        waitForPreloader();
        wait.until(ExpectedConditions.not(
            ExpectedConditions.urlContains("account/login")));

        System.out.println("login ok");

        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("filter_name_desktop")));
        searchBox.clear();
        searchBox.sendKeys("Harry Potter");
        searchBox.sendKeys(Keys.ENTER);
        waitForPreloader();
        wait.until(ExpectedConditions.urlContains("filter_name"));
        System.out.println("search ok");

        // Each product card is wrapped in .single-product; the title link is inside .product-content h3
        WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector(".row-category-grid .single-product .product-content h3 a")));

        String productName = firstProduct.getText().trim();
        System.out.println("adding to cart: " + productName);

        // .addtocart is a direct sibling in the same .single-product container
        WebElement addToCartBtn = firstProduct.findElement(By.xpath(
            "ancestor::div[@class='single-product']//a[contains(@class,'addtocart')]"));

        scrollAndClick(addToCartBtn);
        waitForPreloader();

        driver.get("https://www.periplus.com/checkout/cart");
        waitForPreloader();

        // Cart uses a div-based layout; each item is a .row-cart-product row
        List<WebElement> cartItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
            By.cssSelector(".row-cart-product")));

        Assert.assertFalse(cartItems.isEmpty(), "cart is empty");

        String cartProductName = cartItems.get(0)
            .findElement(By.cssSelector(".product-name a"))
            .getText().trim();

        Assert.assertTrue(
            cartProductName.toLowerCase().contains("harry potter"),
            "unexpected product in cart: " + cartProductName);

        System.out.println("verified: '" + cartProductName + "' in cart (" + cartItems.size() + " item)");
    }
}