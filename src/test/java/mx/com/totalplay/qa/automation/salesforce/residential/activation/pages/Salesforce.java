package mx.com.totalplay.qa.automation.salesforce.residential.activation.pages;

import java.util.ArrayList;
import java.util.List;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.date.SystemDate;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.ReadProperties;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.WriteWordFile;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.output.LoggerX;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.selenium.TakeScreenshot;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.selenium.WebElementActions;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.support.GenerateRandomNumber;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.tables.ActivationTable;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.tables.ConfigTable;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.tables.PlanTable;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class represents salesforce page
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Component
public class Salesforce {

  @Autowired
  private WebDriver driver;

  @Autowired
  private WebElementActions action;

  @Autowired
  private ReadProperties property;

  @Autowired
  private LoggerX loggerX;

  @Autowired
  private WriteWordFile wordFile;

  @Autowired
  private TakeScreenshot screenshot;

  @Autowired
  private GenerateRandomNumber generateRandomNumber;

  @Autowired
  private SystemDate date;

  private String postalCodeNumber;

  private final Logger logger = LoggerFactory.getLogger(Salesforce.class);

  private String TESTER_NAME;
  private String USERNAME;
  private String PASSWORD;
  private String POSTAL_CODE;
  private String STREET;
  private String EXTERNAL_NUMBER;

  private String kindOfServicePrice;
  private String totalMonthRent;
  private String activationCharge;
  private String totalFirstPayment;

  public void goSalesforce() {
    action.openWebPage(property.url("salesforce"));
  }

  public void log() throws InterruptedException {
    action.sendKeys(By.xpath(property.identifier("user")), 10, USERNAME);
    action.sendKeys(By.xpath(property.identifier("password")), 10, PASSWORD);
    screenshot.cutting(0, 0);
    wordFile.insertTestStep(property.report("reportText07"), 165);
    action.click(By.xpath(property.identifier("logButton")), 10);
    Thread.sleep(5000);
  }

  public void selectResidentialModel() throws InterruptedException {
    action.waitUntilFindElement(By.xpath(property.identifier("searchCube")), 3);
    Thread.sleep(3000);
    if (!action.findElement(By.xpath(property.identifier("prospects")), 6)) {
      action.click(By.xpath(property.identifier("searchCube")), 20);
      action.click(By.xpath(property.identifier("seeEverything")), 20);
      action.waitUntilFindElement(By.xpath(property.identifier("aboutElement")), 3);
      action.sendKeys(By.xpath(property.identifier("searchCubeInput")), 10,
          property.text("searchProspects"));
      action.sendKeys(By.xpath(property.identifier("searchCubeInput")), 10, Keys.ENTER);
      action.click(By.xpath(property.identifier("searchCubeOption1")), 10);
      if (!action.findElement(By.xpath(property.identifier("newProspect")), 5)) {
        action.click(By.xpath(property.identifier("searchCube")), 20);
        action.click(By.xpath(property.identifier("seeEverything")), 20);
        action.sendKeys(By.xpath(property.identifier("searchCubeInput")), 10,
            property.text("searchProspects"));
        action.sendKeys(By.xpath(property.identifier("searchCubeInput")), 10, Keys.ENTER);
        action.click(By.xpath(property.identifier("searchCubeOption2")), 10);
      }
    } else {
      action.click(By.xpath(property.identifier("prospects")), 10);
    }
    action.click(By.xpath(property.identifier("newProspect")), 20);
    action.findElement(By.xpath(property.identifier("newProspectNext")), 20);
    action.click(By.xpath(property.identifier("totalPlayProspect")), 10);
    screenshot.cutting(0, 0);
    wordFile.insertTestStep(property.report("reportText08"), 165);
    action.click(By.xpath(property.identifier("newProspectNext")), 10);
  }

