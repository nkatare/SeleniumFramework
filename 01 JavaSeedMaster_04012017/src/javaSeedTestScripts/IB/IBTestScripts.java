package javaSeedTestScripts.IB;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import javaSeed.constants.Const;
import javaSeed.objectRepository.OR;
import javaSeed.utils.Screenshot;
import javaSeedTestScripts.TestConsts;

public class IBTestScripts {
	

	private WebDriver driver = Const.driver;
	private HashMap<String, String[]> ORMap = Const.ORMap;
	private ExtentTest TestLogger = Const.etTestCases;
	
	//#####################################################################
	// Global Variables used in capture data for text execution
	public static String CoASetAddress;
	public static String CoMobileNumber;
	
	public static String getCoMobileNumber() {
		return CoMobileNumber;
	}
	public static void setCoMobileNumber(String coMobileNumber) {
		CoMobileNumber = coMobileNumber;
	}
	public static String getCoASetAddress() {
		return CoASetAddress;
	}
	public void setCoASetAddress(String CoASetAddress) {
		IBTestScripts.CoASetAddress = CoASetAddress;
	}
	//#####################################################################
	
	
/* Method: LoginToInternetBanking will 
 * 1. Login to Internet Banking Portal in three steps -
 * 		- Navigate to Internet Banking portal
 * 		- Update Registration number and navigate to the PAC challenge page
 * 		- Update PAC code numbers and navigate to IB Landing page
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String PAReference
 * 
 * Created by: Nikhil Katare
 * On: 07/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/		
	public String LoginToInternetBanking(String RegNumber, String PACCode) throws IOException{ //String RegistrationID, String PACCode
		
		try{
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			
			// Get the Internet Banking URL
			driver.get(TestConsts.IB_URL);
			
	        // Maximize the Browser
			driver.manage().window().maximize();
			
			// Wait until Registration Number Edit box appear
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_Login_RegNumberText"))));
		
			// Send the Registration number 
			OR.FindElement(driver, ORMap.get("IB_Login_RegNumberText")).sendKeys(RegNumber);
			
			// Logger for navigating to IB Page
			TestLogger.log(LogStatus.INFO,"IB - Navigated to Internet Banking Login page : "+TestConsts.IB_URL);
			//TestLogger.log(LogStatus.INFO,"Navigated to Internet Banking Login page","Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_LoginPage"))));
	
			Thread.sleep(500);
			// Click on Next Button
			OR.FindElement(driver, ORMap.get("IB_Login_NextButton")).click();
			Thread.sleep(1000);
			
			// Wait until First PAC Code Edit box appear
			wait.until(ExpectedConditions.visibilityOf(OR.FindElements(driver, ORMap.get("IB_Login_3PACLabels")).get(0)));
			
			// Get the list of Digit Labels elements (Usually 3)
			List<WebElement> DigitLabels = OR.FindElements(driver, ORMap.get("IB_Login_3PACLabels"));
			// Get the list of Digit Input elements (Usually 3)
			List<WebElement> DigitInputs = OR.FindElements(driver, ORMap.get("IB_Login_3PACInputBoxes"));
			
			// Split the PAC Code string in to individual PAC Code
			String[] arrPACCode = PACCode.split(",");
			
			if(DigitLabels.size()==3){
				// Loop Through Each PAC Code Label to send the correct PAC Code
				for(int i=0;i<DigitLabels.size();i++){
					if(DigitLabels.get(i).getText().contains("Digit 1")){
						DigitInputs.get(i).sendKeys(arrPACCode[0]);
					} else if(DigitLabels.get(i).getText().contains("Digit 2")){
						DigitInputs.get(i).sendKeys(arrPACCode[1]);
					} else if(DigitLabels.get(i).getText().contains("Digit 3")){
						DigitInputs.get(i).sendKeys(arrPACCode[2]);
					} else if(DigitLabels.get(i).getText().contains("Digit 4")){
						DigitInputs.get(i).sendKeys(arrPACCode[3]);
					} else if(DigitLabels.get(i).getText().contains("Digit 5")){
						DigitInputs.get(i).sendKeys(arrPACCode[4]);
					}
				}
			} else if(DigitLabels.size()==4){
				// Loop Through Each PAC Code Label to send the correct PAC Code
				for(int i=0;i<DigitLabels.size();i++){
					if(DigitLabels.get(i).getText().contains("Digit 1")){
						DigitInputs.get(i).sendKeys(arrPACCode[0]);
					} else if(DigitLabels.get(i).getText().contains("Digit 2")){
						DigitInputs.get(i).sendKeys(arrPACCode[1]);
					} else if(DigitLabels.get(i).getText().contains("Digit 3")){
						DigitInputs.get(i).sendKeys(arrPACCode[2]);
					} else if(DigitLabels.get(i).getText().contains("Digit 4")){
						DigitInputs.get(i).sendKeys(arrPACCode[3]);
					} else if(DigitLabels.get(i).getText().contains("Digit 5")){
						DigitInputs.get(i).sendKeys(arrPACCode[4]);
					} else if(DigitLabels.get(i).getText().contains("Last 4 Digits")){
						DigitInputs.get(i).sendKeys(arrPACCode[5]);
					}
				}
			}
			
			// Logger
			TestLogger.log(LogStatus.INFO,"IB - Navigated to Internet Banking PAC Code challenge page");
			//TestLogger.log(LogStatus.INFO,"Navigated to Internet Banking PAC Code challenge page", "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_LoginPage"))));
			
			// Click on the Next button to navigate to Internet banking Landing page
			OR.FindElement(driver, ORMap.get("IB_Login_NextButton")).click();
			Thread.sleep(2000);
			
			// Check to handle My Statement Page and if present click on Don't make changes button
			if(OR.FindElements(driver, ORMap.get("IB_MyStatements_Label")).size()>0){
				OR.FindElement(driver, ORMap.get("IB_MyStatements_DontChangeButton")).click();
			}
			
			// Check to handle Christmas Spending page and if present click on Continue button
			if(OR.FindElements(driver, ORMap.get("IB_XmasSpendingLabel")).size()>0){
				OR.FindElement(driver, ORMap.get("IB_XmasSpend_Continuebutton")).click();
			}
			
			// Wait until Registration Number Edit box appear
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_Landing_ServicesSettingsButton"))));
			
			// Logger 
			TestLogger.log(LogStatus.INFO,"IB - Logged in to Internet Banking with user - "+RegNumber);
			//TestLogger.log(LogStatus.INFO,"Navigated to Internet Banking Landing page", "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			
		} catch(Exception e){
			e.printStackTrace();
			// Logger 
			TestLogger.log(LogStatus.FATAL,"IB - Navigated to Internet Banking Landing page Failed for user "+RegNumber+" with Errors: Error Description - "+e.toString()+ " Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}

	return "Pass";
	}

/* Method: NavigateToServicesSettingsLink will 
 * 1. Navigate to the Page provided as parameter from IB Landing page to Services and Settings section
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String NavigateToLink
 * 
 * Created by: Nikhil Katare
 * On: 07/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
	public String NavigateToServicesSettingsChildLinks(String NavigateToLink) throws IOException{
		
		try{
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			
			// Wait until Services and Settings Button to appear
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_Landing_ServicesSettingsButton"))));
			
			// Click on Services and Settings Link
			OR.FindElement(driver, ORMap.get("IB_Landing_ServicesSettingsButton")).click();
			// Wait until My Details Link Button to appear
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_Landing_MyDetailsLink"))));
			
			// Get the Link Text of My Details Element
			String MyDetailsText = OR.FindElement(driver, ORMap.get("IB_Landing_MyDetailsLink")).getText();
			// Get the Link Text of My Messages Element
			String MyMessageText = OR.FindElement(driver, ORMap.get("IB_Landing_MyMessagesLink")).getText();
						
			// Condition to check if Navigate to My Details
			if(NavigateToLink.toUpperCase().contains(MyDetailsText.toUpperCase())){
				
				// Click on My Details Link
				OR.FindElement(driver, ORMap.get("IB_Landing_MyDetailsLink")).click();
				// Wait until the Element is visible
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_ScSh_COAFormGeneric"))));
				// Logger
				TestLogger.log(LogStatus.INFO,"IB - Navigated to "+MyDetailsText+" page");
				//TestLogger.log(LogStatus.INFO,"Navigated to "+MyDetailsText+" page", "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			} // Condition to check if Navigate to My Messages
			else if(NavigateToLink.toUpperCase().contains(MyMessageText.toUpperCase())){
				
				// Click on My Details Link
				OR.FindElement(driver, ORMap.get("IB_Landing_MyMessagesLink")).click();
				// Wait until the Element is visible
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_MyMsg_Header"))));
				// Logger
				TestLogger.log(LogStatus.INFO,"IB - Navigated to "+MyMessageText+" page");
				//TestLogger.log(LogStatus.INFO,"Navigated to "+MyMessageText+" page", "Screenshot - "+Screenshot.ObjectSnap(driver, (OR.FindElement(driver, ORMap.get("IB_MyMsg_Block")))));
			}// If None of the options provided to navigate
			else {
				// Logger
				TestLogger.log(LogStatus.FATAL,"IB - Navigated to Services and Settings Child page was not provided correctly");
				return "Fatal";
			}
			
		} catch(Exception e){
			e.printStackTrace();
			// Logger for navigating to Internet Banking Landing page 
			TestLogger.log(LogStatus.FATAL,"IB - Navigate to "+NavigateToLink+" page Failed with Errors: Error Description - "+e.toString()+ " Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
	return "Pass"; 
	}
	
/* Method: SetnSubmitChangeOfAddress will 
 * 1. Check the existing address on the profile and pick different address from the Two address passed as parameter to update
 * 2. Check for Country of residence as IE, if yes, update the Country as well as County. Otherwise only Country
 * 3. Navigate until the confirm page, provide card code input and submit the request 
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Input Param: 1. String OneCOA123 (Input Address string as - Add Line 1,Add Line 2,Add line 3)
 * 				2. String OneCOACountryCounty (Input Country and County string as a)- Country Code(if IE),Country Code. b)-Country code)
 * 				3. String TwoCOA123 (Input Address string as - Add Line 1,Add Line 2,Add line 3)
 * 				4. String TwoCOACountryCounty (Input Country and County string as a)- Country Code(if IE),Country Code. b)-Country code)
 * 				5. String COACardCode
 * 
 * Created by: Nikhil Katare
 * On: 07/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
	public String SetnSubmitChangeOfAddress(String OneCOA123,String OneCOACountry, 
				String TwoCOA123, String TwoCOACountry, String MobileNumbers, String COACardCode) throws IOException {
		
		try{
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			
			if(OR.FindElements(driver, ORMap.get("IB_MyDestails_NoCOAMsgPanel")).size()>0){
				// Logger
				TestLogger.log(LogStatus.FATAL,"IB - New Change of Address option is available for this user."
						+ " Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";
				
			}
			
			// Wait untilChange of Address Yes Button to appear
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_MyDestails_COAYes"))));
			
			// Change the Input of Address Line 1, 2, 3 in to Arrays for Two different address
			String[] arrOneCOA123 = OneCOA123.split(",");
			String[] arrTwoCOA123 = TwoCOA123.split(",");
			// Initialize a local variable to set which address to update out of two input
			int WhichAddress = WhichAddress(arrOneCOA123,arrTwoCOA123);
			
			// Check the local variable value which address to set, if 1 set First address
			if(WhichAddress==1){
/*				// Set the COA Address public variable to Address Two
				setCoASetAddress(OneCOA123);*/
				
