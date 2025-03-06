package creation;

public class CreationByExtendingThread extends Thread{
    public void run(){
        try{
            System.out.println(Thread.currentThread().getName());
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
