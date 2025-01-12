import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
* This class represents the Controller part in the MVC pattern.
* It's responsibilities is to listen to the View and responds in a appropriate manner by
* modifying the model state and the updating the view.
 */

public class CarController {
    // member fields:

    // The delay (ms) corresponds to 20 updates a sec (hz)
    private final int delay = 50;
    // The timer is started with an listener (see below) that executes the statements
    // each step between delays.
    private Timer timer = new Timer(delay, new TimerListener());

    // The frame that represents this instance View of the MVC pattern
    CarView frame;
    // A list of cars, modify if needed
    ArrayList<Car> cars = new ArrayList<>();

    //methods:

    public static void main(String[] args) {
        // Instance of this class
        CarController cc = new CarController();

        cc.cars.add(new Volvo240());
        cc.cars.add(new Saab95(0, 100));
        cc.cars.add(new Scania(0, 200));

        // Start a new view and send a reference of self
        cc.frame = new CarView("CarSim 1.0", cc);

        // Start the timer
        cc.timer.start();
    }

    /* Each step the TimerListener moves all the cars in the list and tells the
    * view to update its images. Change this method to your needs.
    * */
    private class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            frame.drawPanel.repaint();
            for (Car car : cars) {
                car.move();
                int x = (int) Math.round(car.getX());
                int y = (int) Math.round(car.getY());
                //TODO: Make so it doesn't overwrite the image
                frame.drawPanel.updatePosAndImg(x, y, car.getClass().toString());
                // repaint() calls the paintComponent method of the panel
                if(collidingWithWall(car.getDirection(), x, y)){
                    turnAroundProcedure(car);
                }
            }

        }

    }

    private boolean collidingWithWall(double rotation, double x, double y){
        if(rotation < 90 || rotation > 270){
            if(x + DrawPanel.CAR_WIDTH > frame.drawPanel.getWidth()){
                return true;
            }
        }
        if(rotation < 180){
            if(y < 0){
                return true;
            }
        }
        if(rotation > 90 && rotation < 270){
            if(x < 0){
                return true;
            }
        }
        if(rotation > 180){
            if(y + DrawPanel.CAR_HEIGHT > frame.drawPanel.getHeight()){
                return true;
            }
        }

        return false;
    }

    private void turnAroundProcedure(Car car){
        car.stopEngine();

        for (int i = 0; i <4 ; i++) {
            car.turnLeft();
        }
        car.startEngine();
    }

    // Calls the gas method for each car once
    void gas(int amount) {
        double gas = ((double) amount) / 100;
        for (Car car : cars) {
            car.gas(gas);
        }
    }

    void brake(int amount){
        double brake = amount / 100.0;
        for(Car car : cars){
            car.brake(brake);
        }
    }

    void turboOn(){
        for(Car car : cars){
            if(car instanceof ITurbo){
                ((ITurbo) car).setTurboOn();
            }
        }
    }

    void turboOff(){
        for(Car car : cars){
            if(car instanceof ITurbo){
                ((ITurbo) car).setTurboOff();
            }
        }
    }

    void liftBeds(){
        for(Car car : cars){
            if(car instanceof Flatbed){
                ((Flatbed) car).raiseBed();
            }
        }
    }

    void lowerBeds(){
        for (Car car : cars){
            if(car instanceof Flatbed){
                ((Flatbed) car).lowerBed();
            }
        }
    }

    void startCars(){
        for(Car car : cars){
            car.startEngine();
        }
    }

    void stopCars(){
        for(Car car : cars){
            car.stopEngine();
        }
    }

}
