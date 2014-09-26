package xelenium;

import org.slf4j.Logger;

/**
 * Class which provides browser manipulation and other features useful for a
 * test.
 */
public interface TestFace<U, V extends Prefs> {

	/**
	 * Access the driver. Generally it should be used by Pages and other helper
	 * classes, but not by the tests directly.
	 * 
	 * @return the driver.
	 */
	U go();

	/**
	 * Print output to TestNG Reporter.
	 * 
         * @param output
	 */
	void log(String output);

	/**
	 * Access the preferences which can be general and/or specific to the test.
	 * 
	 * @return the preferences.
	 */
	V prefs();

	/**
	 * Get the identifier of the test.
	 * 
	 * @return Test identifier.
	 */
	long id();

	/**
	 * Get the name of the test. May be blank or null.
	 * 
	 * @return Test name.
	 */
	String name();

	/**
	 * Release any resources that are no longer needed at the conclusion of the
	 * test.
	 */
	void zap();

}
