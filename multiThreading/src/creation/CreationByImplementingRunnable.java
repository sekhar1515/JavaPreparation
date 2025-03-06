package creation;

public class CreationByImplementingRunnable implements Runnable{

    @Override
    public void run() {
        try{
            System.out.println("Implementing Runnable");
            System.out.println("thread 1 : " + Thread.currentThread().getName());
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
