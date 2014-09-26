package examplesite.tests;

import org.testng.annotations.Test;

import xelenium.Retry;
import examplesite.core.EsFace;
import examplesite.core.ExampleSiteBaseTest;
import examplesite.pages.CompanyAboutPage;
import examplesite.pages.HomePage;

public class CompanyPageTest extends ExampleSiteBaseTest {

    @Test(dataProvider = MULTIBROWSER,retryAnalyzer=Retry.class)
    public void testHomeToCompanyPage(EsFace ef) {
        CompanyAboutPage about = HomePage.open(ef).clickCompanyLink();
        assertEquals(about.getTitle("About Xpanxion"), "About Xpanxion");
    }

    @Test(dataProvider = MULTIBROWSER,retryAnalyzer=Retry.class)
    public void testCompanyPageHeader(EsFace ef) {
        CompanyAboutPage about = CompanyAboutPage.open(ef);

        assertEquals(about.getHeadingText(), "A LITTLE MORE ABOUT US");
    }
    
    @Test(dataProvider = MULTIBROWSER,retryAnalyzer=Retry.class)
	public void testAboutPageSearch(EsFace ef){
    	CompanyAboutPage about = CompanyAboutPage.open(ef);
    	about.search("Xpanxion").clickSearchResult("Xpanxion: Software Consulting Services", HomePage.class).VerifyPage();
	}
}
