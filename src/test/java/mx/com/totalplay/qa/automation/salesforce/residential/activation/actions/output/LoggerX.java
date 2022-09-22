package mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.output;

import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.ReadProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class shows on screen messages using logger slf4j
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Service
public class LoggerX {

  @Autowired
  private ReadProperties property;

  public void error(Logger logger, int option, String action, String identifier) {
    switch (option) {
      case 0:
        if (identifier != null) {
          logger.error(String.format("%sAn error has occurred %s with identifier %s'%s'%s",
              property.color("highRed"), action, property.color("highBlue"), identifier,
              property.color("DEFAULT")));
        } else {
          logger.error(String.format("%sAn error has occurred %s%s",
              property.color("highRed"), action, property.color("DEFAULT")));
        }
        break;
      case 1:
        logger.error(String.format(property.color("logger1"), property.color("highRed"),
            action, property.color("DEFAULT")));
        break;
      default:
        logger.error(String.format(property.color("logger1"), property.color("highRed"),
            "Loggerx error option is incorrect", property.color("DEFAULT")));
        break;
    }
  }

  public void info(Logger logger, int option, String identifier, String text) {
    switch (option) {
      case 0:
        logger.info(String.format(property.color("logger0"), property.color("highWhite"),
            text, property.color("highBlue"), identifier, property.color("DEFAULT")));
        break;
      case 1:
        logger.info(String.format(property.color("logger1"), property.color("highGreen"),
            text, property.color("DEFAULT")));
        break;
      case 2:
        logger.info(String.format(property.color("logger1"), property.color("highWhite"),
            text, property.color("DEFAULT")));
        break;
      default:
        logger.error(String.format(property.color("logger1"), property.color("highRed"),
            "Loggerx error option is incorrect", property.color("DEFAULT")));
        break;
    }
  }
}
