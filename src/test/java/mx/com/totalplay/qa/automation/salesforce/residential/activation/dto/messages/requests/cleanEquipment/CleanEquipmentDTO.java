package mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.messages.requests.cleanEquipment;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.ReadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class represents a SOAP client
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Component
public class CleanEquipmentDTO {

  @Autowired
  private ReadProperties property;

  private String soapAction;
  private String namespace;
  private String namespaceUri;
  private MessageFactory messageFactory;
  private SOAPMessage soapMessage;
  private SOAPPart soapPart;
  private SOAPEnvelope soapEnvelope;
  private SOAPBody body;
  private SOAPElement bodyElement;

  public void defineCreateSoapMessage(String namespace, String namespaceUri, String soapAction){
    this.namespace = namespace;
    this.namespaceUri = namespaceUri;
    this.soapAction = soapAction;
  }

  public void initializeMessage() throws SOAPException {
    messageFactory = MessageFactory.newInstance();
    soapMessage = messageFactory.createMessage();
    soapPart = soapMessage.getSOAPPart();
    soapEnvelope = soapPart.getEnvelope();
    soapEnvelope.addNamespaceDeclaration(namespace, namespaceUri);
    body = soapEnvelope.getBody();
  }

  public void createMessage(String identifier, String mac, String kindOfEquipment) throws Exception {
    bodyElement = body.addChildElement("process",namespace);
    switch (kindOfEquipment) {
      case "ont":
        addSoapNodeElement(bodyElement, "SNONT", identifier);
      break;
      case "stb":
        addSoapNodeElement(bodyElement, "SNSTB", identifier);
        addSoapNodeElement(bodyElement, "MACSTB", mac);
        break;
      case "macStb":
        addSoapNodeElement(bodyElement, "MACSTB", identifier);
        break;
      case "dn":
        addSoapNodeElement(bodyElement, "DN", identifier);
        break;
      case "wifi":
        addSoapNodeElement(bodyElement, "ATA-WIFIExtender", identifier);
        break;
      default:
        throw new Exception(String.format("La opcion de equipo '%s' ingresada para la limpieza del equipo '%s' es incorrecta",
            kindOfEquipment,identifier));
    }
  }

  public void addHeaders(){
    MimeHeaders headers = soapMessage.getMimeHeaders();
    headers.addHeader("SOAPAction", soapAction);
  }

  public SOAPMessage getSoapMessage() throws SOAPException {
    soapMessage.saveChanges();
    return soapMessage;
  }

  private SOAPElement addSoapElement(SOAPElement element, String localName) throws SOAPException {
    return element.addChildElement(localName,namespace);
  }

  private void addSoapNodeElement(SOAPElement element, String localName, String nodeValue)
      throws SOAPException {
    addSoapElement(element,localName).addTextNode(nodeValue);
  }
}
