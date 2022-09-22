package mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.support;

import org.springframework.stereotype.Service;

@Service
public class GenerateRandomNumber {

  public static String randomNumber;

  public String getRandomNumber(){
    return randomNumber;
  }

  public String getRandomNumber(int initialNumber, int finalNumber){
    return String.valueOf((int) Math.floor(Math.random()*(initialNumber-finalNumber+1)+finalNumber));
  }

  public void generateMixDoubleRandomNumber(int initialNumber1, int finalNumber1, int initialNumber2, int finalNumber2){
    randomNumber = getRandomNumber(initialNumber1,finalNumber1)+getRandomNumber(initialNumber2,finalNumber2);
  }
}
