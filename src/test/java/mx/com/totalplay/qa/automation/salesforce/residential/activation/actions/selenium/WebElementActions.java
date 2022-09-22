package mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.selenium;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.ReadProperties;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.output.LoggerX;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class does different kinds of selenium actions on android devices
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Component
public class WebElementActions {

  @Autowired
  private WebDriver driver;

  @Autowired
  private ReadProperties property;

  @Autowired
  private TimeWaits wait;

  @Autowired
  private LoggerX loggerx;

  private static String parentWindowHandle;

  private final Logger logger = LoggerFactory.getLogger(WebElementActions.class);

  public void sendKeys(By identifier, long waitTime, String text) {
    elementActionSelection("0", text, waitTime, identifier, null, null);
  }

  public void sendKeys(By identifier, long waitTime, Keys keys) {
    elementActionSelection("0A", null, waitTime, identifier, keys, null);
  }

  public void sendKeysSequence(By identifier, long waitTime, CharSequence... keysSequence) {
    elementActionSelection("0B", null, waitTime, identifier, null, null,
        keysSequence);
  }

  public void sendKeysGeneral(Keys keys) {
    elementActionSelection("0C", null, 0, null, keys,
        null);
  }

  public void sendKeysGeneral(String text){
    elementActionSelection("0E", text, 0, null, null, null);
  }

  public void sendKeysSequenceGeneral(CharSequence... keys) {
    elementActionSelection("0CS", null, 0, null, null,
        null, keys);
  }

  public boolean click(By identifier, long waitTime) {
    return elementActionSelection("1", null, waitTime, identifier, null, null);
  }

  public boolean clickWithoutNotification(By identifier, long waitTime) {
    return elementActionSelection("1B", null, waitTime, identifier, null, null);
  }

  public void clickToFindElement(By identifier, long waitTime, long waitTimeBetweenRepInMS)
      throws InterruptedException {
    loggerx.info(logger, 2, identifier.toString(), "Trying to click element with this identifier: "+identifier);
    while (!clickWithoutNotification(identifier, waitTime)){
      Thread.sleep(waitTimeBetweenRepInMS);
    }
  }

  public void rightClick(By identifier, long waitTime) {
    elementActionSelection("1R", null, waitTime, identifier, null, null);
  }

  public void rightClickGeneral() {
    elementActionSelection("1RA", null, 0, null, null, null);
  }

  public void clickByJS(String xpath) {
    elementActionSelection("1A", xpath, 0, null, null, null);
  }

  public String getText(By identifier, long waitTime) {
    try {
      return wait.explicitlyWait(waitTime, identifier).getText();
    } catch (Exception e) {
      loggerx.error(logger, 0, "getting text", identifier.toString());
      return null;
    }
  }

  public Point getLocation(By identifier, long waitTime){
    try{
      return wait.explicitlyWait(waitTime, identifier).getLocation();
    }catch(Exception e){
      loggerx.error(logger, 0, "getting element location", identifier.toString());
      return null;
    }
  }

  public String getAttribute(By identifier, long waitTime, String attribute){
    try {
      return wait.explicitlyWait(waitTime, identifier).getAttribute(attribute);
    }catch (Exception e){
      loggerx.error(logger, 0, "getting attribute", identifier.toString());
      return null;
    }
  }

  public void clear(By identifier, long waitTime) {
    elementActionSelection("3", null, waitTime, identifier, null, null);
  }

  public void showAllWindowHandles() {
    elementActionSelection("4", null, 0, null, null, null);
  }

  public void changeWindowHandle(String handle) {
    elementActionSelection("4A", handle, 0, null, null, null);
  }

  public void changeWindowHandleByTitle(String title) {
    elementActionSelection("4B", title, 0, null, null, null);
  }

  public void changeParentWindowHandle(){
    elementActionSelection("4C", null, 0, null, null, null);
  }

  public void closeNewWindowHandle(){
    try {
      Set<String> handlesSet = driver.getWindowHandles();
      List<String> handlesList = new ArrayList<String>(handlesSet);
      driver.switchTo().window(handlesList.get(1));
      driver.close();
      driver.switchTo().window(handlesList.get(0));
      Thread.sleep(1000);
    }catch (Exception e){
      loggerx.error(logger, 1, "An error has occurred closing new window handle", null);
    }
  }

  public String getWindowHandle(){
    try {
      return driver.getWindowHandle();
    }catch (Exception e){
      loggerx.error(logger, 1, "An error has occurred getting window handle", null);
      return "Error";
    }
  }

  public void openWebPage(String url) {
    elementActionSelection("5", url, 0, null, null, null);
  }

  public void scroll(String numVerticalPixel, String numHorizontalPixel) {
    elementActionSelection("6", numVerticalPixel, 0, null, null,
        numHorizontalPixel);
  }

