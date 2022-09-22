package mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.jsonCucumberReport;

import lombok.Getter;
import lombok.Setter;

/**
 * Class which represents cucumber steps inside a json report
 *
 * @version 1.0
 * @author Alejandro Uribe
 */
@Getter
@Setter
public class StepDTO {

  private ResultDTO result;
  private long line;
  private String name;
  private MatchDTO match;
  private String keyword;
}
