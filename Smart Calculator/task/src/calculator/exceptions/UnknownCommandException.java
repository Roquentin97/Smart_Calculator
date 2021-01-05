package calculator.exceptions;

public class UnknownCommandException  extends UnsupportedOperationException{
    public UnknownCommandException(String message) {
        super(message);
    }
}
