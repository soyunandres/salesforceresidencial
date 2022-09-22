package mx.com.totalplay.qa.automation.salesforce.residential.activation.actions.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

/**
 * Object which reads properties files
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@Service
public class ReadProperties {

  @Autowired
  @Qualifier("generalTexts")
  private ResourceBundleMessageSource texts;

  @Autowired
  @Qualifier("identifiers")
  private ResourceBundleMessageSource identifiers;

  @Autowired
  @Qualifier("paths")
  private ResourceBundleMessageSource paths;

  @Autowired
  @Qualifier("textColors")
  private ResourceBundleMessageSource textColors;

  @Autowired
  @Qualifier("urls")
  private ResourceBundleMessageSource urls;

  @Autowired
  @Qualifier("report")
  private ResourceBundleMessageSource report;

  @Autowired
  @Qualifier("pom")
  private ResourceBundleMessageSource pomFile;

  public String text(String value){
    return texts.getMessage(value, null, LocaleContextHolder.getLocale());
  }

  public String identifier(String value){
    return identifiers.getMessage(value, null, LocaleContextHolder.getLocale());
  }

  public String path(String value){
    return paths.getMessage(value, null, LocaleContextHolder.getLocale());
  }

  public String color(String value){
    return textColors.getMessage(value, null, LocaleContextHolder.getLocale());
  }

  public String url(String value){
    return urls.getMessage(value, null, LocaleContextHolder.getLocale());
  }

  public String report(String value){
    return report.getMessage(value, null, LocaleContextHolder.getLocale());
  }

  public String pom(String value){
    return pomFile.getMessage(value, null, LocaleContextHolder.getLocale());
  }
}

