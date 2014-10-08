package examplesite.pages;

import org.jbehave.web.selenium.WebDriverProvider;

public class Pages {
	 
    private final WebDriverProvider driverProvider;
    private Home home;
    private Clients clients;
    private Company company;
 
    public Pages(WebDriverProvider driverProvider) {
        this.driverProvider = driverProvider;
    }
 
    public Home home(){
        if ( home == null ){
            home = new Home(driverProvider);
        }
        return home;
    }
 
    public Clients clients() {
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
    }
     
}