  public void vendorDetails() throws InterruptedException {
    action.automaticChangeFrame(By.xpath(property.identifier("employee")));
    action.sendKeys(By.xpath(property.identifier("employee")), 10, property.text("employee"));
    do{
      action.sendKeys(By.xpath(property.identifier("employee")), 10, Keys.ENTER);
    }while(!action.findElement(By.xpath(property.identifier("prospectCanal")), 1));
    action.sendKeys(By.xpath(property.identifier("contractFoil")), 10, property.text("contractFoil"));
    action.click(By.xpath(property.identifier("prospectCanal")), 10);
    action.click(By.xpath(property.identifier("prospectCanalOption")), 10);
    action.click(By.xpath(property.identifier("prospectSubCanal")), 10);
    action.click(By.xpath(property.identifier("prospectSubCanalOption")), 10);
    screenshot.cutting(0, 0);
    wordFile.insertTestStep(property.report("reportText09"), 165);
  }

  public void installationLocation() throws InterruptedException {
    action.click(By.xpath(property.identifier("installationLocationMenu")), 10);
    do {
      action.clear(By.xpath(property.identifier("postalCode")), 10);
      action.sendKeys(By.xpath(property.identifier("postalCode")), 10,
          POSTAL_CODE);
      action.findElement(
          By.xpath(String.format(property.identifier("postalCodeOption"), postalCodeNumber)), 10);
      action.sendKeys(By.xpath(property.identifier("postalCode")), 10, Keys.ENTER);
      action.sendKeys(By.xpath(property.identifier("street")), 10, STREET);
      action.sendKeys(By.xpath(property.identifier("externalNumber")), 10, EXTERNAL_NUMBER);
      action.sendKeys(By.xpath(property.identifier("internalNumber")), 10, property.text("internalNumber"));
      action.sendKeys(By.xpath(property.identifier("betweenStreets")), 10,
          property.text("betweenStreets"));
      action.sendKeys(By.xpath(property.identifier("urbanReference")), 10,
          property.text("urbanReference"));
      action.click(By.xpath(property.identifier("buildingStatus")), 10);
      action.click(By.xpath(property.identifier("buildingStatusOption")), 10);
      action.click(By.xpath(property.identifier("kindOfBuilding")), 10);
      action.click(By.xpath(property.identifier("kindOfBuildingOption")), 10);
      Thread.sleep(4000);
      action.click(By.xpath(property.identifier("showMap")), 10);
      Thread.sleep(4000);
      action.click(By.xpath(property.identifier("validateCoverage")), 10);
    }while (!action.findElement(By.xpath(property.identifier("coverageOk")), 20));
    action.waitUntilFindElement(By.xpath(property.identifier("coverageOk")), 2);
    screenshot.cutting(0, 0);
    wordFile.insertTestStep(property.report("reportText10"), 165);
  }

  public void consolidation(){
    action.click(By.xpath(property.identifier("consolidationMenu")), 10);
    action.click(By.xpath(property.identifier("kindOfAccount")), 10);
    action.click(By.xpath(property.identifier("kindOfAccountOption")), 10);
    screenshot.cutting(0, 0);
    wordFile.insertTestStep(property.report("reportText11"), 165);
  }

  public void contactDetails(String randomNumber) throws InterruptedException {
    action.click(By.xpath(property.identifier("contactDetailsMenu")), 10);
    action.click(By.xpath(property.identifier("kindOfPerson")), 10);
    action.click(By.xpath(property.identifier("kindOfPersonOption")), 10);
    action.sendKeys(By.xpath(property.identifier("name")), 10,TESTER_NAME);
    action.sendKeys(By.xpath(property.identifier("fatherLastName")), 10,String.format("QA %s",randomNumber));
    action.sendKeys(By.xpath(property.identifier("motherLastName")), 10, date.getDate());
    action.sendKeys(By.xpath(property.identifier("telephone")), 10, property.text("telephone"));
    action.sendKeys(By.xpath(property.identifier("otherTelephone")), 10, property.text("telephone"));
    action.sendKeys(By.xpath(property.identifier("curp")), 10, property.text("curp"));
    action.click(By.xpath(property.identifier("sex")), 10);
    action.click(By.xpath(property.identifier("sexOption")), 10);
    action.sendKeys(By.xpath(property.identifier("mobileNumber")), 10, property.text("telephone"));
    action.sendKeys(By.xpath(property.identifier("email")), 10, property.text("email"));
    action.sendKeys(By.xpath(property.identifier("otherEmail")), 10, property.text("email"));
    action.sendKeys(By.xpath(property.identifier("rfc")), 10, property.text("rfc"));
    action.waitUntilElementDisappears(By.xpath(property.identifier("continueDisabled")),2, 1);
    screenshot.cutting(0, 0);
    wordFile.insertTestStep(property.report("reportText12"), 165);
    /*
    action.click(By.xpath(property.identifier("continue")), 10);
    if(action.findElement(By.xpath(property.identifier("errorServer")), 5)){
      Thread.sleep(4000);
      action.click(By.xpath(property.identifier("continue")), 10);
    }
    */
    do{
      Thread.sleep(5000);
    }while (action.click(By.xpath(property.identifier("continue")), 10));
  }

