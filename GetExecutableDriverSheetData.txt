package javaSeed.driverSheet;

import java.util.ArrayList;
import java.util.HashMap;

import javaSeed.constants.Const;
import javaSeed.utils.ParseExcelFile;

public class GetExecutableDriverSheetData {
	
	private static String arrTestScenarios[][], arrUnflaggedScenarios[][],arrTestSteps[][];
	private static int itrRows=0;
	
	public static String[][] TestScenariosDataLoad(){
		
		try  {
			
			int itrCounter=0;
			
			// Reading Driver Test Scenario Sheet using the ParseExcelFile Class Method and returning to a local 2D Array
			ParseExcelFile oTestScenarios = new ParseExcelFile();
			arrTestScenarios = oTestScenarios.ReadExcel(Const.JAVA_SEED_DRIVERSHEET_PATH, Const.SHDRIVER_TEST_SCENARIOS);
	
	        // Loop through Excel Read Array to pick Length of Valid Data    
	        int i=0, rows=arrTestScenarios.length;
	        for (i=1;i<rows;i++){
	        	// Checking Flag 'Y', if True increase counter to get true counter of Flagged rows
	        	if (arrTestScenarios[i][1].toUpperCase().contentEquals("Y")){
	        		itrCounter++;         		
	        	}
	        }		
			
	        // Sizing the Returned 2D Array to the size of Executable Rows with Flag Y 
	        arrUnflaggedScenarios = new String[itrCounter][3];
			
	        // Looping again to Put data from Array had all data to filter with Flag Y and put data to Returned Array
	        itrCounter=0;
			for (itrRows=0;itrRows<arrTestScenarios.length;itrRows++){
				// Checking Flag 'Y', if True put Row data to 2D Array row.
				if (arrTestScenarios[itrRows][1].toUpperCase().contentEquals("Y")){
	
					arrUnflaggedScenarios[itrCounter] = arrTestScenarios[itrRows];
					itrCounter++;
				}	
			}
		}
		catch (Exception e){
				e.printStackTrace();
		}
		// Return Array Containing only Executable Scenarios
		return arrUnflaggedScenarios;
	}

	public static HashMap<Integer, String[]> TestStepsDataLoad(){
		// Declaring local variables
		int i=0, j=0,validColCounter=0, validRowCounter=0;
		
		//Declaring local HashMap which will then be returned to Const.MAP_TESTSTEP_ARRLIST
		HashMap<Integer, String[]> mapTestStepsData = new HashMap<Integer, String[]>();
		
		// Reading Excel from the Generic Read Excel Function
		ParseExcelFile oTestSteps = new ParseExcelFile();
		// Get 2D array in the local 2D array variable
		arrTestSteps = oTestSteps.ReadExcel(Const.JAVA_SEED_DRIVERSHEET_PATH, Const.SHDRIVER_TEST_STEPS);

		// initiate ArrayList Const.ARRLIST_TESTSTEP_ROWDATA from 1st index
        Const.ARRLIST_TESTSTEP_ROWDATA = new ArrayList<String[]>(1);
        // re-initiate variable counter to count Flagged rows 
        validRowCounter=0;
        // Run through each row in the Excel Data Array
		for (i=0;i<arrTestSteps.length;i++){
			// Re-initiate the Column counter to 0 because in this sheet, each row might have different column data
			validColCounter=0;	
			// Condition to check the Flag to 'Y'
			if (arrTestSteps[i][1].toUpperCase().contentEquals("Y")){
				
				// Loop to run though each cell in the current row loop
				for (j=0;j<arrTestSteps[i].length;j++){
					// Condition to check if the returned array index has 'null' text. Which was returned from the Read Excel function if the cell is blank.
					if (!arrTestSteps[i][j].contentEquals("null")){
						// raise the counter to 1 on each iteration
						validColCounter++;
					}
				}
				
				// Initiating the Local Temporary 1D Array to current Row's iteration of Columns size 
				String arrTempRows[] = new String[validColCounter];
				// Re-initiate the Column counter to 0 for another Row's iteration which could have different number of columns data
				validColCounter=0;
				// Similar Each Cell Loop from current Row iteration to load each cell data to the Temp Array
				for (j=0;j<arrTestSteps[i].length;j++){
					// Condition to check if the returned array index has 'null' text. Which was returned from the Read Excel function if the cell is blank.
					if (!arrTestSteps[i][j].contentEquals("null")){
						// Load Excel Sheet Array current iteration row data to Temp Array 
						arrTempRows[validColCounter]=arrTestSteps[i][j];
						// Increment the Counter for another Cell
						validColCounter++;
					}
				}
				// Load Each row iteration Data to ArrayList Const.ARRLIST_TESTSTEP_ROWDATA
				Const.ARRLIST_TESTSTEP_ROWDATA.add(arrTempRows);
				// Map ArrayList Const.ARRLIST_TESTSTEP_ROWDATA to Local HashMap with Key as Flagged Row counter. Which means only Flagged rows will be loaded and mapped in the ArrayList and HasMap resp.  
				mapTestStepsData.put(validRowCounter, Const.ARRLIST_TESTSTEP_ROWDATA.get(validRowCounter));
				// Row Counter increment
				validRowCounter++;
        	}			
		}
		
		
		// Return Local HashMap to HashMap Object
		return mapTestStepsData;
		
	}


}