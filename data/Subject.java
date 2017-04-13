package data;

/**
 * Created by zemtsovaanna on 11.04.17.
 */
import java.util.ArrayList;

public class Subject {
    private String name;
    private ArrayList<Teacher> teachers;

    public Subject(){
        teachers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeachers(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
    }
}
