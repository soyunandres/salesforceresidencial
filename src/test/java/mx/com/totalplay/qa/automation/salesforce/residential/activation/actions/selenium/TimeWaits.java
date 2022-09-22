package mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.selenium;

import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.ReadProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class waits until happen some action through selenium waits
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Component
public class TimeWaits {

  @Autowired
  private WebDriver driver;

  @Autowired
  private ReadProperties property;

  public WebElement explicitlyWait(long waitTime, By identifier) throws TimeoutException {
    WebDriverWait wait = new WebDriverWait(driver, waitTime);
    wait.until(ExpectedConditions.visibilityOfElementLocated(identifier));
    return driver.findElement(identifier);
  }

  public WebElement explicitlyWaitElementClickable(long waitTime, By identifier){
    WebDriverWait wait = new WebDriverWait(driver, waitTime);
    wait.until(ExpectedConditions.elementToBeClickable(identifier));
    return driver.findElement(identifier);
  }

  public WebElement explicitlyWaitTextShowed(long waitTime, By identifier, String text){
    WebDriverWait wait = new WebDriverWait(driver, waitTime);
    wait.until(ExpectedConditions.textToBePresentInElementLocated(identifier,text));
    return driver.findElement(identifier);
  }

  public WebElement explicitlyWaitElementToBeSelected(long waitTime, By identifier){
    WebDriverWait wait = new WebDriverWait(driver, waitTime);
    wait.until(ExpectedConditions.elementToBeSelected(identifier));
    return driver.findElement(identifier);
  }
}

