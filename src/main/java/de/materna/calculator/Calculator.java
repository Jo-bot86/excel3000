package de.materna.calculator;

import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Scanner;

public class Calculator {
  Parser parser;

  public Calculator(Parser parser) {
    this.parser = parser;
  }

  public void start() {
    Scanner sc = new Scanner(System.in);

    while (true) {
      System.out.print("> ");
      String userInput = sc.nextLine();
      parser.setUserInput(userInput);
      String variable = parser.getVariableName();
      Double value = parser.getVariableValue();

      if (userInput.equals("exit")) break;

      if (variable != null) {
        parser.getVars().put(variable, value);
        System.out.println(value);
      } else {
        try {
          double result = new ExpressionBuilder(userInput)
              .variables(parser.getVars().keySet())
              .build()
              .setVariables(parser.getVars())
              .evaluate();
          System.out.println(result);

        } catch (Exception e) {
          System.out.println("Illegal Input");
        }
      }
    }
  }
}
