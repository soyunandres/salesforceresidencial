package mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.date.SystemDate;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class reads and writes word files
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Service
public class WriteWordFile {

  private final Logger logger = LoggerFactory.getLogger(WriteWordFile.class);

  @Autowired
  private ReadProperties property;

  @Autowired
  private SystemDate date;

  public static int STEP_NUMBER = 1;
  public static int DEFECT_NUMBER = 1;
  public static int TEST_CASE_NUMBER = 1;
  private String wordPath;

  public void setWordPath(String wordPath){
    this.wordPath = wordPath;
  }

  public void insertDateInHeader() throws Exception {
    try (XWPFDocument doc = new XWPFDocument(
        new FileInputStream(property.path("wordOriginalPath")))) {

      XWPFHeader header = doc.createHeader(HeaderFooterType.DEFAULT);
      XWPFParagraph paragraph = header.getParagraphArray(1);
      if (paragraph == null) {
        paragraph = header.createParagraph();
      }
      paragraph.setAlignment(ParagraphAlignment.LEFT);
      XWPFRun run = paragraph.createRun();
      run.setText(String.format("Fecha de emisión: %s",date.getDate()));

      FileOutputStream out = new FileOutputStream(wordPath);
      doc.write(out);
      out.close();
    } catch (Exception e) {
      throw new Exception(String.format("%sAn error has occurred creating word file and inserting date \n%s%s",
          property.color("highRed"), e, property.color("DEFAULT")));
    }
  }

  public void insertCase(String CasoDePrueba, String text, boolean esDefecto) {
    try (XWPFDocument docx = new XWPFDocument(
        new FileInputStream(wordPath))) {

      XWPFParagraph paragraph = docx.createParagraph();
      XWPFRun run = paragraph.createRun();

      if (!esDefecto) {
        run.setText(TEST_CASE_NUMBER + " - Caso de prueba: " + CasoDePrueba);
      } else {
        run.setText(TEST_CASE_NUMBER + " - Defecto en caso de prueba: " + CasoDePrueba);
      }

      XWPFTable tab = docx.createTable();
      XWPFTableRow row = tab.getRow(0);

      row.getCell(0).setText("Tipo de caso de prueba");
      row.addNewTableCell().setText(
          "Documental ( ), Integral ( ), Funcionales (X), Unitarias ( ), Performance ( ), UAT ( ), "
              + "END2END ( ), Sistema ( )");
      row = tab.createRow(); // Second Row
      row.getCell(0).setText("Estatus");
      row.getCell(1).setText("Exitoso");
      row = tab.createRow(); // Third Row
      row.getCell(0).setText("Descripción");
      row.getCell(1).setText(text);

      paragraph = docx.createParagraph();
      run = paragraph.createRun();
      run.setText("");

      FileOutputStream out = new FileOutputStream(wordPath);
      docx.write(out);
      out.close();
      STEP_NUMBER = 1;
      DEFECT_NUMBER = 1;
    } catch (Exception e) {
      logger.error(String.format("%sAn error has occurred inserting case in word file%s",
          property.color("highRed"), property.color("DEFAULT")), e);
    }
  }

  public void insertTestStep(String stepDescription, int imageWidth) {
    try (XWPFDocument docx = new XWPFDocument(
        new FileInputStream(wordPath))) {
      //165 ANCHO DE CAPTURA SIMPLE
      XWPFTable tab = docx.createTable();
      XWPFTableRow row = tab.getRow(0);
      XWPFTableCell cell;

      if (STEP_NUMBER == 1) {
        row.getCell(0).setText("Paso");
        row.addNewTableCell().setText("Descripción");
        row.addNewTableCell().setText("Resultado");

        row = tab.createRow(); // Second Row
        row.getCell(0).setText(STEP_NUMBER + "");
        row.getCell(1).setText(stepDescription);
        cell = row.getCell(2);
      } else {
        row.getCell(0).setText(STEP_NUMBER + "");
        row.addNewTableCell().setText(stepDescription);
        cell = row.addNewTableCell();
      }

      XWPFParagraph paragraph = cell.addParagraph();
      XWPFRun run = paragraph.createRun();
      String capturePath = String.format(property.path("imagesPath"),
          TEST_CASE_NUMBER, STEP_NUMBER);
      FileInputStream captureImage = new FileInputStream(capturePath);
      run.addPicture(captureImage, Document.PICTURE_TYPE_PNG, capturePath, Units.toEMU(350),
          Units.toEMU(imageWidth));

      FileOutputStream out = new FileOutputStream(wordPath);
      docx.write(out);
      out.close();
      STEP_NUMBER++;
    } catch (Exception e) {
      logger.error(String.format("%sAn error has occurred inserting step in word file %s",
          property.color("highRed"), property.color("DEFAULT")), e);
    }
  }

