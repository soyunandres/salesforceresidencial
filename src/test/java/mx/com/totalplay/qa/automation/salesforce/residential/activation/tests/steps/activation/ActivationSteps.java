package mx.com.totalplay.qa.automation.salesforce.residential.activation.tests.steps.activation;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import io.cucumber.spring.CucumberContextConfiguration;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.date.SystemDate;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.ReadExcelFile;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.ReadProperties;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files.WriteWordFile;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.output.LoggerX;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.selenium.TakeScreenshot;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.support.GenerateRandomNumber;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.configuration.IntegrationPhaseSpringConfiguration;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.tables.ActivationTable;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.tables.ConfigTable;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.tables.PlanTable;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.pages.Salesforce;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.services.AutoFindService;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.services.CleanEquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.SkipException;

/**
 * This class contains cucumber scenarios to do a residential activation
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@CucumberContextConfiguration
@ContextConfiguration(classes = IntegrationPhaseSpringConfiguration.class)
public class ActivationSteps extends AbstractTestNGSpringContextTests {

  @Autowired
  private ReadExcelFile readExcelFile;

  @Autowired
  private ReadProperties property;

  @Autowired
  private CleanEquipmentService cleanEquipmentService;

  @Autowired
  private AutoFindService autofind;

  @Autowired
  private Salesforce salesforce;

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

  private final Logger logger = LoggerFactory.getLogger(ActivationSteps.class);
  private String[][] activation;
  private String[] configuration;
  private String[][] plans;
  private String dn;
  private int scenario;

  @Dado("que soy un usuario certificado de Salesforce {string}")
  public void setUp(String escenario) throws Exception {
    this.scenario = Integer.parseInt(escenario);
    activation = readExcelFile.readExcel(property.path("activationFlux"),
        property.path("planTab"), ActivationTable.totalColumns, ActivationTable.initialColumn,
        ActivationTable.totalRows, ActivationTable.initialRow);
    String[][] configurationM = readExcelFile.readExcel(property.path("activationFlux"),
        property.path("configurationTab"), ConfigTable.totalColumns, ConfigTable.initialColumn,
        ConfigTable.totalRows, ConfigTable.initialRow);
    configuration = configurationM[0];
    plans = readExcelFile.readExcel(property.path("activationFlux"),
        property.path("planTab"), PlanTable.totalColumns, PlanTable.initialColumn,
        PlanTable.totalRows, PlanTable.inititalRow);

    Assert.assertTrue(readExcelFile.rowValidation(configuration, ConfigTable.name, ConfigTable.kindOfLocation),
        String.format(property.report("errorText00"), property.path("configurationTab")));

    if(!readExcelFile.rowValidation(activation[scenario], ActivationTable.kindOfOnt,
        ActivationTable.serialOnt) && !configuration[ConfigTable.onlyModels].equals("Si")){
      throw new SkipException(String.format(property.report("errorText00B"),scenario));
    }

    if(plans[scenario][PlanTable.kindOfService].equals("Con_TotalPlay_TV_Y_Video_Soundbox") ||
        plans[scenario][PlanTable.kindOfService].equals("Con_TotalPlay_TV")){
      if(!readExcelFile.rowValidation(activation[scenario], ActivationTable.kindOfStb, ActivationTable.macStb) &&
          !configuration[ConfigTable.onlyModels].equals("Si")){
        throw new SkipException(String.format(property.report("errorText00B"),scenario));
      }
    }
    if(plans[scenario][PlanTable.kindOfService].equals("M_Sin_TotalPlay_TV")){
      if(!readExcelFile.rowValidation(activation[scenario], ActivationTable.kindOfWifi,
          ActivationTable.macWifi) && !configuration[ConfigTable.onlyModels].equals("Si")){
        throw new SkipException(String.format(property.report("errorText00B"),scenario));
      }
    } else if(plans[scenario][PlanTable.kindOfService].equals("M_Con_TotalPlay_TV")){
      if(!readExcelFile.rowValidation(activation[scenario], ActivationTable.kindOfStb,
          ActivationTable.macWifi) && !configuration[ConfigTable.onlyModels].equals("Si")){
        throw new SkipException(String.format(property.report("errorText00B"),scenario));
      }
    }

    if(scenario==0){
      generateRandomNumber.generateMixDoubleRandomNumber(0, 999, 0, 99);
    }

    if(!readExcelFile.rowValidation(plans[scenario], PlanTable.plan, PlanTable.megs)){
      throw new SkipException(String.format(property.report("errorText00C"),scenario));
    }
    salesforce.initializeConfiguration(configuration);
    wordFile.setWordPath(String.format(property.path("wordPath"),configuration[0],
        date.getDateWithSlashFormat(),generateRandomNumber.getRandomNumber()+scenario));
    wordFile.insertDateInHeader();
    wordFile.insertCase(property.report("reportText00"),property.report("reportText00B"), false);
  }

  @Cuando("realizo la limpieza de los equipos")
  public void cleanOnt() throws Exception {
    if(!configuration[ConfigTable.onlyModels].equals("Si")) {
      if (!activation[scenario][ActivationTable.kindOfOnt].equals("-") && !activation[scenario][ActivationTable.serialOnt]
          .equals("-")) {
        loggerX.info(logger, 1, null, property.report("message00"));
        for (int i = 0; i < 5; i++) {
          try {
            cleanEquipmentService.clean(activation[scenario][ActivationTable.serialOnt],"", "ont", scenario);
            break;
          } catch (Exception e) {
            loggerX.error(logger, 1, String.format(property.report("errorText01"),
                activation[scenario][ActivationTable.serialOnt], i, e), null);
          }
          if (i == 4) {
            throw new Exception(property.report("errorText02"));
          }
        }
      } else {
        throw new Exception(property.report("errorText03"));
      }
      if (plans[scenario][PlanTable.kindOfService].equals("Con_TotalPlay_TV") ||
          plans[scenario][PlanTable.kindOfService].equals("Con_TotalPlay_TV_Y_Video_Soundbox") ||
          plans[scenario][PlanTable.kindOfService].equals("M_Con_TotalPlay_TV")) {
        loggerX.info(logger, 1, null, property.report("message01"));
        if (!activation[scenario][ActivationTable.kindOfStb].equals("-") && !activation[scenario][ActivationTable.serialStb].equals("-")) {
          for (int i = 0; i < 5; i++) {
            try {
              cleanEquipmentService.clean(activation[scenario][ActivationTable.serialStb], activation[scenario][ActivationTable.macStb],
                  "stb", scenario);
              break;
            } catch (Exception e) {
              loggerX.error(logger, 1, String.format(property.report("errorText04"),
                  activation[scenario][ActivationTable.serialStb], i, e), null);
            }
            if (i == 4) {
              throw new Exception(property.report("errorText05"));
            }
          }
        } else {
          throw new Exception(property.report("errorText06"));
        }
      }
      if (plans[scenario][PlanTable.kindOfService].equals("M_Sin_TotalPlay_TV") ||
          plans[scenario][PlanTable.kindOfService].equals("M_Con_TotalPlay_TV")) {
        if(!activation[scenario][ActivationTable.kindOfWifi].equals("-") &&
            !activation[scenario][ActivationTable.serialWifi].equals("-") &&
            !activation[scenario][ActivationTable.macWifi].equals("-")){
          for (int i = 0; i < 5; i++) {
            try {
              cleanEquipmentService.clean(activation[scenario][ActivationTable.serialWifi], activation[scenario][ActivationTable.macWifi],
                  "wifi", scenario);
              break;
            } catch (Exception e) {
              loggerX.error(logger, 1, String.format(property.report("errorText11"),
                  activation[scenario][ActivationTable.serialWifi], i, e), null);
            }
            if (i == 4) {
              throw new Exception(property.report("errorText12"));
            }
          }
        } else {
          throw new Exception(property.report("errorText10"));
        }
      }
    }
  }

  @Y("verifico que la ONT se encuentre en autofind")
  public void autoFindVerification() throws Exception {
    if(!configuration[ConfigTable.onlyModels].equals("Si")) {
      loggerX.info(logger, 1, null, property.report("message02"));
      Thread.sleep(10000);
      for (int i = 0; i < 5; i++) {
        if (autofind.searchOntInAutoFind(activation[scenario][ActivationTable.serialOnt])) {
          loggerX.info(logger, 1, null, property.report("message03"));
          break;
        }
        if (i == 4) {
          throw new Exception(property.report("errorText07"));
        }
        Thread.sleep(5000);
      }
    }
  }

  @Y("genero el modelado de la cuenta")
  public void residentialModelGeneration() throws Exception {
    salesforce.goSalesforce();
    if(scenario==0) {
      salesforce.log();
    }
    salesforce.selectResidentialModel();
    salesforce.vendorDetails();
    salesforce.installationLocation();
    //salesforce.consolidation();
    salesforce.contactDetails(generateRandomNumber.getRandomNumber());
    if (configuration[ConfigTable.automaticPlan].equals("Automatico")) {
      salesforce.planSelection(plans[scenario]);
      salesforce.addAddons(plans[scenario]);
    }
    salesforce.additionalInformation();
    salesforce.contract();
    loggerX.info(logger, 1, null, property.report("message04"));
  }

  @Y("obtengo el DN del front")
  public void getDn() throws InterruptedException {
    if(!configuration[ConfigTable.onlyModels].equals("Si")) {
      dn = salesforce.activationFirstPart(activation[scenario], plans[scenario]);
      loggerX.info(logger, 1, null, String.format(property.report("message05"), dn));
    }
  }

  @Y("realizo una correcta limpieza del DN")
  public void cleanDn() {
    if(!configuration[ConfigTable.onlyModels].equals("Si")) {
      for (int i = 0; i < 5; i++) {
        try {
          cleanEquipmentService.clean(dn,"", "dn", scenario);
          Thread.sleep(5000);
          break;
        } catch (Exception e) {
          loggerX.error(logger, 1, String.format(
              property.report("errorText08"), dn, i, e), null);
        }
      }
    }
  }

  @Y("realizo la activacion de la cuenta")
  public void activateAccount() {
    if(!configuration[ConfigTable.onlyModels].equals("Si")) {
      if(!plans[scenario][PlanTable.addons].equals("Si (Manualmente)")) {
        salesforce.activationSecondPart(plans[scenario], activation[scenario]);
      } else {
        loggerX.info(logger, 2, null, property.report("reportText22"));
      }
    }
  }

  @Entonces("recibo un mensaje indicando que la activacion fue exitosa")
  public void entonces(){
    if(!configuration[ConfigTable.onlyModels].equals("Si")) {
      salesforce.validateActivation();
      loggerX.info(logger, 1, null,
          String.format(property.report("message07"), salesforce.getActivationMessage()));
      screenshot.cutting(0, 0);
      wordFile.insertTestStep(property.report("reportText19"), 165);
      wordFile.insertTextIntoTestStep(property.report("reportText20"),
          salesforce.getActivationMessage());
    }
  }
}