  public void scroll(By identifier, String numVerticalPixel, String numHorizontalPixel,
      long waitTime) {
    elementActionSelection("6A", numVerticalPixel, waitTime, identifier, null,
        numHorizontalPixel);
  }

  public boolean findElement(By identifier, long waitTime) {
    return elementActionSelection("7", null, waitTime, identifier, null, null);
  }

  public void automaticChangeFrame(By identifier) throws InterruptedException {
    loggerx.info(logger, 2, identifier.toString(), "Searching element inside the iframes");
    String frame = "";
    while (!elementActionSelection("7", null, 0, identifier, null, null)) {
      changeFrameToPrincipal();
      Thread.sleep(500);
      frame = searchElementInFrame(identifier);
      Thread.sleep(500);
    }
    loggerx.info(logger, 0, frame, "The element was founded in the frame number");
    Thread.sleep(500);
  }

  public void changeFrame(int frameNumber) {
    if (frameNumber != -1) {
      try {
        driver.switchTo().frame(frameNumber);
        //loggerx.info(logger, 0, String.valueOf(frameNumber), "Se cambio el marco al iFrame numero");
      } catch (Exception e) {
        loggerx.error(logger, 1, "An error has occurred trying to change iframe", null);
      }
    } else {
      changeFrameToPrincipal();
    }
  }

  public void changeFrameToPrincipal() {
    try {
      driver.switchTo().defaultContent();
      //loggerx.info(logger, 2, null, "Se cambio el marco al frame principal");
    } catch (Exception e) {
      loggerx.error(logger, 1, "An error has occurred trying to change to principal iframe", null);
    }
  }

  public int framesNumber() {
    try {
      return driver.findElements(By.xpath("//iframe")).size();
    } catch (Exception e) {
      loggerx.error(logger, 1, "An error has occurred trying to count the iframes total number", null);
      return 0;
    }
  }

  public String searchElementInFrame(By identifier) throws InterruptedException {
    for (int i = 0; i < framesNumber(); i++) {
      changeFrame(i);
      Thread.sleep(500);
      if (elementActionSelection("7", null, 0, identifier, null, null)) {
        return String.valueOf(i);
      }
      changeFrameToPrincipal();
      Thread.sleep(500);
    }
    loggerx.error(logger, 1, "Element was not found inside any iframe", null);
    return "default";
  }

  public void waitUntilFindElement(By identifier, long waitTime) {
    boolean elementWasFound = false;
    loggerx.info(logger, 0, identifier.toString(), "Finding element with this identifier:");
    while (!elementWasFound) {
      elementWasFound = elementActionSelection(
          "7", null, waitTime, identifier, null, null);
    }
    loggerx.info(logger, 1, identifier.toString(), "Element was found");
  }

  public void waitUntilElementDisappears(By identifier, long waitTime, long disappearWaitTimeInMs)
      throws InterruptedException {
    boolean elementWasFound = true;
    loggerx.info(logger, 0, identifier.toString(),
        "Waiting that element disappears, identifier: ");
    while (elementWasFound) {
      elementWasFound = elementActionSelection(
          "7", null, waitTime, identifier, null, null);
      Thread.sleep(disappearWaitTimeInMs);
    }
    loggerx.info(logger, 1, identifier.toString(), "Element disappears");
  }

  public void waitElementBeClickable(By identifier, long waitTime){
    elementActionSelection("8", null, waitTime, identifier, null, null);
  }

  public void waitElementTextVisibility(By identifier, long waitTime, String text){
    elementActionSelection("8A", text, waitTime, identifier, null, null);
  }

  public void waitElementBeSelectable(By identifier, long waitTime){
    elementActionSelection("8B", null, waitTime, identifier, null, null);
  }

  public void waitUntilElementBeClicked(By identifier, long waitTime, long waitTimeBetweenClicksInMs)
      throws InterruptedException {
    boolean elementWasFound = true;
    loggerx.info(logger, 0, identifier.toString(),
        "Waiting that element can be clicked, identifier: ");
    while (elementWasFound) {
      elementWasFound = elementActionSelection(
          "1B", null, waitTime, identifier, null, null);
      Thread.sleep(waitTimeBetweenClicksInMs);
    }
    loggerx.info(logger, 1, identifier.toString(), "Element has been clicked");
  }

  public void clickModifyingCoordinates(By identifier, int x, int y, long waitTime){
    try{
      Actions actions = new Actions(driver);
      actions.moveToElement(wait.explicitlyWait(waitTime, identifier)).moveByOffset(x, y)
          .click().perform();
    } catch (Exception e){
      loggerx.error(logger, 0, "clicking by modifying coordinates", identifier.toString());
    }
  }

  public void rightClickModifyingCoordinates(By identifier, int x, int y, long waitTime){
    try{
      Actions actions = new Actions(driver);
      actions.moveToElement(wait.explicitlyWait(waitTime, identifier)).moveByOffset(x, y)
          .contextClick().perform();
    }catch (Exception e){
      loggerx.error(logger, 1, "right clicking by modifying coordinates",null);
    }
  }

