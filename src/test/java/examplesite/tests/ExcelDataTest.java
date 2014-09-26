package examplesite.tests;

import examplesite.core.EsFace;
import examplesite.core.ExampleSiteBaseTest;

import java.util.HashMap;
import java.util.Hashtable;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import jbehavepoc.ExcelDataProviderArgs;
import jbehavepoc.Retry;
import jbehavepoc.XcelReader;

public class ExcelDataTest extends ExampleSiteBaseTest{

	@Test(dataProvider = MULTIBROWSER,retryAnalyzer=Retry.class)
        public void testExcelDataReader(EsFace ef) {
		XcelReader reader = new XcelReader("Workbook1.xls","Data Example");
		ef.log("Rows: "+reader.data().size());
		while(reader.hasNext()){
			reader.next();
			for(String key : reader.currentRow().keySet()){
				String value = reader.getValue(key);
				ef.log("Key: " + key + ", Value: " + value);
			}
		}
	}
	
	@Test(dataProvider = EXCEL,retryAnalyzer=Retry.class)
    @ExcelDataProviderArgs(excelFile = "Workbook1.xls", worksheet = "Data Example")
	public void testExcelRowIterator(EsFace ef, HashMap<String,String> data){
		for(String key : data.keySet()){
			String value = data.get(key);
			ef.log("Key: " + key + ", Value: " + value);
		}
	}
}
