package xelenium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Enumerates available browsers for testing.
 */
public enum Browser {
    FIREFOX(BaseTest.NONTEST_PREFS.maxLocalFirefox(), "*chrome", "firefox"),
    CHROME(BaseTest.NONTEST_PREFS.maxLocalChrome(), "*googlechrome", "google",
            "chrome"),
    IE(BaseTest.NONTEST_PREFS.maxLocalInternetExplorer(), "*iehta", "ie"),
    PHANTOMJS(BaseTest.NONTEST_PREFS.maxLocalPhantomJS(), "phantomjs", "phantom", "js");

    private String[] aliases;
    private Semaphore maxLocalInstances;

    /**
     * Constructs a browser with the associated aliases.
     * 
     * @param browserAliases
     *            aliases which can be used in the
     *            {@link #browsersFromList(String)} method.
     */
    Browser(int maxInstances, String... browserAliases) {
        aliases = browserAliases;
        maxLocalInstances = new Semaphore(maxInstances, true);
    }

    /**
     * Some browsers can be run locally, but there may be a limit to how many
     * instances are allowed. Call this before instantiating the browser.
     */
    public void acquireLocal() {
        try {
            maxLocalInstances.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted waiting for " + toString(),
                    e);
        }
    }

    /**
     * Some browsers can be run locally, but there may be a limit to how many
     * instances are allowed. Call this after finishing the browser instance.
     */
    public void releaseLocal() {
        maxLocalInstances.release();
    }

    private static Map<String, Browser> nameToBrowser = new HashMap<String, Browser>();

    static {
        for (Browser browser : Browser.values()) {
            for (String name : browser.aliases) {
                nameToBrowser.put(name, browser);
            }
        }
    }

    /**
     * Get a list of browsers based on a string list, separated by commas.
     * <p>
     * Example: "*chrome, ie, google"
     * 
     * @param list
     *            The list of browser aliases to use.
     * @return the list of browsers.
     */
    public static List<Browser> browsersFromList(String list) {
        List<Browser> result = new ArrayList<Browser>();

        if (list == null || list.trim().isEmpty()) {
            return result;
        }

        for (String name : list.split("\\s*,\\s*")) {
            Browser browser = nameToBrowser.get(name);

            if (browser != null) {
                result.add(browser);
            }
        }

        return result;
    }
}
