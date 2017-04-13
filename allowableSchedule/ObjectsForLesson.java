package allowableSchedule;
import data.*;

import java.util.ArrayList;
/**
 * Created by zemtsovaanna on 04.04.17.
 */
public class ObjectsForLesson {

    //------------------------------------------------------------------------


    public Group getGroup(String name, int amount) {
        Group group = new Group();
        group.setName(name);
        group.setAmountOfStudents(amount);
        return group;
    }


    //------------------------------------------------------------------------

    public Teacher getTeacher(String name, ArrayList<Time> timesTmp) {
        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setTimes(timesTmp);
        return teacher;
    }

    //------------------------------------------------------------------------

    public Subject getSubject(ArrayList<Teacher> teacher, String nameSubject) {
        Subject subject = new Subject();
        ArrayList<Teacher> teachers;
        teachers = teacher;
        subject.setName(nameSubject);
        subject.setTeachers(teachers);
        return subject;
    }

    //------------------------------------------------------------------------

    public Auditory getAuditory(int type, int capacity, String name, ArrayList<Time> times) {
        Auditory auditory = new Auditory();
        auditory.setCapacity(capacity);
        auditory.setName(name);
        auditory.setType(type);
        auditory.setTimes(times);
        return auditory;
    }


    //------------------------------------------------------------------------

    public Lesson getLesson(ArrayList<Group> groups,
                            ArrayList<Subject> subjects, ArrayList<Teacher> teachers,
                            boolean halfPair, int amountLessonsAtWeek, int intensity, int codeAuditory,
                            boolean isNeedMultimedia) {

        Lesson lesson = new Lesson();
        lesson.setGroups(groups);
        lesson.setSubjects(subjects);
        lesson.setTeachers(teachers);
        lesson.setHalfPair(halfPair);
        lesson.setIntensity(intensity);
        lesson.setAmountLessonAtWeek(amountLessonsAtWeek);
        lesson.setCodeAuditory(codeAuditory);
        lesson.setNeedMultimedia(isNeedMultimedia);
        return lesson;
    }


    //------------------------------------------------------------------------

    public Lesson getLesson(ArrayList<Group> groups, Subject subject, Teacher teacher,
                            int halfPai, int amountLessonsAtWeek, int intensity, int codeAuditory,
                            boolean isNeedMultimedia){
        boolean halfPair = false;
        if(halfPai==2) halfPair = false;
        if(halfPai==1) halfPair = true;
        Lesson lesson = new Lesson();
        lesson.setGroups(groups);

        ArrayList<Subject> subjects = new ArrayList<>();
        subjects.add(subject);
        ArrayList<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);

        lesson.setSubjects(subjects);
        lesson.setTeachers(teachers);
        lesson.setHalfPair(halfPair);
        lesson.setIntensity(intensity);
        lesson.setAmountLessonAtWeek(amountLessonsAtWeek);
        lesson.setCodeAuditory(codeAuditory);
        lesson.setNeedMultimedia(isNeedMultimedia);
        return lesson;
    }
    //------------------------------------------------------------------------

    //------------------------------------------------------------------------

    /*intensity = 1 -> 1 раз в неделю
      intensity = 2 -> 1 раз в 2 недели
      amountLessonTwoWeek -> кол-во пар в 2 недели
      halfPair -> у всей ли группы пара (1 - вся группа, 1/2 - пол группы)
      codeAuditory -> 0 - practise with nothing ,1 - lection,
       2 - practise with computer, 3 - for phisics, 4 - with ostsilograf
     */
    public String toString(Lesson lesson) {

        String result = "";
        for (int i = 0; i < lesson.getGroups().size(); i++) {
            result += lesson.getGroups().get(i).getName() + " - " + lesson.getGroups().get(i).getAmountOfStudents() + "\n";
        }
        for (int i = 0; i < lesson.getTeachers().size(); i++) {
            result += lesson.getTeachers().get(i).getName() + "\n";
            result += lesson.getSubjects().get(i).getName() + "\n";
        }
        result += "halfPair - " + lesson.getHalfPair() + "\n";
        result += "amountLessonAtWeek - " + lesson.getAmountLessonAtWeek() + "\n";
        result += "intensity - " + lesson.getIntensity() + "\n";
        result += "codeAuditory - " + lesson.getCodeAuditory() + "\n";
        result += "________________________________\n";
        return result;
    }

}
