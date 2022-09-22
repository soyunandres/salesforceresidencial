package mx.com.totalplay.qa.automation.salesforce.residential.activation.configuration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.stereotype.Component;

/**
 * Selenium browser configuration file for automation, this class provides the selenium's context
 * configuration for WebDriver
 *
 * @version 1.0
 * @author Alejandro Uribe
 */
@Component
public class SeleniumBrowserConfiguration {

  public WebDriver configuration(String browser){
    WebDriver driver;
    if (browser.equalsIgnoreCase("Firefox")) {
      WebDriverManager.firefoxdriver().setup();
      FirefoxOptions options = new FirefoxOptions();
      options.addArguments("start-maximized");
      options.addArguments("enable-automation");
      options.addArguments("--disable-extensions");
      options.addArguments("--no-sandbox");
      options.addArguments("--disable-dev-shm-usage");
      options.addArguments("--disable-browser-side-navigation");
      options.addArguments("enable-popup-blocking");
      options.addArguments("--disable--notifications");
      options.addArguments("disable-infobars");
      driver = new FirefoxDriver(options);
    } else {
      WebDriverManager.chromedriver().setup();
      ChromeOptions options = new ChromeOptions();
      options.addArguments("start-maximized");
      options.addArguments("enable-automation");
      options.addArguments("--disable-extensions");
      options.addArguments("--no-sandbox");
      options.addArguments("--disable-dev-shm-usage");
      options.addArguments("--disable-browser-side-navigation");
      options.addArguments("enable-popup-blocking");
      options.addArguments("--disable--notifications");
      options.addArguments("disable-infobars");
      driver = new ChromeDriver(options);
    }
    return driver;
  }
}
