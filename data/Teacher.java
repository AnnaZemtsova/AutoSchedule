package data;


import java.util.ArrayList;

/**
 * Created by zemtsovaanna on 18.03.17.
 */
public class Teacher {
    private  String  name;
    private ArrayList<Time> times;

    public Teacher(){
        times = new ArrayList<>();
    }
    public String getName(){
        return  name;
    }
    public ArrayList<Time> getTimes(){
        return times;
    }

    public void setName(String name){
        this.name = name;
    }
    public  void setTimes(ArrayList<Time> times){
        this.times = times;
    }

    public void addTime(Time time){
        times.add(time);
    }

}