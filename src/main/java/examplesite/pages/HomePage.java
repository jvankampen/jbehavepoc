package examplesite.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import examplesite.core.EsFace;

public class HomePage extends ExampleSitePage {

	private PageObjects objs;
	
	public static HomePage open(EsFace face) {
		face.go().get("http://www.xpanxion.com");
		HomePage page = new HomePage(face);
		page.VerifyPage();
		return page;
	}

	public HomePage(EsFace face) {
		super(face);
		objs = new PageObjects();
	}
	
	@Override
	public void VerifyPage(){
                face().log("Verifying Home Page...");
		super.VerifyPage();
		
	}

	public String getNewsListingText() {
                face().log("Getting News Listing text...");
		return objs.getNewsListingElement().getText();
	}
	
	private class PageObjects extends BasePageObjects{
		
		public WebElement getNewsListingElement(){
			return findElement(By.className("news_listing"));
		}
	}
}
