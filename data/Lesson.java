package data;

import java.util.ArrayList;

/**
 * Created by zemtsovaanna on 10.03.17.
 */
public class Lesson {
    private  ArrayList<Teacher> teachers;
    private  ArrayList<Subject> subjects;
    private  ArrayList <Group> groups;
    private boolean halfPair;
    private int amountLessonAtWeek;
    private int intensity; // 2 - если пара раз в 2 недели, 1 - если один раз в неделю
    private int codeAuditory;
    private  boolean isSet;
    private boolean isNeedMultimedia;

    public Lesson(){
        teachers = new ArrayList<>();
        subjects = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public int getCodeAuditory() {
        return codeAuditory;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public void setCodeAuditory(int codeAuditory) {
        this.codeAuditory = codeAuditory;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public boolean getHalfPair() {
        return halfPair;
    }

    public void setHalfPair(boolean halfPair) {
        this.halfPair = halfPair;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public void setTeachers(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public int getAmountLessonAtWeek() {
        return amountLessonAtWeek;
    }

    public void setAmountLessonAtWeek(int amountLessonAtWeek) {
        this.amountLessonAtWeek = amountLessonAtWeek;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public boolean isNeedMultimedia() {
        return isNeedMultimedia;
    }

    public void setNeedMultimedia(boolean needMultimedia) {
        isNeedMultimedia = needMultimedia;
    }
}

