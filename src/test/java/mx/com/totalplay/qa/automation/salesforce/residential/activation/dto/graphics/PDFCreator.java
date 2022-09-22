package mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.graphics;

import java.util.Properties;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates a pdf file with load tests time graphics
 *
 * @version 1.0
 * @author Alejandro Uribe
 */
public class PDFCreator {

  private final Logger LOGGER = LoggerFactory.getLogger(PDFCreator.class);
  Properties path = new Properties();

  /**
   * Method which reads JPEG images of json report graphics and generates a load testing report in
   * pdf format
   */
  public void insertImages() {
    try {
      path.load(PDFCreator.class.getClassLoader().getResourceAsStream("path.properties"));
      String pdfPath = path.getProperty("reportPDF");
      PDDocument doc = new PDDocument();
      PDPage blankPage = new PDPage();
      doc.addPage(blankPage);
      doc.save(pdfPath);

      PDPage page = doc.getPage(0);

      PDImageXObject pdImage = PDImageXObject.createFromFile(
          path.getProperty("reportImagePost"), doc);
      //PDImageXObject pdImage2 = PDImageXObject.createFromFile(
      //    path.getProperty("reportImagePost"), doc);

      PDPageContentStream contents = new PDPageContentStream(doc, page);

      contents.drawImage(pdImage, 25, 410);
      //contents.drawImage(pdImage2, 25, 35);

      contents.close();
      doc.save(pdfPath);
      doc.close();
    } catch (Exception e) {
      LOGGER.error("An error has occurred generating load testing report", e);
    }
  }
}
