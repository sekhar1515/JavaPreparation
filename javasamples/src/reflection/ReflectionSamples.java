package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/*
 * 🚫 Disadvantages of Reflection in Java
 *
 * 1. Breaks Encapsulation
 *    - Can access private fields, methods, and constructors.
 *    - Violates OOP principles and intended class boundaries.
 *
 * 2. Performance Overhead
 *    - Reflective calls are slower than normal method/field access.
 *    - Extra security and type checks add runtime cost.
 *
 * 3. Loss of Compile-time Safety
 *    - Errors appear only at runtime (e.g., NoSuchMethodException).
 *    - Refactoring (renaming methods/fields) can silently break code.
 *
 * 4. Security Risks
 *    - Can expose or modify sensitive private data.
 *    - Java modules (Java 9+) restrict deep reflection for this reason.
 *
 * 5. Code Fragility
 *    - Depends on string names for fields/methods.
 *    - Breaks easily if the class structure changes.
 *
 * 6. Harder to Read & Maintain
 *    - Reflection code is verbose and less intuitive.
 *    - Debugging is more complex compared to normal code.
 *
 * 7. Restricted by Module System
 *    - Since Java 9, reflective access may need --add-opens.
 *    - Makes reflective code less portable across environments.
 *
 * 8. Can Break Design Patterns
 *    - Example: Singleton can be broken by instantiating new objects
 *      via private constructors.
 */

public class ReflectionSamples {
    public static void main(String[] args) throws Exception {
        // 1) Load class
        Class<?> birdClass = Class.forName("reflection.Bird");

        // 2) Instantiate via private no-arg constructor
        Constructor<?> ctor = birdClass.getDeclaredConstructor();
        ctor.setAccessible(true);
        Object bird = ctor.newInstance();

        // 3) Set ALL declared fields with simple defaults
        for (Field f : birdClass.getDeclaredFields()) {
            f.setAccessible(true);
            Class<?> t = f.getType();

            if (t == int.class) f.setInt(bird, 99);
            else if (t == boolean.class) f.setBoolean(bird, true);
            else if (t == String.class) f.set(bird, "reflected-name");
            else f.set(bird, null); // fallback for objects
        }

        // 4) Set a specific private field by name
        Field nameField = birdClass.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(bird, "sparrow");

        // 5) Call a private method
        Method setCanFly = birdClass.getDeclaredMethod("setCanFly");
        setCanFly.setAccessible(true);
        setCanFly.invoke(bird);

        // 6) Read back values (including private ones) and print
        for (Field f : birdClass.getDeclaredFields()) {
            f.setAccessible(true);
            System.out.println(f.getName() + " = " + f.get(bird));
        }
    }
}
