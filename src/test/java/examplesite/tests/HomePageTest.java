package examplesite.tests;

import org.testng.annotations.Test;

import jbehavepoc.Retry;
import examplesite.core.EsFace;
import examplesite.core.ExampleSiteBaseTest;
import examplesite.pages.CompanyAboutPage;
import examplesite.pages.HomePage;

public class HomePageTest extends ExampleSiteBaseTest {

	//@Test(dataProvider = IEBROWSER,retryAnalyzer=Retry.class)
	public void testHomePageOpens(EsFace ef) {
		HomePage home = HomePage.open(ef);
		assertEquals(home.getTitle(),
				"Software Consulting Services | Xpanxion");
	}

	//@Test(dataProvider = MULTIBROWSER,retryAnalyzer=Retry.class)
	public void testHomePageNewsListing(EsFace ef) {
		HomePage home = HomePage.open(ef);
		assertTrue(home.getNewsListingText().contains("Xpanxion"));
	}
	
	@Test(dataProvider = MULTIBROWSER,retryAnalyzer=Retry.class)
	public void testHomePageSearch(EsFace ef){
		HomePage home = HomePage.open(ef);
		home.search("Xpanxion");
	}
}
