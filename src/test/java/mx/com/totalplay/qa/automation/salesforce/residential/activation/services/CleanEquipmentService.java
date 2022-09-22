package mx.com.totalplay.qa.automation.salesforce.residential.activation.services;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.ReadProperties;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.WriteWordFile;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.output.LoggerX;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.files.CreateOutputStreamDTO;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.messages.requests.cleanEquipment.CleanEquipmentDTO;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.tests.steps.activation.ActivationSteps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class sends request to LimpiarEquipo service and clean ONTs, STBs and DNs
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Service
public class CleanEquipmentService {

  @Autowired
  private CleanEquipmentDTO clean;

  @Autowired
  private ReadProperties property;

  @Autowired
  private CreateOutputStreamDTO output;

  @Autowired
  private LoggerX loggerX;

  @Autowired
  private WriteWordFile wordFile;

  private final Logger logger = LoggerFactory.getLogger(CleanEquipmentService.class);

  private SOAPMessage message;
  private SOAPConnection soapConnection;
  private SOAPMessage soapResponse;

  public void clean(String identifier, String mac, String kindOfEquipment,int numEquipment) throws Exception {
    String resultA = "", resultB = "";
    clean.defineCreateSoapMessage(
        property.url("COSoapNamespace"), property.url("COSoapNamespaceUri"),
        property.url("COSoapAction"));
    clean.initializeMessage();
    clean.createMessage(identifier, mac, kindOfEquipment);
    clean.addHeaders();
    message = clean.getSoapMessage();
    SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
    soapConnection = soapConnectionFactory.createConnection();
    message.writeTo(output.getOutputStream(String.format(property.path("cleanEquipmentRequest"),
        kindOfEquipment,numEquipment)));
    soapResponse = soapConnection.call(message, property.url("COEndpoint"));
    soapConnection.close();
    soapResponse.writeTo(output.getOutputStream(String.format(property.path("cleanEquipmentResponse"),
        kindOfEquipment,numEquipment)));

    switch (kindOfEquipment){
      case "ont":
        resultA = soapResponse.getSOAPBody().getElementsByTagName("ResultRedSNONT").item(0).getTextContent();
        resultB = soapResponse.getSOAPBody().getElementsByTagName("ResultBRMSNONT").item(0).getTextContent();
        loggerX.info(logger, 1, null, "ONT clean result red: "+resultA);
        loggerX.info(logger, 1, null, "ONT clean result BRM: "+resultB);
        if(!((resultA.contains(property.text("correctTextResultAOnt")) ||
            resultA.contains(property.text("correctTextResultAOnt1"))) &&
            (resultB.contains(property.text("correctTextResultBOnt")) ||
                resultB.contains(property.text("correctTextResultBOnt1"))))) {
          throw new Exception(String.format(property.report("CEErrorText00"),identifier));
        }
        wordFile.insertTextIntoTestStep(
            String.format(property.report("reportText01"),kindOfEquipment, identifier),
            String.format(property.report("reportText02"),resultA,resultB));
        break;
      case "dn":
        resultA = soapResponse.getSOAPBody().getElementsByTagName("ResultBRMDN").item(0).getTextContent();
        resultB = soapResponse.getSOAPBody().getElementsByTagName("ResultIPTVDN").item(0).getTextContent();
        loggerX.info(logger, 1, null, String.format(property.report("CEMessage00"),resultA));
        loggerX.info(logger, 1, null, String.format(property.report("CEMessage01"),resultB));
        if(!((resultA.contains(property.text("correctTextResultADn")) ||
            resultA.contains(property.text("correctTextResultADn1"))) &&
            (resultB.contains(property.text("correctTextResultBDn")) ||
                resultB.contains(property.text("correctTextResultBDn1"))))){
          throw new Exception(String.format(property.report("CEErrorText01"),identifier));
        }
        wordFile.insertTextIntoTestStep(
            String.format(property.report("reportText17"), identifier),
            String.format(property.report("reportText03"), resultA, resultB));
        break;
      case "stb":
        resultA = soapResponse.getSOAPBody().getElementsByTagName("ResultBRMSNSTB").item(0).getTextContent();
        resultB = soapResponse.getSOAPBody().getElementsByTagName("ResultIPTVSNSTB").item(0).getTextContent();
        loggerX.info(logger, 1, null, String.format(property.report("CEMessage02"),resultA));
        loggerX.info(logger, 1, null, String.format(property.report("CEMessage03"),resultB));
        if(!((resultA.contains(property.text("correctTextResultASTB")) ||
            resultA.contains(property.text("correctTextResultASTB1"))) &&
            (resultB.contains(property.text("correctTextResultBSTB")) ||
                resultB.contains(property.text("correctTextResultBSTB1"))))){
          throw new Exception(String.format(property.report("CEErrorText02"),identifier));
        }
        wordFile.insertTextIntoTestStep(
            String.format(property.report("reportText01"),kindOfEquipment, identifier),
            String.format(property.report("reportText04"),resultA,resultB));
        break;
      case "wifi":
        resultA = soapResponse.getSOAPBody().getElementsByTagName("ResultATA-WIFIExtender").item(0).getTextContent();
        loggerX.info(logger, 1, null, String.format(property.report("CEMessage05"),resultA));
        if(!(resultA.contains(property.text("correctTextResultWifi")) ||
            resultA.contains(property.text("correctTextResultWifi1")))){
          throw new Exception(String.format(property.report("CEErrorText04"), identifier));
        }
        wordFile.insertTextIntoTestStep(
            String.format(property.report("reportText01"), kindOfEquipment, identifier),
            String.format(property.report("reportText04b"), resultA, resultB));
        break;
      default:
        throw new Exception(String.format(property.report("CEErrorText03"),kindOfEquipment));
    }
    loggerX.info(logger,1,null, property.report("CEMessage04"));
  }
}
