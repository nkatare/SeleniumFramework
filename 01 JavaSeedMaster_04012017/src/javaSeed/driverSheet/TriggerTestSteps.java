package javaSeed.driverSheet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TriggerTestSteps{
	
	// Declare Test Step Array for each Scenario
	private static String[] ArrListScenariosTestSteps;
	// Declare Test Data Array for each Scenario
	private static String[] arrTestCaseData;
	// Getter for Test Step Array for each Scenario
	public static String[] getArrListScenariosTestSteps() {
		return ArrListScenariosTestSteps;
	}
	// Setter for Test Step Array for each Scenario
	public static void setArrListScenariosTestSteps(String[] arrListScenariosTestSteps) {
		ArrListScenariosTestSteps = arrListScenariosTestSteps;
	}
	// Getter for Test Data Array for each Scenario
	public static String[] getArrTestCaseData() {
		return arrTestCaseData;
	}
	// Setter for Test Data Array for each Scenario
	public static void setArrTestCaseData(String[] arrTestCaseDataPassed) {
		arrTestCaseData = arrTestCaseDataPassed;
	}

	public static String TestStepsSingleExecution(){
		
		String[] TestStepRow = ArrListScenariosTestSteps;
		String[] TestDataRow = arrTestCaseData;
		String ExecStatus = null;
		
		try {
			
			// Declare Class of Application Test Scripts Class (Passed as Parameter, which was picked up from the TestSteps Sheet) to get all Methods defined in that 
			Class<?> clazz = Class.forName(TestStepRow[2]); 
			Constructor<?> ctor = clazz.getConstructor();
			Object GPSTestPack = ctor.newInstance(new Object[]{});
			
			//GPSTestScripts GPSTestPack = new GPSTestScripts();
					Class<?> GPSFunctionList = GPSTestPack.getClass();
					Method[] AllMethods =  GPSFunctionList.getMethods();
	 
						for (Method method:AllMethods){
						
								// Compare Each Method Name with what defined in the TestStep Sheet. If Condition true Call that Function from Application Test Scripts Class
						if (TestStepRow[3].toString().contentEquals(method.getName().toString())){
							
							switch (TestStepRow.length){
								case 4:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 5:
									try { // Need to Add TestStepRow[4]. "toUpperCase()" ).charAt(0))-65]
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[4]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 6:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[4], TestDataRow[5]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 7:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[4],TestDataRow[5],TestDataRow[6]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 8:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[4],TestDataRow[5],TestDataRow[6],TestDataRow[7]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 9:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[4],TestDataRow[5],TestDataRow[6],TestDataRow[7],TestDataRow[8]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 10:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[4],TestDataRow[5],TestDataRow[6],TestDataRow[7],TestDataRow[8],TestDataRow[9]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 11:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[4],TestDataRow[5],TestDataRow[6],TestDataRow[7],TestDataRow[8],TestDataRow[9],TestDataRow[10]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 12:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[4],TestDataRow[5],TestDataRow[6],TestDataRow[7],TestDataRow[8],TestDataRow[9],TestDataRow[10],TestDataRow[11]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 13:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[4],TestDataRow[5],TestDataRow[6],TestDataRow[7],TestDataRow[8],TestDataRow[9],TestDataRow[10],TestDataRow[11],TestDataRow[12]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 14:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[4],TestDataRow[5],TestDataRow[6],TestDataRow[7],TestDataRow[8],TestDataRow[9],TestDataRow[10],TestDataRow[11],TestDataRow[12],TestDataRow[13]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
							
							}
	
						}
					}

				
			}
						
			catch (Exception e){
				e.printStackTrace();			
		}				
		return ExecStatus;		
	}
	
	public static String TestStepsIterationExecution () throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
			
		String[] TestStepRow = ArrListScenariosTestSteps;
		String[] TestDataRow = arrTestCaseData;
		String ExecStatus = null;
		
			try {
				
			// Declare Class of Application Test Scripts Class (Passed as Parameter, which was picked up from the TestSteps Sheet) to get all Methods defined in that 
			Class<?> clazz = Class.forName(TestStepRow[2]); 
			Constructor<?> ctor = clazz.getConstructor();
			Object GPSTestPack = ctor.newInstance(new Object[]{});
			
			//GPSTestScripts GPSTestPack = new GPSTestScripts();
					Class<?> GPSFunctionList = GPSTestPack.getClass();
					Method[] AllMethods =  GPSFunctionList.getMethods();
	 
						for (Method method:AllMethods){
						
								// Compare Each Method Name with what defined in the TestStep Sheet. If Condition true Call that Function from Application Test Scripts Class
						if (TestStepRow[3].toString().contentEquals(method.getName().toString())){
							
							switch (TestStepRow.length){
								case 4:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 5:
									try { // Need to Add TestStepRow[4]. "toUpperCase()" ).charAt(0))-65]
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[HandleInputData(TestStepRow[4])]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 6:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[HandleInputData(TestStepRow[4])], TestDataRow[HandleInputData(TestStepRow[5])]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 7:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[HandleInputData(TestStepRow[4])],TestDataRow[HandleInputData(TestStepRow[5])],TestDataRow[HandleInputData(TestStepRow[6])]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 8:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[HandleInputData(TestStepRow[4])],TestDataRow[HandleInputData(TestStepRow[5])],TestDataRow[HandleInputData(TestStepRow[6])],TestDataRow[HandleInputData(TestStepRow[7])]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 9:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[HandleInputData(TestStepRow[4])],TestDataRow[HandleInputData(TestStepRow[5])],TestDataRow[HandleInputData(TestStepRow[6])],TestDataRow[HandleInputData(TestStepRow[7])],TestDataRow[HandleInputData(TestStepRow[8])]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 10:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[HandleInputData(TestStepRow[4])],TestDataRow[HandleInputData(TestStepRow[5])],TestDataRow[HandleInputData(TestStepRow[6])],TestDataRow[HandleInputData(TestStepRow[7])],TestDataRow[HandleInputData(TestStepRow[8])],TestDataRow[HandleInputData(TestStepRow[9])]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 11:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[HandleInputData(TestStepRow[4])],TestDataRow[HandleInputData(TestStepRow[5])],TestDataRow[HandleInputData(TestStepRow[6])],TestDataRow[HandleInputData(TestStepRow[7])],TestDataRow[HandleInputData(TestStepRow[8])],TestDataRow[HandleInputData(TestStepRow[9])],TestDataRow[HandleInputData(TestStepRow[10])]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 12:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[HandleInputData(TestStepRow[4])],TestDataRow[HandleInputData(TestStepRow[5])],TestDataRow[HandleInputData(TestStepRow[6])],TestDataRow[HandleInputData(TestStepRow[7])],TestDataRow[HandleInputData(TestStepRow[8])],TestDataRow[HandleInputData(TestStepRow[9])],TestDataRow[HandleInputData(TestStepRow[10])],TestDataRow[HandleInputData(TestStepRow[11])]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 13:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[HandleInputData(TestStepRow[4])],TestDataRow[HandleInputData(TestStepRow[5])],TestDataRow[HandleInputData(TestStepRow[6])],TestDataRow[HandleInputData(TestStepRow[7])],TestDataRow[HandleInputData(TestStepRow[8])],TestDataRow[HandleInputData(TestStepRow[9])],TestDataRow[HandleInputData(TestStepRow[10])],TestDataRow[HandleInputData(TestStepRow[11])],TestDataRow[HandleInputData(TestStepRow[12])]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
								case 14:
									try {
										ExecStatus = (String) method.invoke(GPSTestPack, TestDataRow[HandleInputData(TestStepRow[4])],TestDataRow[HandleInputData(TestStepRow[5])],TestDataRow[HandleInputData(TestStepRow[6])],TestDataRow[HandleInputData(TestStepRow[7])],TestDataRow[HandleInputData(TestStepRow[8])],TestDataRow[HandleInputData(TestStepRow[9])],TestDataRow[HandleInputData(TestStepRow[10])],TestDataRow[HandleInputData(TestStepRow[11])],TestDataRow[HandleInputData(TestStepRow[12])],TestDataRow[HandleInputData(TestStepRow[13])]);
									} catch (IllegalAccessException e){
										e.printStackTrace();
									} 
								break;
							
							}
	
						}
					}

				
			}
						
			catch (Exception e){
				e.printStackTrace();
			
		}
			return ExecStatus;
	}

	public static int HandleInputData(String InputDataField){
		int InputData = -1;
		if(InputDataField.length()==1){
			InputData = (int)(InputDataField.toUpperCase().charAt(0))-65;
		} else if(InputDataField.length()==2){
			InputData = 26+(int)(InputDataField.substring(InputDataField.length()-1).toUpperCase().charAt(0))-65;
		}
		
		return InputData;
	}

}