package mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.files;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.graphics.PDFCreator;
import org.springframework.stereotype.Service;

/**
 * This class helps methods to write json and xml files
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Service
public class CreateOutputStreamDTO {

  public FileOutputStream getOutputStream(String fileRoute) throws IOException {
    String path = PDFCreator.class.getClassLoader().getResource("cucumber.properties")
        .getPath().replace("target/test-classes/cucumber.properties", fileRoute)
        .replace("%C3%B3","ó").replace("%c3%b3","ó");
    return new FileOutputStream(path);
  }

  public void writeJson(String fileRoute, String json) throws IOException {
    String path = PDFCreator.class.getClassLoader().getResource("cucumber.properties")
        .getPath().replace("target/test-classes/cucumber.properties", fileRoute)
        .replace("%C3%B3","ó").replace("%c3%b3","ó");
    FileOutputStream file = new FileOutputStream(path);
    file.write(json.getBytes(StandardCharsets.UTF_8));
    file.close();
  }
}
