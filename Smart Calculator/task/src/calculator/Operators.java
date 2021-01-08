package calculator;

import java.math.BigDecimal;

public enum Operators {
    LEFT_PH("(", 0) {
        @Override
        public <T extends Number> double execute(T a, T b) {
            return Operators.unsupportedOperation("Parenthesis cannot be executed");
        }

        @Override
        public double execute(double a, double b) {
            return Operators.unsupportedOperation("Parenthesis cannot be executed");
        }

        @Override
        public BigDecimal executeBig(BigDecimal a, BigDecimal b) {
            Operators.unsupportedOperation("Parenthesis cannot be executed");
            return null;
        }
    },
    RIGHT_PH(")", 0) {
        @Override
        public <T extends Number> double execute(T a, T b) {
            return Operators.unsupportedOperation("Parenthesis cannot be executed");
        }

        @Override
        public double execute(double a, double b) {
            return Operators.unsupportedOperation("Parenthesis cannot be executed");
        }


        @Override
        public BigDecimal executeBig(BigDecimal a, BigDecimal b) {
            Operators.unsupportedOperation("Parenthesis cannot be executed");
            return null;
        }
    },
    ADD("+", 1) {
        @Override
        public <T extends Number> double execute(T a, T b) {
            return a.doubleValue() + b.doubleValue();
        }

        @Override
        public double execute(double a, double b) {
            return a + b;
        }

        @Override
        public BigDecimal executeBig(BigDecimal a, BigDecimal b) {
            return a.add(b);
        }
    },
    SUB("-", 1) {
        @Override
        public <T extends Number> double execute(T a, T b) {
            return a.doubleValue() - b.doubleValue();
        }

        @Override
        public double execute(double a, double b) {
            return a - b;
        }

        @Override
        public BigDecimal executeBig(BigDecimal a, BigDecimal b) {
            return a.subtract(b);
        }
    },
    MULT("*", 2) {
        @Override
        public <T extends Number> double execute(T a, T b) {
            return a.doubleValue() * b.doubleValue();
        }

        @Override
        public double execute(double a, double b) {
            return a * b;
        }

        @Override
        public BigDecimal executeBig(BigDecimal a, BigDecimal b) {
            return a.multiply(b);
        }
    },
    DIV("/", 2) {
        @Override
        public <T extends Number> double execute(T a, T b) {
            return a.doubleValue() / b.doubleValue();
        }

        @Override
        public double execute(double a, double b) {
            return a / b;
        }

        @Override
        public BigDecimal executeBig(BigDecimal a, BigDecimal b) {
            return a.divide(b);
        }
    },
    MOD("%", 2) {
        @Override
        public <T extends Number> double execute(T a, T b) {
            return a.doubleValue() % b.doubleValue();
        }

        @Override
        public double execute(double a, double b) {
            return a % b;
        }

        @Override
        public BigDecimal executeBig(BigDecimal a, BigDecimal b) {
            return a.remainder(b);
        }
    },
    POW("^", 3) {
        @Override
        public <T extends Number> double execute(T a, T b) {
            return Math.pow(a.doubleValue(), b.doubleValue());
        }

        @Override
        public double execute(double a, double b) {
            return Math.pow(a, b);
        }

        @Override
        public BigDecimal executeBig(BigDecimal a, BigDecimal b) {
            if (b.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0)
                throw new IllegalArgumentException("Power value is too big");
            return a.pow(b.intValue());
        }
    };


    private int priority;
    private String value;

    Operators(String value, int priority) {
        this.value = value;
        this.priority = priority;
    }

    static Operators byValue(String value) {
        switch (value) {
            case "(":
                return LEFT_PH;
            case ")":
                return RIGHT_PH;
            case "+":
                return ADD;
            case "-":
                return SUB;
            case "*":
                return MULT;
            case "/":
                return DIV;
            case "^":
                return POW;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + value);
        }
    }


    public String getValue() {
        return value;
    }

    public abstract <T extends Number> double execute(T a, T b);
    public abstract double execute(double a, double b);
    public abstract BigDecimal executeBig(BigDecimal a, BigDecimal b);

    private static double unsupportedOperation(String message) {
        throw new UnsupportedOperationException(message);
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return value;
    }
}
