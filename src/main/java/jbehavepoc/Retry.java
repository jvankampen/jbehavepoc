package xelenium;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

public class Retry implements IRetryAnalyzer{
	private int retryCount = 0;
	private int maxRetryCount = BaseTest.NONTEST_PREFS.maxRetryOnFail();
	
	public boolean retry(ITestResult result){
		if(retryCount < maxRetryCount){
			retryCount ++;
			Reporter.log("Retrying Test: " + BaseTest.getTestName(result) + ".  Retry Number: " + retryCount, true);
			return true;
		}
		return false;
	}
}
