package javaSeedTestScripts.bpm;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import javaSeed.constants.Const;
import javaSeed.objectRepository.OR;
import javaSeed.utils.Screenshot;
import javaSeedTestScripts.TestConsts;

public class BPMTestScripts {

	private WebDriver driver = Const.driver;
	private HashMap<String, String[]> ORMap = Const.ORMap;
	private ExtentTest TestLogger = Const.etTestCases;

	//#####################################################################
	// Global Variables used in capture data for text execution
	public static String BPMTaskRequestID=null; // This Variable was set in ClaimBPMTask method
	public static String getBPMTaskRequestID() {	// This Get method was called in ReActivateTask method
		return BPMTaskRequestID;
	}
	//#####################################################################
	


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
	public String NavigateLoginToBPM(String UserName, String Password) throws IOException{
		try{
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			
			// Navigate to the BPM Url			
			driver.get(TestConsts.BPM_URL);
			
			// Wait until Element becomes visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_LoginWindowContent"))));
			
	        // Logger
			TestLogger.log(LogStatus.INFO, "BPM - Navigate to BPM Portal successful. URL - "+TestConsts.BPM_URL);
			/*TestLogger.log(LogStatus.INFO, "Navigate to BPM Portal successful"
					,"Screenshot: "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("BPM_LoginWindowContent"))));*/
		
	        // Maximize the Browser
			driver.manage().window().maximize();
			// Condition to check the page gets loaded by validating UserName edit box is present on the page. If True proceed -
			if (OR.FindElements(driver, ORMap.get("BPM_UserName")).size() != 0){
				
				// Put the Username, Password and submit the page
				OR.FindElement(driver, ORMap.get("BPM_UserName")).sendKeys(UserName);
				OR.FindElement(driver, ORMap.get("BPM_Password")).sendKeys(Password);
				OR.FindElement(driver, ORMap.get("BPM_LoginButton")).submit();
				
				Thread.sleep(3000);
			}
			
			// Condition to check if the user logged in correctly, if not take the screenshot and return Fatal
			if (OR.FindElements(driver, ORMap.get("BPM_LoginFailedMsg")).size() != 0){
		        // Logger
				TestLogger.log(LogStatus.FATAL, "BPM - Login Failed To BPM url with user - "+UserName+". Loggin Failed Screenshot: "
						+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("BPM_LoginFrame"))));
				return "Fatal";
				
			// Else user Logged in successfully	
			} else {
				TestLogger.log(LogStatus.INFO, "BPM - Login successful To BPM url with user - "+UserName);
				/*TestLogger.log(LogStatus.INFO, "Login successful To BPM url with user - "+UserName
						,"Screenshot"+Screenshot.ObjectSnapFullPage(driver));*/
			}

		} // Catch any other unexpected exception, and return Fatal if true
		catch(Throwable t){
			if(!t.equals(null)){
				TestLogger.log(LogStatus.FATAL, "BPM - Error while Navigation To BPM url - "+TestConsts.BPM_URL+" : Error Description -- "+t.toString()
						+". Screenshot"+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";
			}
		} // If all good, return 'Pass'
		return "Pass";
	}

/* Method: LogoutBPM will 
 * 1. Logout of the BPM application
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: None
 * 
 * Created by: Nikhil Katare
 * On: 02/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
	public String LogoutBPM() throws InterruptedException, IOException{
		
		try{ 
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			Thread.sleep(1000);
			// Wait until Element becomes visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_LogoutDropdown"))));
			
			// Click on the Dropdown to make Logout link visible. 
			OR.FindElement(driver, ORMap.get("BPM_LogoutDropdown")).click();
			Thread.sleep(1000);
			
			// Wait until Element becomes visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_LogoutButton"))));
			// Click on Logout Button
			OR.FindElement(driver, ORMap.get("BPM_LogoutButton")).click();
			Thread.sleep(500);
			// Wait until Element becomes visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_LoginWindowContent"))));
			
	        // Logger
			TestLogger.log(LogStatus.INFO, "BPM - Logged out of BPM Portal successfully");
			/*TestLogger.log(LogStatus.INFO, "Logged out of BPM Portal successfully"
					,"Screenshot: "+Screenshot.ObjectSnap(driver, OR.FindElement(driver, ORMap.get("BPM_LoginWindowContent"))));*/

		}   // Catch any other unexpected exception, and return Fatal if true
		catch(Exception e){
	        // Logger
			TestLogger.log(LogStatus.ERROR, "BPM - Error while Logging out of BPM"+e.toString()
					+". Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
		}
		return "Pass";
	}

