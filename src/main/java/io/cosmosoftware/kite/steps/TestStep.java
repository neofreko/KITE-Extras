package io.cosmosoftware.kite.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.AllureStepReport;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import static io.cosmosoftware.kite.steps.StepPhase.*;
import static io.cosmosoftware.kite.util.ReportUtils.getLogHeader;

public abstract class TestStep {
  
  
  protected final WebDriver webDriver;
  protected Logger logger = null;
  protected AllureStepReport report;
  private String name = getClassName();
  private boolean stepCompleted = false;

  private StepPhase stepPhase = RAMPUP;
  private StepPhase currentStepPhase = RAMPUP;

  private LinkedHashMap<String, String> csvResult = null;

  public TestStep(WebDriver webDriver) {
    this.webDriver = webDriver;
  }
  
  public TestStep(WebDriver webDriver, StepPhase stepPhase) {
    this.webDriver = webDriver;
    this.stepPhase = stepPhase;
  }
  
  public void execute() {
    try {
      logger.info(stepPhaseName() + "Executing step: " + stepDescription());
      step();
    } catch (Exception e) {
      Reporter.getInstance().processException(this.report, e);
    }
  }
  
  public void finish() {
    this.report.setStopTimestamp();
    stepCompleted = true;
  }
  
  public String getClassName() {
    String s = this.getClass().getSimpleName();
    if (s.contains(".")) {
      s = s.substring(s.lastIndexOf(".") + 1);
    }
    return s;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public AllureStepReport getStepReport() {
    return report;
  }
  
  public void init(StepPhase stepPhase) {
    this.currentStepPhase = stepPhase;
    this.report = new AllureStepReport(getClientID() + ": " + stepDescription());
    this.report.setDescription(stepPhaseName() + stepDescription());
    this.report.setStartTimestamp();
  }
  
  public String getClientID() {
    return stepPhaseName() + getLogHeader(webDriver);
  }
  
  public void setLogger(Logger logger) {
    this.logger = logger;
  }
  
  public void skip() {
    logger.warn(stepPhase.name() + " " + "Skipping step: " + stepDescription());
    this.report.setStatus(Status.SKIPPED);
  }
  
  protected abstract void step() throws KiteTestException;
  
  public boolean stepCompleted() {
    return this.stepCompleted;
  }
  
  public abstract String stepDescription();

  public StepPhase getStepPhase() {
    return stepPhase;
  }

  public void setStepPhase(StepPhase stepPhase) {
    this.stepPhase = stepPhase;
  }
  
  protected String translateClassName() {
    
    String name = this.getClass().getSimpleName();
    Set<String> upperLetters = new HashSet<>();
    
    for (char letter : name.toCharArray()) {
      String letterString = Character.toString(letter);
      if (letterString.matches("[A-Z]") || letterString.matches("[0-9]")) {
        upperLetters.add(letterString);
      }
    }
    
    for (String letterString : upperLetters) {
      name = name.replaceAll(letterString, " " + letterString.toLowerCase());
    }
    
    return name;
  }
  
  private String stepPhaseName() {
    switch (currentStepPhase) {
      case RAMPUP:
        return "RU ";
      case LOADREACHED:
        return "LR ";
      default:
        return "";
    }
  }

  public void setCsvResult(LinkedHashMap<String, String> csvResult) {
    this.csvResult = csvResult;
  }

  public LinkedHashMap<String, String> getCsvResult() {
    return csvResult;
  }

  public void addToCsvResult(String key, String value) {
    if (this.csvResult == null) {
      this.csvResult = new LinkedHashMap<>();
    }
    this.csvResult.put(key, value);
  }
}