  public void planSelection(String[] plan) throws Exception {
    action.automaticChangeFrame(By.xpath(property.identifier("residential")));
    switch (plan[PlanTable.plan]){
      case "Residencial":
        action.clickToFindElement(By.xpath(property.identifier("residential")), 3,2000);
        break;
      case "Micronegocio":
        action.clickToFindElement(By.xpath(property.identifier("microBusiness")), 3, 2000);
        break;
      default:
        throw new Exception(String.format(property.report("sErrorText01"),plan[PlanTable.plan]));
    }
    Thread.sleep(2000);
    switch (plan[PlanTable.kindOfService]){
      case "Con_TotalPlay_TV":
        action.click(By.xpath(property.identifier("withTotalPlayTv")), 10);
        action.click(By.xpath(property.identifier("withoutSoundBox")), 10);
        break;
      case "Con_TotalPlay_TV_Y_Video_Soundbox":
        action.click(By.xpath(property.identifier("withTotalPlayTv")), 10);
        action.click(By.xpath(property.identifier("withSoundBox")), 10);
        break;
      case "Sin_TotalPlay_TV":
      case "M_Sin_TotalPlay_TV":
        action.click(By.xpath(property.identifier("withoutTotalPlayTv")), 10);
        break;
      case "M_Con_TotalPlay_TV":
        action.click(By.xpath(property.identifier("withTotalPlayTv")), 10);
        break;
      default:
        throw new Exception(String.format(property.report("sErrorText00"),plan[PlanTable.kindOfService]));
    }

    action.click(By.xpath(String.format(property.identifier("kindOfPlan"),plan[PlanTable.megs])), 10);

    if(plan[PlanTable.amazonPrime].equals("Si")){
      action.click(By.xpath(property.identifier("amazonPrime")), 10);
    }

    if(!plan[PlanTable.netflix].equals("-")){
      action.click(By.xpath(property.identifier("netflix")), 10);
      if(plan[PlanTable.netflix].equals("Netflix Premium")){
        action.click(By.xpath(property.identifier("netflixPremium")), 10);
      } else if(plan[PlanTable.netflix].equals("Netflix Estándar")){
        action.click(By.xpath(property.identifier("netflixStandard")), 10);
      }
    }

    action.waitUntilElementDisappears(By.xpath(property.identifier("savePlanDisabled")),2,1);
    action.click(By.xpath(property.identifier("savePlan")), 10);
    Thread.sleep(2000);
    action.waitUntilElementDisappears(By.xpath(property.identifier("waitAMomentMessage")), 2,1);
    Thread.sleep(2000);
    action.waitUntilElementDisappears(By.xpath(property.identifier("waitAMomentMessage")), 2,1);
    Thread.sleep(2000);

    screenshot.cutting(0, 0);
    wordFile.insertTestStep(property.report("reportText13"), 165);
  }

  public void addAddons(String[] plan) throws InterruptedException {
    if(!plan[PlanTable.addons].equals("Si (Manualmente)")){
      if(plan[PlanTable.plan].equals("Micronegocio")){
        if(plan[PlanTable.megs].equals("100") || plan[PlanTable.megs].equals("200") || plan[PlanTable.megs].equals("500") ||
            plan[PlanTable.megs].equals("1000")){
          action.scroll("500","0");
          action.click(By.xpath(property.identifier("includedSuite")), 10);
          action.click(By.xpath(property.identifier("totalManager")), 10);
          Thread.sleep(2000);
          action.waitUntilElementDisappears(By.xpath(property.identifier("waitAMomentMessage")), 2,1);
          Thread.sleep(2000);
          action.waitUntilElementDisappears(By.xpath(property.identifier("waitAMomentMessage")), 2,1);
          Thread.sleep(2000);
        }
      }
      getPrices();
      action.scroll("1000", "0");
      Thread.sleep(5000);
      action.waitUntilElementBeClicked(By.xpath(property.identifier("continuePlan")), 2, 1);
      Thread.sleep(5000);
    } else {
      loggerX.info(logger, 2, null, property.report("reportText13b"));
    }
  }

