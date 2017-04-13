package allowableSchedule;

import data.Auditory;
import data.Group;
import data.Lesson;
import data.Time;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by zemtsovaanna on 12.04.17.
 */
public class ScheduleForEveryGroup {
    /*
    Кол-во занятий. Реально число занятий должно не слишком отличаться от этой цифры,
    но превышать не может ни в коем случае!!
     */
    final int AMOUNTOFLESSON = 20;

    private ArrayList<Schedule> schedules;



    public ScheduleForEveryGroup(){
        schedules = new ArrayList<>();
    }

    public void setSchedules(ArrayList<Schedule> schedule){
        this.schedules = schedule;
    }


    public ArrayList<Schedule> getSchedules(){
        return  schedules;
    }

    //------------------------------------------------------------------------------------

    private ArrayList<Lesson> getVoidLessonsArray(){
        ArrayList<Lesson> result = new ArrayList<>();
        for(int i=0;i<AMOUNTOFLESSON;i++){
            result.add(new Lesson());
        }
        return result;
    }
    //------------------------------------------------------------------------------------

    private ArrayList<Time> getVoidTimesArray(){
        ArrayList<Time> result = new ArrayList<>();
        for(int i=0;i<AMOUNTOFLESSON;i++){
            result.add(new Time());
        }
        return result;
    }
    //------------------------------------------------------------------------------------

    private ArrayList<Schedule> fullVoidSchedules(ArrayList<Schedule> schedules, int size,
                                                  ArrayList<Auditory> auditories){
        for(int i=0;i<size;i++){
            schedules.add(new Schedule());
            schedules.get(i).setAuditories(auditories);
            schedules.get(i).setLessons(getVoidLessonsArray());
            schedules.get(i).setFinishAuditories(new int[AMOUNTOFLESSON][2]);
            schedules.get(i).setTimes(getVoidTimesArray());
        }
        return schedules;
    }

    //------------------------------------------------------------------------------------
    /*
      Разбиваем на расписание для каждой группы.
      Проходимся по массиву расписания для каждой группы. Если группа участвует в занятии
      в новый массив расписания для группы пишем этот предмет
      Если для группы прошлись по всем зянятиям -> формируем расписание для следующей группы
     */

    public ArrayList<Schedule> getSchedulesForGroups(ArrayList<Group> groups,Schedule schedule){
        ArrayList<Lesson> lessons = schedule.getLessons();
        ArrayList<Schedule> schedules  = new ArrayList<>();
        schedules = fullVoidSchedules(schedules,groups.size(),schedule.getAuditories());
        int numberOfGroup =0;
        for(Group group: groups){
            int index=0;
            for(int i=0;i<lessons.size();i++){
                ArrayList<Group> groupsForCurrentLesson = lessons.get(i).getGroups();
                for(Group groupForCurrentLesson: groupsForCurrentLesson) {
                    Schedule currentSchedule = schedules.get(numberOfGroup);
                    if (Objects.equals(groupForCurrentLesson.getName(), group.getName())){
                        currentSchedule.setLesson(index,lessons.get(i));
                        currentSchedule.setFinishAuditory(index,schedule.getFinishAuditories()[i]);
                        currentSchedule.setTime(index,schedule.getTimes().get(i));
                        index++;
                        break;
                    }
                }
            }
            numberOfGroup++;
        }
        this.schedules = schedules;
        return schedules;
    }

    //--------------------------------------------------------------------------------

    private Schedule sortScheduleForDays(Schedule schedule) {
         //пробелмы в методе! обнуляет объект!!
        ArrayList<Time> times = schedule.getTimes();
        ArrayList<Lesson> lessons = schedule.getLessons();
        int[][] finishedAuditories = schedule.getFinishAuditories();
        for(int i=0;i<times.size();i++){
           if(lessons.get(i).getGroups().size() == 0) break;
            int currentLesson = times.get(i).getDay();
            for(int j=i;j<times.size();j++) {
                if(lessons.get(j).getGroups().size() != 0) {
                    if (times.get(j).getDay() < currentLesson) {

                        Time tmp = times.get(i);
                        times.set(i, times.get(j));
                        times.set(j, tmp);

                        Lesson tmpL = lessons.get(i);
                        lessons.set(i, lessons.get(j));
                        lessons.set(j, tmpL);

                        int[] tmpA = finishedAuditories[i];
                        finishedAuditories[i] = finishedAuditories[j];
                        finishedAuditories[j] = tmpA;
                    }
                }
            }
        }
        return schedule;
    }


