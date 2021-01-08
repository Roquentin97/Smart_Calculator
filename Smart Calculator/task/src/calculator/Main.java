package calculator;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static <T extends Number> void test(T a) {
        System.out.println(a.getClass());
    }

    static void test(double a) {
        System.out.println("double");
    }

    public static void main(String[] args) {

       ConsoleCalculator cc = new ConsoleCalculator();

       cc.launch();

    }
}