  public void insertTextIntoTestStep(String stepDescription, String text) {
    try (XWPFDocument docx = new XWPFDocument(
        new FileInputStream(wordPath))) {
      //165 ANCHO DE CAPTURA SIMPLE
      XWPFTable tab = docx.createTable();
      XWPFTableRow row = tab.getRow(0);
      XWPFTableCell cell;

      if (STEP_NUMBER == 1) {
        row.getCell(0).setText("Paso");
        row.addNewTableCell().setText("Descripción");
        row.addNewTableCell().setText("Resultado");

        row = tab.createRow(); // Second Row
        row.getCell(0).setText(STEP_NUMBER + "");
        row.getCell(1).setText(stepDescription);
        cell = row.getCell(2);
      } else {
        row.getCell(0).setText(STEP_NUMBER + "");
        row.addNewTableCell().setText(stepDescription);
        cell = row.addNewTableCell();
      }

      XWPFParagraph paragraph = cell.addParagraph();
      XWPFRun run = paragraph.createRun();
      run.setText(text);

      FileOutputStream out = new FileOutputStream(wordPath);
      docx.write(out);
      out.close();
      STEP_NUMBER++;
    } catch (Exception e) {
      logger.error(String.format("%sAn error has occurred inserting step in word file %s",
          property.color("highRed"), property.color("DEFAULT")), e);
    }
  }

  public void insertParagraphsIntoTestStep(String stepDescription, List<String> paragraphs){
    try (XWPFDocument docx = new XWPFDocument(new FileInputStream(wordPath))) {
      //165 ANCHO DE CAPTURA SIMPLE
      XWPFTable tab = docx.createTable();
      XWPFTableRow row = tab.getRow(0);
      XWPFTableCell cell;

      if (STEP_NUMBER == 1) {
        row.getCell(0).setText("Paso");
        row.addNewTableCell().setText("Descripción");
        row.addNewTableCell().setText("Resultado");

        row = tab.createRow(); // Second Row
        row.getCell(0).setText(STEP_NUMBER + "");
        row.getCell(1).setText(stepDescription);
        cell = row.getCell(2);
      } else {
        row.getCell(0).setText(STEP_NUMBER + "");
        row.addNewTableCell().setText(stepDescription);
        cell = row.addNewTableCell();
      }

      for (String s : paragraphs) {
        XWPFParagraph paragraph = cell.addParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(s);
      }

      FileOutputStream out = new FileOutputStream(wordPath);
      docx.write(out);
      out.close();
      STEP_NUMBER++;
    } catch (Exception e) {
      logger.error(String.format("%sAn error has occurred inserting step in word file %s",
          property.color("highRed"), property.color("DEFAULT")), e);
    }
  }

  public void finishTest() {
    try (XWPFDocument docx = new XWPFDocument(
        new FileInputStream(wordPath))) {

      XWPFParagraph paragraph = docx.createParagraph();
      XWPFRun run = paragraph.createRun();
      run.setText("");

      FileOutputStream out = new FileOutputStream(wordPath);
      docx.write(out);
      out.close();
      TEST_CASE_NUMBER++;
    } catch (Exception e) {
      logger.error(String.format("%sAn error has occurred finishing word file %s",
          property.color("highRed"), property.color("DEFAULT")), e);
    }
  }

  public void insertDefect(String descriptionDefect, int imageWidth, int numberStepDefect) {
    try (XWPFDocument docx = new XWPFDocument(
        new FileInputStream(wordPath))) {

      XWPFTable tab = docx.createTable();
      XWPFTableRow row = tab.getRow(0);
      row.getCell(0).setText("Paso");
      row.addNewTableCell().setText("Descripción");
      row.addNewTableCell().setText("Resultado");

      row = tab.createRow();
      row.getCell(0).setText(DEFECT_NUMBER + "");
      row.getCell(1).setText(descriptionDefect);
      XWPFTableCell cell = row.getCell(2);
      XWPFParagraph paragraph = cell.addParagraph();
      XWPFRun run = paragraph.createRun();
      String capturePath = String.format(property.path("imagesPath"),
          TEST_CASE_NUMBER - 1, numberStepDefect);
      FileInputStream captureImage = new FileInputStream(capturePath);
      run.addPicture(captureImage, Document.PICTURE_TYPE_PNG, capturePath, Units.toEMU(350),
          Units.toEMU(imageWidth));

      FileOutputStream out = new FileOutputStream(wordPath);
      docx.write(out);
      out.close();
      DEFECT_NUMBER++;
    } catch (Exception e) {
      logger.error(String.format("%sAn error has occurred creating wordFile %s",
          property.color("highRed"), property.color("DEFAULT")), e);
    }
  }
}
