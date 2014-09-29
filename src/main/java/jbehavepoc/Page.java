package jbehavepoc;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.testng.Assert;

/**
 * The basic structure of a page.
 * 
 * @param <T>
 *            Provides tools for page interaction and navigation.
 */
@SuppressWarnings("rawtypes")
public class Page<T extends TestFace<XeleniumDriver, ?>> {

	/** Provides tools for page interaction and navigation. */
	private final T tf;

	/**
	 * Creates the page.
	 * 
	 * @param face
	 *            Provides tools for page interaction and navigation.
	 */
	public Page(T face) {
		tf = face;
	}

	/**
	 * Allow page to log any meaningful events.
	 * 
         * @param output
	 */
	public void log(String output) {
		face().log(output);
	}

	/**
	 * Access to the test face, which can be used by tests and any helper
	 * classes, since it interfaces directly with pages.
	 */
	public T face() {
		return tf;
	}
	
	/**
	 * Access to the base RemoteWebDriver instance of the driver for the current test 
	 * @return The base RemoteWebDriver instance of the current test
	 */
	public XeleniumDriver driver() {
		return face().go();
	}
	
	/**
	 * Method that encapsulates RemoteWebDriver.findElement(By by) to time the search and log any searches that take
	 * longer than the SearchThreshold specified within the .properties file.
	 * @param by The By object that contains all search parameters
	 * @return the WebElemnt that matches the search parameters passed.
	 */
	public WebElement findElement(By by){
		return driver().findElement(by);
	}
	
	/**
	 * Method that encapsulates RemoteWebDriver.findElements(By by) to time the search and log any searches that take
	 * longer than the SearchThreshold specified within the .properties file.
	 * @param by The By object that contains all search parameters
	 * @return the List of WebElemnts that match the search parameters passed.
	 */
	@SuppressWarnings("unchecked")
	public List<WebElement> findElements(By by){
		return driver().findElements(by);
	}
	
	/**
	 * Method to get the title of the current page in string form.
	 * @return The title of the current page
	 */
	public String getTitle(){
		return driver().getTitle();
	}
	
	/**
	 * Method to get the title of the current page in string form, waiting for the title returned to be the expected title
	 * This can be used to wait for a webpage to load.
	 * @param expected The title that is expected
	 * @return The current title of the webpage.
	 */
	public String getTitle(String expected){
		long maxRetries = face().prefs().findTimeoutMS() / 250;
		long retries = 0;
		String current = null;
		do{
			current = getTitle();
			if(!current.equals(expected)){
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {}
				if(retries % 4 == 0){
					face().log("Current title: '"+ current + "' not equal to Expected title: '" + expected + "', checking again...");
				}
				retries += 1;
			}
			else{
				retries = maxRetries;
			}
		}while(retries < maxRetries);
		return current;
	}

        /**
         * Method to verify that the specified element is visible on the page
         * @param element 
         */
        public void verifyElementVisible(WebElement element){
            try{
                Assert.assertTrue(new WebDriverWait(driver().getBaseDriver(), face().prefs().findTimeoutMS())
                        .until(ExpectedConditions.visibilityOf(element)).isDisplayed(), "Element is not visible.");
            }catch(Throwable e){
                throw new XeleniumException(e,face());
            }
        }
}