/* Method: ClaimBPMTask will 
 * 1. Search all the tasks present on the task summary page and validate it with COA Processed customer Name
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String Customer Name
 * 
 * Created by: Nikhil Katare
 * On: 02/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
	public String ClaimBPMTask(String CustName) throws InterruptedException, IOException{
		
		try{
		
			int TaskAvailableLoop = 0;
			do{
					
				while(OR.FindElements(driver, ORMap.get("BPM_Loading")).size()>0){
					Thread.sleep(1000);
				}
				
				// Enter Customer Name in the Search box
				OR.FindElement(driver, ORMap.get("BPM_TaskSearchBox")).clear();
				OR.FindElement(driver, ORMap.get("BPM_TaskSearchBox")).sendKeys(CustName);
				// Press Search button
				OR.FindElement(driver, ORMap.get("BPM_TaskSearchButton")).click();			
				Thread.sleep(1000);
				
				while(OR.FindElements(driver, ORMap.get("BPM_Loading")).size()>0){
					Thread.sleep(1000);
				}
						
					// Get the number of Tasks present on the My Tasks page
						List<WebElement> ListOfTasks = OR.FindElements(driver, ORMap.get("BPM_TaskRowGeneric"));
						// Condition to see if one of more tasks are present on the page to proceed further
						if(ListOfTasks.size()>0){
							// Run the loop to navigate through all the tasks one by one
							for (WebElement ListOfTask:ListOfTasks){					
									// Click on each task to expand it to see Task details
									
									ListOfTask.click();
									Thread.sleep(1000);
														
									// Get the number of Tasks Details Parameters
									List<WebElement> TaskDetailsCols = driver.findElements(By.xpath("//div[@id='"+ListOfTask.getAttribute("id")+"']"+ORMap.get("BPM_TaskDetailsPartial")[1]));
									String TaskDeatilsRow = driver.findElement(By.xpath("//div[@id='"+ListOfTask.getAttribute("id")+"']"+ORMap.get("BPM_TaskDetailsRow")[1])).getText();
									
									while(TaskDetailsCols.size()==0){
										Thread.sleep(1000);
										TaskDetailsCols = driver.findElements(By.xpath("//div[@id='"+ListOfTask.getAttribute("id")+"']"+ORMap.get("BPM_TaskDetailsPartial")[1]));
									}
									
												
									// Condition to check if the number of task Parameters are Max 5 (which means the task was not yet claimed) to proceed, 
										// If more then task was already Claimed
									if (TaskDetailsCols.size() <= 4){ //################################## Changes Needed from 6 to 4
										// Get the Name of the Customer for which the Task was created							
											if(TaskDeatilsRow.contains(CustName)){
												// Capture Screenshot
												TestLogger.log(LogStatus.INFO,"BPM - BPM Task for Customer's COA was identified");
												//TestLogger.log(LogStatus.INFO,"BPM Task for Customer's COA was identified", "Screenshot: "+Screenshot.ObjectSnap(driver, ListOfTask));
											} else{
												// Capture Screenshot
												TestLogger.log(LogStatus.FAIL,"BPM - BPM Task for Customer's COA could not be found");
												//TestLogger.log(LogStatus.FAIL,"BPM Task for Customer's COA could not be found", "Screenshot: "+Screenshot.ObjectSnap(driver,ListOfTask));
											}
											// Get Request ID data from the page
											String TaskReqIDColumnString = TaskDetailsCols.get(3).getText();
											// Split String to get the actual Request ID
											String[] arrTaskReqIDColumnString = TaskReqIDColumnString.split(":");
									// Capture BPM Request ID
									BPMTaskRequestID = arrTaskReqIDColumnString[1].trim();
									// Click on the Task to Claim it
									driver.findElement(By.xpath("//div[@id='"+ListOfTask.getAttribute("id")+"']"+ORMap.get("BPM_TaskLinkPartial")[1])).click();
									Thread.sleep(1000);
									// Verify that the Claim button is present on the page to be actioned
									if(OR.FindElements(driver, ORMap.get("BPM_TaskClaimButton")).size()>0){
										OR.FindElement(driver, ORMap.get("BPM_TaskClaimButton")).click();
										TestLogger.log(LogStatus.INFO,"BPM - "+TaskReqIDColumnString+" was Claimed");
									// If Claim button is not present, return Fatal
									} else{
										TestLogger.log(LogStatus.FATAL,"BPM - Claim button was not available, Task cannot be Claimed");
										return "Fatal";									
									}
								// Task was picked and actioned, Exit function
								return "Pass";							
								}
									// Collapse the Task Details
									ListOfTask.click();
							} // Loop again for another Task
							
							Thread.sleep(10000);
							// Increment Wait Count
							TaskAvailableLoop++;
						} else if(ListOfTasks.size()>0 && TaskAvailableLoop==9){
							TestLogger.log(LogStatus.FAIL,"BPM - BPM Task for Customer's COA could not be found"
									+ ". Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
							return "Fatal";	
						}
							
						// Verify if Task is still not present, wait for 10 seconds
						if(TaskAvailableLoop==9 && ListOfTasks.size()==0){
							TestLogger.log(LogStatus.FATAL,"BPM - Appropriate Task did not appear in the Task List. "
									+ "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
							return "Fatal";
						} else if (TaskAvailableLoop<9 && ListOfTasks.size()==0) {
							Thread.sleep(10000);
							TaskAvailableLoop++;
						}
					
				} while (TaskAvailableLoop<=9); // End of While		

			
			// Catch any other unexpected exception, and return Fatal if true
		}
		catch(Exception e){
			if(!e.equals(null)){
				e.printStackTrace();
				TestLogger.log(LogStatus.FATAL, "BPM - Error while Claiming BPM Task. Error Description: "+e.toString()
				+". Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";
			}
		}
		return "Pass";
	}

/* Method: ClaimReactivatedBPMTask will 
 * Claim the Reactivated Task based on the condition -
 * 1. Task Detail Columns are more then 4
 * 2. Task Detail column 3 contains "Reactivated: Yes"
 * 3. Task Detail column 4 contains "Request ID" of the task (pulled from current execution)
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String Customer Name
 * 
 * Created by: Nikhil Katare
 * On: 28/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
	public String ClaimReactivatedBPMTask(String CustName) throws InterruptedException{
		
		String ReActivatedTaskID = getBPMTaskRequestID();
		
		try{
			
			int TaskAvailableLoop = 0;
			do{
			
				while(OR.FindElements(driver, ORMap.get("BPM_Loading")).size()>0){
					Thread.sleep(1000);
				}
				
				// Enter Customer Name in the Search box
				OR.FindElement(driver, ORMap.get("BPM_TaskSearchBox")).clear();
				OR.FindElement(driver, ORMap.get("BPM_TaskSearchBox")).sendKeys(ReActivatedTaskID);
				// Press Search button
				OR.FindElement(driver, ORMap.get("BPM_TaskSearchButton")).click();			
				Thread.sleep(1000);
				
				while(OR.FindElements(driver, ORMap.get("BPM_Loading")).size()>0){
					Thread.sleep(1000);
				}
						
					// Get the number of Tasks present on the My Tasks page
						List<WebElement> ListOfTasks = OR.FindElements(driver, ORMap.get("BPM_TaskRowGeneric"));
						// Condition to see if one of more tasks are present on the page to proceed further
						if(ListOfTasks.size()>0){	
							// Run the loop to navigate through all the tasks one by one
							for (WebElement ListOfTask:ListOfTasks){					
									// Click on each task to expand it to see Task details
									
									ListOfTask.click();
									Thread.sleep(1000);
														
									// Get the number of Tasks Details Parameters
									List<WebElement> TaskDetailsCols = driver.findElements(By.xpath("//div[@id='"+ListOfTask.getAttribute("id")+"']"+ORMap.get("BPM_TaskDetailsPartial")[1]));
									String TaskDeatilsRow = driver.findElement(By.xpath("//div[@id='"+ListOfTask.getAttribute("id")+"']"+ORMap.get("BPM_TaskDetailsRow")[1])).getText();
									
									while(TaskDetailsCols.size()==0){
										Thread.sleep(1000);
										TaskDetailsCols = driver.findElements(By.xpath("//div[@id='"+ListOfTask.getAttribute("id")+"']"+ORMap.get("BPM_TaskDetailsPartial")[1]));
									}
									
												
									// Condition to check if the number of task Parameters are Max 5 (which means the task was not yet claimed) to proceed, 
										// If more then task was already Claimed
									if (TaskDetailsCols.size() > 4 
											&& TaskDeatilsRow.contains("Reactivated: \nYes")
												&& TaskDeatilsRow.contains(ReActivatedTaskID)){ 
										// Get the Name of the Customer for which the Task was created							
											if(TaskDeatilsRow.contains(CustName)){
												// Capture Screenshot
												TestLogger.log(LogStatus.INFO,"BPM - Reactivated BPM Task for Customer's COA was identified");
												//TestLogger.log(LogStatus.INFO,"BPM Task for Customer's COA was identified", "Screenshot: "+Screenshot.ObjectSnap(driver, ListOfTask));
											} else{
												// Capture Screenshot
												TestLogger.log(LogStatus.FAIL,"BPM - Reactivated BPM Task for Customer's COA could not be found");
												//TestLogger.log(LogStatus.FAIL,"BPM Task for Customer's COA could not be found", "Screenshot: "+Screenshot.ObjectSnap(driver,ListOfTask));
											}
									// Click on the Task to Claim it
									driver.findElement(By.xpath("//div[@id='"+ListOfTask.getAttribute("id")+"']"+ORMap.get("BPM_TaskLinkPartial")[1])).click();
									Thread.sleep(1000);
									// Verify that the Claim button is present on the page to be actioned
									if(OR.FindElements(driver, ORMap.get("BPM_TaskClaimButton")).size()>0){
										OR.FindElement(driver, ORMap.get("BPM_TaskClaimButton")).click();
										TestLogger.log(LogStatus.INFO,"BPM - "+ReActivatedTaskID+" was Claimed");
									// If Claim button is not present, return Fatal
									} else{
										TestLogger.log(LogStatus.FATAL,"BPM - Claim button was not available, Task cannot be Claimed");
										return "Fatal";									
									}
								// Task was picked and actioned, Exit function
								return "Pass";							
								}
									// Collapse the Task Details
									ListOfTask.click();
							} // Loop again for another Task	
							
							Thread.sleep(10000);
							// Increment Wait Count
							TaskAvailableLoop++;
						} else if(ListOfTasks.size()>0 && TaskAvailableLoop==9){
							TestLogger.log(LogStatus.FAIL,"BPM - BPM Task for Customer's COA could not be found"
									+ ". Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
							return "Fatal";	
						}
	
						// Verify if Task is still not present, wait for 10 seconds
						if(TaskAvailableLoop==9 && ListOfTasks.size()==0){
							TestLogger.log(LogStatus.FATAL,"BPM - Appropriate Task did not appear in the Task List. "
									+ "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
							return "Fatal";
						} else if (TaskAvailableLoop<9 && ListOfTasks.size()==0) {
							Thread.sleep(10000);
							TaskAvailableLoop++;
						}
					
				}  while (TaskAvailableLoop<=9); // End of While		

			
			// Catch any other unexpected exception, and return Fatal if true
		}
		catch(Exception e){
			if(!e.equals(null)){
				e.printStackTrace();
				TestLogger.log(LogStatus.FATAL, "BPM - Error while Claiming Reactivated BPM Task id: "+ReActivatedTaskID+". Error Description: "+e.toString());
				return "Fatal";
			}
		}
		return "Pass";
	}	
		
/* Method: ValidateTaskStatus will 
 * 1. Validate the Task Status from the Task Detail page
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: String Task Status
 * 
 * Created by: Nikhil Katare
 * On: 07/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/	
	public String ValidateTaskStatus(String TaskStatus) throws InterruptedException, IOException{
		
		try{			
				// Switch the WebDriver Object to iFrame of the Task Detail page 
				driver.switchTo().frame(OR.FindElement(driver, ORMap.get("BPM_TaskDeatiliFrame")));
				
				// Validate the Status of the Task on the Task Detail page
				if(OR.FindElement(driver, ORMap.get("BPM_TaskDeatilTaskStatus")).getText().contentEquals(TaskStatus)){
					// Capture Screenshot
					TestLogger.log(LogStatus.PASS,"BPM - Task Status is verified as "+TaskStatus+ ". Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
				} else{
					// Capture Screenshot
					TestLogger.log(LogStatus.FAIL,"BPM - Task Status is NOT Validated as "+TaskStatus+ ". Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
				}
				// Switch WebDriver object back to Page Content
				driver.switchTo().defaultContent();
		}
		catch(Exception e){
			if(!e.equals(null)){
				e.printStackTrace();
				TestLogger.log(LogStatus.FATAL, "BPM - Error while Validating BPM Task Status on Task Detail page. Error Description: "+e.toString()+ ". Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
				
				return "Fatal";
			}
		}
		return "Pass";
	}
	
/* Method: SetPAReferenceForTask will 
 * 1. Set the PA Reference on the Task Detail page
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
	public String SetPAReferenceForTask(String PAReference) throws IOException{
		try{
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			// Wait until Element becomes visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_TaskDeatiliFrame"))));
						
			// Switch the WebDriver Object to iFrame of the Task Detail page 
			driver.switchTo().frame(OR.FindElement(driver, ORMap.get("BPM_TaskDeatiliFrame")));
			
			// Wait until Element becomes visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_TaskDetailsCustomerDetailsBlock"))));
			
			// Verify that only one Customer Block is present on the page
			if(OR.FindElements(driver, ORMap.get("BPM_TaskDetailsCustomerDetailsBlock")).size()==1){
				// Get the ID of the Customer Block 
				String CustBlockID = OR.FindElement(driver, ORMap.get("BPM_TaskDetailsCustomerDetailsBlock")).getAttribute("id");
				// Update the PA Reference Text Box
				driver.findElement(By.xpath("//div[@id='"+CustBlockID+"']"+ORMap.get("BPM_TaskDeatilPAInputBox")[1])).clear();
				driver.findElement(By.xpath("//div[@id='"+CustBlockID+"']"+ORMap.get("BPM_TaskDeatilPAInputBox")[1])).sendKeys(PAReference);
				// Capture Screenshot
				TestLogger.log(LogStatus.INFO,"BPM - PA Reference was set for the Task -"+PAReference);
			}
			else{
				TestLogger.log(LogStatus.FAIL,"BPM - PA Reference was NOT set for the Task because Customer Block could not be located. Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
			}
			driver.switchTo().defaultContent();
		} catch(Exception e){
			if(!e.equals(null)){
				e.printStackTrace();
				TestLogger.log(LogStatus.FATAL, "BPM - Error while Validating BPM Task Status on Task Detail page. Error Description: "+e.toString()+ ". Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";
			}
		}
		return "Pass";
	}

/* Method: UpdateTaskForAllAccounts will 
 * 1. Update All the Product (Accounts) to either Complete or Reject and Submit the Task
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: 	1. String TaskAction (Either Complete or Reject)
 * 					2. String Reject Reason (Used only if Tasks Rejects)
 * 					3. String Reject Comments (Used only if Tasks Rejects)
 * Created by: Nikhil Katare
 * On: 07/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/			
	public String UpdateTaskForAllAccounts(String TaskAction, String RejectReason, String RejectComment) throws IOException{
		try{
			
			// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			// Wait until Element becomes visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_TaskDeatiliFrame"))));
			
			// Switch the WebDriver Object to iFrame of the Task Detail page 
			driver.switchTo().frame(OR.FindElement(driver, ORMap.get("BPM_TaskDeatiliFrame")));
			
			// Exception handling in Case Confirmation pop-up remained open
			if(OR.FindElement(driver, ORMap.get("BPM_AccountUpdateConfirmationNo")).isDisplayed()==true){
				OR.FindElement(driver, ORMap.get("BPM_AccountUpdateConfirmationNo")).click();
			}
			
				// get the ID of the Web element of Customer Details Block
				String ProductContentBoxID = OR.FindElement(driver, ORMap.get("BPM_TaskDetailsProductsBlock")).getAttribute("id");
				
				// If the Action is to Complete all the Accounts
				if(TaskAction.toUpperCase().contentEquals("COMPLETE")){
					// Get all the list of Complete Button present in the Product Content Block
					List<WebElement> ActionButtons = driver.findElements(By.xpath("//div[@id='"+ProductContentBoxID+"']"+ORMap.get("BPM_TaskDetailsCompleteButton")[1]));

					// Loop for all the Buttons to be clicked for all Products
					for(int i=0;i<(ActionButtons.size()-1);i++){

						// Check Visibility of the Button for each product
						wait.until(ExpectedConditions.visibilityOf(ActionButtons.get(i)));
						
						// If Button is Enable
						if(ActionButtons.get(i).isEnabled()==true){
							// Click the Button
							if(i==1){
								Thread.sleep(5000);
								ActionButtons.get(i).click();
							}else{
								ActionButtons.get(i).click();
							}
							// Check Visibility of the OK Button to become visible
							wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_AccountUpdateConfirmationYes"))));
							// Click OK button to confirm the Action
							OR.FindElement(driver, ORMap.get("BPM_AccountUpdateConfirmationYes")).click();
						}
					}// Loop through all the Product
					// Check Visibility of the Submit Button to become visible
					wait.until(ExpectedConditions.invisibilityOfAllElements(OR.FindElements(driver, ORMap.get("BPM_AccountUpdateConfirmationYes"))));
					Thread.sleep(2000);
					// Check Visibility of the Submit Button to become visible
					wait.until(ExpectedConditions.elementToBeClickable(OR.FindElement(driver, ORMap.get("BPM_TaskSubmitButton"))));
					// Submit the Task
					OR.FindElement(driver, ORMap.get("BPM_TaskSubmitButton")).click();
					// Check Visibility of the OK Button to become visible
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_SubmittingTaskOKButton"))));

					// Capture Screenshot of the Frame before Submitting the Task Action
					TestLogger.log(LogStatus.INFO,"BPM - All the Products on the Task Detail were Action as Complete");
					//TestLogger.log(LogStatus.INFO,"All the Products on the Task Detail were Action as Complete", "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));					
					
					// Click OK button to Submit the task
					OR.FindElement(driver, ORMap.get("BPM_SubmittingTaskOKButton")).click();
					wait.until(ExpectedConditions.invisibilityOfAllElements(OR.FindElements(driver, ORMap.get("BPM_SubmittingTaskOKButton"))));

				} 
				// If the Action is to Reject all the Accounts
				else if (TaskAction.toUpperCase().contentEquals("REJECT")){
					// Get all the list of Complete Button present in the Product Content Block
					List<WebElement> ActionButtons = driver.findElements(By.xpath("//div[@id='"+ProductContentBoxID+"']"+ORMap.get("BPM_TaskDetailsRejectButton")[1]));

					// Loop for all the Buttons to be clicked for all Products
					for(int i=0;i<ActionButtons.size();i++){
						// Check Visibility of the Button for each product
						wait.until(ExpectedConditions.visibilityOf(ActionButtons.get(i)));
						// If Button is Enable
						if(ActionButtons.get(i).isEnabled()==true){
							// Click the Button
							if(i==1){
								Thread.sleep(5000);
								ActionButtons.get(i).click();
							}else{
								ActionButtons.get(i).click();
							}
						// Wait for the Reject Reason Element to appear on the page
						wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_TaskRejectReasonCombo"))));
						Thread.sleep(500);
						// Send Reject Reason Text to the Element
						driver.findElement(By.id(OR.FindElement(driver, ORMap.get("BPM_TaskRejectReasonCombo")).getAttribute("id"))).sendKeys(RejectReason);
						Thread.sleep(500);
						// Send Reject Comment Text to the Element
						driver.findElement(By.id(OR.FindElement(driver, ORMap.get("BPM_TaskRejectComments")).getAttribute("id"))).sendKeys(RejectComment);
						Thread.sleep(500);
						// Click on OK Button to Reject
						OR.FindElement(driver, ORMap.get("BPM_TaskRejectOkButton")).click();
						
						}
					}// Loop through all the Product
					// Check Visibility of the Submit Button to become visible
					wait.until(ExpectedConditions.invisibilityOfAllElements(OR.FindElements(driver, ORMap.get("BPM_TaskRejectOkButton"))));
					Thread.sleep(2000);
					// Check Visibility of the Submit Button to become visible
					wait.until(ExpectedConditions.elementToBeClickable(OR.FindElement(driver, ORMap.get("BPM_TaskSubmitButton"))));
					
					// Submit the Task
					OR.FindElement(driver, ORMap.get("BPM_TaskSubmitButton")).click();
					// Check Visibility of the OK Button to become visible
					wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_SubmittingTaskOKButton"))));
					
					// Capture Screenshot of the Frame before Submitting the Task Action
					TestLogger.log(LogStatus.PASS,"BPM - All the Products on the Task Detail were Action as Reject");
					//TestLogger.log(LogStatus.PASS,"All the Products on the Task Detail were Action as Reject", "Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
					
					// Click OK button to Submit the task
					OR.FindElement(driver, ORMap.get("BPM_SubmittingTaskOKButton")).click();
					// Check Visibility of the Submit Button to become visible
					wait.until(ExpectedConditions.invisibilityOfAllElements(OR.FindElements(driver, ORMap.get("BPM_SubmittingTaskOKButton"))));
					
				}
				// If the Action could neither be Complete or Reject
				else{
					TestLogger.log(LogStatus.FATAL,"BPM - Could not Confirm appropriate Action to be taken on the Task products, Check Input!");
					// switching Driver back to original window
					driver.switchTo().defaultContent();
					return "Fatal";					
				}

		} catch(Exception e){
			if(!e.equals(null)){
				e.printStackTrace();
				TestLogger.log(LogStatus.FATAL, "BPM - Error while Updating BPM Task Status for each Account. Error Description: "+e.toString()+ ". Screenshot - "+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";
			}
		}
		// switching Driver back to original window
		driver.switchTo().defaultContent();
		return "Pass";
	}

/* Method: ReActivateTask will 
 * 1. Re-Activate the task which was either Completed or Rejected in the run
 * 
 * Return: String execution status as 'Pass' (if ran with no error), 'Fatal' (If failed at any stage - in this case further methods will be skipped)
 * Passing Param: 	NONE. NOTE: Request ID of the task was pulled from the execution from previous Completed or Rejected tasks in the run
 * 
 * Created by: Nikhil Katare
 * On: 29/Nov/2016
 * Reviewed by:
 * Updated on:
 * UPdated with:	
*/		
	public String ReActivateTask() throws IOException{
	try{
		// Driver Wait object to wait until 10 Seconds for the Web Element to becomes visible.
		WebDriverWait wait = new WebDriverWait(driver, 10);
		// Wait until Element becomes visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_ReAct_OrgTabsLink"))));
		
		OR.FindElement(driver, ORMap.get("BPM_ReAct_OrgTabsLink")).click();
		Thread.sleep(1000);
		// Wait until Element becomes visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_ReAct_DashboardLink"))));
		
		OR.FindElement(driver, ORMap.get("BPM_ReAct_DashboardLink")).click();
		Thread.sleep(1000);
		while(OR.FindElements(driver, ORMap.get("BPM_Loading")).size()>0){
			Thread.sleep(1000);
		}
		Thread.sleep(3000);
		if(!(driver.findElements(By.name(ORMap.get("BPM_ReAct_iFrame")[1])).size()>0)){
			driver.navigate().refresh();
			Thread.sleep(2000);
		}
		// Wait until Element becomes visible
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name(ORMap.get("BPM_ReAct_iFrame")[1]))));
		// Switch the Driver to the iFrame of the page
		driver.switchTo().frame(ORMap.get("BPM_ReAct_iFrame")[1]);
		
		// Wait until Element becomes visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_ReAct_ReActivateTab"))));
		OR.FindElement(driver, ORMap.get("BPM_ReAct_ReActivateTab")).click();
		 Thread.sleep(1000);
		// Wait until Element becomes visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_ReAct_RequestIdTextbox"))));			
		
		OR.FindElement(driver, ORMap.get("BPM_ReAct_RequestIdTextbox")).sendKeys(getBPMTaskRequestID());

		OR.FindElement(driver, ORMap.get("BPM_ReAct_ShowDataButton")).click();
		Thread.sleep(2000);
		// Wait until Element becomes visible
		if(!(OR.FindElements(driver, ORMap.get("BPM_ReAct_ReActivateMsg")).size()>0)){
			Thread.sleep(1000);
		}
		
		if(OR.FindElements(driver, ORMap.get("BPM_ReAct_SearchResultRows")).size()==1){
			OR.FindElement(driver, ORMap.get("BPM_ReAct_SearchRowRadioButton")).click();
		} else{
			// Capture Screenshot of the Frame before Submitting the Task Action
			TestLogger.log(LogStatus.FATAL,"BPM - The Task having Request ID: "+getBPMTaskRequestID()+" was either do not have any task or have more then one tasks. This could not be reactivated");
			return "Fatal";
		}
		// Wait until Element becomes visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_ReAct_ReactivateButton"))));
		
		OR.FindElement(driver, ORMap.get("BPM_ReAct_ReactivateButton")).click();
		
		// Wait until Element becomes visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_ReAct_ReActivateYesButton"))));
		OR.FindElement(driver, ORMap.get("BPM_ReAct_ReActivateYesButton")).click();
		
		// Wait until Element becomes visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_ReAct_ReActivateCloseLink"))));
		
		OR.FindElement(driver, ORMap.get("BPM_ReAct_ReActivateCloseLink")).click();

		// Wait until Element becomes Invisible
		wait.until(ExpectedConditions.invisibilityOfAllElements(OR.FindElements(driver, ORMap.get("BPM_ReAct_ReactivateButton"))));
		
		// Capture Screenshot of the Frame before Submitting the Task Action
		TestLogger.log(LogStatus.INFO,"BPM - The Task having Request ID: "+getBPMTaskRequestID()+" was Reactivated");
		
		driver.switchTo().defaultContent();
		
		// Wait until Element becomes visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_ReAct_OrgTabsLink"))));		
		OR.FindElement(driver, ORMap.get("BPM_ReAct_OrgTabsLink")).click();		
		// Wait until Element becomes visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_ReAct_WorkLink"))));		
		OR.FindElement(driver, ORMap.get("BPM_ReAct_WorkLink")).click();
		
		Thread.sleep(1000);

		if(OR.FindElements(driver, ORMap.get("BPM_ReAct_SocialDialog")).size()>0){
			OR.FindElement(driver, ORMap.get("BPM_ReAct_SocialDialogOK")).click();
			Thread.sleep(1000);
			// Wait until Element becomes visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_ReAct_OrgTabsLink"))));		
			OR.FindElement(driver, ORMap.get("BPM_ReAct_OrgTabsLink")).click();		
			Thread.sleep(1000);
			// Wait until Element becomes visible
			wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_ReAct_WorkLink"))));		
			OR.FindElement(driver, ORMap.get("BPM_ReAct_WorkLink")).click();		
			Thread.sleep(1000);
		}
		
		while(OR.FindElements(driver, ORMap.get("BPM_Loading")).size()>0){
			Thread.sleep(1000);
		}
		Thread.sleep(1000);	
		
		// Wait until Element becomes visible
		wait.until(ExpectedConditions.visibilityOf(OR.FindElement(driver, ORMap.get("BPM_TaskSearchBox"))));
		// Enter Customer Name in the Search box
		OR.FindElement(driver, ORMap.get("BPM_TaskSearchBox")).clear();
		
		while(OR.FindElements(driver, ORMap.get("BPM_Loading")).size()>0){
			Thread.sleep(1000);
		}
		
		} catch(Exception e){
			if(!e.equals(null)){
				e.printStackTrace();
				TestLogger.log(LogStatus.FATAL, "BPM - Error while Reactivating BPM Task id :"+getBPMTaskRequestID()+". Error Description: "+e.toString()+ ". Screenshot: "+Screenshot.ObjectSnapFullPage(driver));
				return "Fatal";
			}
		}
		return "Pass";
		
	}
	
}

