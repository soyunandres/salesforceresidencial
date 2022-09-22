package mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.date;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.springframework.stereotype.Service;

/**
 * This class gets system date
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Service
public class SystemDate {

  /**
   * This method get system date
   *
   * @return character strings
   */
  public String getDate() {
    Calendar objDate = new GregorianCalendar();
    String stringDate;
    int year = objDate.get(Calendar.YEAR);
    int month = objDate.get(Calendar.MONTH);
    int day = objDate.get(Calendar.DAY_OF_MONTH);
    if (month < 9) {
      stringDate = String.format("%s/0%s/%s", day, month + 1, year);
    } else {
      stringDate = String.format("%s/%s/%s", day, month + 1, year);
    }
    return stringDate;
  }

  public String getDateWithSlashFormat() {
    Calendar objDate = new GregorianCalendar();
    String stringDate;
    int year = objDate.get(Calendar.YEAR);
    int month = objDate.get(Calendar.MONTH);
    int day = objDate.get(Calendar.DAY_OF_MONTH);

    if (month < 9) {
      stringDate = String.format("%s-0%s-%s-", day, month + 1, year);
    } else {
      stringDate = String.format("%s-%s-%s-", day, month + 1, year);
    }
    return stringDate;
  }
}
