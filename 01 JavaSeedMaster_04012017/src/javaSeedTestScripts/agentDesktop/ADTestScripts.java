package javaSeedTestScripts.agentDesktop;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import javaSeed.constants.Const;
import javaSeed.objectRepository.OR;
import javaSeed.utils.Screenshot;
import javaSeedTestScripts.TestConsts;
import javaSeedTestScripts.IB.IBTestScripts;

public class ADTestScripts {
	
	private WebDriver driver = Const.driver;
	private HashMap<String, String[]> ORMap = Const.ORMap;
	private ExtentTest TestLogger = Const.etTestCases;
	
/* Method: LoginAgentDesktop will 
 * 1. Login to Agent Desktop application
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String UserName
 * 
 * Created by: Nikhil Katare
 * On: 23/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/
	public String LoginAgentDesktop(String AgentDesktopUser) throws IOException{
		try{			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);			
			// Get the Agent Desktop URL
			driver.get(TestConsts.AD_URL);			
	        // Maximize the Browser
			driver.manage().window().maximize();			
			// Wait until Expected Element is visible on the page
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_Login_UserName"))));			
			// Logger 
			TestLogger.log(LogStatus.INFO,"AD - Navigated to Agent Desktop Login page successfully having URL - "+TestConsts.AD_URL);			
			// Enter User Name
			OR.FindElement(driver, ORMap.get("AD_Login_UserName")).sendKeys(AgentDesktopUser);			
			// Click Submit
			OR.FindElement(driver, ORMap.get("AD_Login_Submit")).click();
			// Wait until Expected Element is visible on the page
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_Landing_ListSearchType"))));
			// Logger 
			TestLogger.log(LogStatus.INFO,"AD - Logged in to Agent Desktop successfully with User ID - "+AgentDesktopUser);
			
			
		} catch(Exception e){
			e.printStackTrace();
			// Logger 
			TestLogger.log(LogStatus.FATAL,"AD - Navigate and Login to Agent Desktop Failed with Errors.: Error Desceiption - "+e.toString()+" Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
		return "Pass";
	}

/* Method: ValidateProfileAddress will 
 * 1. Validate the Profile address of the Customer on Agent Desktop application
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String SearchText, String CustomerName
 * 
 * Created by: Nikhil Katare
 * On: 23/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
	public String SearchCustomerValidateProfileAddress(String SearchText, String CustomerName) throws IOException{
		try{			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);	
			// Wait until Expected Element is visible on the page
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_Landing_ListSearchType"))));
			
			// Select Search type option from the Select List
			Select ListSearchType = new Select(OR.FindElement(driver, ORMap.get("AD_Landing_ListSearchType")));
			ListSearchType.selectByValue("Reg");
			
			// Type in Search Criteria data
			OR.FindElement(driver, ORMap.get("AD_Landing_RegNumberEdit")).sendKeys(SearchText);
			// Click on the Search button.
			OR.FindElement(driver, ORMap.get("AD_Landing_SearchButton")).click();
			
			// Wait until Expected Element is visible on the page
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
			// Confirm if Logged in to Agent Desktop show correct Customer Name
			if(OR.FindElement(driver, ORMap.get("AD_SearchResult_CustName")).getText().toUpperCase().contains(CustomerName.toUpperCase())){
				// Logger 
				TestLogger.log(LogStatus.INFO,"AD - Customer Name is verified with respect to the Reg ID '"+SearchText+"' and Customer Name : "+CustomerName);
			}
			else{
				// Logger 
				TestLogger.log(LogStatus.FATAL,"AD - Customer Name is was NOT verified with respect to the Reg ID '"+SearchText+"' and Customer Name : "+CustomerName+" "
						+ "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
				return "Fatal";				
			}
			
			// Get Customer Address which was updated for COA through Internet Banking
			String CustAdd = IBTestScripts.getCoASetAddress();
			String[] arrCustAdd = CustAdd.split(",");
			// Get the Profile Address from the Application 

			String ValidatedProfAdd = null;
			int RefreshLoop = 1;
			do{
				String ProfileAddressString = OR.FindElement(driver, ORMap.get("AD_SearchResult_ProfileAddress")).getText();

				// Validate the Address
				if(ProfileAddressString.toUpperCase().contains(arrCustAdd[0].toUpperCase()) 
					&& ProfileAddressString.toUpperCase().contains(arrCustAdd[1].toUpperCase())
					&& ProfileAddressString.toUpperCase().contains(arrCustAdd[2].toUpperCase())
					//&& ProfileAddressString.toUpperCase().contains(arrCustAdd[3].toUpperCase())
					){
					ValidatedProfAdd = arrCustAdd[0]+","+arrCustAdd[1]+","+arrCustAdd[2];
					// Logger
					TestLogger.log(LogStatus.PASS,"AD - Customer Profile Address: "+ValidatedProfAdd+" has been updated and validated in Agent Desktop application. "
							+ "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
					break;
				} else if (RefreshLoop<4){
					Thread.sleep(2000);
					// Refresh all Data once and check again
					OR.FindElement(driver, ORMap.get("AD_RefreshAlllink")).click();
					// Wait until Expected Element is visible on the page
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_RefreshButton"))));
					OR.FindElement(driver, ORMap.get("AD_RefreshButton")).click();
					
					// Wait until Expected Element is visible on the page
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
					RefreshLoop++;
				} else {
					// Logger
					TestLogger.log(LogStatus.FAIL,"AD - Refreshed Profile "+RefreshLoop+" times: Customer Profile Address has NOT updated in Agent Desktop application. "
							+" Expected: "+arrCustAdd[0]+","+arrCustAdd[1]+","+arrCustAdd[2]+". "
									+ "Actual: "+ProfileAddressString+". "
							+ "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
					break;
				}
			} while(RefreshLoop<5);
			
		} catch(Exception e){
			e.printStackTrace();
			// Logger 
			TestLogger.log(LogStatus.FATAL,"AD - Navigate and Login to Agent Desktop Failed with Errors.: Error Desceiption - "+e.toString()+" Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
		return "Pass";
	}						

/* Method: ValidateAccountsMailingAddress will 
 * 1. Validate the Mailing address of the Customer for all the Accounts as input on Agent Desktop application
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String Account Numbers
 * 
 * Created by: Nikhil Katare
 * On: 24/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/		
	public String ValidateAccountsMailingAddress(String AccountNumbers) throws IOException{
		try{			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);	
			// Check if the Navigation is on the Search Customer Summary page
			if(OR.FindElements(driver, ORMap.get("AD_SearchResult_ResultForm")).size()==0){
				// Logger 
				TestLogger.log(LogStatus.FATAL,"AD - Navigation is not on the Customer Search Result page: Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";
			}
			// Wait until Expected Element is visible on the page
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_SearchResult_ConfirmButton"))));
			OR.FindElement(driver, ORMap.get("AD_SearchResult_ConfirmButton")).click();
			// Wait until Account list table appears
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_AccSum_AccListTable"))));
			Thread.sleep(1000);
			// Get the number of rows in the Account list table
			int ProductTableRows = OR.FindElements(driver, ORMap.get("AD_AccSum_AccListTableRows")).size();

			// Split the list of Accounts from the input. If input contains ',' split the string
			String[] arrAccountNumbers = null;
			if(AccountNumbers.contains(",")){
				arrAccountNumbers = AccountNumbers.split(",");
			} // If not "," found assign the String to the Array on [0] 
			else {
				arrAccountNumbers = new String[1];
				arrAccountNumbers[0]=AccountNumbers;
			}
			
			// Loop through all the input Account Numbers. For Each Account Number Validation has to be performed
			for (String AccountNumber:arrAccountNumbers){
				// Loop through all the Rows found in the Account List table
				for(int i=1;i<=ProductTableRows;i++){
					// Dynamic Xpath for each Row
					String AccountNumberLinkXpath = ORMap.get("AD_AccSum_AccListTableRows")[1]+"["+i+"]/td[3]";
					// Match Input Account Number with the Account number in the Table list 
					if(driver.findElement(By.xpath(AccountNumberLinkXpath)).getText().contentEquals(AccountNumber)){
						// Click on the Account Number Link
						AccountNumberLinkXpath = AccountNumberLinkXpath+"/a";
						driver.findElement(By.xpath(AccountNumberLinkXpath)).click();						
						// Wait until Element appears
						wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_AccSum_AccAvailableFund"))));
						Thread.sleep(1500);
						//System.out.println("");
						
						Actions actions = new Actions(driver);
						actions.moveToElement(OR.FindElement(driver, ORMap.get("AD_TopMenu_Information")));
						actions.click();
						actions.perform();
			
						// Click on the Information Top menu
						//OR.FindElement(driver, ORMap.get("AD_TopMenu_Information")).click();
						Thread.sleep(500);
						
						if(!(OR.FindElements(driver, ORMap.get("AD_TopMenu_AccountDetails")).size()>0)){
							actions.moveToElement(OR.FindElement(driver, ORMap.get("AD_TopMenu_Information")));
							actions.click();
							actions.perform();
						}
						
						// Wait until Account Details option appears 
						wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_TopMenu_AccountDetails"))));
						Thread.sleep(1000);
						// Click on the Account Details option
						OR.FindElement(driver, ORMap.get("AD_TopMenu_AccountDetails")).click();
						// Wait until Element appears 
						wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_AccDetail_MailAddLabel"))));
						
						// Get Customer Address which was updated for COA through Internet Banking
						String CustAdd = IBTestScripts.getCoASetAddress();

						String[] arrCustAdd = CustAdd.split(",");
						// Get the Mailing Address from the Application 
						String MailingAddressString = OR.FindElement(driver, ORMap.get("AD_AccDetail_MailAddText")).getText();
						
						String ValidatedMailingAddress =null;
						// Validate the Address
						if(MailingAddressString.toUpperCase().contains(arrCustAdd[0].toUpperCase()) 
							&& MailingAddressString.toUpperCase().contains(arrCustAdd[1].toUpperCase())
							&& MailingAddressString.toUpperCase().contains(arrCustAdd[2].toUpperCase())
							//&& MailingAddressString.toUpperCase().contains(arrCustAdd[3].toUpperCase())
							//&& MailingAddressString.toUpperCase().contains(arrCustAdd[4].toUpperCase())
							){
							ValidatedMailingAddress = arrCustAdd[0]+","+arrCustAdd[1]+","+arrCustAdd[2];
							// Logger
							TestLogger.log(LogStatus.PASS,"AD - Customer Account number "+AccountNumber+" Mailing Address: "+ValidatedMailingAddress+" has been updated and validated in Agent Desktop application. "
									+ "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
						} else {
							// Logger
							TestLogger.log(LogStatus.FAIL,"AD - Customer Account number "+AccountNumber+" Mailing Address: "+ValidatedMailingAddress+"  has NOT updated in Agent Desktop application. "
									+" Expected: "+arrCustAdd[0]+","+arrCustAdd[1]+","+arrCustAdd[2]+". Actual: "+MailingAddressString+". "
									+"Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
						}
						
					break;	
					} else if(i==ProductTableRows) {
						// Logger 
						TestLogger.log(LogStatus.FAIL,"AD - Account Number :"+AccountNumber+" provided as input could not be Located in the Account List.");
					break;
					}
				}
			}
				
		} catch(Exception e){
			e.printStackTrace();
			// Logger 
			TestLogger.log(LogStatus.FATAL,"AD - Error occured while validating Mailing Address of the Account Numbers.: Error Description - "+e.toString()+" Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
		return "Pass";
	}

/* Method: ValidateMobileNumber will 
 * 1. Validate the Mobile Number of the Customer on the Profile Detail page on Agent Desktop application
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String MobileNumber
 * 
 * Created by: Nikhil Katare
 * On: 25/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/
	public String SearchCustomerValidateMobileNumber(String SearchText, String CustomerName) throws IOException{
		try{
			// Get Customer Mobile Number which was updated for COA through Internet Banking			
			String MobileNumber = IBTestScripts.getCoMobileNumber();
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);	
			// Wait until Expected Element is visible on the page
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_Landing_ListSearchType"))));
			
			// Select Search type option from the Select List
			Select ListSearchType = new Select(OR.FindElement(driver, ORMap.get("AD_Landing_ListSearchType")));
			ListSearchType.selectByValue("Reg");
			
			// Type in Search Criteria data
			OR.FindElement(driver, ORMap.get("AD_Landing_RegNumberEdit")).sendKeys(SearchText);
			// Click on the Search button.
			OR.FindElement(driver, ORMap.get("AD_Landing_SearchButton")).click();
			
			// Wait until Expected Element is visible on the page
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
			
			// Confirm if Logged in to Agent Desktop show correct Customer Name
			if(OR.FindElement(driver, ORMap.get("AD_SearchResult_CustName")).getText().toUpperCase().contentEquals(CustomerName.toUpperCase())){
				// Logger 
				TestLogger.log(LogStatus.INFO,"AD - Customer Name is verified with respect to the Reg ID '"+SearchText+"' and Customer Name : "+CustomerName);
			}
			else{
				// Logger 
				TestLogger.log(LogStatus.FATAL,"AD - Customer Name is was NOT verified with respect to the Reg ID '"+SearchText+"' and Customer Name : "+CustomerName+" "
						+ "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
				return "Fatal";
			}
			
			// Wait until Expected Element is visible on the page
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_SearchResult_MobileNumber"))));
			
			String appMobileNumber = OR.FindElement(driver, ORMap.get("AD_SearchResult_MobileNumber")).getText();
			if(appMobileNumber.contains("-")){
				String[] arrMobileNumber = appMobileNumber.split("-");
				appMobileNumber = arrMobileNumber[0].trim()+arrMobileNumber[1].trim();
			}
						// Validate the Address
			if(appMobileNumber.toUpperCase().contains(MobileNumber.toUpperCase())){
				// Logger
				TestLogger.log(LogStatus.PASS,"AD - Customer Mobile number "+MobileNumber+" is validated in Agent Desktop application. "
						+ "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
			} else {
				// Logger
				TestLogger.log(LogStatus.FAIL,"AD - Customer Mobile number "+MobileNumber+" has NOT been updated in Agent Desktop application. "
						+" Expected: "+MobileNumber+". Actual: "+appMobileNumber+". "
						+"Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
			}
				
		} catch(Exception e){
			e.printStackTrace();
			// Logger 
			TestLogger.log(LogStatus.FATAL,"AD - Error occured while validating Mobile Number of the Customer.: Error Description - "+e.toString()+" Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
		return "Pass";
	}

/* Method: SearchCustValidateProfileAddressAndMobileNumber will 
 * 1. Validate the Change of Address and Mobile Number of the Customer on the Profile Detail page on Agent Desktop application
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String MobileNumber, String Customer Name
 * 
 * Created by: Nikhil Katare
 * On: 25/Nov/2016
 * Reviewed by:
 * Updated on:
 * Updated with:	
*/	
	public String SearchCustValidateProfileAddressAndMobileNumber(String SearchText, String CustomerName) throws IOException{
		try{			
			
			String MobileUpdateStatus=null;
			// Get Customer Address which was updated for COA through Internet Banking
			String CustAdd = IBTestScripts.getCoASetAddress();
			// Get Customer Mobile Number which was updated for COA through Internet Banking			
			String MobileNumber = IBTestScripts.getCoMobileNumber();
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);	
			// Wait until Expected Element is visible on the page
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_Landing_ListSearchType"))));
			
			// Select Search type option from the Select List
			Select ListSearchType = new Select(OR.FindElement(driver, ORMap.get("AD_Landing_ListSearchType")));
			ListSearchType.selectByValue("Reg");
			
			// Type in Search Criteria data
			OR.FindElement(driver, ORMap.get("AD_Landing_RegNumberEdit")).sendKeys(SearchText);
			// Click on the Search button.
			OR.FindElement(driver, ORMap.get("AD_Landing_SearchButton")).click();
			
			// Wait until Expected Element is visible on the page
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
			
			if(OR.FindElement(driver, ORMap.get("AD_SearchResult_CustName")).getText().toUpperCase().contains(CustomerName.toUpperCase())){
				// Logger 
				TestLogger.log(LogStatus.INFO,"AD - Customer Name is verified with respect to the Reg ID '"+SearchText+"' and Customer Name : "+CustomerName);
			}
			else{
				// Logger 
				TestLogger.log(LogStatus.FATAL,"AD - Customer Name is was NOT verified with respect to the Reg ID '"+SearchText+"' and Customer Name : "+CustomerName+" "
						+ "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
				return "Fatal";
			}
			// Changing the Mobile Number Pulled from application
			String appMobileNumber = OR.FindElement(driver, ORMap.get("AD_SearchResult_MobileNumber")).getText();
			if(appMobileNumber.contains("-")){
				String[] arrMobileNumber = appMobileNumber.split("-");
				appMobileNumber = arrMobileNumber[0].trim()+arrMobileNumber[1].trim();
			}
			// Validate the Address
			if(appMobileNumber.toUpperCase().contentEquals(MobileNumber.toUpperCase())){
				MobileUpdateStatus = "Pass";
			} else {
				MobileUpdateStatus = "Fail";
			}
			
			//String CustAdd = "AUTO ADDRESS 1 ADD1,AUTO ADDRESS 2 ADD2,AUTO ADDRESS 3 ADD3";
			String[] arrCustAdd = CustAdd.split(",");
			
			int RefreshLoop = 1;
			do{
				// Get the Profile Address from the Application 
				String ProfileAddressString = OR.FindElement(driver, ORMap.get("AD_SearchResult_ProfileAddress")).getText();
				// Validate the Address
				if(ProfileAddressString.toUpperCase().contains(arrCustAdd[0].toUpperCase()) 
					&& ProfileAddressString.toUpperCase().contains(arrCustAdd[1].toUpperCase())
					&& ProfileAddressString.toUpperCase().contains(arrCustAdd[2].toUpperCase())
					//&& ProfileAddressString.toUpperCase().contains(arrCustAdd[3].toUpperCase())
									&& MobileUpdateStatus == "Pass"){
					// Logger
					TestLogger.log(LogStatus.PASS,"AD - Customer Profile Address: "+CustAdd+" and Customer Mobile: "+MobileNumber+" number has been updated and validated in Agent Desktop application. "
							+ "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
					break;
				} else if (RefreshLoop<4){				
					Thread.sleep(2000);
					// Refresh all Data once and check again
					OR.FindElement(driver, ORMap.get("AD_RefreshAlllink")).click();
					// Wait until Expected Element is visible on the page
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_RefreshButton"))));
					OR.FindElement(driver, ORMap.get("AD_RefreshButton")).click();
					
					// Wait until Expected Element is visible on the page
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
					RefreshLoop++;
				} else {
					// Logger
					TestLogger.log(LogStatus.FAIL,"AD - Refreshed profile "+RefreshLoop+" times: Customer Profile Address and Customer Mobile number has NOT updated in Agent Desktop application. "
							+" Expected Address: "+arrCustAdd[0]+","+arrCustAdd[1]+","+arrCustAdd[2]+""
									+ ". Actual Address: "+ProfileAddressString+". Expected Mobile Number: "+MobileNumber+". Actual Mobile Number: "+appMobileNumber+"."
							+ "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
					break;
				}
			} while(RefreshLoop<5);
		
		} catch(Exception e){
			e.printStackTrace();
			// Logger 
			TestLogger.log(LogStatus.FATAL,"AD - Navigate, Login and Validate Address and Mobile Number to Agent Desktop Failed with Errors.: Error Desceiption - "
			+e.toString()+" Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
		return "Pass";
	}
	
/* Method: DeleteAllCustomerRemarksAgentDesktop will 
 * 1. Delete all Customer remarks from Agent Desktop related to Change of Address
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String AgentDesktopUser, String SearchText
 * 
 * Created by: Nikhil Katare
 * On: 29/Nov/2016
 * Reviewed by:
 * Updated on:
 * Updated with:	
*/	
	public String DeleteAllCustomerRemarksAgentDesktop(String AgentDesktopUser, String SearchText) throws IOException{
		try{		
			// Login to Agent Desktop Call Method to Login
			String strFlag = LoginAgentDesktop(AgentDesktopUser);
			if(strFlag.toUpperCase().contentEquals("FATAL")){
				// Logger 
				TestLogger.log(LogStatus.FATAL,"AD - Delete all Customer Remarks for Change of Address from Agent Desktop Failed with Errors. Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";
			}
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);	
			// Wait until Expected Element is visible on the page
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_Landing_ListSearchType"))));
			
			// Select Search type option from the Select List
			Select ListSearchType = new Select(OR.FindElement(driver, ORMap.get("AD_Landing_ListSearchType")));
			ListSearchType.selectByValue("Reg");
			
			// Type in Search Criteria data
			OR.FindElement(driver, ORMap.get("AD_Landing_RegNumberEdit")).sendKeys(SearchText);
			// Click on the Search button.
			OR.FindElement(driver, ORMap.get("AD_Landing_SearchButton")).click();
			Thread.sleep(1000);
			
			// Wait until Expected Element is visible on the page
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));
			
			OR.FindElement(driver, ORMap.get("AD_SearchResult_ConfirmButton")).click();
			
			// Wait until Account list table appears
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_AccSum_AccListTable"))));
			Thread.sleep(1500);
			//System.out.println("");
			
			Actions actions = new Actions(driver);
			actions.moveToElement(OR.FindElement(driver, ORMap.get("AD_TopMenu_Information")));
			actions.click();
			actions.perform();

			// Click on the Information Top menu
			//OR.FindElement(driver, ORMap.get("AD_TopMenu_Information")).click();
			Thread.sleep(500);
			
			if(!(OR.FindElements(driver, ORMap.get("AD_TopMenu_Remarks")).size()>0)){
				actions.moveToElement(OR.FindElement(driver, ORMap.get("AD_TopMenu_Information")));
				actions.click();
				actions.perform();
				Thread.sleep(500);
			}
			// Wait until Remarks Menu appears
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_TopMenu_Remarks"))));
			OR.FindElement(driver, ORMap.get("AD_TopMenu_Remarks")).click();
			
			if(OR.FindElements(driver, ORMap.get("AD_Remarks_NoCOALabel")).size()>0){
				// Logger 
				TestLogger.log(LogStatus.INFO,"AD - Customer do not have any Remarks related to Change of Address to delete");
				return "Pass";
			} else if(OR.FindElements(driver, ORMap.get("AD_Remarks_RemarksTable")).size()>0){
			
				// Wait until Remarks Menu appears
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_Remarks_RemarksTable"))));
				
				// Condition to Check if any Remarks are already present, If yes Click and initiate Delete
				List<WebElement> RemarksThen = OR.FindElements(driver, ORMap.get("AD_Remarks_RemarkTableCOARows"));
				if(RemarksThen.size()>0){
					do{
						OR.FindElements(driver, ORMap.get("AD_Remarks_RemarkTableCOARows")).get(0).click();
						Thread.sleep(500);
						OR.FindElement(driver, ORMap.get("AD_Remarks_DeleteButton")).click();
						// Wait until Confirm Button appears
						wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_Remarks_ConfirmButton"))));
						OR.FindElement(driver, ORMap.get("AD_Remarks_ConfirmButton")).click();
						Thread.sleep(1000);
						if(!(OR.FindElements(driver, ORMap.get("AD_Remarks_RemarksTable")).size()>0)){
							TestLogger.log(LogStatus.INFO,"AD - All Customer Remarks related to Change of Address were deleted");
							break;
						}
						
					}while(OR.FindElements(driver, ORMap.get("AD_Remarks_RemarkTableCOARows")).size()>0);
				}
			}
			
			
		} catch(Exception e){
			e.printStackTrace();
			// Logger 
			TestLogger.log(LogStatus.FATAL,"AD - Delete all Customer Remarks for Change of Address from Agent Desktop Failed with Errors.: Error Desceiption - "+e.toString()+" Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
		return "Pass";
	}

/* Method: ValidateCustomerRemarksAgentDesktop will 
 * 1. Validate Customer remarks from Agent Desktop related to Change of Address
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String AgentDesktopUser, String SearchText, String RemarksString
 * 
 * Created by: Nikhil Katare
 * On: 29/Nov/2016
 * Reviewed by:
 * Updated on:
 * Updated with:	
*/	
	public String ValidateCustomerRemarksAD(String RemarksString) throws IOException{
		try{		
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);	
			
			if(OR.FindElements(driver, ORMap.get("AD_SearchResult_ConfirmButton")).size()>0){
				OR.FindElement(driver, ORMap.get("AD_SearchResult_ConfirmButton")).click();
				// Wait until Account list table appears
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_AccSum_AccListTable"))));
				Thread.sleep(500);
				//System.out.println("");
				
				Actions actions = new Actions(driver);
				actions.moveToElement(OR.FindElement(driver, ORMap.get("AD_TopMenu_Information")));
				actions.click();
				actions.perform();
				Thread.sleep(500);
				
				if(!(OR.FindElements(driver, ORMap.get("AD_TopMenu_Remarks")).size()>0)){
					actions.moveToElement(OR.FindElement(driver, ORMap.get("AD_TopMenu_Information")));
					actions.click();
					actions.perform();
					Thread.sleep(500);
				}
			} else if(OR.FindElements(driver, ORMap.get("AD_AccSum_AccListTable")).size()>0){
				Thread.sleep(500);
				Actions actions = new Actions(driver);
				actions.moveToElement(OR.FindElement(driver, ORMap.get("AD_TopMenu_Information")));
				actions.click();
				actions.perform();
				Thread.sleep(500);
				
				if(!(OR.FindElements(driver, ORMap.get("AD_TopMenu_Remarks")).size()>0)){
					actions.moveToElement(OR.FindElement(driver, ORMap.get("AD_TopMenu_Information")));
					actions.click();
					actions.perform();
					Thread.sleep(500);
				}			
			} else{
				// Logger 
				TestLogger.log(LogStatus.FATAL,"AD - Navigation is not on the Customer Search Result page on Agent Desktop: Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";
			}
			
			// Wait until Remarks Menu appears
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_TopMenu_Remarks"))));
			Thread.sleep(500);
			OR.FindElement(driver, ORMap.get("AD_TopMenu_Remarks")).click();
			
			int RefreshCount=0;
			do{
				if(OR.FindElements(driver, ORMap.get("AD_Remarks_RemarksTable")).size()>0){
				
					// Wait until Remarks Menu appears
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_Remarks_RemarksTable"))));
					
					// Condition to Check if any Remarks are already present, If yes Click and initiate Delete
					List<WebElement> RemarksList = OR.FindElements(driver, ORMap.get("AD_Remarks_RemarkTableCOARows"));
	
					if(RemarksList.size()==1){
						
						OR.FindElements(driver, ORMap.get("AD_Remarks_RemarkTableCOARows")).get(0).click();
						Thread.sleep(500);
						OR.FindElement(driver, ORMap.get("AD_Remarks_ViewButton")).click();
						// Wait until Confirm Button appears
						wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_SearchResult_ResultForm"))));

						if(OR.FindElement(driver, ORMap.get("AD_Remarks_RemarksText")).getText().trim().toUpperCase()
								.contains(RemarksString.toUpperCase())){
							// Logger 
							TestLogger.log(LogStatus.PASS,"AD - Customer Change of Address Remarks is validated as :"+RemarksString+". Screenshot "+Screenshot.ObjectSnap(driver, 
									OR.FindElement(driver, ORMap.get("AD_Remarks_RemarksText"))));
						} else{
							// Logger 
							TestLogger.log(LogStatus.FAIL,"AD - Customer Change of Address Remarks validation has Failed. Expected Remarks :"+RemarksString+""
									+ ". Actual Remarks: "+OR.FindElement(driver, ORMap.get("AD_Remarks_RemarksText")).getText()+". Screenshot "+Screenshot.ObjectSnap(driver, 
									OR.FindElement(driver, ORMap.get("AD_Remarks_RemarksText"))));
						}
						OR.FindElement(driver, ORMap.get("AD_Remarks_CloseButton")).click();
						// Wait until Remarks Menu appears
						wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_Remarks_RemarksTable"))));
						
						break;

					} else if(RemarksList.size()>1){
						// Logger 
						TestLogger.log(LogStatus.FAIL,"AD - Customer received more then One Remarks related to Change of Address"
								+"Screenshot: "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_Remarks_RemarksTable"))));
						return "Fail";
					}
				} else if(OR.FindElements(driver, ORMap.get("AD_Remarks_NoCOALabel")).size()>0 && RefreshCount<4) {
					Thread.sleep(2000);
					
					// Refresh all Data once and check again
					OR.FindElement(driver, ORMap.get("AD_RefreshAlllink")).click();
					// Wait until Expected Element is visible on the page
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_RefreshButton"))));
					OR.FindElement(driver, ORMap.get("AD_RefreshButton")).click();
					
					// Wait until Account list table appears
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_AccSum_AccListTable"))));
					Thread.sleep(500);
					//System.out.println("");
					
					Actions actions = new Actions(driver);
					actions.moveToElement(OR.FindElement(driver, ORMap.get("AD_TopMenu_Information")));
					actions.click();
					actions.perform();
					Thread.sleep(500);
					
					if(!(OR.FindElements(driver, ORMap.get("AD_TopMenu_Remarks")).size()>0)){
						actions.moveToElement(OR.FindElement(driver, ORMap.get("AD_TopMenu_Information")));
						actions.click();
						actions.perform();
						Thread.sleep(500);
					}				
					
					// Wait until Remarks Menu appears
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("AD_TopMenu_Remarks"))));
					OR.FindElement(driver, ORMap.get("AD_TopMenu_Remarks")).click();
					Thread.sleep(500);
					RefreshCount++;
				} else if (OR.FindElements(driver, ORMap.get("AD_Remarks_NoCOALabel")).size()>0 && RefreshCount>=4){
					// Logger 
					TestLogger.log(LogStatus.FAIL,"AD - Customer do not have any Remarks related to Change of Address"
							+"Screenshot: "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("AD_Remarks_NoCOALabel"))));
					return "Fail";
				}
				
			} while(RefreshCount<5);
			
			
		} catch(Exception e){
			e.printStackTrace();
			// Logger 
			TestLogger.log(LogStatus.FATAL,"AD - Customer Change of Address Remarks validation thwors errors: Error Desceiption - "+e.toString()+" Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
		return "Pass";
	}

}
