package race;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static final int CARS_COUNT = 4 ;

    //для наглядности все разместил в мэйне, но можно раскидать по классам
    //например это закинуть в Car
    public static final CountDownLatch cdlCarsStart = new CountDownLatch(CARS_COUNT);//для определения что все участники готовы стратовать
    public static final CountDownLatch cdlCarsFinish = new CountDownLatch(CARS_COUNT);//для определения что все участники финишировали
    public static final CyclicBarrier preparationCars = new CyclicBarrier( CARS_COUNT );//для того что бы показать что участники готовы
    public static final Lock winnerLock = new ReentrantLock();//для определения победителя, unlock не использую так как достаточно только один раз определить победителя
    //это в туннель
    public static final Semaphore tunnelCapacity = new Semaphore( 2 );//используется только в тунеле


    public static void main (String[] args) {
        System.out.println( "ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!" );
        Race race = new Race(  new Road( 60 ),new Tunnel(), new Road( 40 ));
        Car[] cars = new Car[CARS_COUNT];
        for ( int i = 0 ; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + ( int ) (Math.random() * 10 ));
        }
        for ( int i = 0 ; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        try {
            cdlCarsStart.await();
            System.out.println( "ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!" );
            cdlCarsFinish.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println( "ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!" );
    }
}
