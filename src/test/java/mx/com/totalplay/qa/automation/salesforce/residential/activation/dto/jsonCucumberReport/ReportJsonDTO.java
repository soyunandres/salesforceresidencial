package mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.jsonCucumberReport;

import lombok.Getter;
import lombok.Setter;

/**
 * Class which represents a cucumber json report file
 *
 * @version 1.0
 * @author Alejandro Uribe
 */
@Getter
@Setter
public class ReportJsonDTO {

  private int line;
  private ElementDTO[] elements;
  private String name;
  private String description;
  private String id;
  private String keyword;
  private String uri;
  private TagDTO[] tags;
}