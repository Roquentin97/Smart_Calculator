package calculator;

import calculator.exceptions.ParenthesesMismatchException;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author roquentin Roquentin9700
 */
public class ExpressionUtils {



    /**
     * @param expression String representation of arithmetical expression
     * @return String Is a String without redundancy
     *
     *  Eliminate redundancy in successive operators.
     *  Successive plus symbols are simplified to single plus (+{2, } --> +).
     *  Even sequences of minus signs are replaced with single minus sign ( (--){1, } --> - )
     */
    public static String eliminateRedundancy(String expression) {

        expression = expression.replaceAll(" +", "");
        while (expression.matches(".*(-{2,}|\\+-|-\\+|\\+{2,}).*")){
            expression = expression.replaceAll("(--){1,}", "+")
                    .replaceAll("\\+{2,}", "+")
                    .replaceAll("(\\+-|-\\+)", "-");
        }

        return expression;
    }

    /**
     * @param expression Arithmetical expression
     * @return The same expression <i>whitespaces</i> and with missing multiplication signs around braces.
     */
    public static String putMissingMultSigns(String expression) {
        expression = expression.replaceAll(" ", "");
        StringBuilder sb = new StringBuilder(expression);
        Pattern lP = Pattern.compile("\\d\\(");
        Pattern rP = Pattern.compile("\\)[\\d|\\(]");

        int counter = 0;

        Matcher lM = lP.matcher(expression);
        while (lM.find())
            sb.insert(lM.end() - 1 + counter++, Operators.MULT );

        expression = sb.toString();
        counter = 0;

        Matcher rM = rP.matcher(expression);
        while (rM.find())
            sb.insert(rM.start() + 1 + counter++, Operators.MULT);

        return sb.toString();
    }

    /**
     * Takes an expression in standard (infix) notation and returns the same expression in postfix notation.
     * @see <a href="https://en.wikipedia.org/wiki/Reverse_Polish_notation">Reverse Polish Notation</a>
     * @param infixExpression Expression in standard (infix) notation, e.g.: (2 + 3) * 5
     * @return input expression converted to postfix notation, e.g.: 235+*
     *
     */
    // TODO handle negative numbers
    public static List<String> infixToPostfix(String infixExpression) {

        infixExpression = putMissingMultSigns(eliminateRedundancy(infixExpression));

        var list = new ArrayList<String>();
        var stack = new ArrayDeque<Operators>();

        // Uses regex lookbehind to spot negative numbers after signs  +, -, *, /, (,
        String numberRegex = "((?<![a-zA-Z0-9\\)])-(\\d+(\\.\\d+)?|[a-zA-Z]+)" +
                // spots negative numbers at the very beginning of the line or positive number
                "|^-(\\d+(\\.\\d+)?|[a-zA-Z]+)|(\\d+(\\.\\d+)?|[a-zA-Z]+))";

        // Spots operators excluding unary minus
        String operatorRegex = "([\\+\\*\\\\(\\)\\^/]|(?<=\\d|\\)|[a-zA-Z])-)";

        var matcher = Pattern.compile(numberRegex+"|"+operatorRegex).matcher(infixExpression);

        Operators operator;
        String match;
        while (matcher.find()) {
            match = matcher.group();
            if (match.matches(numberRegex)) {
                list.add(match);
            }
            else {
                handleOperator(match, stack, o -> list.add(o.getValue()));
            }
        }

        while (!stack.isEmpty())
            list.add(stack.pop().getValue());

        if (list.contains("("))
            throw new ParenthesesMismatchException();

        return list;
    }

    /**
     *
     * @param operator String representing operator
     * @param stack Stack where operators are placed
     * @param action Consumer accepting operators popped from stack
     *
     * Depending on the priorities of participating operators decides whether push
     * operator to the stack or exchange it with already present in stack operators
     */
    private static void handleOperator(String operator, Deque<Operators> stack, Consumer<Operators> action) {

        Operators op = Operators.byValue(operator);
        if (stack.peek() == null || stack.peek().getPriority() < op.getPriority() || op == Operators.LEFT_PH) {
            stack.push(op);
        }
        else {
            exchangeWithStack(stack, action, op);
        }
    }

    /**
     *
     * @param stack Stack where operators are placed
     * @param action Action, applied to each retrieved from stack operator
     * @param operator Coming operator
     *
     * Retrieves operators from the stack until empty or left parentheses is met.
     * Applies given action to each retrieved operator.
     * Places coming operator on the top of the stack (or simply ignores if right parentheses).
     */
    private static void exchangeWithStack(Deque<Operators> stack, Consumer<Operators> action, Operators operator) {

        Operators last = null;

        while (!stack.isEmpty() && (last = stack.peek()).getPriority() > operator.getPriority()) {
            action.accept(last);
            stack.pop();
        }

        if (stack.isEmpty() && operator == Operators.RIGHT_PH ) {
            throw new ParenthesesMismatchException();
        }

        if (!stack.isEmpty()){
            if (operator.getPriority() > last.getPriority())
                stack.push(operator);
            else {
                if (operator == Operators.RIGHT_PH) {
                    stack.pop();
                } else {
                    action.accept(last);
                    stack.pop();
                    stack.push(operator);
                }
            }
        } else {
            stack.push(operator);
        }

    }


    public static void main(String[] args) {

        String expression = "1 + 2 * (21 / 5) ^ (43 + 12)";
        System.out.println(infixToPostfix(expression));

    }


}
