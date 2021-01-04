package calculator;

public enum Operators {
    LEFT_PH("(", 0),
    RIGHT_PH(")", 0),
    ADD("+", 1),
    SUB("-", 1),
    MULT("*", 2),
    DIV("/", 2);

    private int priority;
    private String value;

    Operators(String value, int priority) {
        this.value = value;
        this.priority = priority;
    }

    static Operators byValue(String value) {
        switch (value) {
            case "+":
                return ADD;
            case "-":
                return SUB;
            case "*":
                return MULT;
            case "/":
                return DIV;
            case "(":
                return LEFT_PH;
            case ")":
                return RIGHT_PH;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + value);
        }
    }

    public String getValue() {
        return value;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return value;
    }
}
