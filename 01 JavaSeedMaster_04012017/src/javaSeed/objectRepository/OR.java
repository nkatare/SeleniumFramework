package javaSeed.objectRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class OR {
	
	// Local Web Element type Object Declaration 
	private static WebElement element=null;
	private static List<WebElement> elementList=null;
	
	// "Find Element" Method to get Web Driver and ObjectArray as parameter
	public static WebElement FindElement(WebDriver driver, String[] ORobjArray){
		
		try {
			// Condition for the Web Object Attribute type and putting the Web Element object to the local object 
			if (ORobjArray[0].toUpperCase().contentEquals("ID")){
				element = driver.findElement(By.id(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("NAME")){
				element = driver.findElement(By.name(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("CLASSNAME")){
				element = driver.findElement(By.className(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("CSSSELECTOR")){
				element = driver.findElement(By.cssSelector(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("LINKTEXT")){
				element = driver.findElement(By.linkText(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("PARTIALLINKTEXT")){
				element = driver.findElement(By.partialLinkText(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("TAGNAME")){
				element = driver.findElement(By.tagName(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("XPATH")){
				element = driver.findElement(By.xpath(ORobjArray[1]));
			}
			else {
				System.out.println("Web objects were not defined, there might be some corupt data in Object Repository Excel");
			}

		}
		catch(Exception e) {
			e.printStackTrace();			
		}
		
		// Returning the WebElement to the Method
		return element;
	}

	// Method to get Web Driver and ObjectArray as parameter
	public static List<WebElement> FindElements(WebDriver driver, String[] ORobjArray){
		
		try {
			// Condition for the Web Object Attribute type and putting the Web Element object to the local object 
			if (ORobjArray[0].toUpperCase().contentEquals("ID")){
				elementList = driver.findElements(By.id(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("NAME")){
				elementList = driver.findElements(By.name(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("CLASSNAME")){
				elementList = driver.findElements(By.className(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("CSSSELECTOR")){
				elementList = driver.findElements(By.cssSelector(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("LINKTEXT")){
				elementList = driver.findElements(By.linkText(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("PARTIALLINKTEXT")){
				elementList = driver.findElements(By.partialLinkText(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("TAGNAME")){
				elementList = driver.findElements(By.tagName(ORobjArray[1]));
			}
			else if (ORobjArray[0].toUpperCase().contentEquals("XPATH")){
				elementList = driver.findElements(By.xpath(ORobjArray[1]));
			}
			else {
				System.out.println("Web objects were not defined, there might be some corupt data in Object Repository Excel");
			}

		}
		catch(Exception e) {
			e.printStackTrace();			
		}
		
		// Returning the WebElement to the Method
		return elementList;
	}
	
}