    //--------------------------------------------------------------------------------
    /*
      Сортирует расписание по дням недели и парам.
      Результат - все дни и пары в каждом расписании упорядочены от понедельника до пятницы,
      от первой до пятой
     */

    private ArrayList<Schedule> sortAllSchedulesForDaysAndLessons(ArrayList<Schedule> schedules){
       /* for(int i=0;i<schedules.size();i++){
            schedules.get(i).printSchedule(schedules.get(i));
            System.out.println("||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println();
        }*/

        System.out.println(Schedule.count);
        ArrayList<Schedule> schedules1 = new ArrayList<>();
        for (Schedule currentScheduleForEveryGroup : schedules) {
            Schedule schedule = sortScheduleForDays(currentScheduleForEveryGroup);
            schedules1.add(schedule);
        }
        return schedules1;
    }



    //------------------------------------------------------------------------------------


    public static void main(String [] args){
        ScheduleForEveryGroup scheduleForEveryGroup= new ScheduleForEveryGroup();
        Schedule schedule = new Schedule();
        ArrayList<Group> groups = scheduleForEveryGroup.getGroups();
        ArrayList<Schedule> schedules1 = scheduleForEveryGroup.getSchedulesForGroups(groups,schedule.getSchedule());
        ArrayList<Schedule> schedules =scheduleForEveryGroup.sortAllSchedulesForDaysAndLessons(schedules1);
        for(int i=0;i<schedules.size();i++){
            schedules.get(i).printSchedule(schedules.get(i));
            System.out.println("||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println();
        }
        System.out.println(Schedule.count);

    }

//------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------
    /*
      Дополнтиельный метод. Удалить после тестирования!
     */

    public ArrayList<Group> getGroups(){
        ArrayList<Group> groups = new ArrayList<>();
        ObjectsForLesson ob = new ObjectsForLesson();
        Group ks11 = ob.getGroup("KS11", 30);
        Group ks12 = ob.getGroup("KS12", 21);
        Group ks13 = ob.getGroup("KS13", 24);
        Group ku11 = ob.getGroup("KU11", 16);
        Group kb11 = ob.getGroup("KB11", 27);
        Group kb12 = ob.getGroup("KB12",21);
        Group ki11 = ob.getGroup("KI11",16);

        Group ks21 = ob.getGroup("KS21", 15);
        Group ks22 = ob.getGroup("KS22", 19);
        Group ku21 = ob.getGroup("KU21", 23);
        Group kb21 = ob.getGroup("KB21", 26);
        Group kb22 = ob.getGroup("KB22", 21);

        Group ks31 = ob.getGroup("KS31", 25);
        Group ks32 = ob.getGroup("KS32", 19);
        Group ku31 = ob.getGroup("KU31", 20);
        Group kb31 = ob.getGroup("KB31", 26);

        Group ks41 = ob.getGroup("KS41", 25);
        Group ks42 = ob.getGroup("KS42", 29);
        Group ku41 = ob.getGroup("KU41", 13);
        Group kb41 = ob.getGroup("KB41", 16);

        Group ks51 = ob.getGroup("KS51", 15);
        Group ks52 = ob.getGroup("KS52", 12);
        Group ku51 = ob.getGroup("KU51", 16);
        Group ku52 = ob.getGroup("KU52", 14);
        Group kb51 = ob.getGroup("KB51",18);
        Group kya51 = ob.getGroup("KYA51", 10);
        Group kb52 = ob.getGroup("KB52", 15);

        groups.add(kb11);
        groups.add(kb12);
        groups.add(ki11);
        groups.add(ks11);
        groups.add(ks12);
        groups.add(ks13);
        groups.add(ku11);

        groups.add(kb21);
        groups.add(kb22);
        groups.add(ks21);
        groups.add(ks22);
        groups.add(ku21);

        groups.add(kb31);
        groups.add(ks31);
        groups.add(ks32);
        groups.add(ku31);

        groups.add(kb41);
        groups.add(ks41);
        groups.add(ks42);
        groups.add(ku41);

        groups.add(kb51);
        groups.add(kb52);
        groups.add(ks51);
        groups.add(ks52);
        groups.add(ku51);
        groups.add(ku52);
        groups.add(kya51);
        return groups;
    }
}


