package calculator;

import calculator.exceptions.IllegalSyntaxException;

import java.math.BigDecimal;
import java.util.*;

public class Calculator {

    private List<String> postfixExpression;
    private String expression;

    private HashMap<String, BigDecimal> variables = new HashMap<>();
    public void readExpression(String expression) {
        this.expression = expression;
        this.postfixExpression = ExpressionUtils.infixToPostfix(expression);
    }

    public BigDecimal calculate() {

        if (postfixExpression.isEmpty()) {
            throw new IllegalStateException("No expression is given. Use readExpression method first or pass expression as an argument.");
        }

        var stack = new ArrayDeque<BigDecimal>();

        for ( var s : this.postfixExpression) {
            if (s.matches("-?\\d+\\.*\\d*")) {
                stack.push(new BigDecimal(s));
            } else if (s.matches("[a-zA-Z]+")) {
                BigDecimal variable = variables.get(s);
                if (variable == null)
                    throw new IllegalArgumentException("Unknown argument");
                stack.push(variable);
            }  else {
                operate(stack, Operators.byValue(s));
            }
        }

        if (stack.size() != 1) {
            throw new IllegalSyntaxException();
        }

        return stack.pop();
    }

    public BigDecimal calculate(String expression) {
        readExpression(expression);
        return calculate();
    }

    public String getExpression() {
        return expression;
    }

    private void operate(Deque<BigDecimal> stack, Operators operator) {
        // todo unary operators here

        BigDecimal x2 = stack.pop();
        BigDecimal x1 = stack.pop();

        switch (operator) {
            case ADD:
                stack.push(x1.add(x2));
                break;
            case SUB:
                stack.push(x1.subtract(x2));
                break;
            case MULT:
                stack.push(x1.multiply(x2));
                break;
            case DIV:
                stack.push(x1.divide(x2));
                break;
            case POW:
                if (x2.compareTo(new BigDecimal(Integer.MAX_VALUE)) > 0)
                    throw new IllegalArgumentException("Too big value");
                stack.push(x1.pow(x2.intValue()));
                break;
            default:
                throw  new UnsupportedOperationException("Unknown operator: " + operator);
        }

    }
    private void substituteVariables(String expression) {

    }

    public void setVariables(Map<String, BigDecimal> values) {
        values.forEach((s, n) -> {
            variables.put(s, n);
        });
    }
    public  BigDecimal putVariable(String name, BigDecimal value) {
        return variables.put(name, value);
    }

    public BigDecimal getVariable(String name) {
        return variables.get(name);
    }
    public void resetVariables() {variables.clear();}


    public static void main(String[] args) {

    }


}
