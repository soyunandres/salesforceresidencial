package mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.output.LoggerX;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class reads and writes excel files
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Service
public class ReadExcelFile {

  @Autowired
  private ReadProperties property;

  @Autowired
  private LoggerX loggerX;

  private final Logger logger = LoggerFactory.getLogger(ReadExcelFile.class);

  public String[][] readExcel(String filepath, String sheetName, int totalColumns,
      int initialColumn,
      int totalRows, int initialRow) {
    try {
      File file = new File(filepath);
      FileInputStream inputStream = new FileInputStream(file);
      XSSFWorkbook newWorkbook = new XSSFWorkbook(inputStream);
      XSSFSheet newSheet = newWorkbook.getSheet(sheetName);

      String[][] planes = new String[totalRows][totalColumns];

      for (int i = 0; i < totalRows; i++) {
        XSSFRow row = newSheet.getRow(i + initialRow - 1);
        for (int j = 0; j < totalColumns; j++) {//row.getLastCellNum()
          switch (row.getCell(j + initialColumn - 1).getCellType()) {
            case BLANK:
              planes[i][j] = "-";
              break;
            case STRING:
              planes[i][j] = row.getCell(j + initialColumn - 1).getStringCellValue();
              break;
            case NUMERIC:
              planes[i][j] = String.valueOf((int) row.getCell(j + initialColumn - 1)
                  .getNumericCellValue());
              break;
            case ERROR:
            default:
              throw new Exception("An error has occurred reading excel cell value");
          }
        }
      }
      inputStream.close();
      return planes;
    } catch (Exception e) {
      String[][] planes = new String[0][];
      logger.error(String.format("%sAn error has occurred reading excel file%s",
          property.color("highRed"), property.color("DEFAULT")), e);
      return planes;
    }
  }

  public void writeCellValues(String filepathIn, String filepathOut, String sheetName,
      int rowNumber, int firstCellNumber, List<String> dataToWrite) {
    try {
      File file = new File(filepathIn);
      File files = new File(filepathOut);
      FileInputStream inputStream = new FileInputStream(file);
      XSSFWorkbook newWorkbook = new XSSFWorkbook(inputStream);
      XSSFSheet newSheet = newWorkbook.getSheet(sheetName);
      XSSFRow row = newSheet.getRow(rowNumber);
      //XSSFCell firstCell = row.getCell(cellNumber - 1);
      //System.out.println("first cell value is:" + firstCell.getStringCellValue());

      for (int i = 0; i < dataToWrite.size(); i++) {
        XSSFCell nextCell = row.createCell(firstCellNumber + i);
        nextCell.setCellValue(dataToWrite.get(i));
      }

      //System.out.println("nextcell value:" + nextCell.getStringCellValue());
      FileOutputStream outputStream = new FileOutputStream(files);
      newWorkbook.write(outputStream);
      outputStream.close();
      inputStream.close();
    } catch (Exception e) {
      logger.error(String.format("%sAn error has occurred writing excel file%s",
          property.color("highRed"), property.color("DEFAULT")), e);
    }
  }

  public void writeModelUrls(String filepathIn, String filepathOut, String sheetName, int rowNumber,
      int columnNumber, List<String> urls){
    try {
      File inFile = new File(filepathIn);
      File outFile = new File(filepathOut);
      FileInputStream inputStream = new FileInputStream(inFile);
      XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
      XSSFSheet sheet = workbook.getSheet(sheetName);

      for(int i = 0; i < urls.size(); i++){
        XSSFRow row = sheet.getRow(rowNumber + i);
        XSSFCell cell = row.getCell(columnNumber);
        cell.setCellValue(urls.get(i));
      }

      FileOutputStream outputStream = new FileOutputStream(outFile);
      workbook.write(outputStream);
      outputStream.close();
      inputStream.close();
    }catch (Exception e){
      loggerX.error(logger, 1,
          String.format("An error has occurred writing excel file '%s'",filepathOut), null);
    }
  }

  public void characterConversion(String[][] planes) {
    for (int i = 0; i < planes.length; i++) {
      for (int j = 0; j < planes[i].length; j++) {
        if(j==1)
          planes[i][j] = planes[i][j].replace("_"," ");
        if(j==2) {
          planes[i][j] = planes[i][j].replace("_pl01_", " + ");
          planes[i][j] = planes[i][j].replace("_entre_", "/").replace("_a__"," - ");
          planes[i][j] = planes[i][j].replace("Plan1_","").replace("Plan2_","")
              .replace("Plan3_","").replace("Plan4_","")
              .replace("Plan5_","").replace("Edu_","Edu. ");
          planes[i][j] = planes[i][j].replace("_"," ");
        }
      }
    }
  }

  public void showTable(String[][] table) {
    System.out.println();
    for (int i = 0; i < table.length; i++) {
      System.out.println();
      for (int j = 0; j < table[i].length; j++) {
        System.out.print(table[i][j] + "||");
      }
    }
    System.out.println();
  }

  public Boolean rowValidation(String[] row, int initialRow, int finalRow){
    for (int i = initialRow; i <= finalRow; i++) {
      if(row[i].equals("-")){
        return false;
      }
    }
    return true;
  }
}


