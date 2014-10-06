package jbehavepoc.steps;

import mx4j.log.Logger;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import examplesite.core.EsFace;
import examplesite.pages.HomePage;

public class HomePageSteps {
	
	EsFace ef;
	HomePage home;
	Logger logger;
	
	@Given("Xpanxion Home")
	public void xpanxionHome() {
		homepageLaunch();
	}
	
	@When("I launch www.xpanxion.com")
	public void homepageLaunch() {
		 home = HomePage.open(ef);
			
	}
	  
	@When("I search for $searchTerm")
	public void performSearch(String searchTerm) {
		logger.info("Searching for " + searchTerm);
		home.search(searchTerm);
	}		
		  
	
	  @Then("xpanxion home page should be displayed")
	  public void homepageVerify()
	  {
	  home.VerifyPage();
	  }
}
