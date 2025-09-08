import java.util.Arrays;

/*
================================================================================
Java Varargs (variable-length arguments)
================================================================================

WHAT ARE VARARGS?
-----------------
- Varargs let a method accept ZERO or MORE arguments of the same type.
- Syntax: place `...` after the type: `void m(int... xs)`.
- At compile-time, Java rewrites varargs calls into array creation.
  Example: `m(1,2,3)` becomes `m(new int[]{1,2,3})`.

DECLARATION RULES
-----------------
1) Only ONE varargs parameter is allowed.
2) It MUST be the LAST parameter in the method signature.
   ✓  void f(int fixed, String... rest)
   ✗  void g(String... rest, int tail)  // compilation error

CALLING BEHAVIOR
----------------
- You can pass:
   • no args      -> the varargs parameter becomes an empty array
   • one arg      -> array of length 1
   • many args    -> array with those values
- You may also explicitly pass an array: `f(new String[]{"a","b"})`.

OVERLOADING + RESOLUTION ORDER
------------------------------
When multiple overloads match, Java chooses in this order:
  1) Exact match
  2) Primitive widening (e.g., int -> long)
  3) Boxing / Unboxing (e.g., int -> Integer)
  4) Varargs (lowest priority fallback)

AMBIGUITY PITFALLS
------------------
- Two varargs overloads with different element types can be ambiguous,
  especially with zero arguments, e.g. `f(int...)` vs `f(long...)`.
- Avoid overload sets where multiple varargs could match the same call.

VARARGS VS ARRAY PARAMETERS
---------------------------
- `void g(int[] xs)` and `void g(int... xs)` are different declarations.
- If you CALL with a literal list (`g(1,2,3)`), only the varargs version matches.
- If you CALL with an explicit array (`g(new int[]{1,2,3})`),
  both signatures *could* exist and overload resolution applies.

PERFORMANCE NOTES
-----------------
- Every varargs call creates an array (unless the compiler can apply
  a special-case optimization for zero-arg calls on certain well-known APIs).
- In hot paths, consider overloads for common small arities (0/1/2) to avoid
  array allocation, or accept an explicit array if the caller can reuse it.


REAL-WORLD EXAMPLES
-------------------
- `String.format(String fmt, Object... args)`
- `System.out.printf(String fmt, Object... args)`
- Logging frameworks: `logger.debug("x={}, y={}", x, y)`


================================================================================
Below: A runnable demo covering:
  - Basic usage
  - Rules (varargs last)
  - Overloading behavior
  - Ambiguity (commented to compile)
  - Resolution order (exact, widening, boxing, varargs)
  - Varargs vs array
  - Simple printf-style logger
================================================================================
*/
public class VarargsDemo {

    // 1. Basic Varargs Example
    // A method that accepts zero or more integers and prints them
    void printNumbers(int... numbers) {
        System.out.println("Numbers: " + Arrays.toString(numbers));
    }

    // 2. Varargs must be the LAST parameter
    // First parameter is multiplied with sum of all others
    long computeSumAndMultiplyWithFirst(int a, int... varargs) {
        int sum = Arrays.stream(varargs).sum(); // sum of varargs
        return (long) a * sum;
    }

    // 3. Method Overloading with Varargs
    void print(int a, int b) {
        System.out.println("Overload: two ints → " + a + ", " + b);
    }

    void print(int... numbers) {
        System.out.println("Overload: int varargs → " + Arrays.toString(numbers));
    }

    // 4. Ambiguity Case (commented to avoid compilation error)

//    void show(int... x) {
//        System.out.println("int varargs");
//    }
//
//    void show(long... x) {
//        System.out.println("long varargs");
//    }

    // 5. Varargs vs Array
    void printArray(int[] arr) {
        System.out.println("Array method → " + Arrays.toString(arr));
    }

    void printVarargs(int... arr) {
        System.out.println("Varargs method → " + Arrays.toString(arr));
    }

    // 6. Resolution Order Example
    void test(int a) {
        System.out.println("Exact match: int");
    }

    void test(long a) {
        System.out.println("Widening: long");
    }

    void test(Integer a) {
        System.out.println("Autoboxing: Integer");
    }

    void test(int... a) {
        System.out.println("Varargs: int...");
    }

    // 7. Real-world use case: simple logger (like printf / format)
    void log(String message, Object... args) {
        String formatted = String.format(message, args);
        System.out.println("LOG: " + formatted);
    }

    public static void main(String[] args) {
        VarargsDemo demo = new VarargsDemo();

        System.out.println("=== 1. Basic Varargs ===");
        demo.printNumbers();             // No args → []
        demo.printNumbers(1);            // Single arg
        demo.printNumbers(1, 2, 3, 4, 5);// Multiple args

        System.out.println("\n=== 2. Rule: Varargs must be last ===");
        long result = demo.computeSumAndMultiplyWithFirst(10, 1, 2, 3, 4, 5);
        // sum = 15 → 15 * 10 = 150
        System.out.println("Result: " + result);

        System.out.println("\n=== 3. Overloading with Varargs ===");
        demo.print(1, 2);       // Exact two ints method wins
        demo.print(1, 2, 3, 4); // Falls back to varargs

        System.out.println("\n=== 4. Ambiguity (commented out) ===");
        // demo.show(); // Would cause compilation error (ambiguous)

        System.out.println("\n=== 5. Varargs vs Array ===");
        demo.printArray(new int[]{7, 8, 9}); // Calls array method
        demo.printVarargs(7, 8, 9);          // Calls varargs method

        System.out.println("\n=== 6. Resolution Order ===");
//        When choosing between overloaded methods:
//        Exact match
//        Widening (e.g., int → long)
//        Autoboxing (e.g., int → Integer)
//        Varargs
        demo.test(5); // Matches exact int
        demo.test(5L); // Matches long
        demo.test(Integer.valueOf(5)); // Matches Integer
        demo.test(); // No args → varargs

        System.out.println("\n=== 7. Real-world Use Case ===");
        demo.log("Hello %s, you have %d new messages.", "Alice", 3);
        demo.log("The sum of %d + %d is %d", 5, 10, 15);
    }
}
