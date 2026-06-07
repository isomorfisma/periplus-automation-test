import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class PeriplusApp {
    private WebDriver driver;
    private WebDriverWait wait;

    public PeriplusApp(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void open() {
        driver.get("https://www.periplus.com/");
    }

    public void login(String email, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".right-bar a[href*='Your-Account']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='email'], #input-email"))).sendKeys(email);
        driver.findElement(By.cssSelector("input[name='password'], #input-password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit'], #button-login")).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("account/login")));
    }

    public void searchProduct(String keyword) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filter_name_desktop")));
        searchBox.clear();
        searchBox.sendKeys(keyword);
        searchBox.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.urlContains("filter_name"));
    }

    public WebElement getFirstProductFromSearch() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".row-category-grid .single-product .product-content h3 a")));
    }

    public WebElement getAddToCartButtonFromProduct(WebElement productElement) {
        return productElement.findElement(By.xpath("ancestor::div[@class='single-product']//a[contains(@class,'addtocart')]"));
    }

    public void openCart() {
        driver.get("https://www.periplus.com/checkout/cart");
    }

    public List<WebElement> getCartItems() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".row-cart-product")));
    }

    public String getProductNameFromCartItem(WebElement cartItem) {
        return cartItem.findElement(By.cssSelector(".product-name a")).getText().trim();
    }
}