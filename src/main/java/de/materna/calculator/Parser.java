package de.materna.calculator;

import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.HashMap;
import java.util.Map;

public class Parser {

  private String userInput;
  Map<String, Double> vars = new HashMap<>();

  public String getVariableName() {
    if (userInput.contains("=")) {
      int index = userInput.indexOf("=");
      String potVariable = userInput.substring(0, index).trim();
      if (potVariable.matches("_*[a-zA-Z]+[a-zA-Z0-9]*")) {
        return potVariable;
      } else {
        throw new IllegalArgumentException("no valid variable name");
      }
    }
    return null;
  }

  public Double getVariableValue() {
    if (userInput.contains("=")) {
      int index = userInput.indexOf("=");
      String potValue = userInput.substring(index+1).trim();
      return new ExpressionBuilder(potValue)
          .variables(vars.keySet())
          .build()
          .setVariables(vars)
          .evaluate();
    }
    return null;
  }

  public Map<String, Double> getVars() {
    return vars;
  }

  public void setUserInput(String userInput) {
    this.userInput = userInput;
  }
}
