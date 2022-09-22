package mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.jsonCucumberReport;

import lombok.Getter;
import lombok.Setter;

/**
 * Class which represents arguments inside a cucumber json report
 *
 * @version 1.0
 * @author Alejandro Uribe
 */
@Getter
@Setter
public class ArgumentDTO {

  private String val;
  private long offset;
}
