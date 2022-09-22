package mx.com.totalplay.qa.automation.salesforce.residential.activation.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Spring configuration file for integration test, this class provides the spring's context
 * configuration for all dependencies need on the integration tests
 *
 * @version 1.0
 * @author Alejandro Uribe
 */
@Configuration
@PropertySource({
    "classpath:path.properties",
    "classpath:pom.properties",
    "classpath:urls.properties",
    "classpath:generalTexts.properties",
    "classpath:identifiers.properties",
    "classpath:textColors.properties",
    "classpath:reportTexts.properties"
})
@ComponentScan({"mx.com.totalplay.qa.automation.salesforce.residential.activation"})
public class IntegrationPhaseSpringConfiguration {

  private WebDriver driver;

  public IntegrationPhaseSpringConfiguration(){
    SeleniumBrowserConfiguration seleniumConfig = new SeleniumBrowserConfiguration();
    driver = seleniumConfig.configuration("Chrome");
  }

  @Bean(name = "driver")
  public WebDriver getDriver(){
    return driver;
  }

  @Bean(name = "generalTexts")
  public ResourceBundleMessageSource getGeneralTexts(){
    ResourceBundleMessageSource generalTexts = new ResourceBundleMessageSource();
    generalTexts.setBasename("generalTexts");
    generalTexts.setDefaultEncoding("UTF-8");
    return generalTexts;
  }

  @Bean(name = "identifiers")
  public ResourceBundleMessageSource getIdentifiers(){
    ResourceBundleMessageSource identifiers = new ResourceBundleMessageSource();
    identifiers.setBasename("identifiers");
    identifiers.setDefaultEncoding("UTF-8");
    return identifiers;
  }

  @Bean(name = "paths")
  public ResourceBundleMessageSource getPaths(){
    ResourceBundleMessageSource paths = new ResourceBundleMessageSource();
    paths.setBasename("path");
    paths.setDefaultEncoding("UTF-8");
    return paths;
  }

  @Bean(name = "textColors")
  public ResourceBundleMessageSource getTextColors(){
    ResourceBundleMessageSource textColors = new ResourceBundleMessageSource();
    textColors.setBasename("textColors");
    textColors.setDefaultEncoding("UTF-8");
    return textColors;
  }

  @Bean(name = "urls")
  public ResourceBundleMessageSource getUrls(){
    ResourceBundleMessageSource urls = new ResourceBundleMessageSource();
    urls.setBasename("urls");
    urls.setDefaultEncoding("UTF-8");
    return urls;
  }

  @Bean(name = "report")
  public ResourceBundleMessageSource getReportTexts(){
    ResourceBundleMessageSource report = new ResourceBundleMessageSource();
    report.setBasename("reportTexts");
    report.setDefaultEncoding("UTF-8");
    return report;
  }

  /**
   * Get the pom resource bundle
   *
   * @return pom resource bundle
   */
  @Bean(name = "pom")
  public ResourceBundleMessageSource getPom() {
    ResourceBundleMessageSource pom = new ResourceBundleMessageSource();
    pom.setBasename("pom");
    pom.setDefaultEncoding("UTF-8");
    return pom;
  }

  /**
   * Get the mock server
   *
   * @return mock server
   */
  @Bean(name = "mock")
  public WireMockServer getMock() {
    return new WireMockServer(8181);
  }

  /**
   * Get object mapper which convert java object in json character strings
   *
   * @return ObjectMapper
   */
  @Bean(name = "mapper")
  public ObjectMapper getMapper() {
    return new ObjectMapper();
  }
}
