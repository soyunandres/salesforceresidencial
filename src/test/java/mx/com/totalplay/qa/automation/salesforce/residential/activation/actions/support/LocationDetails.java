package mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class LocationDetails {

  private String street;
  private String externalNumber;
  private String municipality;
  private String city;
  private String state;
  private String postalCode;
  private String district;
  private String inputSite;
  private String siteName;
}
