package examplesite.pages;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;
import org.jbehave.web.selenium.WebDriverProvider;

public class Pages {
	 
    //private final WebDriverProvider driverProvider;
    private final Selenium selenium;
    private final ConditionRunner conditionRunner;
    private Home home;
    private Clients clients;
    private Company company;
 
    public Pages(Selenium selenium, ConditionRunner conditionRunner) {

        //this.driverProvider = driverProvider;
        this.selenium = selenium;
        this.conditionRunner = conditionRunner;
    }
 
    public Home home(){
        if ( home == null ){
            home = new Home(selenium, conditionRunner);
        }
        return home;
    }
 
/*    public Clients clients() {
        if ( clients == null ){
            clients = new Clients(driverProvider);

        }
        return clients;
    }
    
    public Company company() {
        if ( company == null ){
            company = new Company(driverProvider);

        }
        return company;
    }*/
     
}