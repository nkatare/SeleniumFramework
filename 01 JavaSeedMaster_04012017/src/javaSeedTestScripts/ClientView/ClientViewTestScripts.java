package javaSeedTestScripts.ClientView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import javaSeed.constants.Const;
import javaSeed.objectRepository.OR;
import javaSeed.utils.Screenshot;
import javaSeedTestScripts.TestConsts;
import javaSeedTestScripts.IB.IBTestScripts;

public class ClientViewTestScripts {

	private static WebDriver IEdriver = null;
	
	private static WebDriver getIEdriver() {
		return IEdriver;
	}

	public static void setIEdriver(WebDriver iEdriver) {
		IEdriver = iEdriver;
	}

	private HashMap<String, String[]> ORMap = Const.ORMap;
	private ExtentTest TestLogger = Const.etTestCases;
	private String ValidFrame;
	private String NBPWindow;
	
/* Method: NavigationLoginToBPM will 
 * 1. 'Navigate to the BPM URL'
 * 2. Login to BPM with username and password
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String 'Username' and String 'Password'
 * 
 * Created by: Nikhil Katare
 * On: 02/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
		public String NavigateSearchValidateCustProfileAddNBP(String CustCIF, String CustName) throws IOException{
			try{
				
				// Get Customer Address which was updated for COA through Internet Banking
				String CustAdd = IBTestScripts.getCoASetAddress();
				//String CustAdd = "Auto Lane 1,Auto Lane 2,Auto Lane 3";
				
				File file = new File(Const.IE_DRIVERW32_PATH);
				System.setProperty("webdriver.ie.driver", file.getPath());
				DesiredCapabilities cap = new DesiredCapabilities();
			    cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			    DesiredCapabilities.internetExplorer().setCapability("ignoreProtectedModeSettings", true);
			    cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			    cap.setJavascriptEnabled(true);
			    cap.setCapability("requireWindowFocus", true);
			    cap.setCapability("enablePersistentHover", true);
				IEdriver = new InternetExplorerDriver();
				
				// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
				WebDriverWait wait = new WebDriverWait(IEdriver, 10);
				
				// Navigate to the BPM Url			
				IEdriver.get(TestConsts.CLIENTVIEW_URL);
				IEdriver.manage().window().maximize();
				
				// Store the current IE window handle
				String mainHandle = IEdriver.getWindowHandle();
				
				// Condition if ROI selection page appears select ROI and proceed
				if(OR.FindElements(IEdriver, ORMap.get("NBP_Portal_ROILink")).size()>0){
					OR.FindElement(IEdriver, ORMap.get("NBP_Portal_ROILink")).click();
				}
				
				// Wait until Element becomes visible
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(IEdriver, ORMap.get("NBP_Portal_CurrentEdgeLink"))));
				
				// Select the appropriate link to navigate to NBP 
				OR.FindElement(IEdriver, ORMap.get("NBP_Portal_CurrentEdgeLink")).click();
				
				Thread.sleep(3000);	
				
				// Collect all Windows Handles opened
				Set<String> allHandles = IEdriver.getWindowHandles();
				
				// Loop through all window handles to switch to the new one (which is NBP)
				for(String currentHandle : allHandles) {
				  if (!currentHandle.equals(mainHandle)) {
					IEdriver.switchTo().window(currentHandle);
					NBPWindow = currentHandle;
				    break;
				  }
				}
				
				// Logger
				TestLogger.log(LogStatus.INFO,"Navigation to NBP has been successful");
				
				// Switch to the Frame to select appropriate Tabs
				IEdriver.switchTo().frame(ORMap.get("NBP_ClientV_NBPTopFrame")[1]);
				
				// Select Client View tab
				OR.FindElement(IEdriver, ORMap.get("NBP_ClientV_ClientViewTab")).click();
				
				// Wait until Element becomes visible
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(IEdriver, ORMap.get("NBP_ClientV_FindButton"))));
				
				// Click on the Find option
				OR.FindElement(IEdriver, ORMap.get("NBP_ClientV_FindButton")).click();
				Thread.sleep(1000);
				
				// Find all the Frames on the page
				IEdriver.switchTo().defaultContent();
				List<WebElement> NoOfFrames = IEdriver.findElements(By.xpath("//frame"));
				
				// Switch to the Frame which contains the Element appear on the page
				for (int i=1;i<=NoOfFrames.size();i++){
					IEdriver.switchTo().defaultContent();
					IEdriver.switchTo().frame("frame"+i);
					if(OR.FindElements(IEdriver, ORMap.get("NBP_ClientV_FindCustomerMenu")).size()>0){
						ValidFrame = "frame"+i;
						break;
					}
				}
				
				// Select Find Customer drop-down option
				OR.FindElement(IEdriver, ORMap.get("NBP_ClientV_FindCustomerMenu")).click();
				
				// Wait until Element becomes visible
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(IEdriver, ORMap.get("NBP_ClientV_inputName"))));
				// Write the CIF ID of the customer to find the details
				OR.FindElement(IEdriver, ORMap.get("NBP_ClientV_inputName")).sendKeys(CustCIF);
				// Click on the Search Submit button
				OR.FindElement(IEdriver, ORMap.get("NBP_ClientV_SubmitButton")).click();
				
				Thread.sleep(2000);
				
				// Switch to the Child iFrame contains the search results
				IEdriver.switchTo().frame(ORMap.get("NBP_ClientV_SearchResultFrame")[1]);
				
				if(IEdriver.findElements(By.xpath(ORMap.get("NBP_ClientV_CustNameRow")[1]+CustName+"')]")).size()==1){
									
					// Logger
					TestLogger.log(LogStatus.INFO,"Customer Details for "+CustName+" were successfully retrieved having CIF ID - "+CustCIF);
					
					// Get the Address of the user from the Search result
					String CustomerDetailString = IEdriver.findElement(By.xpath(ORMap.get("NBP_ClientV_CustNameRow")[1]+CustName+"')]")).getText();
					
					String[] arrCustAdd = CustAdd.split(",");		
				
					if(CustomerDetailString.toUpperCase().contains(arrCustAdd[0].toUpperCase()) 
							&& CustomerDetailString.toUpperCase().contains(arrCustAdd[1].toUpperCase())
								&& CustomerDetailString.toUpperCase().contains(arrCustAdd[2].toUpperCase())){
						// Logger
						TestLogger.log(LogStatus.PASS,"Customer Address has been updated in NBP Customer Profile. "
								+ "Screenshot - "+Screenshot.ObjectSnapFullPage(IEdriver));
					} else {
						// Logger
						TestLogger.log(LogStatus.FAIL,"Customer Address has NOT updated in NBP Customer Profile. "
								+ "Screenshot - "+Screenshot.ObjectSnapFullPage(IEdriver));
					}
				} else {
					// Logger
					TestLogger.log(LogStatus.FATAL,"Customer Details could not be retrieved for customer having CIF ID - "+CustCIF+ ". Screenshot - "+Screenshot.ObjectSnapFullPage(IEdriver));
					return "Fatal";
				}
				
				// Close NBP Portal window
				IEdriver.switchTo().window(mainHandle);
				IEdriver.close();
				// Switch back to the NBP Application window
				IEdriver.switchTo().window(NBPWindow);
				ClientViewTestScripts.setIEdriver(IEdriver);
				
				
			} // Catch any other unexpected exception, and return Fatal if true
			catch(Throwable t){
				if(!t.equals(null)){
					t.printStackTrace();
					TestLogger.log(LogStatus.FATAL, "Error while Navigation NBP - : Error Description -- "+t.toString()
						+ ". Screenshot - "+Screenshot.ObjectSnapFullPage(IEdriver));
					return "Fatal";
				}
			} // If all good, return 'Pass'
			return "Pass";
		}

/* Method: CloseNBP will 
 * 1. 'Close the NBP Application'
 *  
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: None
 * 
 * Created by: Nikhil Katare
 * On: 21/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/		
		
		public String CloseNBP() throws IOException{
			
			try{
				IEdriver=ClientViewTestScripts.getIEdriver();
				IEdriver.quit();
			}
			catch(Throwable t){
				if(!t.equals(null)){
					t.printStackTrace();
					TestLogger.log(LogStatus.WARNING, "Error while Closing NBP - : Error Description -- "+t.toString()
						, "Screenshot - "+Screenshot.ObjectSnapFullPage(IEdriver));
					return "Warning";
				}
			} // If all good, return 'Pass'
			return "Pass";
		}
		
		//%%%%%%%%%%%%%% Method not Complete %%%%%%%%%%%%%% 
		public String NavigateValidateAccountAddress(String CustCIF) throws IOException{
			try{
				
				// Setting Customer Address which was updated for COA through Internet Banking
				//String CustAdd = IBTestScripts.getCoASetAddress();
				
				File file = new File(Const.IE_DRIVERX64_PATH);
				System.setProperty("webdriver.ie.driver", file.getPath());
				DesiredCapabilities cap = new DesiredCapabilities();
			    cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			    //DesiredCapabilities.internetExplorer().setCapability("ignoreProtectedModeSettings", true);
			    //cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			    //cap.setJavascriptEnabled(true);
			    cap.setCapability("requireWindowFocus", true);
			    cap.setCapability("enablePersistentHover", true);
				IEdriver = new InternetExplorerDriver(cap);
				
				// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
				WebDriverWait wait = new WebDriverWait(IEdriver, 10);
				
				// Navigate to the BPM Url			
				IEdriver.get(TestConsts.CLIENTVIEW_URL);
				IEdriver.manage().window().maximize();
				
				// Store the current IE window handle
				String mainHandle = IEdriver.getWindowHandle();
				
				
				// Condition if ROI selection page appears select ROI and proceed
				if(IEdriver.findElements(By.xpath("//div[@id='region']/a[contains(.,'ROI')]")).size()>0){
					IEdriver.findElement(By.xpath("//div[@id='region']/a[contains(.,'ROI')]")).click();
				}
				
				// Wait until Element becomes visible
				wait.until(ExpectedConditions.visibilityOf(IEdriver.findElement(By.xpath("//tbody/descendant::a[@id='edgeLink'][contains(@url,'systest')][contains(.,'Current Edge Edge')]"))));
				
				// Select the appropriate link to navigate to NBP 
				IEdriver.findElement(By.xpath("//tbody/descendant::a[@id='edgeLink'][contains(@url,'systest')][contains(.,'Current Edge Edge')]")).click();
				
				Thread.sleep(3000);	
				
				// Collect all Windows Handles opened
				Set<String> allHandles = IEdriver.getWindowHandles();
				
				// Loop through all window handles to switch to the new one (which is NBP)
				for(String currentHandle : allHandles) {
				  if (!currentHandle.equals(mainHandle)) {
					IEdriver.switchTo().window(currentHandle);
					NBPWindow = currentHandle;
				    break;
				  }
				}
				
				// Logger
				TestLogger.log(LogStatus.INFO,"Navigation to NBP has been successful"
						, "Screenshot - "+Screenshot.ObjectSnapFullPage(IEdriver));
				
				// Switch to the Frame to select appropriate Tabs
				IEdriver.switchTo().frame("nbptop");
				
				// Select Client View tab
				IEdriver.findElement(By.xpath("//table[@id='nbpTabs']/descendant::div[@label='Clientview'][@id='tab5']")).click();
				
				// Wait until Element becomes visible
				wait.until(ExpectedConditions.visibilityOf(IEdriver.findElement(By.id("mnuCltviewLocateSCMenuTd"))));
				
				// Click on the Find option
				IEdriver.findElement(By.id("mnuCltviewLocateSCMenuTd")).click();
				Thread.sleep(1000);
				
				// Find all the Frames on the page
				IEdriver.switchTo().defaultContent();
				List<WebElement> NoOfFrames = IEdriver.findElements(By.xpath("//frame"));
				
				// Switch to the Frame which contains the Element appear on the page
				for (int i=1;i<=NoOfFrames.size();i++){
					IEdriver.switchTo().defaultContent();
					IEdriver.switchTo().frame("frame"+i);
					if(IEdriver.findElements(By.xpath("//div[@id='mnuCltviewLocateSCMenu']/descendant::p[contains(.,'Find Customer')]")).size()>0){
						ValidFrame = "frame"+i;
						System.out.println(ValidFrame);
						break;
					}
				}
				
				// Select Find Customer drop-down option
				IEdriver.findElement(By.xpath("//div[@id='mnuCltviewLocateSCMenu']/descendant::p[contains(.,'Find Customer')]")).click();
				
				// Wait until Element becomes visible
				wait.until(ExpectedConditions.visibilityOf(IEdriver.findElement(By.id("nameInput"))));
				// Write the CIF ID of the customer to find the details
				IEdriver.findElement(By.id("nameInput")).sendKeys(CustCIF);
				// Click on the Search Submit button
				IEdriver.findElement(By.name("Submit")).click();
				
				Thread.sleep(2000);
				
				// Switch to the Child iFrame contains the search results
				IEdriver.switchTo().frame("locateResults");
				if(IEdriver.findElements(By.xpath("//div[@id='listBody']/descendant::td[contains(.,'MR COA SEVENTYFIVE')]")).size()==1){
					
					// Wait until Element becomes visible
					wait.until(ExpectedConditions.visibilityOf(IEdriver.findElement(By.xpath("//div[@id='listBody']/descendant::td[contains(.,'MR COA SEVENTYFIVE')]"))));
					
					// Logger
					TestLogger.log(LogStatus.INFO,"Customer Details were successfully retrieved for customer having CIF ID - "+CustCIF);
					
				} else {
					// Logger
					TestLogger.log(LogStatus.FATAL,"Customer Details could not be retrieved for customer having CIF ID - "+CustCIF, "Screenshot - "+Screenshot.ObjectSnapFullPage(IEdriver));
					return "Fatal";
				}

				IEdriver.switchTo().window(mainHandle);
				IEdriver.close();
				IEdriver.switchTo().window(NBPWindow);
				
				// =======================================================
				
				IEdriver.switchTo().defaultContent();
				IEdriver.switchTo().frame("nbptop");
				
				wait.until(ExpectedConditions.visibilityOf(IEdriver.findElement(By.id("mnuCltviewFinancialsSCMenuTd"))));
				//wait.until(ExpectedConditions.visibilityOf(IEdriver.findElement(By.id("mnuCltviewComplaintsSCMenuTd"))));
				
				IEdriver.findElement(By.id("mnuCltviewFinancialsSCMenuTd")).click();
				//IEdriver.findElement(By.id("mnuCltviewComplaintsSCMenuTd")).click();
				Thread.sleep(1000);
				
				// Find all the Frames on the page
				IEdriver.switchTo().defaultContent();
				IEdriver.switchTo().frame(ValidFrame);
				IEdriver.findElement(By.xpath("//div[@id='mnuCltviewFinancialsSCMenu']/descendant::p[contains(.,'Account List')]")).click();
				//IEdriver.findElement(By.xpath("//div[@id='mnuCltviewComplaintsSCMenu']/descendant::p[contains(.,'List Complaints')]")).click();
				Thread.sleep(2000);
				IEdriver.switchTo().defaultContent();
				System.out.println(IEdriver.getPageSource());
				
				// Switch to the Frame which contains the Element appear on the page
				for (WebElement NoOfFrame : IEdriver.findElements(By.xpath("//frame"))){
					IEdriver.switchTo().defaultContent();
					IEdriver.switchTo().frame(NoOfFrame);
					//if(IEdriver.findElements(By.xpath("//td[@type='button']")).size()>0){
						System.out.println(IEdriver.getPageSource());
						//break;
					//}
				}
				
				List<WebElement> Buttons = IEdriver.findElements(By.xpath("//td[@type='button']"));
				
				for(WebElement Button:Buttons){
					System.out.println(Button.getAttribute("value"));
				}

/*				
				for (WebElement AccountListFrame:AccountListFrames){
					IEdriver.switchTo().defaultContent();
					IEdriver.switchTo().frame(AccountListFrame.getAttribute("name"));
					if(IEdriver.findElements(By.xpath("//td[contains(.,'931012-36257089')]")).size()>0){
						System.out.println(AccountListFrame.getAttribute("name"));
						break;
					}
				}*/
				
				Thread.sleep(1000);
				IEdriver.quit();
				
			} // Catch any other unexpected exception, and return Fatal if true
			catch(Throwable t){
				if(!t.equals(null)){
					t.printStackTrace();
					TestLogger.log(LogStatus.FATAL, "Error while Navigation NBP - : Error Description -- "+t.toString()
						, "Screenshot - "+Screenshot.ObjectSnapFullPage(IEdriver));
					return "Fatal";
				}
			} // If all good, return 'Pass'
			return "Pass";
		}
	
}
