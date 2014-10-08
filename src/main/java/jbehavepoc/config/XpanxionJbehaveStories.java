
package jbehavepoc.config;

import static java.util.Arrays.asList;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.web.selenium.WebDriverHtmlOutput.WEB_DRIVER_HTML;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import examplesite.pages.Pages;
import jbehavepoc.stories.HomePageSteps;

import org.jbehave.core.embedder.MetaFilter;


import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.PrintStreamEmbedderMonitor;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.failures.PassingUponPendingStep;
import org.jbehave.core.failures.RethrowingFailure;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.Meta;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.core.steps.StepMonitor;
import org.jbehave.core.Embeddable;
import org.jbehave.core.InjectableEmbedder;
import org.jbehave.web.selenium.ContextView;
import org.jbehave.web.selenium.LocalFrameContextView;
import org.jbehave.web.selenium.PerStoriesWebDriverSteps;
import org.jbehave.web.selenium.PropertyWebDriverProvider;
import org.jbehave.web.selenium.SeleniumConfiguration;
import org.jbehave.web.selenium.SeleniumContext;
import org.jbehave.web.selenium.SeleniumContextOutput;
import org.jbehave.web.selenium.SeleniumStepMonitor;
import org.jbehave.web.selenium.WebDriverProvider;
import org.jbehave.web.selenium.WebDriverScreenshotOnFailure;
import org.jbehave.web.selenium.WebDriverSteps;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;
//import org.testng.annotations.Test;
import org.junit.Test;

import com.google.common.util.concurrent.MoreExecutors;


public class XpanxionJbehaveStories extends JUnitStories {

	private WebDriverProvider driverProvider = new PropertyWebDriverProvider();
    private Selenium selenium = SeleniumConfiguration.defaultSelenium();
    private ConditionRunner conditionRunner = SeleniumConfiguration.defaultConditionRunner(selenium);
    private WebDriverSteps lifecycleSteps = new PerStoriesWebDriverSteps(driverProvider); 
    private SeleniumContext context = new SeleniumContext();
    private ContextView contextView = new LocalFrameContextView().sized(500, 100);
    //private Pages pages = new Pages(driverProvider);
    private Pages pages = new Pages(selenium, conditionRunner);
     
    public XpanxionJbehaveStories() {
        if ( lifecycleSteps instanceof PerStoriesWebDriverSteps ){
            configuredEmbedder().useExecutorService(MoreExecutors.sameThreadExecutor());
        }
    }
 
    @Override
    public Configuration configuration() {
        Class<? extends Embeddable> embeddableClass = this.getClass();
        return new SeleniumConfiguration()
                .useSeleniumContext(context)
                .useWebDriverProvider(driverProvider)
                .useStepMonitor(new SeleniumStepMonitor(contextView, context, new SilentStepMonitor()))
                .useStoryLoader(new LoadFromClasspath(embeddableClass))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                    .withCodeLocation(codeLocationFromClass(embeddableClass))
                    .withDefaultFormats()
                    .withFormats(CONSOLE));
    }
 
    @Override
    public InjectableStepsFactory stepsFactory() {
        Configuration configuration = configuration();
        return new InstanceStepsFactory(configuration, 
                new HomePageSteps(pages),
                lifecycleSteps,
                new WebDriverScreenshotOnFailure(driverProvider, configuration.storyReporterBuilder()));
    }
 
	@Override
	protected List<String> storyPaths() {
		
		//return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()).getFile(),
			//	asList("**/" + System.getProperty("storyFilter", "*") + ".story"), null);
		
		return new StoryFinder()
        .findPaths(codeLocationFromClass(this.getClass()).getFile(), asList("**/home.story"), null);
	}
	

	  public static class SameThreadEmbedder extends Embedder {
	         
	        public SameThreadEmbedder() {
	            useExecutorService(MoreExecutors.sameThreadExecutor());
	        }
	
}
}