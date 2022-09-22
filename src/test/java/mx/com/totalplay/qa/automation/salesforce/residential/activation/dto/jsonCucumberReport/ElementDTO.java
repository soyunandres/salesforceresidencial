package mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.jsonCucumberReport;

import lombok.Getter;
import lombok.Setter;

/**
 * Class which represents elements inside a cucumber json report
 *
 * @version 1.0
 * @author Alejandro Uribe
 */
@Getter
@Setter
public class ElementDTO {

  private String start_timestamp;
  private long line;
  private String name;
  private String description;
  private String id;
  private String type;
  private String keyword;
  private StepDTO[] steps;
}
