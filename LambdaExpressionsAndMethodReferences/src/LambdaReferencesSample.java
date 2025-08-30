import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
/*
 Bottlenecks / Caveats when using Lambdas & Method References in Java:

 1. Capturing variables:
    - Lambdas can only capture variables that are *effectively final*.
    - Trying to modify a variable after it’s used in a lambda causes compilation errors.

 2. Readability vs. Verbosity:
    - Method references (e.g., String::compareToIgnoreCase) improve readability,
      but can sometimes make intent less explicit than a lambda (a, b) -> a.compareToIgnoreCase(b).

 3. Checked exceptions:
    - Lambdas and method references cannot throw checked exceptions
      unless the functional interface explicitly allows it.

 4. Autoboxing/unboxing:
    - Using lambdas with generic functional interfaces (like Function<T, R>)
      may cause hidden boxing/unboxing costs (e.g., int → Integer).

 5. Overuse of method references:
    - Not every lambda needs to be replaced with a method reference.
      Sometimes a lambda is clearer, especially if logic is non-trivial.

 6. Sorting:
    - Comparators built with method references are elegant,
      but repeated sorting of large collections can still be expensive —
      prefer sorted data structures if frequent ordering is required.

 7. Debugging difficulty:
    - Lambdas and method references don’t have names, so stack traces
      can be harder to interpret compared to named methods.
 */

public class LambdaReferencesSample {

    public static void main(String[] args) {
        // A local variable that will be used inside the lambda expression
        int factor = 2;

        // Define a lambda function that multiplies its input by 'factor'
        // Function<T, R> means: takes an Integer as input, returns an Integer as output
        Function<Integer, Integer> multiplyByFactor = (a) -> {
            return factor * a;  // Uses 'factor' from the enclosing scope (effectively final)
        };

        // Uncommenting the line below will cause a compilation error,
        // because variables captured by lambdas must be "effectively final"
        // factor++;

        // Apply the lambda function to the value 10
        int multipliedResult = multiplyByFactor.apply(10);

        // Print the result (should output 20)
        System.out.println(multipliedResult);

        // Define a Function that takes a String and returns an Integer.
        // Instead of writing a lambda like (s) -> Integer.parseInt(s),
        // we use a method reference to Integer::parseInt.
        Function<String, Integer> convertToInteger = Integer::parseInt;
        System.out.println(convertToInteger.apply("123"));  // Output: 123

        String prefix = "hello";
        Function<String, String> concatValues = prefix::concat;

        System.out.println(concatValues.apply(" world"));

//        Instance method of an arbitrary object of a given type
        List<String> names = Arrays.asList("Hi", "Hello", "how are you");
        names.sort(Collections.reverseOrder(String::compareToIgnoreCase));
//        names.sort(Comparator.comparingInt(String::length).reversed());
        names.forEach(System.out::println);
    }
}

