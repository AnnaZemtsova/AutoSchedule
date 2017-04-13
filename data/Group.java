package data;

import java.util.ArrayList;

/**
 * Created by zemtsovaanna on 18.03.17.
 */
public class Group {
    private String name;
    private int amountOfStudents;
    private ArrayList<Time> times;


    public Group(){
        times = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public int getAmountOfStudents() {
        return amountOfStudents;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmountOfStudents(int amountOfStudents) {
        this.amountOfStudents = amountOfStudents;
    }

    public void addTime(Time time){
        times.add(time);
    }

    public ArrayList<Time> getTimes(){
        return times;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (amountOfStudents != group.amountOfStudents) return false;
        if (name != null ? !name.equals(group.name) : group.name != null) return false;
        return times != null ? times.equals(group.times) : group.times == null;

    }
}
