/*** Limitations for Volatile Keyword
1. No atomicity for compound actions:
    If you do something that’s more than a single read or write, volatile won’t make it atomic.
    Ex: Count++;

 2. Doesn’t protect invariants across multiple variables:
    If your state spans several variables that must change together, volatile won’t help keep them consistent.
     volatile int width;
     volatile int height;
     // Must always maintain: width * height < 1000
     Two threads updating width and height separately could break the invariant because there’s no atomic “update both” with volatile.

 3. No mutual exclusion:
    volatile does not stop two threads from writing the same variable at the same time — it only ensures that changes are visible.
     volatile boolean flag = false;
     Two threads can set this to different values at the same time — no lockout.

 4.Only works for variables, not whole objects:
    If you make an object reference volatile, the reference visibility is guaranteed, but not automatic safety for the object’s internal fields (unless they’re final and set in the constructor).

 5. Performance trade-offs:
     Volatile reads/writes are slower than normal because they bypass thread-local CPU caches and add memory barriers.

     On very hot paths, this can be significant

 6. When NOT to use volatile:
     You need atomic read-modify-write (++, +=, check-then-act)
     You’re guarding multiple fields that must be updated together
     You need mutual exclusion (ensuring only one thread runs a block of code at a time)
     You’re doing non-atomic composite data structure updates
 **/
public class HappensBefore {
    // BROKEN version: comment the next line and uncomment the one after it to see the difference
//     private  int x;              // <-- NO happens-before (may hang)
    private volatile int x;        // <-- YES happens-before via volatile

    void usingOfVolatileKeyWord() {
        x = 1;                     // writer sets the flag
        System.out.println("Writer set x = " + x);
    }

    void updatedXValue() {
        // Wait until writer sets x = 1
        while (x != 1) {
            // If the variable is not volatile then it will be infinite loop waiting for x to be 1
            // Even though the writer sets it to 1 the value is updated in it's thread cache
            // Reader thread won't be able to see this..
//            System.out.println("Reader saw x = " + x);
        }
        System.out.println("Reader saw x = " + x);
    }

    public static void main(String[] args) throws Exception {
        HappensBefore hb = new HappensBefore();

        Thread tWriter = new Thread(hb::usingOfVolatileKeyWord, "t-writer");

        Thread tReader = new Thread(hb::updatedXValue, "t-reader");

        tWriter.start();
        tReader.start();

        tWriter.join();
        tReader.join();
    }
}
