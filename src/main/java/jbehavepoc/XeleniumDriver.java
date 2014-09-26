package xelenium;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.ErrorHandler;
import org.openqa.selenium.remote.FileDetector;
import org.openqa.selenium.remote.RemoteStatus;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

public class XeleniumDriver <DriverClass extends RemoteWebDriver>{
	private RemoteWebDriver _driver;
	private BaseTestFace<XeleniumDriver<DriverClass>,Prefs> _face;
	
	private RemoteWebDriver driver(Class<DriverClass> driverClass){
		try {
                    if(driverClass == PhantomJSDriver.class){
                        DesiredCapabilities sCaps = DesiredCapabilities.phantomjs();
                        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, System.getProperty(_face.prefs().phantomJSPropertyName()));
                        _driver = new PhantomJSDriver(sCaps);
                        manage().window().setSize(
                            new Dimension(1024, 768));
                    }else if(driverClass == FirefoxDriver.class){
			_driver = new FirefoxDriver();
                    }else{
                        _driver = driverClass.newInstance();
                    }
		} catch (Throwable e){
			throw new XeleniumException(e, _face);
		}
		return _driver;
	}
        
        /**
         * Method to get the base driver of this XeleniumDriver instance
         * @return 
         */
        public RemoteWebDriver getBaseDriver(){
            return _driver;
        }
	
	private BaseTestFace<XeleniumDriver<DriverClass>,Prefs> face(BaseTestFace<XeleniumDriver<DriverClass>,Prefs> face){
		_face = face;
		return _face;
	}
	
	public XeleniumDriver(Class<DriverClass> driverClass, BaseTestFace<XeleniumDriver<DriverClass>,Prefs> face){
            face(face);
            driver(driverClass);
	}
	
	public void quit(){
		_driver.quit();
	}
	
	public void close(){
		_driver.close();
	}
	
	/**
	 * Method that encapsulates RemoteWebDriver.findElement(By by) to time the search and log any searches that take
	 * longer than the SearchThreshold specified within the .properties file.
	 * @param by The By object that contains all search parameters
	 * @return the WebElemnt that matches the search parameters passed.
	 */
	public WebElement findElement(By by){
		_driver.manage().timeouts().implicitlyWait(_face.prefs().findTimeoutMS(), TimeUnit.MILLISECONDS);
		long startMs = System.currentTimeMillis();
		WebElement element = _driver.findElement(by);
		long endMs = System.currentTimeMillis();
		long searchTime = endMs - startMs;
		if(searchTime >= _face.prefs().findSearchThresholdMS()){
			_face.log("Item Found.  Search Time: " + searchTime + "ms.");
		}
		return element;
	}
	
	/**
	 * Method that encapsulates RemoteWebDriver.findElements(By by) to time the search and log any searches that take
	 * longer than the SearchThreshold specified within the .properties file.
	 * @param by The By object that contains all search parameters
	 * @return the List of WebElemnts that match the search parameters passed.
	 */
	public List<WebElement> findElements(By by){
		_driver.manage().timeouts().implicitlyWait(_face.prefs().findTimeoutMS(), TimeUnit.MILLISECONDS);
		long startMs = System.currentTimeMillis();
		List<WebElement> elements = _driver.findElements(by);
		long endMs = System.currentTimeMillis();
		long searchTime = endMs - startMs;
		if(searchTime >= _face.prefs().findSearchThresholdMS()){
			_face.log("Item Found.  Search Time: " + searchTime + "ms.");
		}
		return elements;
	}
	/**
	 * Method to switch the base driver to the specified window
	 * @param windowNameOrHandle
	 * @return
	 */
	public XeleniumDriver<DriverClass> switchToWindow(String windowNameOrHandle){
		_driver.switchTo().window(windowNameOrHandle);
		return this;
	}
	/**
	 * Method to switch the base driver to the specified frame
	 * @param frameIndex
	 * @return
	 */
	public XeleniumDriver<DriverClass> switchToFrame(int frameIndex){
		_driver.switchTo().frame(frameIndex);
		return this;
	}
	/**
	 * Method to switch the base driver to the specified frame
	 * @param frameName
	 * @return
	 */
	public XeleniumDriver<DriverClass> switchToFrame(String frameName){
		_driver.switchTo().frame(frameName);
		return this;
	}
	/**
	 * Set the file detector to be used when sending keyboard input. By default, this is set to a file
	 * detector that does nothing.
	 * @param detector The detector to use. Must not be null.
	 */
	public void setFileDetector(FileDetector detector){
		_driver.setFileDetector(detector);
	}
	
	public SessionId getSessionId(){
		return _driver.getSessionId();
	}
	
	public ErrorHandler getErrorHandler(){
		return _driver.getErrorHandler();
	}
	
	public CommandExecutor getCommandExecutor(){
		return _driver.getCommandExecutor();
	}
	
	public Capabilities getCapabilities(){
		return _driver.getCapabilities();
	}
	
	public void get(String url){
            _face.log("Navigating to: "+url);
            _driver.get(url);
	}
	
	public RemoteStatus getRemoteStatus(){
		return _driver.getRemoteStatus();
	}
	
	public String getTitle(){
		return _driver.getTitle();
	}
	
	public String getCurrentUrl(){
		return _driver.getCurrentUrl();
	}
	
	public WebElement findElementById(String id){
		return findElement(By.id(id));
	}
	public List<WebElement> findElementsById(String id){
		return findElements(By.id(id));
	}
	
	public WebElement findElementByLinkText(String text){
		return findElement(By.linkText(text));
	}
	public List<WebElement> findElementsByLinkText(String text){
		return findElements(By.linkText(text));
	}
	
	public WebElement findElementByPartialLinkText(String partialText){
		return findElement(By.partialLinkText(partialText));
	}
	public List<WebElement> findElementsByPartialLinkText(String partialText){
		return findElements(By.partialLinkText(partialText));
	}
	
	public WebElement findElementByTagName(String tagName){
		return findElement(By.tagName(tagName));
	}
	public List<WebElement> findElementsByTagName(String tagName){
		return findElements(By.tagName(tagName));
	}
	
	public WebElement findElementByName(String name){
		return findElement(By.name(name));
	}
	public List<WebElement> findElementsByName(String name){
		return findElements(By.name(name));
	}
	
	public WebElement findElementByClassName(String className){
		return findElement(By.className(className));
	}
	public List<WebElement> findElementsByClassName(String className){
		return findElements(By.className(className));
	}
	
	public WebElement findElementByCssSelector(String cssSelector){
		return findElement(By.cssSelector(cssSelector));
	}
	public List<WebElement> findElementsByCssSelector(String cssSelector){
		return findElements(By.cssSelector(cssSelector));
	}
	
	public WebElement findElementByXPath(String xpath){
		return findElement(By.xpath(xpath));
	}
	public List<WebElement> findElementsByXPath(String xpath){
		return findElements(By.xpath(xpath));
	}
	
	public String getPageSource(){
		return _driver.getPageSource();
	}
	
	public Set<String> getWindowHandles(){
		return _driver.getWindowHandles();
	}
	
	public String getWindowHandle(){
		return _driver.getWindowHandle();
	}
	
	public Object executeScript(String script, Object... args){
		return _driver.executeScript(script, args);
	}
	
	public Object executeAsyncScript(String script, Object... args){
		return _driver.executeAsyncScript(script, args);
	}
	
	public TargetLocator switchTo(){
		return _driver.switchTo();
	}
	
	public Navigation navigate(){
		return _driver.navigate();
	}
	
	public Options manage(){
		return _driver.manage();
	}
	
	public void setLogLevel(Level level){
		_driver.setLogLevel(level);
	}
	
	public Keyboard getKeyboard(){
		return _driver.getKeyboard();
	}
	
	public Mouse getMouse(){
		return _driver.getMouse();
	}
	
	public FileDetector getFileDetector(){
		return _driver.getFileDetector();
	}
}