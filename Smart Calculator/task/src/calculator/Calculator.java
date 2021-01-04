package calculator;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    private List<String> postfixExpression;
    private String expression;

    public void readExpression(String expression) {
        this.expression = expression;
        this.postfixExpression = ExpressionUtils.infixToPostfix(expression);
    }

    public Double calculate() {

        if (postfixExpression.isEmpty()) {
            throw new IllegalStateException("No expression is given. Use readExpression method first or pass expression as an argument.");
        }

        var stack = new ArrayDeque<Double>();

        for ( var s : this.postfixExpression) {
            if (s.matches("-?\\d+\\.*\\d*")) {
                stack.push(Double.parseDouble(s));
            } else {
                operate(stack, Operators.byValue(s));
            }
        }

        if (stack.size() != 1) {
            throw new IllegalSyntaxException();
        }

        return stack.pop();
    }

    public Double calculate(String expression) {
        readExpression(expression);
        return calculate();
    }

    public String getExpression() {
        return expression;
    }

    private void operate(Deque<Double> stack, Operators operator) {
        // todo unary operators here

        double x2 = stack.pop();
        double x1 = stack.pop();

        switch (operator) {
            case ADD:
                stack.push(x1 + x2);
                break;
            case SUB:
                stack.push(x1 - x2);
                break;
            case MULT:
                stack.push(x1 * x2);
                break;
            case DIV:
                stack.push(x1 / x2);
                break;
            default:
                throw  new UnsupportedOperationException("Unknown operator: " + operator);
        }

    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        calculator.readExpression("3");
        System.out.println(calculator.calculate());
    }


}
