package calculator.exceptions;

import java.util.IllegalFormatException;

public class IllegalSyntaxException extends IllegalArgumentException {

    public IllegalSyntaxException() {
        super("Expression has unexpected syntax format");
    }
}
