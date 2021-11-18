import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        for (int i = 1; i <= 15; i++) {
            Hilos hilo = new Hilos(false);
            hilo.setName("Jugador " + i);
            hilo.start();
        }

    }
}

class Hilos extends Thread {
    boolean bonus;

    Hilos(boolean bonus){
        this.bonus = bonus;
    }
    @Override
    public void run() {
        try {
            Batalla.addParticipante(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Batalla{

    private static final int NUM_ACCESO_SIMULTANEOS = 10;
    static AtomicInteger cont = new AtomicInteger(0);
    static AtomicBoolean primero = new AtomicBoolean(false);
    static Semaphore semaphore = new Semaphore(NUM_ACCESO_SIMULTANEOS,true);

    public static void addParticipante(Hilos hilo) throws InterruptedException {
        Random r = new Random();
        try {
                semaphore.acquire();
                Thread.sleep(r.nextInt(5000) + 1000);
                if (!primero.compareAndExchange(false,true))
                {
                    hilo.bonus=true;
                }
            if (cont.get()<5) {
                System.out.println(hilo.getName()+" - "+hilo.bonus+"\n");
                cont.getAndIncrement();
            }
            System.out.println("5 Últimos\n");
            if (cont.get()<10 && cont.get()>=5) {
                System.out.println(hilo.getName()+" - "+hilo.bonus+"\n");
                cont.getAndIncrement();
            }
            if (cont.get()==10) {
                System.out.println("Dejo pasar 5\n");
                semaphore.release(5);
                cont.getAndIncrement();
                System.out.println(hilo.getName()+" - "+hilo.bonus+"\n");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}