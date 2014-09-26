package examplesite.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import examplesite.core.EsFace;


public class CompanyAboutPage extends ExampleSitePage {

	private PageObjects objs;
	
	public static CompanyAboutPage open(EsFace face) {
		face.go().get("http://www.xpanxion.com/about_us.html");
		CompanyAboutPage page = new CompanyAboutPage(face);
		page.VerifyPage();
		return page;
	}

	public CompanyAboutPage(EsFace face) {
		super(face);
		objs = new PageObjects();
	}
	
	@Override
	public void VerifyPage(){
                face().log("Verifying Company Page...");
		super.VerifyPage();
	}
	
	public String getHeadingText() {
                face().log("Getting Heading Text...");
		return objs.getHeadingElement().getText();
	}
	
	private class PageObjects extends BasePageObjects{
		
		public WebElement getHeadingElement(){
			return findElement(By.className("heading"));
		}
		
		
	}
}
