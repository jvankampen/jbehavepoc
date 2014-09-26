package jbehavepoc.steps;

import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import examplesite.core.EsFace;
import examplesite.pages.HomePage;

public class HomePageSteps {
	
	EsFace ef;
	HomePage home;
	
	  @When("I launch www.xpanxion.com")
	    public void homepageLaunch() {
			 home = HomePage.open(ef);
			
	    }
	
	  @Then("xpanxion home page should be displayed")
	  public void homepageVerify()
	  {
	  home.VerifyPage();
	  }
}
