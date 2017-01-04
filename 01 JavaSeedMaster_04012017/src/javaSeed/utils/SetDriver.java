package javaSeed.utils;

import java.io.File;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import javaSeed.constants.Const;

public class SetDriver{
	
	 public static WebDriver SetWebDriver(WebDriver driver, String strBrowserType){		// Constructor of Browser Handling with one Passing parameter for Browser Type
		 
		 
		 
		if (strBrowserType.toUpperCase().equals("IE")) {
			
			// Chunk of code for IE browser
			File file = new File(Const.IE_DRIVERX64_PATH);
			System.setProperty("webdriver.ie.driver", file.getPath());
			driver = new InternetExplorerDriver();
		} 
			// Chunk of code for Chrome browser
			else if (strBrowserType.toUpperCase().equals("CHROME")) {
				
				System.setProperty("webdriver.chrome.driver", Const.CHROME_DRIVER_PATH);
				ChromeOptions options = new ChromeOptions();
				options.addArguments(Arrays.asList("start-maximized"));
				driver = new ChromeDriver(options);
		} 
		 	// Chunk of code for FireFox browser
			else if (strBrowserType.toUpperCase().equals("FIREFOX")) {
			System.setProperty("webdriver.firefox.marionette", Const.FIREFOX_DRIVER_PATH);
		    DesiredCapabilities capabilities=DesiredCapabilities.firefox();
		    capabilities.setCapability("marionette", true);
		    driver = new FirefoxDriver(capabilities);
		} 
		 
			// When nothing was defined
			else {
			System.out.println("Driver can not be Defined! Browser Type was not properly mentioned.");
			System.exit(0);
		}
		return driver;
	 }

}