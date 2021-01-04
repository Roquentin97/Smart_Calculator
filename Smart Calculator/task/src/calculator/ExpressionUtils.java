package calculator;

import java.lang.reflect.Array;
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
     * Takes an expression in standard (infix) notation and returns the same expression in postfix notation.
     * @see <a href="https://en.wikipedia.org/wiki/Reverse_Polish_notation">Reverse Polish Notation</a>
     * @param infixExpression Expression in standard (infix) notation, e.g.: (2 + 3) * 5
     * @return input expression converted to postfix notation, e.g.: 235+*
     *
     */
    // TODO handle negative numbers
    public static List<String> infixToPostfix(String infixExpression) {

        infixExpression = eliminateRedundancy(infixExpression);

        var list = new ArrayList<String>();
        var stack = new ArrayDeque<Operators>();

        Pattern digit = Pattern.compile("\\d+");
        Pattern notDigit = Pattern.compile("\\D");
        Matcher mDigit = digit.matcher(infixExpression);
        Matcher mNotDigit = notDigit.matcher(infixExpression);

        Operators operator;



        while (true) {

            if (mDigit.find())
                list.add(mDigit.group());

            handleOperator(mNotDigit, stack, o -> list.add(o.getValue()));

            if (mDigit.hitEnd() && mNotDigit.hitEnd())
                break;
        }

        while (!stack.isEmpty())
            list.add(stack.pop().getValue());

        return list;
    }

    /**
     *
     * @param nonDigit Matcher handling operators
     * @param stack Stack where operators are placed
     * @param action Consumer accepting operators popped from stack
     *
     * Depending on the priorities of participating operators decides whether push
     * operator to the stack or exchange it with already present in stack operators
     */
    private static void handleOperator(Matcher nonDigit, Deque<Operators> stack, Consumer<Operators> action) {
        Operators operator;
        if (nonDigit.find()) {
            operator = Operators.byValue(nonDigit.group());
            if (stack.peek() == null || stack.peek().getPriority() < operator.getPriority() || operator == Operators.LEFT_PH) {
                stack.push(operator);
            }
            else {
                exchangeWithStack(stack, action, operator);
                if (operator == Operators.RIGHT_PH)
                    handleOperator(nonDigit, stack, action);
            }
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
        }

    }




    public static void main(String[] args) {

        System.out.println(infixToPostfix("3 - 5 * 1 / 5 - 4 * 3 * ( 8 - 4)"));
    }


}
