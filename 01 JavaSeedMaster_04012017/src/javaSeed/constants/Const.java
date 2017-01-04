package javaSeed.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import javaSeed.driverSheet.GetExecutableDriverSheetData;
import javaSeed.objectRepository.CreateObjectRepository;
import javaSeed.utils.ParseExcelFile;
import javaSeed.utils.TimeStamp;

public class Const {
	
	// Object Declarations to read EXCEL sheets
	static ParseExcelFile ExcelSheetObj = new ParseExcelFile();
	
	// Reference Paths for various resources
	public static final String JAVA_SEED_PATH = "C:\\JavaSeed\\";
			//System.getProperty("java.io.tmpdir")+"\\JavaSeed\\";
	//public static final String JAVA_SEED_ENV_PATH = JAVA_SEED_PATH+"EnvironmentSheets\\EnvironmentSheet.xlsx";
	public static final String JAVA_SEED_DRIVERSHEET_PATH = JAVA_SEED_PATH+"01DriverSheets\\DriverSheet.xlsx";
	public static final String JAVA_SEED_OR_PATH = JAVA_SEED_PATH+"02ObjectRepository\\ObjectRepository.xlsx";
	public static final String JAVA_SEED_REPORTS_PATH = JAVA_SEED_PATH+"03Reports\\";
	//public static final String JAVA_SEED_SCREENSHOT_PATH = JAVA_SEED_REPORTS_PATH+"screenshots\\";
	public static final String JAVA_SEED_EXTENTREPORTNAME = "JavaSeedReports"+TimeStamp.RetunrDate("YYYYMMdd")+TimeStamp.ReturnTime("HHmmss");
	public static final String JAVA_SEED_EXTENTREPORTPATH = JAVA_SEED_REPORTS_PATH+JAVA_SEED_EXTENTREPORTNAME;
	public static final String JAVA_SEED_TESTDATA_PATH = JAVA_SEED_PATH+"04TestDataFiles\\";
	
// Web Browsers Executable Drivers Path
	public static final String CHROME_DRIVER_PATH = JAVA_SEED_TESTDATA_PATH+"chromedriver\\chromedriver.exe";
	public static final String IE_DRIVERX64_PATH = JAVA_SEED_TESTDATA_PATH+"IEDriverServer\\IEDriverServer_x64.exe";
	public static final String IE_DRIVERW32_PATH = JAVA_SEED_TESTDATA_PATH+"IEDriverServer\\IEDriverServer_Win32.exe";
	public static final String FIREFOX_DRIVER_PATH = JAVA_SEED_TESTDATA_PATH+"FirefoxDriver\\geckodriver.exe";
	
// AutoIT Executable Path

	
	// Excel Sheet Names of various Workbooks
	public static final String ENV_SHEET = "Env";	
	public static final String OR_SHEET = "objectRepo";
	public static final String SHDRIVER_TEST_SCENARIOS = "testScenarios";
	public static final String SHDRIVER_TEST_STEPS = "testSteps";
	
//ENVIRONMENT EXCEL: Array Declaration and Data Retrieved for ENVIRONMENT EXCEL Sheet 
	public static String ENVIRONMENT_DATA_ARRAY[][] = ExcelSheetObj.ReadExcel(Const.JAVA_SEED_DRIVERSHEET_PATH, Const.ENV_SHEET);
	public static String ENVIRONMENT_EXEC_ARRAY[] = null;
	
//DRIVERSHEET'S TEST_SCENARIO: Array Declarations and Data Retrieved for DRIVERSHEET'S TEST_SCENARIO EXCEL SHEET for executable flagged 'Y' scenarios Only
	public static String SHTEST_SCENARIOS[][] = GetExecutableDriverSheetData.TestScenariosDataLoad();
	
//DRIVER SHEET'S TESTSTEPS SHEET: Driver Sheet's testSteps Sheet Constants declaration with Array List Loaded Mapped with HashMap.  
	public static ArrayList<String[]> ARRLIST_TESTSTEP_ROWDATA;
	public static HashMap<Integer,String[]> MAP_TESTSTEP_ARRLIST = GetExecutableDriverSheetData.TestStepsDataLoad();	
	
//OBJECT REPOSITORY: OBJECT REPOSITORY 2D Array and HASHMAP declaration with ORMap Data loaded.  
	public static String ORElementArray[][];
	public static HashMap<String, String[]> ORMap = CreateObjectRepository.BuildOR(Const.JAVA_SEED_OR_PATH, Const.OR_SHEET);

//WEBDRIVER OBJECT: Setting driver (Key Driver) as WebDriver Object pointing to Executable Browser
	public static WebDriver driver = null;
	
	//public static WebDriver driver = SetDriver.SetWebDriver(Const.driver, Const.ENVIRONMENT_DATA_ARRAY[1][2]);	//??????????? Hard-Coded ????????????????
	
// EXTENT Reports Objects
	public static ExtentReports oExtent = new ExtentReports(JAVA_SEED_EXTENTREPORTPATH+".html", true);
	public static ExtentTest etScenarios = null;
	public static ExtentTest etTestCases = null;

// JIRA auto update Variables
	
	public static List<String> JIRATCKeyPASSLIST = new ArrayList<String>();
	public static List<String> JIRATCKeyFAILLIST = new ArrayList<String>();
}