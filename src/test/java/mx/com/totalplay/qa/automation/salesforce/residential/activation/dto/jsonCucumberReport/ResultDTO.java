package mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.jsonCucumberReport;

import lombok.Getter;
import lombok.Setter;

/**
 * Class which represents tests results inside a json report
 *
 * @version 1.0
 * @author Alejandro Uribe
 */
@Getter
@Setter
public class ResultDTO {

  private String error_message;
  private long duration;
  private String status;
}
