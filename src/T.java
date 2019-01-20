import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class T {
    Object object = new Object();
    volatile  List list = new ArrayList();

    private void add(Object o){
        list.add(o);
    }
    private int size(){
        return list.size();
    }
    public static void main(String[] args){
        T t = new T();
        final Object lock = new Object();
        new Thread(()->{
            synchronized (lock){
                System.out.println("t2启动");

                if (t.size()!=5){
                    try {
                        System.out.println("t2等待");
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("t2结束");
                lock.notify();
            }

        },"t2").start();
        new Thread(()->{
            synchronized (lock){
                System.out.println("t1启动");
                for (int i=0;i<10;i++){
                    t.add(new Object());
                    System.out.println("T添加"+(i+1));
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (t.size()==5){
                        lock.notify();
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println("t1结束");
            }

        },"t1").start();

    }
}
