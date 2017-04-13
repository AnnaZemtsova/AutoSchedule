package data;

import java.util.ArrayList;

/**
 * Created by zemtsovaanna on 18.03.17.
 */
public class Auditory {
    private int type;
    private int capacity;
    private int capacityForComputer;
    private String name;
    private ArrayList<Time> times;
    private boolean isMultimedia;

    public  Auditory(){
        times = new ArrayList<>();
    }

    public int getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Time> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Time> times) {
        this.times = times;
    }

    public void addTime(Time time){
        times.add(time);
    }

    public int getCapacityForComputer() {
        return capacityForComputer;
    }

    public boolean isMultimedia() {
        return isMultimedia;
    }

    public void setMultimedia(boolean multimedia) {
        isMultimedia = multimedia;
    }

    public void setCapacityForComputer(int capacityForComputer) {
        this.capacityForComputer = capacityForComputer;
    }
}
