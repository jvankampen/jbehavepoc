package jbehavepoc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

/**
 * Provides the base resources and structure for any test to run in a concurrent
 * web-testing environment. Tests should not inherit from this class directly,
 * unless those tests are willing to implement the abstract methods. Instead,
 * they should inherit from a concrete base class instead (example:
 * {@link xelenium.webdriver.WdTest})
 */
public abstract class BaseTest<T extends TestFace<?, ?>> extends Assert
implements IHookable {

    /**
     * Use this as the data provider for any test that only needs a
     * {@link TestFace}, and can support multiple browsers running concurrently.
     * <p/>
     * Example: <br/>
     * <pre>
     * @Test(dataProvider = MULTIBROWSER)
     * public void sampleTest(TestFace tf) {
     *   // ... and so on
     * }
     * </pre>
     */
    public static final String MULTIBROWSER = "multibrowser";
    public static final String EXCEL = "excel";

    /**
     * These preferences can be referenced when preferences are needed, but no
     * tests are actively running yet.
     */
    public static final Prefs NONTEST_PREFS = Prefs.standardPrefs(null);

    /**
     * Used by {@link BaseTest#provideMultibrowser()} when a test
     * needs no parameters.
     */
    private static final Object[][] NO_PARAMS = new Object[][] { {} };

    /** Provides identifiers for each test. */
    private static final AtomicInteger testCounter = new AtomicInteger();

    /** Prevent too many tests from running at once. */
    private static volatile Semaphore driverPermits = null;

    /** Preferences for the tests. */
    private Prefs prefs = null;

    /**
     * The preferences of the currently running test. May be null if no test is
     * active on the thread, in which case pretestPrefs could be used instead.
     */
    private static ConcurrentMap<ITestResult, Prefs> activePrefs = new ConcurrentHashMap<ITestResult, Prefs>();

    /**
     * Usually, tests come with their own preferences and those should be used.
     * However, in the case of DataProviders and before a test begins, such
     * preferences may not exist, and this may be used instead.
     */
    public Prefs prefs() {
    	if(prefs == null){
			prefs = NONTEST_PREFS;
		}
        return prefs;
    }

    /**
     * Create a TestFace for the test.
     * 
     * @param browser
     *            Run the test on this browser.
     * @param id
     *            The id of the test.
     * @param params
     *            The parameters passed to the test.
     * @param testName
     *            The name of the test.
     * @param prefs
     *            The preferences to be used by the test.
     * @return The face used in the test.
     */
    protected abstract T createFace(Browser browser, int id,
            String testName, Prefs prefs);
    
    /**
     * Returns an array of HashMaps that have been read from an excel worksheet.  One HashMap per row.
     * Keys in the HashMaps are the content of the first row in the worksheet (column names).
     * @param path the relative path of the workbook that the sheet is located in.
     * @param sheetName the sheet name where the intended data is.
     * @return
     */
    @SuppressWarnings("unchecked")
	public HashMap<String,String>[] excelSheet(String path, String sheetName){
    	XcelReader xcelData = new XcelReader(path, sheetName);
    	return xcelData.data().toArray(new HashMap[]{});
    }
    
    /**
     * Tests that would like to run multiple different browsers can use this as
     * their provider.
     * 
     * @return an array of Object arrays. Each Object array will have a TestFace
     *         instance.
     */
    @DataProvider(name = MULTIBROWSER, parallel = true)
    public Object[][] provideMultibrowser() {
        return injectMultibrowser(NO_PARAMS);
    }
    /**
     * Tests that would like to run based off an excel sheet can use this as their data provider.
     * Use in tandem with the @ExcelDataProviderArgs annotation on test methods.
     * @param testMethod
     * @return 
     */
    @DataProvider(name = EXCEL, parallel = true)
    public Object[][] provideExcelData(Method testMethod){
        String excelFile = ExcelDataProviderUtils.resolveExcelDataProvider_fileName(testMethod);
        String worksheet = ExcelDataProviderUtils.resolveExcelDataProvider_worksheet(testMethod);
        return injectMultibrowser(excelSheet(excelFile,worksheet));
    }

    /**
     * When a data provider has provided all of the data for testing, and needs
     * to run all of that data once per browser, then this method may be called
     * by that provider.
     * <p/>
     * Example: a provider wants to provide two Strings to all the browsers
     * under test.<br/>
     * // * Contents of the provider's Object[][] before calling
     * injectMultibrowser:<br/>
     * {@code String[][] "Alice"}, {"Bob}} }<br/>
     * Browsers under test:
     * <ol>
     * <li>Firefox</li>
     * <li>Google Chrome</li>
     * </ol>
     * <br/>
     * injectMultibrowser will return the following:<br/>
     * {@code String[][] Browser.FIREFOX, "Alice"}, {Browser.FIREFOX,
     * "Bob}, {Browser.CHROME, "Alice"}, {Browser.CHROME, "Bob}} }<br/>
     * <p/>
     * In other words, the number of tests will be multiplied by the number of
     * browsers.
     * 
     * @param sourceParamsArray
     *            The parameters to be contained in the new array.
     * @return A new array that contains information for the testing browser.
     */
    public Object[][] injectMultibrowser(Object[][] sourceParamsArray) {
        List<Browser> browsers = prefs().browsers();
        int numOriginalParams = sourceParamsArray.length;
        int numBrowsers = browsers.size();
        Object[][] paramsArray = new Object[numOriginalParams * numBrowsers][];
        int currTest = 0;
        for (int spaIndex = 0; spaIndex < numOriginalParams; ++spaIndex) {
            Object[] sourceParams = sourceParamsArray[spaIndex];
            for (int bIndex = 0; bIndex < numBrowsers; ++bIndex) {
                Browser browser = browsers.get(bIndex);

                Object[] params = new Object[sourceParams.length + 1];
                params[0] = browser;
                for (int spIndex = 0; spIndex < sourceParams.length; ++spIndex) {
                    params[spIndex + 1] = sourceParams[spIndex];
                }

                paramsArray[currTest] = params;
                ++currTest;
            }
        }

        return paramsArray;
    }
    
    public Object[][] injectMultibrowser(HashMap<String,String>[] data){
    	List<Browser> browsers = prefs().browsers();
        int numOriginalParams = data.length;
        int numBrowsers = browsers.size();
        Object[][] paramsArray = new Object[numOriginalParams * numBrowsers][];
        int currTest = 0;
        for (int spaIndex = 0; spaIndex < numOriginalParams; ++spaIndex) {
            HashMap<String,String> sourceParams = data[spaIndex];
            for (int bIndex = 0; bIndex < numBrowsers; ++bIndex) {
                Browser browser = browsers.get(bIndex);

                Object[] params = new Object[2];
                params[0] = browser;
                params[1] = sourceParams;

                paramsArray[currTest] = params;
                ++currTest;
            }
        }

        return paramsArray;
    }

    /**
     * Prepare preferences to be used in all the tests.
     * 
     * @param context
     *            The context that can contain preferences.
     */
    @BeforeTest(alwaysRun = true)
    public void setupPrefs(ITestContext context) {
        prefs = Prefs.standardPrefs(context.getCurrentXmlTest());
    }

    /**
     * Release preference reference from this instance.
     */
    @AfterTest(alwaysRun = true)
    public void teardownPrefs() {
        prefs = null;
    }

    /**
     * <strong>FOR INTERNAL USE ONLY.</strong> Called by TestNG to run the test.
     * Should not be called again by any test.
     * 
     * @see org.testng.IHookable#run(org.testng.IHookCallBack,
     *      org.testng.ITestResult)
     */
    @Override
    public void run(IHookCallBack ihc, ITestResult test) {
        Semaphore permits = getDriverSemaphore(test);
        try {
            permits.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(
                    "Interrupted while waiting to run. Aborting test.");
        }
        try {
            injectFace(test);
    		ihc.runTestMethod(test);
        } finally {
            try {
                destroyFace(test);
            } finally {
                permits.release();
            }
        }
    }

    /**
     * Looks into the parameters that will be given to a test method and
     * replaces the first parameter of type Browser with a TestFace.
     * 
     * @param test
     *            The test method to run.
     * @return The TestFace which was injected into the test. Null if no
     *         injection occurred.
     */
	private TestFace<?, ?> injectFace(ITestResult test) {
        TestFace<?, ?> result = null;
        
        int testId = testCounter.incrementAndGet();
        Object[] params = test.getParameters();
        String testName = getTestName(test);

        int browserIndex = findBrowserParamIndex(params);
        // If a browser was found, replace it with the test face.
        if (browserIndex >= 0) {
            Browser browser = (Browser) params[browserIndex];
            Prefs prefs = Prefs.standardPrefs(test.getMethod().getXmlTest());
            // Overwrite the default prefs with more specific prefs if needed.
            activePrefs.put(test, prefs);

            Object testObj = test.getInstance();
            if (testObj instanceof BaseTest) {
                BaseTest<?> baseTest = (BaseTest<?>) testObj;
                TestFace<?, ?> testFace = baseTest.createFace(browser, testId, testName, prefs);
                params[browserIndex] = testFace;
                result = testFace;
            }

        } else {
            result.log("No test face will be injected into test #" + testId
                    + ": " + testName);
        }

        result.log("Start test #" + testId + ": " + testName);

        return result;
    }

    /**
     * Cleans up anything remaining from the test, such as closing the browser
     * driver. Optionally test results may be published.
     * 
     * @param test
     *            The results of the test.
     */
    private void destroyFace(ITestResult test) {
        TestFace<?, ?> tf = (TestFace<?, ?>) test.getParameters()[0];
        if (tf != null) {
            try {
                tf.log("Finish test #" + tf.id() + ": " + tf.name());
                tf.zap();
                tf = null;
            } finally {
                activePrefs.remove(test);
            }
        }
    }

    /**
     * Get a human-readable name for the test.
     * 
     * @param itr
     *            The method that will be run.
     * @return The test name.
     */
    public static String getTestName(ITestResult itr) {
        StringBuilder testName = new StringBuilder();

        String name = itr.getMethod().getMethodName();
        Object[] parameters = itr.getParameters();

        testName.append(name);
        testName.append("(");
        for (Object parameter : parameters) {
        	if(parameter instanceof HashMap){
        		testName.append("{DATAROW}");
        	}
        	else{
        		testName.append(parameter.toString());
        	}
        }
        testName.append(")");

        return testName.toString();
    }

    /**
     * Finds the index of the parameter containing a Browser object.
     * 
     * @param params
     *            The array of parameters to look through. May be null or empty.
     * @return the index in the array where the Browser object was found, or -1
     *         if the array was null, empty, or no Browser could be found.
     */
    private int findBrowserParamIndex(Object[] params) {
        if (params == null) {
            return -1;
        }

        for (int i = 0; i < params.length; ++i) {
            if (params[i] instanceof Browser) {
                return i;
            }
        }

        return -1;
    }

    /**
     * The Driver Permit limits the number of tests that are active at once.
     * 
     * @return a semaphore to limit the number of simultaneous tests.
     */
    private Semaphore getDriverSemaphore(ITestResult test) {
        // A valid double-check lock for Java 1.5 on up according to
        // http://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java
        Semaphore result = driverPermits;
        if (result == null) {
            synchronized (this) {
                result = driverPermits;
                if (result == null) {
                    driverPermits = result = new Semaphore(prefs()
                            .maxConcurrentDrivers());
                }
            }
        }
        return result;
    }
}