  public String getCurrentUrl(){
    try{
      return driver.getCurrentUrl();
    }catch (Exception e){
      loggerx.error(logger, 1, "An error has occurred obtaining current URL",null);
      return "error";
    }
  }

  private boolean elementActionSelection(String option, String text, long waitTime, By identifier,
      Keys keys, String text2, CharSequence... keySequence) {
    String action = "";
    Actions actions = new Actions(driver);
    boolean showError = true;

    try {
      switch (option) {
        case "0":
          action = "sending keys";
          wait.explicitlyWait(waitTime, identifier).sendKeys(text);
          break;
        case "0A":
          action = "sending keys";
          wait.explicitlyWait(waitTime, identifier).sendKeys(keys);
          break;
        case "0B":
          action = "sending keys sequence";
          wait.explicitlyWait(waitTime, identifier).sendKeys(keySequence);
          break;
        case "0C":
          action = "sending keys in general";
          actions.sendKeys(keys).perform();
          break;
        case "0D":
          action = "sending keys sequence in general";
          actions.sendKeys(keySequence).perform();
          break;
        case "0E":
          action = "sending keys in general";
          actions.sendKeys(text).perform();
          break;
        case "1":
          action = "clicking button";
          wait.explicitlyWait(waitTime, identifier).click();
          break;
        case "1A":
          action = "clicking button by JS";
          JavascriptExecutor js3 = (JavascriptExecutor) driver;
          js3.executeScript("document.getElementByXpath('" + text + "').click();");
          break;
        case "1B":
          action = "clicking button without notification";
          showError = false;
          wait.explicitlyWait(waitTime, identifier).click();
          break;
        case "1R":
          action = "clicking by mouse right button";
          actions.contextClick(wait.explicitlyWait(waitTime, identifier)).perform();
          break;
        case "1RA":
          action = "general clicking by mouse right button";
          actions.contextClick().perform();
          break;
        case "2":
          break;
        case "3":
          action = "clearing text";
          wait.explicitlyWait(waitTime, identifier).clear();
          break;
        case "4":
          action = "showing window handles";
          String parent = driver.getWindowHandle();
          Set<String> handles = driver.getWindowHandles();
          Iterator<String> iterator = handles.iterator();

          while (iterator.hasNext()) {
            String child_window = iterator.next();
            if (!parent.equals(child_window)) {
              driver.switchTo().window(child_window);
              loggerx.info(logger, 2, null, String.format("Window handle name: %s",
                  driver.switchTo().window(child_window).getTitle()));
              loggerx.info(logger, 2, null, driver.getWindowHandle());
            }
          }
          driver.switchTo().window(parent);
          break;
        case "4A":
          action = "switching window handle";
          driver.switchTo().window(text);
          break;
        case "4B":
          action = "switching window handle by title";
          parentWindowHandle = driver.getWindowHandle();
          Set<String> handle = driver.getWindowHandles();
          for (String child_window : handle) {
            if (!parentWindowHandle.equals(child_window)) {
              if (driver.switchTo().window(child_window).getTitle().equals(text)) {
                loggerx.info(logger, 1, null, "Active window was switched successfully");
                return false;
              }
            }
          }
          loggerx.error(logger, 1, "window handle was not found", null);
          break;
        case "4C":
          action = "switching parent window handle";
          driver.switchTo().window(parentWindowHandle);
          break;
        case "5":
          action = "opening page";
          driver.get(text);
          break;
        case "6":
          action = "scrolling content";
          JavascriptExecutor js = (JavascriptExecutor) driver;
          js.executeScript("window.scrollBy(" + text2 + "," + text + ")");
          break;
        case "6A":
          action = "scrolling element content";
          JavascriptExecutor js2 = (JavascriptExecutor) wait.explicitlyWait(waitTime, identifier);
          js2.executeScript("window.scrollBy(" + text2 + "," + text + ")");
          break;
        case "7":
          action = "finding element";
          showError = false;
          wait.explicitlyWait(waitTime, identifier).isEnabled();
          break;
        case "8":
          action = "waiting element be clickable";
          wait.explicitlyWaitElementClickable(waitTime, identifier).click();
          break;
        case "8A":
          action = "waiting element text visibility";
          wait.explicitlyWaitTextShowed(waitTime, identifier, text);
          break;
        case "8B":
          action = "waiting element be selectable";
          wait.explicitlyWaitElementToBeSelected(waitTime, identifier);
          break;
        default:
          loggerx.info(logger, 1, null, "Element action selected does not exist");
          break;
      }
      return true;
    } catch (Exception e) {
      if (showError) {
        if (identifier != null) {
          loggerx.error(logger, 0, action, identifier.toString());
        } else {
          loggerx.error(logger, 0, action, null);
        }
      }
      return false;
    }
  }
}
