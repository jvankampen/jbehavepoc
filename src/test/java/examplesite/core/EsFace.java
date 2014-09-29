package examplesite.core;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import jbehavepoc.BaseTestFace;
import jbehavepoc.Browser;
import jbehavepoc.Prefs;
import jbehavepoc.Res;
import jbehavepoc.XeleniumDriver;

/**
 * Class which gives tests access to Selenium 2 WebDriver.
 */
@SuppressWarnings("rawtypes")
public class EsFace extends BaseTestFace<XeleniumDriver, Prefs> {

    private static File chromeDriver;
    private static File ieDriver;
    private static File phantomJS;
    private static Object chromeLock = "chrome";
    private static Object ieLock = "ie";
    private static Object phantomJSLock = "phantomJS";

    /**
     * Create a TestFace which relies upon ExampleSiteWebDriver to provide
     * access to the browser.
     * 
     * @param browser
     *            The browser to use. Must not be null.
     * @param testId
     *            The identifier of the test. May be null or blank.
     * @param testName
     *            The name of the test. May be null or blank.
     * @param prefs
     *            The preferences used for the test.
     */
    public EsFace(Browser browser, long testId, String testName, Prefs prefs) {
        super(browser, testId, testName, prefs);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
	@Override
    protected XeleniumDriver createDriver(Browser browser) {
    	XeleniumDriver driver = null;
        switch (browser) {
        case FIREFOX:
        	//driver = new ExampleSiteWebDriver(new FirefoxDriver(), browser);
            //driver = ExampleSiteWebDriver.getDriver(FirefoxDriver.class);
        	driver = new XeleniumDriver(FirefoxDriver.class, this);
            break;
        case CHROME:
            synchronized (chromeLock) {
                if (chromeDriver == null) {
                    String ref = "browsers/chromedriver.exe";
                    if (SystemUtils.IS_OS_LINUX
                            && (SystemUtils.OS_ARCH.contains("x86") || SystemUtils.OS_ARCH.contains("i386"))) {
                        ref = "browsers/chromedriverLinux";
                    } else if (SystemUtils.IS_OS_LINUX
                            && SystemUtils.OS_ARCH.contains("64")) {
                        ref = "browser/chromedriverLinux";
                    } else if (SystemUtils.IS_OS_MAC_OSX) {
                        ref = "browser/chromedriverMac";
                    }
                    try {
                        chromeDriver = Res.getFile(ref);
                        System.setProperty("webdriver.chrome.driver",
                                chromeDriver.getCanonicalPath());
                    } catch (IOException e) {
                        throw new RuntimeException("Cannot load Chrome driver",
                                e);
                    }
                }
                //driver = new ExampleSiteWebDriver(new ChromeDriver(), browser);
                //driver = ExampleSiteWebDriver.getDriver(ChromeDriver.class);
                driver = new XeleniumDriver(ChromeDriver.class, this);
            }
            break;
        case IE:
            synchronized (ieLock) {
                if (ieDriver == null) {
                    String ref = "browsers/IEDriverServer32.exe";
                    //if (SystemUtils.OS_ARCH.contains("64")) {
                    //    ref = "browsers/IEDriverServer64.exe";
                    //}
                    try {
                        ieDriver = Res.getFile(ref);
                        System.setProperty("webdriver.ie.driver",
                                ieDriver.getCanonicalPath());
                    } catch (IOException e) {
                        throw new RuntimeException(
                                "Cannot load Internet Explorer driver",
                                e);
                    }
                }
                //driver = new ExampleSiteWebDriver(new InternetExplorerDriver(), browser);
                //driver = ExampleSiteWebDriver.getDriver(InternetExplorerDriver.class);
                driver = new XeleniumDriver(InternetExplorerDriver.class, this);
            }
            break;
        case PHANTOMJS:
            synchronized(phantomJSLock){
                if (phantomJS == null) {
                    String ref = "browsers/phantomjs_windows.exe";
                    if (SystemUtils.IS_OS_LINUX
                            && (SystemUtils.OS_ARCH.contains("x86") || SystemUtils.OS_ARCH.contains("i386"))) {
                        ref = "/usr/lib/phantomjs/phantomjs";
                    } else if (SystemUtils.IS_OS_LINUX
                            && SystemUtils.OS_ARCH.contains("64")) {
                        ref = "/usr/lib/phantomjs/phantomjs";
                    } else if (SystemUtils.IS_OS_MAC_OSX) {
                        ref = "browser/phantomjs_mac";
                    }
                    try {
                        phantomJS = Res.getFile(ref);
                        System.setProperty(prefs().phantomJSPropertyName(),
                                phantomJS.getCanonicalPath());
                    } catch (IOException e) {
                        throw new RuntimeException("Cannot load PhantomJS",
                                e);
                    }
                }
                driver = new XeleniumDriver(PhantomJSDriver.class, this);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown browser: "
                    + browser);
        }
        return driver;
    }

    /** {@inheritDoc} */
    @Override
    protected void destroyDriver(XeleniumDriver browserDriver) {
        browserDriver.quit();
    }

}
