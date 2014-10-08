package examplesite.pages;

import org.jbehave.web.selenium.WebDriverProvider;

public class Home extends AbstractPage{
	
	@FindBy(how = How.NAME, using = "username")
	 private WebElement userNameElem;
	 
	 @FindBy(how = How.NAME, using = "password")
	 private WebElement passwordElem;
	 
	 @FindBy(how = How.NAME, using = "role")
	 private WebElement roleElem;
	 
	 @FindBy(how = How.NAME, using = "B3")
	 private WebElement submitButton;

	public Home(WebDriverProvider driverProvider) {
		super(driverProvider);
	}
	
	public void open()
	{
		this.get("http://www.xpanxion.com/");
	}

}
