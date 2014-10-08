package examplesite.pages;

import org.jbehave.web.selenium.WebDriverPage;
import org.jbehave.web.selenium.SeleniumPage;
import org.openqa.selenium.WebDriver;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;
import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.interactions.HasInputDevices;

public abstract class AbstractPage extends SeleniumPage {

	//public AbstractPage(WebDriverProvider driverProvider) {
	//	super(driverProvider);
		// TODO Auto-generated constructor stub
	//}

    public AbstractPage(Selenium driverProvider, ConditionRunner runner) {
        super(driverProvider, runner);
        // TODO Auto-generated constructor stub
    }

}
