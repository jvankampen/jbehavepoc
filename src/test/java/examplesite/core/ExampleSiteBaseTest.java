package examplesite.core;

import xelenium.BaseTest;
import xelenium.Browser;
import xelenium.Prefs;

/**
 * The base test structure for all tests for this site.
 */
public class ExampleSiteBaseTest extends BaseTest<EsFace> {

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
     *            The preferences used for the test.
     * @return The face used in the test.
     */
    @Override
    protected EsFace createFace(Browser browser, int id,
            String testName, Prefs prefs) {
        return new EsFace(browser, id, testName, prefs);
    }
}
