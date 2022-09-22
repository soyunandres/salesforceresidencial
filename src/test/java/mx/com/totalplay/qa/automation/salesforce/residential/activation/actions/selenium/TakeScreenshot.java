package mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.selenium;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.ReadProperties;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.WriteWordFile;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

/**
 * This class takes different kinds of screenshots on a web page, mobile device or windows pc
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Service
public class TakeScreenshot {

  private final Logger logger = LoggerFactory.getLogger(TakeScreenshot.class);

  @Autowired
  private ReadProperties property;

  @Autowired
  private WebDriver driver;

  public void viewportPasting() {
    try {
      final Screenshot screenshot = new AShot().shootingStrategy(
          ShootingStrategies.viewportPasting(500)).takeScreenshot(driver);
      saveScreenshot(screenshot);
    } catch (Exception e) {
      logger.error(String.format("%sAn error has occurred taking a viewport pasting screenshot%s",
          property.color("highRed"), property.color("DEFAULT")));
    }
  }

  public void cutting(int headerToCut, int footerToCut) {
    try {
      final Screenshot screenshot = new AShot().shootingStrategy(
          ShootingStrategies.cutting(headerToCut, footerToCut)).takeScreenshot(driver);
      saveScreenshot(screenshot);
    } catch (Exception e) {
      logger.error(String.format("%sAn error has occurred taking a cutting screenshot%s",
          property.color("highRed"), property.color("DEFAULT")));
    }
  }

  public void cuttingAndViewportPasting(int headerToCut, int footerToCut) {
    try {
      final Screenshot screenshot = new AShot().shootingStrategy(
          ShootingStrategies.viewportPasting(ShootingStrategies
              .cutting(headerToCut, footerToCut), 1000)).takeScreenshot(driver);
      saveScreenshot(screenshot);
    } catch (Exception e) {
      logger.error(
          String.format("%sAn error has occurred taking a cutting viewport pasting screenshot%s",
              property.color("highRed"), property.color("DEFAULT")));
    }
  }

  private void saveScreenshot(Screenshot screenshot) throws IOException {
    final BufferedImage image = screenshot.getImage();
    ImageIO.write(image, "PNG", new File(
        String.format("images/screenshots/screenshot(%o-%o).png",
            WriteWordFile.TEST_CASE_NUMBER, WriteWordFile.STEP_NUMBER)));
  }
}

