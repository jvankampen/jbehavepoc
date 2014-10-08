package jbehavepoc.stories;

import mx4j.log.Logger;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import examplesite.core.EsFace;
import examplesite.pages.HomePage;
import examplesite.pages.Pages;

public class HomePageSteps {
	
	 private final Pages pages;
	 
	    public HomePageSteps(Pages pages) {
	        this.pages = pages;
	    }




    @Given("Xpanxion Home")
	public void xpanxionHome() {
		homepageLaunch();
	}
	
	@When("I launch www.xpanxion.com")
	public void homepageLaunch() {
		pages.home().open();
	}
	  
	/*@When("I search for $searchTerm")
	public void performSearch(String searchTerm) {
		logger.info("Searching for " + searchTerm);
		home.search(searchTerm);
	}	*/	
		  
	
	  @Then("xpanxion home page should be displayed")
	  public void homepageVerify(EsFace ef)
	  {
		  HomePage home = HomePage.open(ef);
	home.VerifyPage();
	  }
	  
	  @Then("I should get a search result of Xpanxion: Software Consulting Services") 
	  public void searchVerify() {
	  }
}
