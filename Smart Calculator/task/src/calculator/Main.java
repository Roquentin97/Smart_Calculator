package calculator;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {



    public static void main(String[] args) {

        Calculator calculator = new Calculator();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (true) {
                line = scanner.nextLine();
                if ("".equals(line))
                    continue;
                if ("/exit".equals(line)) {
                    System.out.println("Bye!");
                    return;
                }
                if ("/help".equals(line)) {
                    System.out.println("Helpful information");
                    continue;
                }

                int result = calculator.calculate(line).intValue();
                System.out.println(result);
            }
        }
    }
}
