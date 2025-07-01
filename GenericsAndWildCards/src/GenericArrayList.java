// A custom generic ArrayList-like class that stores elements of any type T
public class GenericArrayList<T> {
    // Internal array to store elements of type T
    private T[] list;

    // Counter to keep track of number of elements added
    private int count;

    // Constructor initializes the array and count
    @SuppressWarnings("unchecked") // Suppress warning about generic array creation
    GenericArrayList() {
        // Java does not allow direct creation of generic arrays (new T[20])
        // So we create an Object array and cast it to T[]
        list = (T[]) new Object[20];
        count = 0;
    }

    // Adds a value of type T to the list
    void addValue(T t) {
        // Add the element and increment the count
        list[count++] = t;
    }

    // Prints all the elements that have been added
    void printObjects() {
        for (int i = 0; i < count; i++) {
            System.out.println(list[i]);
        }
    }
}