  public void getPrices(){
    List<String> prices = new ArrayList<>();
    kindOfServicePrice = action.getText(By.xpath(property.identifier("kindOfServicePrice")), 10);
    prices.add(String.format(property.text("kindOfServicePrice"),kindOfServicePrice));
    totalMonthRent = action.getText(By.xpath(property.identifier("totalMonthRent")), 10);
    prices.add(String.format(property.text("totalMonthRent"),totalMonthRent));
    activationCharge = action.getText(By.xpath(property.identifier("activationCharge")), 10);
    prices.add(String.format(property.text("activationCharge"),activationCharge));
    totalFirstPayment = action.getText(By.xpath(property.identifier("totalFirstPayment")), 10);
    prices.add(String.format(property.text("totalFirstPayment"),totalFirstPayment));
    wordFile.insertParagraphsIntoTestStep(property.text("reportPricesStep"), prices);
  }

  public void additionalInformation() throws InterruptedException {
    action.automaticChangeFrame(By.xpath(property.identifier("kindOfIdentification")));
    action.click(By.xpath(property.identifier("kindOfIdentification")), 10);
    action.click(By.xpath(property.identifier("kindOfIdentificationOption")), 10);
    action.sendKeys(By.xpath(property.identifier("officialIdentification")), 10, property.text("ine"));
    action.click(By.xpath(property.identifier("kindOfClientCommunication")), 10);
    action.click(By.xpath(property.identifier("kindOfClientCommunicationOption")), 10);
    screenshot.cutting(0, 0);
    wordFile.insertTestStep(property.report("reportText14"), 165);
    action.click(By.xpath(property.identifier("continueAdditionalInformation")), 10);
  }

  public void contract() throws InterruptedException {
    action.automaticChangeFrame(By.xpath(property.identifier("recordContract")));
    action.findElement(By.xpath(property.identifier("recordContract")), 20);
    action.clickToFindElement(By.xpath(property.identifier("recordContract")), 2,1);
    Thread.sleep(5000);
    action.sendKeys(By.xpath(property.identifier("extension")), 10,
        property.text("extension"));
    Thread.sleep(3000);
    action.click(By.xpath(property.identifier("record")), 10);
    action.click(By.xpath(property.identifier("finishRecord")), 10);
    action.findElement(By.xpath(property.identifier("messageSuccessfulRecord")), 20);
    screenshot.cutting(0, 0);
    wordFile.insertTestStep(property.report("reportText15"), 165);
    action.click(By.xpath(property.identifier("exitRecord")), 20);
    action.click(By.xpath(property.identifier("recommendTotalPlay")), 20);
    action.click(By.xpath(property.identifier("finishContract")), 20);
    action.waitUntilFindElement(By.xpath(property.identifier("moreOptionsMenu")), 3);
    Thread.sleep(3000);
    String urlAccount = action.getCurrentUrl();
    loggerX.info(logger,1, null, String.format("URL account: %s",urlAccount));
    wordFile.insertTextIntoTestStep(property.report("reportText21"), urlAccount);
  }

