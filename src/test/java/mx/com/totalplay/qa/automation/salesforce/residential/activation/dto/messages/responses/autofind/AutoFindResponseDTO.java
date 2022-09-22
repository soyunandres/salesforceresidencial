package mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.messages.responses.autofind;

import lombok.Getter;
import lombok.Setter;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.messages.responses.autofind.minorDTO.AutofindDTO;
import org.springframework.stereotype.Service;

/**
 * This class represents a response from autofind service
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Service
@Getter
@Setter
public class AutoFindResponseDTO {

  private AutofindDTO autofind;
  private String idResult;
  private String resultDescription;
  private String datetime;
  private Boolean isEnded;
  private long idLog;
}
