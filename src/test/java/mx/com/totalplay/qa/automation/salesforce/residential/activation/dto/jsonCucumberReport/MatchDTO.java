package mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.jsonCucumberReport;

import lombok.Getter;
import lombok.Setter;

/**
 * Class which represents element match inside a cucumber json report
 *
 * @version 1.0
 * @author Alejandro Uribe
 */
@Getter
@Setter
public class MatchDTO {

  private ArgumentDTO[] arguments;
  private String location;
}