  public String activationFirstPart(String[] activation, String[] plan) throws InterruptedException {
    do {
      action.clickWithoutNotification(By.xpath(property.identifier("moreOptionsMenu")), 2);
      Thread.sleep(2000);
    }while (!action.clickWithoutNotification(By.xpath(property.identifier("activationMenu")), 2));
    action.automaticChangeFrame(By.xpath(property.identifier("configureActivation")));
    screenshot.cutting(0, 0);
    wordFile.insertTestStep(property.report("reportText16"), 165);
    action.click(By.xpath(property.identifier("configureActivation")), 10);
    action.click(By.xpath(property.identifier("kindOfOnt")), 10);
    action.click(By.xpath(String.format(property.identifier("kindOfOntOption"), activation[ActivationTable.kindOfOnt])), 10);
    action.sendKeys(By.xpath(property.identifier("serialOnt")), 10, activation[ActivationTable.serialOnt]);
    if(plan[PlanTable.kindOfService].equals("Con_TotalPlay_TV") ||
        plan[PlanTable.kindOfService].equals("Con_TotalPlay_TV_Y_Video_Soundbox") ||
        plan[PlanTable.kindOfService].equals("M_Con_TotalPlay_TV")) {
      action.click(By.xpath(property.identifier("kindOfSTB")), 10);
      action.click(By.xpath(String.format(property.identifier("kindOfSTBOption"), activation[ActivationTable.kindOfStb])), 5);
      action.sendKeys(By.xpath(property.identifier("serialSTB")), 10, activation[ActivationTable.serialStb]);
    }
    action.click(By.xpath(property.identifier("obtainDn")), 30);
    return action.getText(By.xpath(property.identifier("dn")), 10);
  }

  public void activationSecondPart(String[] plan, String[] activation) {
    if(plan[PlanTable.kindOfService].equals("M_Con_TotalPlay_TV") || plan[PlanTable.kindOfService].equals("M_Sin_TotalPlay_TV")){
      action.click(By.xpath(property.identifier("kindOfWifi")), 10);
      action.click(By.xpath(String.format(property.identifier("kindOfWifiOption"),activation[ActivationTable.kindOfWifi])), 10);
      action.sendKeys(By.xpath(property.identifier("serialWifi")), 10, activation[ActivationTable.serialWifi]);
      action.sendKeys(By.xpath(property.identifier("macWifi")), 10, activation[ActivationTable.macWifi]);
    }
    action.click(By.xpath(property.identifier("searchEquipment")), 10);
    action.waitUntilFindElement(By.xpath(property.identifier("messageEquipmentFounded")), 2);
    action.click(By.xpath(property.identifier("saveActivationConfiguration")), 10);
    action.waitUntilFindElement(By.xpath(property.identifier("messageSaveSuccessfully")), 10);
    screenshot.cutting(0, 0);
    wordFile.insertTestStep(property.report("reportText18"), 165);
    action.click(By.xpath(property.identifier("closeActivation")), 10);
    action.click(By.xpath(property.identifier("activatePlan")), 10);
  }

  public void validateActivation(){
    action.waitUntilFindElement(By.xpath(property.identifier("messageActivationSuccessfully")), 120);
  }

  public String getActivationMessage(){
    return action.getText(By.xpath(property.identifier("messageActivation")), 10);
  }

  public void initializeConfiguration(String[] configuration){
    TESTER_NAME = configuration[ConfigTable.name];
    USERNAME = configuration[ConfigTable.mail];
    PASSWORD = configuration[ConfigTable.password];

    switch(configuration[ConfigTable.kindOfLocation]){
      case "Fronteriza":
        postalCodeNumber = property.text("postalCodeNumberFronteriza");
        POSTAL_CODE = property.text("postalCodeFronteriza");
        STREET = property.text("streetFronteriza");
        EXTERNAL_NUMBER = property.text("externalNumberFronteriza");
        break;
      case "No Fronteriza":
        postalCodeNumber = property.text("postalCodeNumberNoFronteriza");
        POSTAL_CODE = property.text("postalCodeNoFronteriza");
        STREET = property.text("streetNoFronteriza");
        EXTERNAL_NUMBER = property.text("externalNumberNoFronteriza");
        break;
      case "Prepago":
        postalCodeNumber = property.text("postalCodeNumberPrepago");
        POSTAL_CODE = property.text("postalCodePrepago");
        STREET = property.text("streetPrepago");
        EXTERNAL_NUMBER = property.text("externalNumberPrepago");
        break;
      case "Manual":
        postalCodeNumber = configuration[ConfigTable.postalCode];
        POSTAL_CODE = configuration[ConfigTable.postalCode];
        STREET = configuration[ConfigTable.street];
        EXTERNAL_NUMBER = configuration[ConfigTable.externalNumber];
        break;
      default:
        break;
    }
  }
}
