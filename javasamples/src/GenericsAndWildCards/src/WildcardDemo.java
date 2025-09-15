import java.util.List;

class Animal {
    public void speak() {
        System.out.println("Animal speaks");
    }

    @Override
    public String toString() {
        return "Animal";
    }
}

// Subclass of Animal
class Dog extends Animal {
    public void speak() {
        System.out.println("Dog barks");
    }

    @Override
    public String toString() {
        return "Dog";
    }
}

// Subclass of Dog
class Poodle extends Dog {
    public void speak() {
        System.out.println("Poodle yaps");
    }

    @Override
    public String toString() {
        return "Poodle";
    }
}

public class WildcardDemo {

    /**
     * Unbounded wildcard: List of unknown type
     * Can read values as Object but cannot write anything (except null)
     */
    public static void printList(List<?> list) {
        System.out.println("Printing list with unbounded wildcard:");
        for (Object obj : list) {
            System.out.println(obj);
        }
        // list.add("something"); //  compile error: can't add
        System.out.println();
    }

    /**
     * Upper bounded wildcard: Accepts List of Animal or any subclass
     * Good for reading values that are at least of type Animal
     */
    public static void makeAnimalsSpeak(List<? extends Animal> animals) {
        System.out.println("Animals speaking (upper bounded wildcard):");
        for (Animal animal : animals) {
            animal.speak(); // Safe to call because all are Animal or subtype
        }
        // animals.add(new Dog()); //  compile error: can't safely add
        System.out.println();
    }

    /**
     * Lower bounded wildcard: Accepts List of Dog or any superclass
     * Good for adding values of Dog or its subclasses
     */
    public static void addDogsToList(List<? super Dog> list) {
        System.out.println("Adding dogs to list (lower bounded wildcard):");

        list.add(new Dog());   //  allowed
        list.add(new Poodle()); //  allowed

        // Can't safely read as Dog; only Object is guaranteed
        for (Object obj : list) {
            System.out.println(obj);
        }

        System.out.println();
    }
}