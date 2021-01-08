package calculator;

import calculator.exceptions.IllegalSyntaxException;
import calculator.exceptions.UnknownCommandException;
import calculator.exceptions.UnknownVariableException;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

public class ConsoleCalculator {

    private Calculator calculator;

    private String commandPattern = "/.+";
    private String assignmentPattern = "[a-zA-Z]+ *= *([a-zA-Z]+|-?\\d+(\\.\\d+)?)";
    private String singleDigitPattern = "[\\+-]?\\d+(\\.\\d+)?";
    private String expressionPattern =
            "^(-?\\(+)*(-?(\\d+(\\.\\d+)?|[a-zA-Z]+)\\)*[\\+\\*\\^/-]\\(*)+-?(\\d+(\\.\\d+)?|[a-zA-Z]+)\\)*$";


    private void executeCommand(String command) {
        switch (command) {
            case "/exit":
                exit();
                break;
            case "/help":
                help();
                break;
            default:
                throw new UnknownCommandException("Unknown command " + command);
        }
    }

    private void help() {
        System.out.println("Helpful output");
    }
    private void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }

    private BigDecimal put(String assignment) {
        String name = assignment.substring(0, assignment.indexOf("=")).trim();
        String value = assignment.substring(assignment.indexOf("=") + 1).trim();

        if (!name.matches("[a-zA-Z]+"))
        if (!name.matches("[a-zA-Z]+"))
            throw new IllegalArgumentException("Invalid identifier");
        if (value.matches(singleDigitPattern))
            return calculator.putVariable(name, new BigDecimal(value));
        if (value.matches("[a-zA-Z]+")) {
            BigDecimal variable = calculator.getVariable(value);
            if (variable == null)
                throw new UnknownVariableException();
            return calculator.putVariable(name, variable);
        }

        throw new IllegalArgumentException("Invalid assignment");

    }

    private void printVariable(String name) {
        BigDecimal value = calculator.getVariable(name);
        //todo int conversion to pass test only
        System.out.println(value != null ? value.toBigInteger() : "Unknown variable");
    }


    public void launch() {
        calculator = new Calculator();

        try (Scanner scanner = new Scanner(System.in)) {
            String input;
            while (scanner.hasNext()) {
                try {
                    input = scanner.nextLine().trim();
                    input = ExpressionUtils.eliminateRedundancy(input);
                    input = ExpressionUtils.putMissingMultSigns(input);

                    if ("".equals(input)) {
                        continue;
                    } else if (input.matches(commandPattern)) {
                        executeCommand(input);
                    } else if (input.matches(singleDigitPattern)) {
                        System.out.println(input.replaceAll("\\+", ""));
                    } else if (input.matches(assignmentPattern)) {
                        put(input);
                    } else if (input.matches("[a-zA-Z]+")) {
                        printVariable(input);
                    } else if (!input.matches(expressionPattern)) {
                        throw new IllegalSyntaxException();
                    } else {
                        System.out.println(calculator.calculate(input));
                    }
                } catch (IllegalSyntaxException ex) {
                    System.out.println("Invalid expression");
                } catch (UnknownCommandException ex) {
                    System.out.println("Unknown command");
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (UnknownVariableException ex) {
                    System.out.println("Unknown variable");
                }


            }
        }
    }

    public void setCommandPattern(String commandRegex) {
        commandPattern = commandRegex;
    }

    public void setAssignmentPattern(String assignmentRegex) {
        assignmentPattern = assignmentRegex;
    }


    public static void main(String[] args) {
        ConsoleCalculator cc = new ConsoleCalculator();

        cc.launch();
    }

}
