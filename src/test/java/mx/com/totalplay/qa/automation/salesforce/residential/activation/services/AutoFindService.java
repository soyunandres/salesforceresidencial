package mx.com.totalplay.qa.automation.salesforce.residential.activation.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.ReadProperties;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.WriteWordFile;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.output.LoggerX;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.files.CreateOutputStreamDTO;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.initializer.InitializerJSON;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.messages.responses.autofind.AutoFindResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;

/**
 * This class searches ONTs in autofind to know if an ont is available to be activated
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Service
public class AutoFindService {

  private final Logger logger = LoggerFactory.getLogger(AutoFindService.class);

  @Autowired
  private InitializerJSON initialize;

  @Autowired
  private ReadProperties property;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private CreateOutputStreamDTO writeOutput;

  @Autowired
  private AutoFindResponseDTO autofindResponse;

  @Autowired
  private LoggerX loggerX;

  @Autowired
  private WriteWordFile wordFile;

  private RequestSpecification request;
  private Response response;
  private String bodyString;

  public Boolean searchOntInAutoFind(String serialOnt) throws IOException {
    RestAssured.baseURI = property.url("urlAutofind");
    request = RestAssured.given();
    request.header("Content-Type","application/json");
    request.header("Accept", "text/plain");
    response = request.body(String.format("\"%s\"",serialOnt)).post();

    logger.info(String.format("Answer body: \n%s\n",response.getBody().asString()));
    writeOutput.writeJson(property.path("autoFindResponse"),response.getBody().asString());
    Assert.assertEquals(response.getStatusCode(), 200,
        String.format(property.report("AError00"),response.getBody().asString()));

    Assert.assertNotNull(response.getBody().asString(),
        "Response body is null");
    Assert.assertFalse(response.getBody().asString().trim().equalsIgnoreCase(""),
        "Response body is empty");
    Assert.assertFalse(response.getBody().asString().trim().equalsIgnoreCase("{}"),
        "Response body is an empty class");
    Assert.assertFalse(response.getBody().asString().trim().equalsIgnoreCase("[]"),
        "Response body is an empty list");
    autofindResponse = response.getBody().as(AutoFindResponseDTO.class);
    if(!autofindResponse.getResultDescription().contains(property.text("correctResultDescription"))){
      loggerX.error(logger,1,String.format(property.report("AError01"),
          autofindResponse.getResultDescription(),property.text("correctResultDescription")), null);
      return false;
    }
    wordFile.insertTextIntoTestStep(property.report("reportText05"),
        String.format(property.report("reportText06"),response.getBody().asString()));
    return true;
  }
}
