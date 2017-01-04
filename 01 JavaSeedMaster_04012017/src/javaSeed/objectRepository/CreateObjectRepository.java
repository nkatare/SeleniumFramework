package javaSeed.objectRepository;

import java.util.HashMap;

import javaSeed.constants.Const;
import javaSeed.utils.ParseExcelFile;

public class CreateObjectRepository {
	
	private static HashMap<String,String[]> ObjRepoMap = null;
	private static String ORSheetArray[][];
		
	public static HashMap<String, String[]> BuildOR(String OR_Path, String OR_Sheet){
			
		//Setting ParseExcelFile Class Object to read Object Repository sheet
			ParseExcelFile objRepoSheetObj = new ParseExcelFile();		
		// Parsing Object Repository Sheet in to an Private Array (Declared above)
			ORSheetArray = objRepoSheetObj.ReadExcel(OR_Path, OR_Sheet);	
		// Local Variable declaration
			int rowIndex=0,arrIndexRead=0,arrIndexWrite=0,rows;
         // Reading OR Array length to run the loop to get Web elements and writing in HashMap with Key  
            rows=ORSheetArray.length;
         // Loop through Excel Read Array to pick Length of Valid Data    
            int i=0;
            for (i=1;i<rows;i++){
            	if (!ORSheetArray[i][1].contentEquals("null") & !ORSheetArray[i][0].contentEquals("*")){
            		arrIndexRead++;         		
            	}
            }
          // Size the Global Array to the Row length of OR Excel length and Column Length as 2 from Web Object Attribute ID and Value 
            Const.ORElementArray=new String[arrIndexRead][2];
            ObjRepoMap = new HashMap<String,String[]>(arrIndexRead+1);
         
         // Loop to Run through OR local Array and put the Web Object attribute ID and Value to the Global Array and Mapping that Array to HashMap with Key as Object Name defined in OR Excel 
            for (rowIndex=1;rowIndex<rows;rowIndex++){
            	// Condition to check if Specific Column marked as * which means Labels or blank ID value will be Skipped
            	if (!ORSheetArray[rowIndex][1].contentEquals("null") & !ORSheetArray[rowIndex][0].contentEquals("*")){
            		Const.ORElementArray[arrIndexWrite][0]=ORSheetArray[rowIndex][1];
            		Const.ORElementArray[arrIndexWrite][1]=ORSheetArray[rowIndex][2];
                	ObjRepoMap.put(ORSheetArray[rowIndex][3],Const.ORElementArray[arrIndexWrite]);
                	arrIndexWrite++;
            	}
            }
         // Returning HashMap to the Method.          
			return ObjRepoMap;
		}

}