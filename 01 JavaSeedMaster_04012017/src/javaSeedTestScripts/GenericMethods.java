package javaSeedTestScripts;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javaSeed.constants.Const;

public class GenericMethods {
	
	private static WebDriver driver = Const.driver;
	public static void highlightElement(WebElement element) throws InterruptedException {
        for (int i = 0; i <2; i++) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: yellow; border: 6px solid red;");
            Thread.sleep(500);
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
            }
        }
}
