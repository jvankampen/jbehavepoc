package examplesite.pages;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;
import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

public class Home extends AbstractPage{


	@FindBy(how = How.NAME, using = "username")
	 private WebElement userNameElem;
	 
	 @FindBy(how = How.NAME, using = "password")
	 private WebElement passwordElem;
	 
	 @FindBy(how = How.NAME, using = "role")
	 private WebElement roleElem;
	 
	 @FindBy(how = How.NAME, using = "B3")
	 private WebElement submitButton;

	//public Home(WebDriverProvider driverProvider) {
	//	super(driverProvider);
	//}

    public Home(Selenium selenium, ConditionRunner conditionRunner) {
        super(selenium, conditionRunner);
    }
	
	public void open()
	{
		open("http://www.xpanxion.com/");
	}

}
