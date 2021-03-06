package javaSeed.driverSheet;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javaSeed.constants.Const;
import javaSeed.utils.FileHandling;
import javaSeed.utils.ParseExcelFile;
import javaSeed.utils.SetDriver;
import javaSeed.utils.jiraConnection.JIRAUpdate;

public class TriggerJavaSeed {

	@SuppressWarnings("rawtypes")
	public static void TriggerScenarios(String args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		// Consume 'Row Index' parameter for Environment file from Executable Jar  
		String vEnvParam = args;
		String ExeStatus = null;
		
		
		// Define the WebDriver based on input from Environment File
				Const.ENVIRONMENT_EXEC_ARRAY = Const.ENVIRONMENT_DATA_ARRAY[Integer.parseInt(vEnvParam)];
				//Const.ENVIRONMENT_EXEC_ARRAY = Const.ENVIRONMENT_DATA_ARRAY[1];
		
				Const.driver = SetDriver.SetWebDriver(Const.driver, Const.ENVIRONMENT_EXEC_ARRAY[2]);
				
				String Scenarios[][] = Const.SHTEST_SCENARIOS;
								
				int itrS=0;
				while(itrS < Scenarios.length){
					
					// Taking the Iteration data in to local 1D array to consume further
					String[] arrTestScenario = Scenarios[itrS];
					// Taking HasMap of the Test Steps Sheet data in to local HashMap variable
					HashMap<Integer,String[]> LocMAP_TESTSTEP_ARRLIST = Const.MAP_TESTSTEP_ARRLIST;
					// Creating an Array-list of String array to consume Test Steps data (each row in to each Array-list String Array
					ArrayList<String[]> ArrListScenariosTestSteps = new ArrayList<String[]>(1);
					// Declaring Parse Excel File object to reach Excel Files
				    ParseExcelFile oTestCaseData = new ParseExcelFile();
				    // Declaring 2D array to consume Test Step Excel Sheet Data for each Scenario
					String arrTestCaseData[][];
					// Declaring Variables to consume Scenario Iteration Indexes
					int ScenarioIndexFrom=0,ScenarioIndexTo=0;
					
					// Initializing Iterator object for HashMap of Test Step ArrayList 
					Iterator<?> it = LocMAP_TESTSTEP_ARRLIST.entrySet().iterator();
					// This loop is to Capture all the Test Step Rows for the Scenario in Execution
					// Loop Each Test Step Row in this Loop
				    while (it.hasNext()) {
				        Map.Entry pair = (Map.Entry)it.next();
				        // Putting Array from each row Array List to a temporary Array
				        String[] TestStepRow = (String[]) pair.getValue();
				        	// Compare the Scenario Name from Scenario in execution with Test Step Sheet Scenario Name if True...	        
					        if (arrTestScenario[0].contentEquals(TestStepRow[0])){
					        	// Add the Test Step row to the temporary Array list defined above @59.
					        	// NOTE: This Temp array will reinitialize in next
					        	ArrListScenariosTestSteps.add(TestStepRow);
					        }
				    }
				    
				    // Execution of the Test Step rows (Functions) for the Scenario in execution
				    // If Test Step sheet has functions for the Scenario in execution then run below code
				    if (ArrListScenariosTestSteps.size()>0){	    	
						
				    	// Starting Extent Report Test Execution for the Scenario in execution 
						Const.etScenarios = Const.oExtent.startTest(Scenarios[itrS][0], Scenarios[itrS][3]);
						
						// ITERATION INDEX: If the Iteration Index was defined in the Scenario sheet, run If condition and take Iteration Index to run the iteration data from Data Sheets
				    	// Getting Scenario Execution Index Defined on testScenarios Sheet
						int ItrIndex = arrTestScenario[2].indexOf("-");
						if (ItrIndex==1 || ItrIndex==2){
					    	String[] arrSceIndex = arrTestScenario[2].split("-");
					    	ScenarioIndexFrom = Integer.parseInt(arrSceIndex[0]);
					    	ScenarioIndexTo = Integer.parseInt(arrSceIndex[1]);
					    	// Pulling Test Case Data for the current Scenario in a 2D Array 
							arrTestCaseData = oTestCaseData.ReadExcel(Const.JAVA_SEED_DRIVERSHEET_PATH, "tc"+arrTestScenario[0]);	
							

							// Loop for Scenario Execution Index, 'Iteration Index' column on the testScenario Sheet 
							for (int ScenItr=ScenarioIndexFrom;ScenItr<=ScenarioIndexTo;ScenItr++){
								
								//+++++++++++++ Add Code here to verify that Test Case present in the ALM Test Set, if not, skip the test execution.
								// Using - String = ReadALMEntity.GetTestCaseInstanceID(TestSetFodlerName,TestSetName,TestCaseID). If String = "0000" Means TestCase not found.

								// Starting Extent Report Test Execution for the Test Case per Iteration
						    	Const.etTestCases = Const.oExtent.startTest(arrTestCaseData[ScenItr][0], //Test Case ID
						    			 arrTestCaseData[ScenItr][1] //Test Case Description
						    					);
						    	 
								
								// Loop Each Scenario Execution Index, Loop through each Function defined in the testSteps sheets.
							    for (int j=0;j<ArrListScenariosTestSteps.size();j++){
							    	
							    	// Setting Test Steps array from the Array List of that row running in this loop for Scenario in execution
							    	TriggerTestSteps.setArrListScenariosTestSteps(ArrListScenariosTestSteps.get(j));
							    	// Setting Test Data array from the 2D Array of that row running in this loop for Scenario in execution
							    	TriggerTestSteps.setArrTestCaseData(arrTestCaseData[ScenItr]);
							    	// ###########################################################
							    	// Executing the Function defined in Test Step
							    	ExeStatus = TriggerTestSteps.TestStepsIterationExecution();
							    	
								    // If there is a Fatal error in the Script execution - Terminate the Scenario
								    if (ExeStatus.toUpperCase().contentEquals("FATAL")){
								    	break;
								    }
							    	
							    	// ###########################################################				    	
							    	
							    }
							    // Ending Extent Report Test Execution for the Test Case per Iteration
							    Const.oExtent.endTest(Const.etTestCases);
							    
							    // JIRA Update TC IDs collection Block
							    if (Const.etTestCases.getRunStatus().toString().toUpperCase().contentEquals("PASS")){
							    	Const.JIRATCKeyPASSLIST.add(arrTestCaseData[ScenItr][0]);
							    } else {
							    	Const.JIRATCKeyFAILLIST.add(arrTestCaseData[ScenItr][0]);
							    }
							    // JIRA Update TC IDs collection Block
							    
							    // Appending Extent Report Test Execution for the Test Case per Iteration to the Test Scenario Test Run
							    Const.etScenarios.appendChild(Const.etTestCases);
							}	    	
						} 
						// ITERATION INDEX: Else if Iteration Index was not defined. This means the scenario was not supposed to run through Iteration and only one instance of data
						// which was defined in the Test Step Sheet itself
						else {
							Const.etTestCases = Const.oExtent.startTest(arrTestScenario[2], //Test Case ID
									arrTestScenario[3]); //Test Case Description
							// Loop Each Scenario Execution Index, Loop through each Function defined in the testSteps sheets.
						    for (int j=0;j<ArrListScenariosTestSteps.size();j++){
						    	// Setting Test Steps array from the Array List of that row running in this loop for Scenario in execution						    	
						    	TriggerTestSteps.setArrListScenariosTestSteps(ArrListScenariosTestSteps.get(j));
						    	// Setting Test Data array from the 2D Array of that row running in this loop for Scenario in execution						    	
						    	TriggerTestSteps.setArrTestCaseData(TriggerTestSteps.getArrListScenariosTestSteps());
						    	// ###########################################################
						    	// Executing the Function defined in Test Step
						    	ExeStatus = TriggerTestSteps.TestStepsSingleExecution();
						    	
						    	
							    // If there is a Fatal error in the Script execution - Terminate the Scenario
							    if (ExeStatus.toUpperCase().contentEquals("FATAL")){
							    	break;
							    }
						    	// ###########################################################				    	
						    	
						    }
						   // System.out.println(Const.etTestCases.getRunStatus());
						    Const.oExtent.endTest(Const.etTestCases);
						   
						    // JIRA Update TC IDs collection Block
						    if (Const.etTestCases.getRunStatus().toString().toUpperCase().contentEquals("PASS")){
						    	if(arrTestScenario[2].contains(",")){
						    		String[] TCKeys = arrTestScenario[2].split(",");
						    		for(String TCKey:TCKeys){
						    			Const.JIRATCKeyPASSLIST.add(TCKey);
						    		}
						    	} else{
						    		Const.JIRATCKeyPASSLIST.add(arrTestScenario[2]);
						    	}
						    } else {
						    	if(arrTestScenario[2].contains(",")){
						    		String[] TCKeys = arrTestScenario[2].split(",");
						    		for(String TCKey:TCKeys){
						    			Const.JIRATCKeyFAILLIST.add(TCKey);
						    		}
						    	}else{
						    		Const.JIRATCKeyFAILLIST.add(arrTestScenario[2]);
						    	}
						    }
						    // JIRA Update TC IDs collection Block
						    
						    Const.etScenarios.appendChild(Const.etTestCases);
							
						}
						
				    	

					Const.oExtent.endTest(Const.etScenarios);
				    }
					
					itrS++;
				}
		
		Const.oExtent.flush();
		FileHandling.ZipFile(Const.JAVA_SEED_EXTENTREPORTPATH+".zip", Const.JAVA_SEED_EXTENTREPORTPATH+".html");
		FileHandling.DeleteFile(Const.JAVA_SEED_EXTENTREPORTPATH+".html");
		
		// JIRA Update TC IDs collection Block
		String JIRA_UPDATEFLAG = Const.ENVIRONMENT_EXEC_ARRAY[1];
		String JIRA_REPORTUPLOAD_TCKEY=Const.ENVIRONMENT_EXEC_ARRAY[13];
		
		if(JIRA_UPDATEFLAG.toUpperCase().trim().contentEquals("Y")){
			JIRAUpdate.ConnectJiraUpdateTCStatus(Const.JIRATCKeyPASSLIST, Const.JIRATCKeyFAILLIST);
			String sReportPath = Const.JAVA_SEED_EXTENTREPORTPATH+".zip";
					//"C:\\JavaSeed\\03Reports\\OAT_BPMEventingMicroserviceFailure_60Itr.zip";
					//Const.JAVA_SEED_EXTENTREPORTPATH+".zip";
			Boolean AttachmentStatus = JIRAUpdate.UploadTestExecutionReport(sReportPath, JIRA_REPORTUPLOAD_TCKEY);
			if(AttachmentStatus==true){
				FileHandling.DeleteFile(Const.JAVA_SEED_EXTENTREPORTPATH+".zip");
			}
		}
		
		Const.driver.quit();

	}

}
