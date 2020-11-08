package race;

public class Car implements Runnable {


    private static int CARS_COUNT;
    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            Main.preparationCars.await();// все участники готовяться, как только все будут готовы они говорят что готовы
            System.out.println(this.name + " готов");
            Main.cdlCarsStart.countDown();//начало соревнования
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        if (Main.winnerLock.tryLock()) {
            //какой поток первый успел того и тапочки, т.е. первым пришел к финишу
            System.out.println(this.name + " победил!!!");
        }

        Main.cdlCarsFinish.countDown();
    }
}
