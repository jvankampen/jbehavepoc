package jbejavepoc.config;

import static java.util.Arrays.asList;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.web.selenium.WebDriverHtmlOutput.WEB_DRIVER_HTML;

import java.util.List;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.failures.PassingUponPendingStep;
import org.jbehave.core.failures.RethrowingFailure;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.StepMonitor;
import org.jbehave.web.selenium.ContextView;
import org.jbehave.web.selenium.SeleniumConfiguration;
import org.jbehave.web.selenium.SeleniumContext;
import org.jbehave.web.selenium.SeleniumContextOutput;
import org.jbehave.web.selenium.WebDriverProvider;
import org.testng.annotations.Test;

public class XpanxionJbehaveStories extends JUnitStories {
    private WebDriverProvider driverProvider;
    private boolean shouldDoDryRun = false;
    private ContextView contextView;
    private StepMonitor stepMonitor;

	private SeleniumContext seleniumContext = new SeleniumContext() {

        ThreadLocal<String> currentScenario = new ThreadLocal<String>();

        @Override
        public String getCurrentScenario() {
            return currentScenario.get();
        }

        @Override
        public void setCurrentScenario(String currentScenario) {
            this.currentScenario.set(currentScenario);
     
        }
	};

	public XpanxionJbehaveStories() {
        Class<?> embeddableClass = this.getClass();
        CrossReference crossReference = new CrossReference().withJsonOnly().withOutputAfterEachStory(true)
                .excludingStoriesWithNoExecutedScenarios(true);
        Format[] formats = new Format[] { new SeleniumContextOutput(seleniumContext), CONSOLE, WEB_DRIVER_HTML };


        Configuration configuration = new SeleniumConfiguration()
                .useWebDriverProvider(driverProvider)
                .useSeleniumContext(seleniumContext)
                .usePendingStepStrategy(shouldDoDryRun ? new PassingUponPendingStep() : new FailingUponPendingStep())
                .useParameterConverters(new ParameterConverters(true))
                .useFailureStrategy(new RethrowingFailure())
                .doDryRun(shouldDoDryRun)
                .useStepMonitor(stepMonitor)
                .useStoryLoader(new LoadFromClasspath(embeddableClass.getClassLoader()))
                .useStoryReporterBuilder(
                        new StoryReporterBuilder()
                        .withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass))
                        .withFailureTraceCompression(true).withDefaultFormats().withFormats(formats)
                        .withCrossReference(crossReference));

        useConfiguration(configuration);
		
	}


	protected List<String> storyPaths() {
		return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()).getFile(),
                asList("**/" + System.getProperty("storyFilter", "*") + ".story"), null);
	}
	


}
