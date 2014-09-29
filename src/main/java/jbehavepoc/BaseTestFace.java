package jbehavepoc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

@SuppressWarnings("hiding")
public abstract class BaseTestFace<XeleniumDriver, U extends Prefs> implements
TestFace<XeleniumDriver, Prefs> {

    /** Run on this browser. */
    private Browser browserInfo;
    /** The driver instance will be created only when needed. */
    private XeleniumDriver driver;
    /** The identifier for the test. */
    private long id;
    /** The name of the test. */
    private String name;
    /** The preferences for the test. */
    private Prefs prefs;
    /** The SqlSessionFactory used to generate new SqlSessions in combination with MyBatis */
    private Map<String,SqlXessionFactory> sqlSessFacts;

    /**
     * Create a TestFace which provides browser manipulation and other features
     * useful for a test.
     * 
     * @param browser
     *            The browser to use. Must not be null.
     * @param testId
     *            The identifier of the test. May be null or blank.
     * @param testName
     *            The name of the test. May be null or blank.
     * @param preferences
     *            The preferences used for the test.
     * @param testData
     * 			  The test data that has been read in from an excel sheet.  Null if excel data sheet is not used.
     */
    public BaseTestFace(Browser browser, long testId, String testName,
            Prefs preferences) {
    	if (browser == null) {
            throw new IllegalArgumentException("Browser cannot be null.");
        }
        browserInfo = browser;
        id = testId;
        name = testName;
        prefs = preferences;
    }

    /**
     * Called whenever a new driver is needed. Generally this only occurs once.
     * 
     * @param browser
     *            Create a driver for this browser.
     * @return A driver controlling the desired browser.
     */
    protected abstract XeleniumDriver createDriver(Browser browser);

    /**
     * Called at the end when {@code zap is called}. Generally this only occurs
     * once.
     */
    protected abstract void destroyDriver(XeleniumDriver browserDriver);

    /** {@inheritDoc} */
    @Override
    public void log(String output) {
        Reporter.log("["+id()+": "+name()+"{"+browserInfo+"}]- "+output, prefs().printLogToConsole());
    }

    /** {@inheritDoc} */
    @Override
    public XeleniumDriver go() {
        if (driver == null) {
            browserInfo.acquireLocal();
            log(
                    "Creating driver for browser " + browserInfo.name() + "...");
            driver = createDriver(browserInfo);
            log("... driver created.");
        }
        return driver;
    }

    /** {@inheritDoc} */
    @Override
    public Prefs prefs() {
        return prefs;
    }

    /** {@inheritDoc} */
    @Override
    public String name() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public long id() {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    public void zap() {
        if (driver != null) {
            log(
                    "Destroying driver for browser " + browserInfo.name()
                    + "...");
            destroyDriver(driver);
            driver = null;
            browserInfo.releaseLocal();
            log("...driver destroyed.");
        }
    }
    
    /**
     * Method to generate a new SqlSessionFactory and set it to the private variable sqlSessFact.
     * Use sqlSessionFactory(factoryName) to access the generated SqlSessionFactory.
     * @param mybatisConfigPath path to the myBatis config file to use to generate a new SqlSessionFactory
     * @param factoryName the name of the factory to access it using sqlSessionFactories(factoryName)
     * @return sqlSessionFactory(factoryName)
     * @throws IOException
     */
    public SqlXessionFactory newSqlXessionFactory(String mybatisConfigPath, String factoryName) throws IOException{
    	InputStream inputStream = Resources.getResourceAsStream(mybatisConfigPath);
    	sqlSessFacts.put(factoryName ,new SqlXessionFactory(new SqlSessionFactoryBuilder().build(inputStream)));
    	return getXessionFactory(factoryName);
    }
    
    /**
     * Method to access the private variable sqlSessFacts which holds all instances of SqlSessionFactory, used to generate new SqlSessions
     * use sqlSessionFactory(String factoryname) to access a single SqlSessionFactory
     * @return sqlSessFacts
     */
    public Map<String,SqlXessionFactory> sqlSessionFactories(){
    	if(sqlSessFacts == null){
    		sqlSessFacts = new Hashtable<String,SqlXessionFactory>();
    	}
    	return sqlSessFacts;
    }
    
    /**
     * Method to get a specific SqlSessionFactory from the library of generated SqlSessionFactories
     * @param factoryName the name of the factory
     * @return the SqlSessionFactory that is associated with the specified factory name
     */
    public SqlXessionFactory getXessionFactory(String factoryName){
    	return sqlSessionFactories().get(factoryName);
    }
}