				String strFlag = SetAddressAuto(OneCOACountry, OneCOA123);
				if(strFlag.contentEquals("Fatal")){
					return "Fatal";
				}
				
				// Handle if Mobile number is not present on the profile, set the number and go Next
				if(OR.FindElements(driver, ORMap.get("IB_COA_MobileNumberError")).size()>0){
					OR.FindElement(driver, ORMap.get("IB_COA_MobileTextbox")).sendKeys(MobileNumbers.split(",")[0]);
					OR.FindElement(driver, ORMap.get("IB_COA_NextButton")).click();
				}
			// Logger to update Change of Address is updated
				TestLogger.log(LogStatus.INFO,"IB - New Address was updated to "+getCoASetAddress());

				//TestLogger.log(LogStatus.INFO,"New Address was updated to "+OneCOA123+" "+OneCOACountryCounty, "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			} // Check the local variable value which address to set, if 2 set Second address 
			else if(WhichAddress==2){
				
/*				// Set the COA Address public variable to Address Two
				setCoASetAddress(TwoCOA123);*/
				
				String strFlag = SetAddressAuto(TwoCOACountry, TwoCOA123);
				if(strFlag.contentEquals("Fatal")){
					return "Fatal";
				}				

				// Click on Next Button to Navigate to Next page
				OR.FindElement(driver, ORMap.get("IB_COA_NextButton")).click();
				// Handle if Mobile number is not present on the profile, set the number and go Next				
				if(OR.FindElements(driver, ORMap.get("IB_COA_MobileNumberError")).size()>0){
					OR.FindElement(driver, ORMap.get("IB_COA_MobileTextbox")).sendKeys(MobileNumbers.split(",")[0]);
					OR.FindElement(driver, ORMap.get("IB_COA_NextButton")).click();
				}
			// Logger to update Change of Address is updated
				TestLogger.log(LogStatus.INFO,"IB - New Address was updated to "+getCoASetAddress());
				//TestLogger.log(LogStatus.INFO,"New Address was updated to "+TwoCOA123+" "+TwoCOACountryCounty, "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			}
		
		// Wait until WebElement is visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton"))));
		// Logger
		TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update First Confirm page");
		//TestLogger.log(LogStatus.INFO,"New Address update First Confirm page", "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_COAConfirm12"))));
		// Click on Next button on COA First Confirm page
		OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton")).click();
		// Wait until WebElement is visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton"))));
		// Logger
		TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update Second Confirm page");
		
		//TestLogger.log(LogStatus.INFO,"New Address update Second Confirm page", "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
		// Click on Next button on COA Second Confirm page		
		OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton")).click();
		Thread.sleep(1000);
		
		if(OR.FindElements(driver, ORMap.get("IB_COAConfirm_CodeCardInput")).size()>0){			
			// Wait until WebElement is visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput"))));
			// Update the Card Code on COA Final Confirm page
			OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput")).sendKeys(COACardCode);
			// Logger
			TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update Final Confirm page");
			//TestLogger.log(LogStatus.INFO,"New Address update Final Confirm page", "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_COAFormGeneric"))));
		} else if(OR.FindElements(driver, ORMap.get("IB_COAConfirm1_NextButton")).size()>0){
			OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton")).click();
			// Wait until WebElement is visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput"))));
			// Update the Card Code on COA Final Confirm page
			OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput")).sendKeys(COACardCode);
			// Logger
			TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update Final Confirm page");
		}
		
		// Click on Confirm Button
		OR.FindElement(driver, ORMap.get("IB_COAConfirm_ConfirmButton")).click();	
		// Wait until WebElement is visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm_MyAccountButton"))));
		if(OR.FindElements(driver, ORMap.get("IB_COAConfirm_ConfirmMessage")).size()==1
				&& OR.FindElements(driver, ORMap.get("IB_COAConfirm_ConfirmAddress")).size()==1){
			// Logger
			TestLogger.log(LogStatus.PASS,"IB - New Address update request submitted successfully. "
					+ "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_COAConfirmation"))));		
		} else{
			// Logger
			TestLogger.log(LogStatus.FATAL,"IB - New Address update request was NOT submitted successfully. Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
			
		} catch(Exception e){
			e.printStackTrace();
			// Logger for navigating to Internet Banking Landing page 
			TestLogger.log(LogStatus.FATAL,"IB - New Change of Address could not be set and has errors: Error Desceiption - "+e.toString(), "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}

	return "Pass"; 
}

/* Method: LogoutIB will 
 * 1. Logout of the Internet Banking portal
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Input Param: None
 * 
 * Created by: Nikhil Katare
 * On: 07/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
	public String LogoutIB() throws IOException{
		
		try{
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			// Click on Logout button
			OR.FindElement(driver, ORMap.get("IB_Landing_LogoutButton")).click();
			// Wait until Element becomes visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_Login_LogoutMessage"))));
			
			if(OR.FindElements(driver, ORMap.get("IB_Login_LogoutMessage")).size()==1){
				// Logger to update Change of Address is updated
				TestLogger.log(LogStatus.INFO,"IB - User Logged out successfully from Internet Banking Portal");
				//TestLogger.log(LogStatus.INFO,"User Logged out successfully", "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_Login_LogoutMessage"))));
			}
		}
		catch(Exception e){
			e.printStackTrace();
			// Logger for navigating to Internet Banking Landing page 
			TestLogger.log(LogStatus.FAIL,"IB - Errors while Logging out of IB: Error Desceiption - "+e.toString(), "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
		return "Fatal";
		}
		return "Pass";
	}

/* Method: DeleteMyCOAMessages will 
 * 1. Delete Secure Messages from Internet Banking portal for COA
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Input Param: None
 * 
 * Created by: Nikhil Katare
 * On: 07/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
	public String DeleteMyCOAMessages() throws IOException{
		try{
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			// Local variable to count Refresh Messages count
			int RefreshCount=0;
			
			// Condition to Check if COA Messages are already present, If yes Click and initiate Delete
			List<WebElement> COAMessagesThen = OR.FindElements(driver, ORMap.get("IB_MyMsg_SubjRowWithCOA"));
			if(COAMessagesThen.size()>0){
				//int MsgItr = COAMessagesThen.size();
				
				do {
					// Open the message
					OR.FindElements(driver, ORMap.get("IB_MyMsg_SubjRowWithCOA")).get(0).click();
					
					// Wait until Element becomes visible
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_MyMsg_DeleteMsgButton"))));
					// Click on the Delete button
					OR.FindElement(driver, ORMap.get("IB_MyMsg_DeleteMsgButton")).click();
					// Wait until Element becomes visible
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_MyMsg_ConfirmDeleteButton"))));
					// Click on the Confirm Delete Button
					OR.FindElement(driver, ORMap.get("IB_MyMsg_ConfirmDeleteButton")).click();
					// Wait until Element becomes visible
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_MyMsg_ReturnMyMsgButton"))));
					// Click on My Messages button to return to My Message page
					OR.FindElement(driver, ORMap.get("IB_MyMsg_ReturnMyMsgButton")).click();
					
					// Wait until Element becomes visible
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_MyMsg_Block"))));
					//List<WebElement> COAMessagesNow = OR.FindElements(driver, ORMap.get("IB_MyMsg_SubjRowWithCOA"));
					//MsgItr--;
				} while(OR.FindElements(driver, ORMap.get("IB_MyMsg_SubjRowWithCOA")).size()>0);
				// Check if No messages are left
				if(OR.FindElements(driver, ORMap.get("IB_MyMsg_SubjRowWithCOA")).size()==0){
					// Logger
					TestLogger.log(LogStatus.INFO,"IB - CoA related Secure messages are deleted");
					//TestLogger.log(LogStatus.INFO,"CoA related Secure messages are deleted", "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_MyMsg_Block"))));
				}
			} // Check if Message isnt't appear on the Page, Refresh My Messages and wait 5 Seconds for 2 instances
			else if (RefreshCount<2){
				Thread.sleep(5000);
				OR.FindElement(driver, ORMap.get("IB_MyMsg_RefreshButton")).click();
				RefreshCount++;				
			} // Confirm there are no Messages appear on the page
			else if ((RefreshCount>2)){
				// Logger
				TestLogger.log(LogStatus.INFO,"IB - No CoA messages present on My Message list");
				//TestLogger.log(LogStatus.INFO,"No CoA messages present on My Message list", "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_MyMsg_Block"))));
			}		
			
		}
		catch(Exception e){
			e.printStackTrace();
			// Logger 
			TestLogger.log(LogStatus.FATAL,"IB - Errors while Deleting My Secure Messages: Error Desceiption - "+e.toString(), "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
		return "Fatal";
		}
	return "Pass";
	}

/* Method: ValidateMySecureMessage will 
 * 1. Validate Secure Messages from Internet Banking portal for COA
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Input Param: String MessageContent (String that content the message type)
 * 
 * Created by: Nikhil Katare
 * On: 07/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
	public String ValidateMySecureMessage(String MessageContent) throws IOException{
		
		try{
			
			new WebDriverWait(driver, 10);
			// Local variable to count Refresh Messages count
			int RefreshCount=0;
			
			do{
							
				// Condition to Check if COA Messages are already present, If yes Click and initiate Delete
				if(OR.FindElements(driver, ORMap.get("IB_MyMsg_SubjRowWithCOA")).size()>0){
					// Open the message
					OR.FindElement(driver, ORMap.get("IB_MyMsg_SubjRowWithCOA")).click();
				
					if(OR.FindElements(driver, ORMap.get("IB_MyMsgDetail_SuccessMsgSubj")).size()>0){
						// Condition to check Message Subject Content is Successful
						if(MessageContent.toUpperCase().contains(OR.FindElement(driver, ORMap.get("IB_MyMsgDetail_SuccessMsgSubj")).getText().toUpperCase())){
							// Logger
							TestLogger.log(LogStatus.PASS,"IB - CoA Secure message present on My Message list for \"Successfull\" Completion. "
									+ "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
						break;
						}
					} else if(OR.FindElements(driver, ORMap.get("IB_MyMsgDetail_UnsuccessMsgSubj")).size()>0){
						if(MessageContent.toUpperCase().contains(OR.FindElement(driver, ORMap.get("IB_MyMsgDetail_UnsuccessMsgSubj")).getText().toUpperCase())) {
							// Logger
							TestLogger.log(LogStatus.PASS,"IB - CoA Secure message present on My Message list for \"Unsuccessfull\" Completion. "
									+ "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
						break;
						}
					} else{
							// Logger
							TestLogger.log(LogStatus.FAIL,"IB - CoA Secure message present on My Message list could not be verified. "
									+ "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
						break;
						}
					
				} // Check if Message isnt't appear on the Page, Refresh My Messages and wait 5 Seconds for 2 instances
				else if (RefreshCount<3){
					Thread.sleep(10000);
					OR.FindElement(driver, ORMap.get("IB_MyMsg_RefreshButton")).click();
					RefreshCount++;				
				} // Confirm there are no Messages appear on the page
				else if (RefreshCount>=3){
					// Logger
					TestLogger.log(LogStatus.FAIL,"IB - No CoA messages appears on My Message list in 60 Seconds. "
							 +"Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_MyMsg_Block"))));
				break;
				}
			} while (RefreshCount<4);
			
		}
		catch(Exception e){
			e.printStackTrace();
			// Logger 
			TestLogger.log(LogStatus.FATAL,"IB - Errors while Validating My Secure Messages: Error Desceiption - "+e.toString()+ ". Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
		return "Fatal";
		}
		
	return "Pass";
	}

/* Method: ValidateCoAInternetBanking will 
 * 1. Validate the Change of Address on Internet Banking portal 'My Detail'
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Input Param: None
 * 
 * Created by: Nikhil Katare
 * On: 21/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/
	public String ValidateCoAInternetBanking() throws IOException {
		
		try{
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			
			// Wait untilChange of Address Yes Button to appear
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_MyDestails_COAYes"))));
			
			String UpdatedCoAddress = IBTestScripts.getCoASetAddress();
			String[] arrUpdatedCoAddress = UpdatedCoAddress.split(",");
			
			String ActulAdd = null;
			// Condition to check if First input address is already on the profile, if yes update local variable to set Add 2
			if(arrUpdatedCoAddress[0].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line1")).getText())
				&& arrUpdatedCoAddress[1].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line2")).getText())
				&& arrUpdatedCoAddress[2].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line3")).getText())
				//&& arrUpdatedCoAddress[3].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line4")).getText())
				//&& arrUpdatedCoAddress[3].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_PostCode")).getText())
				){
				ActulAdd = OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line1")).getText();
				ActulAdd = ActulAdd+","+OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line2")).getText();
				ActulAdd = ActulAdd+","+OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line3")).getText();/*
				ActulAdd = ActulAdd+","+OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line4")).getText();
				ActulAdd = ActulAdd+","+OR.FindElement(driver, ORMap.get("IB_CurrentAdd_PostCode")).getText();*/
				//Logger
				TestLogger.log(LogStatus.PASS,"IB - New Address: "+UpdatedCoAddress+" was updated and can be found on Internet Banking 'My Details'. "
						+ "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			}  // Condition to check if none of the two input address are already on the profile, update local variable to set Add 1  
			else{
				TestLogger.log(LogStatus.FAIL,"IB - New Address was NOT updated on Internet Banking 'My Details'. "
						+ "Expected: "+UpdatedCoAddress+". Actual: "+ActulAdd+". Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			}
			
		} catch(Exception e){
			e.printStackTrace();
			// Logger for navigating to Internet Banking Landing page 
			TestLogger.log(LogStatus.FATAL,"IB - New Change of Address could not be validated and has errors: Error Desceiption - "+e.toString()+
					". Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}

	return "Pass"; 
	}
		
/* Method: SetnSubmitOnlyMobileNumber will 
 * 1. Update only the Mobile Number for the Customer from Internet Banking application
 *  
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Input Param: 1. String Mobile Number (Two Mobile Numbers comma separated)
 * 				2. String COACardCode
 * 
 * Created by: Nikhil Katare
 * On: 25/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/		
	public String SetnSubmitOnlyMobileNumber(String MobileNumber, String COACardCode) throws IOException{
		try{			
			// Initializing Which Number variable
			int WhichNumber;
			// Split the comma separated Mobile Number String 
			String[] arrMobileNumber = MobileNumber.split(",");
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);	

			if(OR.FindElements(driver, ORMap.get("IB_MyDestails_COANo")).size()>0){
				// Wait until Expected Element is visible on the page
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_MyDestails_COANo"))));
				
				// If COA No button is not selected, select it
				if(OR.FindElement(driver, ORMap.get("IB_MyDestails_COANo")).getAttribute("class").contentEquals("")){
					OR.FindElement(driver, ORMap.get("IB_MyDestails_COANo")).click();	
				}
			}
			// Get the Content of the Mobile number field
			String AppMobileNumber = OR.FindElement(driver, ORMap.get("IB_COA_MobileTextbox")).getAttribute("placeholder");
			// Check if Mobile Number exists, if yes check for * in the content
			if(AppMobileNumber.contains("*")){
				// Get the last 4 digits of the String which represents the numbers
				AppMobileNumber = AppMobileNumber.substring(AppMobileNumber.length() - 4);
			// Else if no Mobile already present, blank the Mobile Number string
			} else if (AppMobileNumber.contentEquals("")){
				AppMobileNumber="";
			}
			
			// Condition to check if First input Number is already on the profile, if yes update local variable to set # 2
			if(arrMobileNumber[0].contains(AppMobileNumber)){
				WhichNumber = 2;
			} // Condition to check if Second input Number is already on the profile, if yes update local variable to set # 1 
			else if(arrMobileNumber[1].contains(AppMobileNumber)){
				WhichNumber = 1;
			} // Condition to check if none of the two input Numbers are already on the profile, update local variable to set # 1  
			else{
				WhichNumber = 1;
			}
			
			if(WhichNumber==1){
				// Enter Mobile Number #1
				OR.FindElement(driver, ORMap.get("IB_COA_MobileTextbox")).sendKeys(arrMobileNumber[0]);
				IBTestScripts.setCoMobileNumber(arrMobileNumber[0]);
				// Logger
				TestLogger.log(LogStatus.INFO,"IB - New Mobile Number "+arrMobileNumber[0]+" updated for the Customer");
			} else if (WhichNumber==2){
				// Enter Mobile Number #2
				OR.FindElement(driver, ORMap.get("IB_COA_MobileTextbox")).sendKeys(arrMobileNumber[1]);
				IBTestScripts.setCoMobileNumber(arrMobileNumber[1]);
				// Logger
				TestLogger.log(LogStatus.INFO,"IB - New Mobile Number "+arrMobileNumber[1]+" updated for the Customer");
			}
			// Click on Next Button to Navigate to Next page
			OR.FindElement(driver, ORMap.get("IB_COMobile_NextButton")).click();
			// Wait until WebElement is visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton"))));
			// Logger
			TestLogger.log(LogStatus.INFO,"IB - Navigated to New Mobile Number update First Confirm page");
			//TestLogger.log(LogStatus.INFO,"New Address update First Confirm page", "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_COAConfirm12"))));
			// Click on Next button on COA First Confirm page
			OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton")).click();
			Thread.sleep(1000);
			
			if(OR.FindElements(driver, ORMap.get("IB_COAConfirm_CodeCardInput")).size()>0){			
				// Wait until WebElement is visible
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput"))));
				// Update the Card Code on COA Final Confirm page
				OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput")).sendKeys(COACardCode);
				// Logger
				TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update Final Confirm page");
				//TestLogger.log(LogStatus.INFO,"New Address update Final Confirm page", "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_COAFormGeneric"))));
			} else if(OR.FindElements(driver, ORMap.get("IB_COAConfirm1_NextButton")).size()>0){
				OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton")).click();
				// Wait until WebElement is visible
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput"))));
				// Update the Card Code on COA Final Confirm page
				OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput")).sendKeys(COACardCode);
				// Logger
				TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update Final Confirm page");
			}
			// Click on Confirm Button
			OR.FindElement(driver, ORMap.get("IB_COAConfirm_ConfirmButton")).click();	
			// Wait until WebElement is visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm_MyAccountButton"))));
			if(OR.FindElements(driver, ORMap.get("IB_COAConfirm_ConfirmMessage")).size()==1
					&& OR.FindElements(driver, ORMap.get("IB_COAConfirm_ConfirmMobile")).size()==1){
				// Logger
				TestLogger.log(LogStatus.PASS,"IB - New Mobile Number update request submitted successfully. Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_COAConfirmation"))));		
			} else{
				// Logger
				TestLogger.log(LogStatus.FATAL,"IB - New Mobile Number update request was NOT submitted successfully. Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";
			}
				
		} catch(Exception e){
			e.printStackTrace();
			// Logger 
			TestLogger.log(LogStatus.FATAL,"IB - Error occured while Setting Mobile Number of the Customer.: Error Description - "+e.toString()+" Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
		return "Pass";
	}
	
/* Method: SetnSubmitCoAAndMobilePhone will 
 * 1. Check the existing address on the profile and pick different address from the Two address passed as parameter to update
 * 2. Check for Country of residence as IE, if yes, update the Country as well as County. Otherwise only Country
 * 3. Check the existing Mobile Number on the profile and pick different number from the Two numbers passed as parameter to update
 * 4. Navigate until the confirm page, provide card code input and submit the request 
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Input Param: 1. String OneCOA123 (Input Address string as - Add Line 1,Add Line 2,Add line 3)
 * 				2. String OneCOACountryCounty (Input Country and County string as a)- Country Code(if IE),Country Code. b)-Country code)
 * 				3. String TwoCOA123 (Input Address string as - Add Line 1,Add Line 2,Add line 3)
 * 				4. String TwoCOACountryCounty (Input Country and County string as a)- Country Code(if IE),Country Code. b)-Country code)
 * 				5. String MobileNumbers (Two Mobile Numbers comma separated)
 * 				6. String COACardCode
 * 
 * Created by: Nikhil Katare
 * On: 07/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
	public String SetnSubmitCoAAndMobilePhone(String OneCOA123,String OneCOACountry, 
			String TwoCOA123, String TwoCOACountry, String MobileNumbers, String COACardCode) throws IOException {
		
		try{
			// Initializing Which Number variable
			int WhichNumber;
			// Split the comma separated Mobile Number String 
			String[] arrMobileNumber = MobileNumbers.split(",");			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			
			if(OR.FindElements(driver, ORMap.get("IB_MyDestails_NoCOAMsgPanel")).size()>0){
				// Logger
				TestLogger.log(LogStatus.FATAL,"IB - New Change of Address option is NOT available for this user."
						+ " Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";				
			}
			
			// Wait untilChange of Address Yes Button to appear
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_MyDestails_COAYes"))));
			
			// Change the Input of Address Line 1, 2, 3 in to Arrays for Two different address
			String[] arrOneCOA123 = OneCOA123.split(",");
			String[] arrTwoCOA123 = TwoCOA123.split(",");

			int WhichAddress = WhichAddress(arrOneCOA123,arrTwoCOA123);
			
			
			// Get the Content of the Mobile number field
			String AppMobileNumber = OR.FindElement(driver, ORMap.get("IB_COA_MobileTextbox")).getAttribute("placeholder");
			// Check if Mobile Number exists, if yes check for * in the content
			if(AppMobileNumber.contains("*")){
				// Get the last 4 digits of the String which represents the numbers
				AppMobileNumber = AppMobileNumber.substring(AppMobileNumber.length() - 4);
			// Else if no Mobile already present, blank the Mobile Number string
			} else if (AppMobileNumber.contentEquals("")){
				AppMobileNumber="";
			}
			// Condition to check if First input Number is already on the profile, if yes update local variable to set # 2
			if(arrMobileNumber[0].contains(AppMobileNumber)){
				WhichNumber = 2;
			} // Condition to check if Second input Number is already on the profile, if yes update local variable to set # 1 
			else if(arrMobileNumber[1].contains(AppMobileNumber)){
				WhichNumber = 1;
			} // Condition to check if none of the two input Numbers are already on the profile, update local variable to set # 1  
			else{
				WhichNumber = 1;
			}
			
			
			// Check the local variable value which address to set, if 1 set First address
			if(WhichAddress==1){
				
/*				// Set the COA Address public variable to Address one
				setCoASetAddress(OneCOA123);*/
				
				String strFlag = SetAddressAuto(OneCOACountry, OneCOA123);
				if(strFlag.contentEquals("Fatal")){
					return "Fatal";
				}
				
				
				if(WhichNumber==1){
					// Enter Mobile Number #1
					OR.FindElement(driver, ORMap.get("IB_COA_MobileTextbox")).sendKeys(arrMobileNumber[0]);
					IBTestScripts.setCoMobileNumber(arrMobileNumber[0]);
					// Logger
					TestLogger.log(LogStatus.INFO,"IB - New Mobile Number "+arrMobileNumber[0]+" updated for the Customer");
				} else if (WhichNumber==2){
					// Enter Mobile Number #2
					OR.FindElement(driver, ORMap.get("IB_COA_MobileTextbox")).sendKeys(arrMobileNumber[1]);
					IBTestScripts.setCoMobileNumber(arrMobileNumber[1]);
					// Logger
					TestLogger.log(LogStatus.INFO,"IB - New Mobile Number "+arrMobileNumber[1]+" updated for the Customer");
				}
				
				// Click on Next Button to Navigate to Next page
				OR.FindElement(driver, ORMap.get("IB_COA_NextButton")).click();

			// Logger to update Change of Address is updated
				TestLogger.log(LogStatus.INFO,"IB - New Address was updated to "+getCoASetAddress());
				//TestLogger.log(LogStatus.INFO,"New Address was updated to "+OneCOA123+" "+OneCOACountryCounty, "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			} // Check the local variable value which address to set, if 2 set Second address 
			else if(WhichAddress==2){
				
/*				// Set the COA Address public variable to Address Two
				setCoASetAddress(TwoCOA123);*/
				
				String strFlag = SetAddressAuto(TwoCOACountry, TwoCOA123);
				if(strFlag.contentEquals("Fatal")){
					return "Fatal";
				}				
				
				if(WhichNumber==1){
					// Enter Mobile Number #1
					OR.FindElement(driver, ORMap.get("IB_COA_MobileTextbox")).sendKeys(arrMobileNumber[0]);
					IBTestScripts.setCoMobileNumber(arrMobileNumber[0]);
					// Logger
					TestLogger.log(LogStatus.INFO,"IB - New Mobile Number "+arrMobileNumber[0]+" updated for the Customer");
				} else if (WhichNumber==2){
					// Enter Mobile Number #2
					OR.FindElement(driver, ORMap.get("IB_COA_MobileTextbox")).sendKeys(arrMobileNumber[1]);
					IBTestScripts.setCoMobileNumber(arrMobileNumber[1]);
					// Logger
					TestLogger.log(LogStatus.INFO,"IB - New Mobile Number "+arrMobileNumber[1]+" updated for the Customer");
				}
				
				// Click on Next Button to Navigate to Next page
				OR.FindElement(driver, ORMap.get("IB_COA_NextButton")).click();
			// Logger to update Change of Address is updated
				TestLogger.log(LogStatus.INFO,"IB - New Address was updated to "+getCoASetAddress());
				//TestLogger.log(LogStatus.INFO,"New Address was updated to "+TwoCOA123+" "+TwoCOACountryCounty, "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			}
		
		// Wait until WebElement is visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton"))));
		// Logger
		TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update First Confirm page");
		//TestLogger.log(LogStatus.INFO,"New Address update First Confirm page", "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_COAConfirm12"))));
		// Click on Next button on COA First Confirm page
		OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton")).click();
		// Wait until WebElement is visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton"))));
		// Logger
		TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update Second Confirm page");
		//TestLogger.log(LogStatus.INFO,"New Address update Second Confirm page", "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
		// Click on Next button on COA Second Confirm page		
		OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton")).click();
		Thread.sleep(1000);
		
		if(OR.FindElements(driver, ORMap.get("IB_COAConfirm_CodeCardInput")).size()>0){			
			// Wait until WebElement is visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput"))));
			// Update the Card Code on COA Final Confirm page
			OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput")).sendKeys(COACardCode);
			// Logger
			TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update Final Confirm page");
			//TestLogger.log(LogStatus.INFO,"New Address update Final Confirm page", "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_COAFormGeneric"))));
		} else if(OR.FindElements(driver, ORMap.get("IB_COAConfirm1_NextButton")).size()>0){
			OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton")).click();
			// Wait until WebElement is visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput"))));
			// Update the Card Code on COA Final Confirm page
			OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput")).sendKeys(COACardCode);
			// Logger
			TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update Final Confirm page");
		}
		
		// Click on Confirm Button
		OR.FindElement(driver, ORMap.get("IB_COAConfirm_ConfirmButton")).click();	
		// Wait until WebElement is visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm_MyAccountButton"))));
		if(OR.FindElements(driver, ORMap.get("IB_COAConfirm_ConfirmMessage")).size()==1
				&& OR.FindElements(driver, ORMap.get("IB_COAConfirm_ConfirmAddress")).size()==1
				&& OR.FindElements(driver, ORMap.get("IB_COAConfirm_ConfirmMobile")).size()==1){
			// Logger
			TestLogger.log(LogStatus.PASS,"IB - New Address update request submitted successfully. Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_COAConfirmation"))));		
		} else{
			// Logger
			TestLogger.log(LogStatus.FATAL,"IB - New Address update request was NOT submitted successfully. Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
			
		} catch(Exception e){
			e.printStackTrace();
			// Logger for navigating to Internet Banking Landing page 
			TestLogger.log(LogStatus.FATAL,"IB - New Change of Address could not be set and has errors: Error Desceiption - "+e.toString()
			+". Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}

	return "Pass"; 
	}
	
/* Method: ValidateCoAEligibilityMessage will 
 * 1. Check the Error Message on the Change of Address page
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Input Param: 1. String RegNumber
 * 				2. String ErrorMessage
 * 
 * Created by: Nikhil Katare
 * On: 05/Dec/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
	public String ValidateCoAEligibilityMessage(String RegNumber,String ErrorMessage) throws IOException {
		
		try{
			
			
			if(OR.FindElements(driver, ORMap.get("IB_MyDestails_NoCOAMsgPanel")).size()>0){
				
				
				if(OR.FindElement(driver, ORMap.get("IB_MyDestails_NoCOAMsgPanel")).getText().toUpperCase().trim()
						.contentEquals(ErrorMessage.toUpperCase().trim())){
					TestLogger.log(LogStatus.PASS,"IB - COA Eligibility Message for user "+RegNumber+" is validated as: "+ErrorMessage
							+ " Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_MyDestails_NoCOAMsgPanel"))));
				} else {
				
				// Logger
				TestLogger.log(LogStatus.FAIL,"IB - COA Eligibility Message for user "+RegNumber+" validation has Failed. Expected Message: "+ErrorMessage
						+". Actual Message: "+OR.FindElement(driver, ORMap.get("IB_MyDestails_NoCOAMsgPanel")).getText()+"."
						+ " Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_MyDestails_NoCOAMsgPanel"))));				
				}
			} else {
				// Logger
				TestLogger.log(LogStatus.FATAL,"IB - COA Eligibility Message was not Found."
						+ " Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";		
			}
			
		} catch(Exception e){
			e.printStackTrace();
			// Logger for navigating to Internet Banking Landing page 
			TestLogger.log(LogStatus.FATAL, "IB - COA Eligibility Message validation was caught with errors: Error Description - "+e.toString(), "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}

	return "Pass"; 
	}

/* Method: SubmitCOAnValidateDIRTnAccountList will 
 * 1. Do same as SetnSubmitChangeOfAddress and in addition -
 * 2. Validate the Account list in the profile on COA Account List page
 * 3. Validate the DIRT message for Irish and Non-Irish Address change.
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Input Param: 1. String OneCOA123
 * 				2. String OneCOACountryCounty
 * 				3. String TwoCOA123
 * 				4. String TwoCOACountryCounty
 * 				5. String MobileNumbers
 * 				6. String COACardCode
 * 				7. String DIRTMsg
 * 				8. String AccountList
 * 
 * Created by: Nikhil Katare
 * On: 08/Dec/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/		
	public String SubmitCOAnValidateDIRTnAccountList(String OneCOA123,String OneCOACountry, 
			String TwoCOA123, String TwoCOACountry, String MobileNumbers, 
			String COACardCode, String DIRTMsg, String AccountList) throws IOException {
		
		try{
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			
			if(OR.FindElements(driver, ORMap.get("IB_MyDestails_NoCOAMsgPanel")).size()>0){
				// Logger
				TestLogger.log(LogStatus.FATAL,"IB - New Change of Address option is available for this user."
						+ " Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";
				
			}
			
			// Wait untilChange of Address Yes Button to appear
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_MyDestails_COAYes"))));
			
			// Change the Input of Address Line 1, 2, 3 in to Arrays for Two different address
			String[] arrOneCOA123 = OneCOA123.split(",");
			String[] arrTwoCOA123 = TwoCOA123.split(",");
			// Initialize a local variable to set which address to update out of two input
			int WhichAddress = WhichAddress(arrOneCOA123,arrTwoCOA123);
			
			// Check the local variable value which address to set, if 1 set First address
			if(WhichAddress==1){
				
				String strFlag = SetAddressAuto(OneCOACountry, OneCOA123);
				if(strFlag.contentEquals("Fatal")){
					return "Fatal";
				}

				// Click on Next Button to Navigate to Next page
				OR.FindElement(driver, ORMap.get("IB_COA_NextButton")).click();
				// Handle if Mobile number is not present on the profile, set the number and go Next
				if(OR.FindElements(driver, ORMap.get("IB_COA_MobileNumberError")).size()>0){
					OR.FindElement(driver, ORMap.get("IB_COA_MobileTextbox")).sendKeys(MobileNumbers.split(",")[0]);
					OR.FindElement(driver, ORMap.get("IB_COA_NextButton")).click();
				}
			// Logger to update Change of Address is updated
				TestLogger.log(LogStatus.INFO,"IB - New Address was updated to "+getCoASetAddress());
				//TestLogger.log(LogStatus.INFO,"New Address was updated to "+OneCOA123+" "+OneCOACountryCounty, "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			} // Check the local variable value which address to set, if 2 set Second address 
			else if(WhichAddress==2){
				
				String strFlag = SetAddressAuto(TwoCOACountry, TwoCOA123);
				if(strFlag.contentEquals("Fatal")){
					return "Fatal";
				}
				
				// Click on Next Button to Navigate to Next page
				OR.FindElement(driver, ORMap.get("IB_COA_NextButton")).click();
				// Handle if Mobile number is not present on the profile, set the number and go Next				
				if(OR.FindElements(driver, ORMap.get("IB_COA_MobileNumberError")).size()>0){
					OR.FindElement(driver, ORMap.get("IB_COA_MobileTextbox")).sendKeys(MobileNumbers.split(",")[0]);
					OR.FindElement(driver, ORMap.get("IB_COA_NextButton")).click();
				}
			// Logger to update Change of Address is updated
				TestLogger.log(LogStatus.INFO,"IB - New Address was updated to "+getCoASetAddress());
				//TestLogger.log(LogStatus.INFO,"New Address was updated to "+TwoCOA123+" "+TwoCOACountryCounty, "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			}
		
		// Wait until WebElement is visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton"))));
		
		if(OR.FindElements(driver, ORMap.get("IB_COAConfirm_AccountTable")).size()>0){
			String AccStatus = null;
			String[] arrAccountList = null;
			if(AccountList.contains(",")){			
				arrAccountList = AccountList.split(",");
			} else{
				arrAccountList= new String[1];
				arrAccountList[0]=AccountList;
			}
			
			List<WebElement> AccountListRows = driver.findElements(By.xpath(ORMap.get("IB_COAConfirm_AccountTable")[1]+"/descendant::li"));
			
			for(String aAccountList:arrAccountList){
				AccStatus="";
				for(WebElement AccountListRow:AccountListRows){
					if(aAccountList.toUpperCase().trim().contentEquals(AccountListRow.getText().toUpperCase().trim())){
						AccStatus = "Pass";
						break;
					} 
				}
				if(!(AccStatus.contentEquals("Pass"))){
					// Logger
					TestLogger.log(LogStatus.FAIL,"IB - Account: "+aAccountList+" in the list of Accounts was not found");
				break;
				}
			}
			if(AccStatus.contentEquals("Pass")){
				// Logger
				TestLogger.log(LogStatus.PASS,"IB - All the Accounts in the Accounts List were found: "+AccountList+". Screenshot - "
						+ ""+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_COAConfirm_AccountTable"))));
			}
		} else {
			// Logger
			TestLogger.log(LogStatus.FATAL,"IB - Accounts List could not be found on the page. Screenshot - "
					+ ""+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
				
		// Logger
		TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update First Confirm page");
		//TestLogger.log(LogStatus.INFO,"New Address update First Confirm page", "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_COAConfirm12"))));
		// Click on Next button on COA First Confirm page
		OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton")).click();
		// Wait until WebElement is visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton"))));		
		
		// Logger
		TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update Second Confirm page");
		
		if(OR.FindElements(driver, ORMap.get("IB_COAConfirm_AddAccOnline")).size()>0){
			OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton")).click();
		}
		// Wait until WebElement is visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton"))));
		
		// DIRT Message validation
		if(DIRTMsg.contains("DIRT")){
			if(OR.FindElements(driver, ORMap.get("IB_COAConfirm_DIRTLabel")).size()>0){
				if(OR.FindElement(driver, ORMap.get("IB_COAConfirm_DIRTLabel")).getText().toUpperCase().trim()
						.contentEquals(DIRTMsg.toUpperCase().trim())){
					// Logger 
					TestLogger.log(LogStatus.PASS,"IB - DIRT Message was validated for COA request. Screenshot - "
							+ ""+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_COAConfirm_DIRTLabel"))));				
				}else {
					// Logger 
					TestLogger.log(LogStatus.FAIL,"IB - DIRT Message was Not correct for COA request. Expected Message: "
							+ DIRTMsg+". Actual Message: "+OR.FindElement(driver, ORMap.get("IB_COAConfirm_DIRTLabel")).getText()+". Screenshot - "
							+ Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_COAConfirm_DIRTLabel"))));	
				}			
			} else {
				// Logger 
				TestLogger.log(LogStatus.FAIL,"IB - DIRT Message was Not present for COA request. Screenshot - "
						+ Screenshot.ObjectSnapFullPage(driver));
			}
		}
		
		//TestLogger.log(LogStatus.INFO,"New Address update Second Confirm page", "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
		// Click on Next button on COA Second Confirm page		
		OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton")).click();
		Thread.sleep(1000);
		
		if(OR.FindElements(driver, ORMap.get("IB_COAConfirm_CodeCardInput")).size()>0){			
			// Wait until WebElement is visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput"))));
			// Update the Card Code on COA Final Confirm page
			OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput")).sendKeys(COACardCode);
			// Logger
			TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update Final Confirm page");
			//TestLogger.log(LogStatus.INFO,"New Address update Final Confirm page", "Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_COAFormGeneric"))));
		} else if(OR.FindElements(driver, ORMap.get("IB_COAConfirm1_NextButton")).size()>0){
			OR.FindElement(driver, ORMap.get("IB_COAConfirm1_NextButton")).click();
			// Wait until WebElement is visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput"))));
			// Update the Card Code on COA Final Confirm page
			OR.FindElement(driver, ORMap.get("IB_COAConfirm_CodeCardInput")).sendKeys(COACardCode);
			// Logger
			TestLogger.log(LogStatus.INFO,"IB - Navigated to New Address update Final Confirm page");
		}
		
		// Click on Confirm Button
		OR.FindElement(driver, ORMap.get("IB_COAConfirm_ConfirmButton")).click();	
		// Wait until WebElement is visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COAConfirm_MyAccountButton"))));
		if(OR.FindElements(driver, ORMap.get("IB_COAConfirm_ConfirmMessage")).size()==1
				&& OR.FindElements(driver, ORMap.get("IB_COAConfirm_ConfirmAddress")).size()==1){
			// Logger
			TestLogger.log(LogStatus.PASS,"IB - New Address update request submitted successfully. Screenshot - "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("IB_ScSh_COAConfirmation"))));		
		} else{
			// Logger
			TestLogger.log(LogStatus.FATAL,"IB - New Address update request was NOT submitted successfully. Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
			
		} catch(Exception e){
			e.printStackTrace();
			// Logger for navigating to Internet Banking Landing page 
			TestLogger.log(LogStatus.FATAL,"IB - New Change of Address could not be set and has errors: Error Desceiption - "+e.toString()+". Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}

	return "Pass"; 
}
	
	
	private String SetAddressAuto(String COACountry, String COALineAddresses) throws IOException{
		
		try{
			
			// Click on Yes button for Change of Address
			OR.FindElement(driver, ORMap.get("IB_MyDestails_COAYes")).click();
			
			// Check if Input Country is IE or GB, if yes Enter the Line Address 1,2,3,4 and Post Code 
			if(COACountry.toUpperCase().contains("IE")){
				String Flag = AutoAddressConditions(COACountry, COALineAddresses);
				if(Flag.toUpperCase().contentEquals("FATAL")){
					TestLogger.log(LogStatus.FATAL,"IB - Address was not filled in the Fields properly."
							+ "Exepcted: "+COALineAddresses+","+COACountry+". Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
					return "Fatal";
				}
			}else if(COACountry.toUpperCase().contains("GB")){

				String Flag = AutoAddressConditions(COACountry, COALineAddresses);
				if(Flag.toUpperCase().contentEquals("FATAL")){
					TestLogger.log(LogStatus.FATAL,"IB - Address was not filled in the Fields properly."
							+ "Exepcted: "+COALineAddresses+","+COACountry+". Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
					return "Fatal";
				}
				
			}// If Country input isn't IE or GB Enter address 1,2,3 and Post Code
			else {

				String ManualAddStatus = SetAddressManual(COACountry, COALineAddresses);
				if(ManualAddStatus.toUpperCase().contentEquals("FATAL")){
					TestLogger.log(LogStatus.FATAL,"IB - Address was not filled in the Fields properly."
							+ "Exepcted: "+COALineAddresses+","+COACountry+". Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
					return "Fatal";
				}
			}	
		}
		catch(Exception e){
			e.printStackTrace();
			// Logger
			TestLogger.log(LogStatus.FATAL,"IB - New Change of Address could not be set and has errors: Error Description - "+e.toString()+ ". Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
		return "Pass";
	}
	
	private String SetAddressManual(String COACountry, String COALineAddresses) throws IOException{
		
		try{
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);

			String[] arrCOALineAddresses = COALineAddresses.split(",");
			
			// Check if Input Country is IE or GB, if yes Enter the Line Address 1,2,3,4 and Post Code 
			if(COACountry.toUpperCase().contains("IE")){
				
				String[] arrCOACountry = COACountry.split(",");
				
				// Wait until County field appears
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COA_AddCountry"))));
				
				Select SelectIRLCounty = new Select(OR.FindElement(driver, ORMap.get("IB_COA_IrelandCounty")));
				SelectIRLCounty.selectByValue(arrCOACountry[1]);
				
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COA_AddLine1"))));
				// Set Line Address 1
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine1")).sendKeys(arrCOALineAddresses[0]);
				Thread.sleep(500);
				// Set Line Address 2
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine2")).sendKeys(arrCOALineAddresses[1]);
				Thread.sleep(500);
				// Set Line Address 3
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine3")).sendKeys(arrCOALineAddresses[2]);
				Thread.sleep(500);
				
				setCoASetAddress(arrCOALineAddresses[0]+","+arrCOALineAddresses[1]
						+","+arrCOALineAddresses[2]+","+arrCOACountry[1]);

			} else {
				// Wait until County field appears
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COA_AddCountry"))));
				// Select County of Ireland
				Select SelectIRLCounty = new Select(OR.FindElement(driver, ORMap.get("IB_COA_AddCountry")));
				SelectIRLCounty.selectByValue(COACountry);
				
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COA_AddLine1"))));
				// Set Line Address 1
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine1")).sendKeys(arrCOALineAddresses[0]);
				Thread.sleep(500);
				// Set Line Address 2
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine2")).sendKeys(arrCOALineAddresses[1]);
				Thread.sleep(500);
				// Set Line Address 3
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine3")).sendKeys(arrCOALineAddresses[2]);
				Thread.sleep(500);
				// Set Post Code
				OR.FindElement(driver, ORMap.get("IB_COA_AddPostCode")).sendKeys(arrCOALineAddresses[3]);
				Thread.sleep(500);
				
				setCoASetAddress(arrCOALineAddresses[0]+","+arrCOALineAddresses[1]
						+","+arrCOALineAddresses[2]);
					// Click on Next Button to Navigate to Next page
					//OR.FindElement(driver, ORMap.get("IB_COA_NextButton")).click();
				
			}	
		}
		catch(Exception e){
			e.printStackTrace();
			// Logger for navigating to Internet Banking Landing page 
			TestLogger.log(LogStatus.FATAL,"IB - New Change of Address could not be set and has errors: Error Desceiption - "+e.toString()+ ". Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
			return "Fatal";
		}
		
		return "Pass";
	}
	
	private Integer WhichAddress(String[] arrOneCOA123,String[] arrTwoCOA123){
		
		int whichAddress;
		// Condition to check if First input address is already on the profile, if yes update local variable to set Add 2
			if(arrOneCOA123[0].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line1")).getText())
				&& arrOneCOA123[1].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line2")).getText())
				&& arrOneCOA123[2].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line3")).getText())
				//&& arrOneCOA123[3].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line4")).getText())
				//&& arrOneCOA123[4].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_PostCode")).getText())
				){
				whichAddress = 2;
			} // Condition to check if Second input address is already on the profile, if yes update local variable to set Add 1 
			else if(arrTwoCOA123[0].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line1")).getText())
					&& arrTwoCOA123[1].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line2")).getText())
					&& arrTwoCOA123[2].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line3")).getText())
					//&& arrTwoCOA123[3].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_Line4")).getText())
					//&& arrTwoCOA123[4].toUpperCase().contentEquals(OR.FindElement(driver, ORMap.get("IB_CurrentAdd_PostCode")).getText())
					){
				whichAddress = 1;
			} // Condition to check if none of the two input address are already on the profile, update local variable to set Add 1  
			else{
				whichAddress = 1;
			
		}
		
		return whichAddress;
	}
	
	private String AutoAddressConditions(String COACountry, String COALineAddresses) throws IOException, InterruptedException{
		
		// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		String[] arrCOALineAddresses = COALineAddresses.split(",");
		String[] arrCOACountry = COACountry.split(",");

		// Wait until County field appears
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COA_AddCountry"))));
		// Select County of Ireland
		Select SelectIRLCounty = new Select(OR.FindElement(driver, ORMap.get("IB_COA_AddCountry")));
		SelectIRLCounty.selectByValue(arrCOACountry[0]);
		
		Thread.sleep(500);
		if(OR.FindElements(driver, ORMap.get("IB_COA_AutoAddBar")).size()>0){
				wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("IB_COA_AutoAddTextBox"))));
				OR.FindElement(driver, ORMap.get("IB_COA_AutoAddTextBox")).sendKeys(COALineAddresses);
				Thread.sleep(500);
				OR.FindElement(driver, ORMap.get("IB_COA_AutoAddSearchButton")).click();
				int Cntr = 0;
				while(!(OR.FindElements(driver, ORMap.get("IB_COA_AutoAddMsg")).size()>0) && Cntr<=20){
					Thread.sleep(1000);
					Cntr++;
				}
				// Check Auto Address Response message appears
				if(OR.FindElements(driver, ORMap.get("IB_COA_AutoAddMsg")).size()>0){
					if(OR.FindElement(driver, ORMap.get("IB_COA_AutoAddMsg")).getText().toUpperCase()
							.contains("NO SEARCH RESULTS")){
						TestLogger.log(LogStatus.FATAL,"IB - Unique Auto Address "+COALineAddresses+", "+arrCOACountry[0]+" could not be found. Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
						return "Fatal";
						
					} else if(OR.FindElement(driver, ORMap.get("IB_COA_AutoAddMsg")).getText().toUpperCase()
							.contains("POSTCODE FOUND")){
						if(!(OR.FindElement(driver, ORMap.get("IB_COA_AddLine1")).getAttribute("value").toUpperCase().trim()
							.contentEquals(arrCOALineAddresses[0].toUpperCase()) && 
							OR.FindElement(driver, ORMap.get("IB_COA_AddLine2")).getAttribute("value").toUpperCase().trim()
							.contentEquals(arrCOALineAddresses[1].toUpperCase()) &&
							OR.FindElement(driver, ORMap.get("IB_COA_AddLine3")).getAttribute("value").toUpperCase().trim()
							.contentEquals(arrCOALineAddresses[2].toUpperCase()) &&
							OR.FindElement(driver, ORMap.get("IB_COA_AddLine4")).getAttribute("value").toUpperCase().trim()
							.contentEquals(arrCOALineAddresses[3].toUpperCase()) &&
							OR.FindElement(driver, ORMap.get("IB_COA_AddPostCode")).getAttribute("value").toUpperCase().trim()
							.contentEquals(arrCOALineAddresses[4].toUpperCase()))){
							TestLogger.log(LogStatus.FATAL,"IB - Auto Address "+COALineAddresses+" was not filled in the Fields properly. Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
							return "Fatal";
						} else{
							// Set the COA Address public variable to Address
							setCoASetAddress(COALineAddresses);
						}
					} else if(OR.FindElement(driver, ORMap.get("IB_COA_AutoAddMsg")).getText().toUpperCase()
							.contains("PLEASE SELECT AN OPTION BELOW")){
						List<WebElement>AutoOptionsElements = OR.FindElements(driver, ORMap.get("IB_COA_AutoAddOptions"));
						for(WebElement AutoOptionsElement:AutoOptionsElements){
							String AutoAddText=AutoOptionsElement.getText();
							if(AutoAddText.contains(",")){
								String[] arrAutoAddText=AutoAddText.split(",");
								if(arrAutoAddText[0].toUpperCase().trim().contentEquals(arrCOALineAddresses[0].toUpperCase()) && 
										arrAutoAddText[1].toUpperCase().trim().contentEquals(arrCOALineAddresses[1].toUpperCase())){
									AutoOptionsElement.click();
									if(OR.FindElement(driver, ORMap.get("IB_COA_AutoAddMsg")).getText().toUpperCase()
											.contains("POSTCODE FOUND")){
										if(!(OR.FindElement(driver, ORMap.get("IB_COA_AddLine1")).getAttribute("value").toUpperCase().trim()
											.contentEquals(arrCOALineAddresses[0].toUpperCase()) && 
											OR.FindElement(driver, ORMap.get("IB_COA_AddLine2")).getAttribute("value").toUpperCase().trim()
											.contentEquals(arrCOALineAddresses[1].toUpperCase()) &&
											OR.FindElement(driver, ORMap.get("IB_COA_AddLine3")).getAttribute("value").toUpperCase().trim()
											.contentEquals(arrCOALineAddresses[2].toUpperCase()) &&
											OR.FindElement(driver, ORMap.get("IB_COA_AddLine4")).getAttribute("value").toUpperCase().trim()
											.contentEquals(arrCOALineAddresses[3].toUpperCase()) &&
											OR.FindElement(driver, ORMap.get("IB_COA_AddPostCode")).getAttribute("value").toUpperCase().trim()
											.contentEquals(arrCOALineAddresses[4].toUpperCase()))){
											String PostCode = OR.FindElement(driver, ORMap.get("IB_COA_AddPostCode")).getAttribute("value");
											TestLogger.log(LogStatus.FATAL,"IB - Auto Address was not filled in the Fields properly."
													+ "Exepcted: "+COALineAddresses+","+PostCode+". Actual: "+AutoAddText+". Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
											return "Fatal";
										} else {
											// Set the COA Address public variable to Address
											setCoASetAddress(COALineAddresses);
										}
									break;
									}								
								}
							}
						}
					} 
			} else if (!(OR.FindElements(driver, ORMap.get("IB_COA_AutoAddMsg")).size()>0) && 
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine1")).getAttribute("value").toUpperCase().trim()
				.contentEquals(arrCOALineAddresses[0].toUpperCase()) && 
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine2")).getAttribute("value").toUpperCase().trim()
				.contentEquals(arrCOALineAddresses[1].toUpperCase()) &&
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine3")).getAttribute("value").toUpperCase().trim()
				.contentEquals(arrCOALineAddresses[2].toUpperCase()) &&
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine4")).getAttribute("value").toUpperCase().trim()
				.contentEquals(arrCOALineAddresses[3].toUpperCase()) &&
				OR.FindElement(driver, ORMap.get("IB_COA_AddPostCode")).getAttribute("value").toUpperCase().trim()
				.contentEquals(arrCOALineAddresses[4].toUpperCase())){
						// Set the COA Address public variable to Address
						setCoASetAddress(COALineAddresses);
			} else{
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine1")).sendKeys(arrCOALineAddresses[0]); 
				Thread.sleep(500);
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine2")).sendKeys(arrCOALineAddresses[1]);
				Thread.sleep(500);
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine3")).sendKeys(arrCOALineAddresses[2]);
				Thread.sleep(500);
				OR.FindElement(driver, ORMap.get("IB_COA_AddLine4")).sendKeys(arrCOALineAddresses[3]);
				Thread.sleep(500);
				OR.FindElement(driver, ORMap.get("IB_COA_AddPostCode")).sendKeys(arrCOALineAddresses[4]);
				// Set the COA Address public variable to Address
				setCoASetAddress(COALineAddresses);
				TestLogger.log(LogStatus.FAIL,"IB - Auto Address did not pull the address in 20 Seconds, Entered Address: "+COALineAddresses+" Manually. "
						+ "Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
			}
		} else {
			if(COACountry.toUpperCase().trim().contains("IE")){
				TestLogger.log(LogStatus.FAIL,"IB - Auto Address option was not available, Entered Address: "+COALineAddresses+" Manually. "
						+ "Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
			}
				String ManualAddStatus = SetAddressManual(COACountry, COALineAddresses);
				if(ManualAddStatus.toUpperCase().contentEquals("FATAL")){
					TestLogger.log(LogStatus.FATAL,"IB - Address was not filled in the Fields properly."
							+ "Exepcted: "+COALineAddresses+","+COACountry+". Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
					return "Fatal";
				}
			}
		return "Pass";
	}
}
