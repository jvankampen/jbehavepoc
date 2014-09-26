package examplesite.pages;

//import java.util.List;

import java.util.List;

import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import xelenium.Page;
import xelenium.XeleniumException;
import examplesite.core.EsFace;
import ch.lambdaj.collection.LambdaCollections;
import ch.lambdaj.Lambda;

public class ExampleSitePage extends Page<EsFace>{
	
	protected BasePageObjects baseObjs;
	
	public ExampleSitePage(EsFace face) {
		super(face);
		baseObjs = new BasePageObjects();
	}
	
	public HomePage clickHomePageLink() {
                face().log("Clicking Home Page Link...");
		baseObjs.getHomePageLink().click();
		HomePage homePage = new HomePage(face());
		homePage.VerifyPage();
		return homePage;
	}

	public CompanyAboutPage clickCompanyLink() {
                face().log("Clicking Company Link...");
		baseObjs.getCompanyLink().click();
		CompanyAboutPage companyPage = new CompanyAboutPage(face());
		companyPage.VerifyPage();
		return companyPage;
	}
	
	public ExampleSitePage search(String text){
                face().log("Searching for: "+text+" using search box...");
		baseObjs.getSearchBox().clear();
		baseObjs.getSearchBox().sendKeys(text);
		baseObjs.getSearchButton().click();
		baseObjs.getSearchResultsParent();
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public <T extends Page> T clickSearchResult(String resultText, Class<T> pageClass){
                face().log("Clicking search result: "+resultText+"...");
		baseObjs.getSearchResultLink(resultText).click();
		try {
			return pageClass.getConstructor(EsFace.class).newInstance(face());
		} catch (Throwable e) {
		}
		return null;
	}
	
	//region Global Verification Methods
	public void VerifyNavBar(){
            try{
                face().log("Verifying Navigation Bar...");
		verifyElementVisible(baseObjs.getCompanyLink());
		verifyElementVisible(baseObjs.getRuralSourcingLink());
		verifyElementVisible(baseObjs.getGlobalOperationsLink());
		verifyElementVisible(baseObjs.getServicesLink());
		verifyElementVisible(baseObjs.getClientsLink());
		verifyElementVisible(baseObjs.getNewsAndResourcesLink());
		verifyElementVisible(baseObjs.getCareersLink());
		verifyElementVisible(baseObjs.getContactLink());
            }catch(Throwable e){
                throw new XeleniumException(e,face());
            }
	}
	
	public void VerifySearchBox(){
            try{
                face().log("Verifying Search Box...");
		verifyElementVisible(baseObjs.getSearchBox());
		verifyElementVisible(baseObjs.getSearchButton());
            }catch(Throwable e){
                throw new XeleniumException(e,face());
            }
	}
	
	public void VerifyPage(){
		verifyElementVisible(baseObjs.getHomePageLink());
		VerifyNavBar();
		VerifySearchBox();
	}
	//endregion
	
	protected class BasePageObjects{
		
		public WebElement getHomePageLink(){
			return findElement(By.cssSelector(".logo a"));
		}
		
		//region Navigation Bar
		public WebElement getCompanyLink(){
			return findElement(By.xpath("//header[@id='main-header']//a[.='Company']"));
		}
		
		public WebElement getRuralSourcingLink(){
			return findElement(By.xpath("//header[@id='main-header']//a[.='Rural Sourcing']"));
		}
		
		public WebElement getGlobalOperationsLink(){
			return findElement(By.xpath("//header[@id='main-header']//a[.='Global Operations']"));
		}
		
		public WebElement getServicesLink(){
			return findElement(By.xpath("//header[@id='main-header']//a[.='Services']"));
		}
		
		public WebElement getClientsLink(){
			return findElement(By.xpath("//header[@id='main-header']//a[.='Clients']"));
		}
		
		public WebElement getNewsAndResourcesLink(){
			return findElement(By.xpath("//header[@id='main-header']//a[contains(.,'News')][contains(.,'Resources')]"));
		}
		
		public WebElement getCareersLink(){
			return findElement(By.xpath("//header[@id='main-header']//a[.='Careers']"));
		}
		
		public WebElement getContactLink(){
			return findElement(By.xpath("//header[@id='main-header']//a[.='Contact']"));
		}
		//endregion
		
		//region Search
		public WebElement getSearchBox(){
			return findElement(By.id("gsc-i-id1"));
		}
		
		public WebElement getSearchButton(){
			return findElement(By.cssSelector("input.gsc-search-button"));
		}
		
		public WebElement getSearchResultsParent(){
			return findElement(By.cssSelector("div.gsc-resultsbox-visible"));
		}
		
		public WebElement getSearchResultLink(String linkText){
			return findElement(By.xpath("//table[@class='gsc-table-result']//a[@class='gs-title'][contains(.,'"+linkText+"')]"));
		}
		//endregion
	}
}
