package mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.initializer;

import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.ReadProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitializerJSON {

  @Autowired
  private ReadProperties property;

  private final Logger logger = LoggerFactory.getLogger(InitializerJSON.class);


}
