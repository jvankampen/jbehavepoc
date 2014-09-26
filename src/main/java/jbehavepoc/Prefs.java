package xelenium;

import org.testng.xml.XmlTest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Preferences/Settings. The structure is hierarchical so that default
 * preferences are provided but can be overridden by command-line, or via
 * another file.
 * <p>
 * The order-of-operations is as follows:
 * <ul>
 * <li>Default properties file</li>
 * <li>Custom properties file</li>
 * <li>Suite-level preferences from TestNG Suite file</li>
 * <li>Test-level preferences from TestNG Suite file</li>
 * <li>System properties</li>
 * <li>Command-line properties</li>
 * </ul>
 * </p>
 * <p>
 * Example:<br/>
 * A property named "example.property" is defined in default.properties as
 * "default" and also defined in "custom.properties" as "custom". The user
 * starts the tests with the following command line arguments: <br/>
 * {@code -DcustomProps="custom.properties" -Dexample.property="commandline"}<br/>
 * Ultimately, the value of "example.property" will be "commandline" because it
 * was a property specified on command-line, which is listed as more important
 * than custom properties, which is also listed more important than default
 * properties file.
 * </p>
 */
public class Prefs {

    private final Properties prefs;

    private static final Prefs DEFAULT_AND_CUSTOM;
    static {
        String defaultPropertiesRes = null;
        String customPropertiesRes = null;
        try {
            defaultPropertiesRes = System.getProperty(
                    "defaultPreferences", "default.properties");
            Properties defProps = loadProperties(Res.get(defaultPropertiesRes));
            // Load the resource location for custom properties, if it exists.
            // use the value from system properties if it exists, since it
            // overrides the value from default file.
            customPropertiesRes = System.getProperty(
                    "customPreferences",
                    defProps.getProperty("customPreferences"));
            Properties customProps = null;
            if (customPropertiesRes != null) {
                customProps = loadProperties(Res.get(customPropertiesRes));
            }
            DEFAULT_AND_CUSTOM = new Prefs(defProps, customProps);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not load default or custom properties: Default is "
                            + defaultPropertiesRes + " and Custom is "
                            + customPropertiesRes);
        }
    }

    /**
     * Load the standard preferences which are the default preferences, then
     * custom preferences (if any), TestNG preferences, and finally System
     * preferences (which includes any commandline preferences).
     * 
     * @param prefsMap
     *            The TestNG preferences as a map. If null then they will be
     *            skipped.
     * @return the Prefs.
     */
    public static Prefs standardPrefsFromMap(Map<String, String> prefsMap) {
        Properties testNgProps = new Properties();
        if (prefsMap != null) {
            testNgProps.putAll(prefsMap);
        }
        Prefs prefs = new Prefs(DEFAULT_AND_CUSTOM, testNgProps,
                System.getProperties());
        return prefs;
    }

    /**
     * Load the standard preferences which are the default preferences, then
     * custom preferences (if any), TestNG preferences, and finally System
     * preferences (which includes any commandline preferences).
     * 
     * @param testNgPrefs
     *            The TestNG xmlTest which contains the TestNG preferences. If
     *            null then they will be skipped.
     * @return the Prefs.
     */
    public static Prefs standardPrefs(XmlTest testNgPrefs) {
        if (testNgPrefs != null) {
            return Prefs.standardPrefsFromMap(testNgPrefs.getParameters());
        }
        return Prefs.standardPrefsFromMap(null);
    }

    /**
     * Loads preferences from an array of URLs, which can either be files, files
     * inside a jar, or resources located on remote servers. The files must
     * either be java properties files or java xml properties files. Any
     * properties listed first may be overwritten by later properties whose keys
     * match.
     * 
     * @param propsUrls
     *            The properties to use, from least important to most important.
     *            If any are null they will be skipped.
     * @throws IOException
     *             when a properties file could not be loaded.
     */
    public Prefs(URL... propsUrls) throws IOException {
        this(null, propsUrls);
    }

    /**
     * Construct preferences from an array of properties, from least important
     * to most important, in such a fashion that a property previously mentioned
     * may be overwritten by a later instance of that property.
     * 
     * @param props
     *            The properties to use, from least important to most important.
     */
    public Prefs(Properties... props) {
        this(null, props);
    }

    /**
     * Loads preferences from an array of URLs, which can either be files, files
     * inside a jar, or resources located on remote servers, and will overwrite
     * anything from the old preferences. The files must either be java
     * properties files or java xml properties files. Any properties listed
     * first may be overwritten by later properties whose keys match.
     * 
     * @param oldPrefs
     *            The old preferences which will be used as a base. Any
     *            preferences lists may get overwritten by properties loaded
     *            from the URLs.
     * @param propsUrls
     *            The properties to use, from least important to most important.
     *            If any are null they will be skipped.
     * @throws IOException
     *             when a properties file could not be loaded.
     */
    public Prefs(Prefs oldPrefs, URL... propsUrls) throws IOException {
        this(oldPrefs, Prefs.loadProperties(propsUrls));
    }

    /**
     * Construct preferences from an array of properties, from least important
     * to most important, in such a fashion that a property previously mentioned
     * may be overwritten by a later instance of that property, and will
     * overwrite anything from the old preferences.
     * 
     * @param oldPrefs
     *            The old preferences which will be used as a base. Any
     *            preferences lists may get overwritten by properties loaded
     *            from the URLs.
     * @param props
     *            The properties to use, from least important to most important.
     */
    public Prefs(Prefs oldPrefs, Properties... props) {
        Properties internalProps = new Properties();
        if (oldPrefs != null) {
            overwriteProperties(internalProps, oldPrefs.prefs);
        }

        for (Properties prop : props) {
            if (prop == null) {
                continue;
            }
            overwriteProperties(internalProps, prop);
        }

        prefs = internalProps;
    }

    /**
     * Return the environment under test. This should be defined according to
     * the requirements of the project, but examples may include "DEV" for
     * development, "CI" for continuous integration, "QA" for quality assurance,
     * "Stage" for staging, "PROD" for production, etc.
     * <p>
     * Property: environment
     * 
     * @return
     */
    public String environment() {
        return getProperty("environment");
    }

    /**
     * Return the list of browsers to use in the testing.
     * <p>
     * Property: browsers
     * <p>
     * Values: One or more of the following values can be used. Each value
     * should be separated by a comma:
     * <ul>
     * <li>*chrome - Local Firefox</li>
     * <li>*googlechrome - Local Google Chrome</li>
     * <li>*iehta - Local Internet Explorer</li>
     * <li>firefox - Local Firefox</li>
     * <li>google - Local Google Chrome</li>
     * <li>ie - Local Internet Explorer</li>
     * </ul>
     * <p>
     * Example: {@code browsers = *chrome, google, ie}
     * 
     * @return A list of browsers to use in testing.
     */
    public List<Browser> browsers() {
        return Browser.browsersFromList(getProperty("browsers"));
    }

    /**
     * The maximum number of drivers to run at once.
     * <p>
     * Property: maxConcurrentDrivers
     * <p>
     * Values: positive integer.
     * 
     * @return The maximum number of tests to run at once.
     */
    public int maxConcurrentDrivers() {
        return Integer.parseInt(getProperty("maxConcurrentDrivers"));
    }

    /**
     * The maximum number of local Firefox browsers to run.
     * <p>
     * Property: maxLocalFirefox
     * <p>
     * Values: positive integer. Note: as of Firefox 18 and Selenium 2.29, the
     * most simultaneous Firefox instances on one machine is 4 based upon my
     * observations.
     * 
     * @return The maximum number of Firefox browsers to run locally.
     */
    public int maxLocalFirefox() {
        return Integer.parseInt(getProperty("maxLocalFirefox"));
    }

    /**
     * The maximum number of local Chrome browsers to run.
     * <p>
     * Property: maxLocalChrome
     * <p>
     * Values: positive integer.
     * 
     * @return The maximum number of Chrome browsers to run locally.
     */
    public int maxLocalChrome() {
        return Integer.parseInt(getProperty("maxLocalChrome"));
    }
    
    /**
     * The maximum number of local PhantomJS instances to run.
     * <p>
     * Property: maxLocalPhantomJS
     * <p>
     * Values: positive integer.
     * 
     * @return The maximum number of PhantomJS instances to run locally.
     */
    public int maxLocalPhantomJS(){
        return Integer.parseInt(getProperty("maxLocalPhantomJS"));
    }
    
    /**
     * Returns a string to use for setting system property for retrieving path to phantomJS
     * @return 
     */
    public String phantomJSPropertyName(){
        return "webdriver.phantom.driver";
    }
    
    public boolean printLogToConsole(){
        return getProperty("printLogToConsole").toUpperCase().equals("TRUE");
    }
    
    /**
     * The maximum number of retries on each test if it fails
     * <p>
     * Property: maxRetryOnFail
     * <p>
     * Values: positive integer
     * 
     * @return The maximum number of retries on each test if it fails
     */
    public int maxRetryOnFail() {
    	return Integer.parseInt(getProperty("maxRetryOnFail"));
    }
    
    public long findTimeoutMS(){
    	return Long.parseLong(getProperty("findTimeoutMS"));
    }
    
    public long findSearchThresholdMS(){
    	if(getProperty("searchThresholdMS") != null){
    		return Long.parseLong(getProperty("searchThresholdMS"));
    	}
    	else{
    		return findTimeoutMS() / 2;
    	}
    }

    /**
     * The maximum number of local InternetExplorer browsers to run.
     * <p>
     * Property: maxLocalInternetExplorer
     * <p>
     * Values: positive integer. Note: as of Firefox 18 and Selenium 2.29, there
     * CAN be more than one simultaneous IE instance, but may not be stable. See
     * <a href=
     * "http://code.google.com/p/selenium/wiki/InternetExplorerDriver#Multiple_instances_of"
     * >http://code.google.com/p/selenium/wiki/InternetExplorerDriver#
     * Multiple_instances_of</a> for more information.
     * 
     * @return The maximum number of Internet Explorer browsers to run locally.
     */
    public int maxLocalInternetExplorer() {
        return Integer.parseInt(getProperty("maxLocalInternetExplorer"));
    }

    /**
     * Return a property value associated with the key. Generally this is
     * expected to be used internally by Prefs or by classes extending from
     * Prefs.
     * 
     * @param key
     *            Retrieve the property associated with the key.
     * @return The property value.
     */
    public String getProperty(String key) {
        return prefs.getProperty(key);
    }

    /**
     * Overwrites old properties and adds any previously unspecified properties.
     * 
     * @param target
     *            The target properties to overwrite or add values.
     * @param newProperties
     *            The new properties.
     */
    private static void overwriteProperties(Properties target,
            Properties newProperties) {
        for (Entry<Object, Object> entry : newProperties.entrySet()) {
            target.setProperty((String) entry.getKey(),
                    (String) entry.getValue());
        }
    }

    /**
     * Loads the properties from a list of URLs in the order given. Anything
     * from earlier properties may be overwritten by a later properties. The
     * properties may be java properties format *.properties or *.xml.
     * 
     * @param propsUrls
     *            Load the properties from these URLs. If any URLs are null they
     *            will be skipped.
     * @return the properties that were loaded.
     * @throws IOException
     *             If at least one of the URLs could not be loaded.
     */
    private static Properties loadProperties(URL... propsUrls)
            throws IOException {
        Properties result = new Properties();
        for (int i = 0; i < propsUrls.length; ++i) {
            if (propsUrls[i] == null) {
                continue;
            }
            Properties prop = new Properties();
            InputStream is = propsUrls[i].openStream();
            if (propsUrls[i].getPath().toLowerCase().endsWith("xml")) {
                prop.loadFromXML(is);
            } else {
                prop.load(is);
            }
            is.close();
            overwriteProperties(result, prop);
        }

        return result;
    }

}
