package quality;

import allowableSchedule.Schedule;
import allowableSchedule.ScheduleForEveryGroup;
import data.Group;
import data.Time;

import java.util.ArrayList;

/**
 * Created by zemtsovaanna on 11.04.17.
 */

public class ScheduleQuality {

    private ArrayList<Schedule> scheduleForEveryGroup;
    private double quality;

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public double getQuality() {
        return quality;
    }

    public ArrayList<Schedule> getScheduleForEveryGroup(){
        return scheduleForEveryGroup;
    }

    public void setScheduleForEveryGroup(ArrayList<Schedule> scheduleForEveryGroup){
        this.scheduleForEveryGroup = scheduleForEveryGroup;
    }


    //--------------------------------------------------------------------------------
    /*
    Получаем расписание для всех групп
     */
    public ArrayList<Schedule> getSchedules(){
        ScheduleForEveryGroup scheduleForEveryGroup= new ScheduleForEveryGroup();
        Schedule schedule = new Schedule();
        ArrayList<Group> groups = scheduleForEveryGroup.getGroups();
        ArrayList<Schedule> schedules = scheduleForEveryGroup.getSchedulesForGroups(groups,schedule.getSchedule());
        this.scheduleForEveryGroup = schedules;
        return schedules;
    }

    //--------------------------------------------------------------------------------

    /*
     Возвращает количество первых пар
     если lessonNumber=1 возвращает кол-во первых пар, если 5 - пятых
     */
    private int getAmountFirstOrFifthLessons(int lessonNumber){
        int amountFirstOrFifthLessons=0;
        for (Schedule currentScheduleForEveryGroup : scheduleForEveryGroup) {
            ArrayList<Time> times = currentScheduleForEveryGroup.getTimes();
            for (int l = 0; l < times.size(); l++) {
                if(times.get(l).getLesson()==lessonNumber) amountFirstOrFifthLessons++;
            }
        }
        return amountFirstOrFifthLessons;
    }


    //--------------------------------------------------------------------------------

    /*
      Возвращает номер пары , минимальной для дня day
     */

    private int findMinLesson(ArrayList<Time> times,int day){
        int minLesson=10;
        for(int i=0;i<times.size();i++){
            if(times.get(i).getDay()==day) {
                int currentLesson = times.get(i).getLesson();
                if (currentLesson < minLesson) {
                    minLesson = currentLesson;
                }
            }
        }
        return minLesson;
    }
    //--------------------------------------------------------------------------------

    private void sortScheduleForDaysAndLessons(Schedule schedule){
        ArrayList<Time> times = schedule.getTimes();
        for(int i=1;i<6;i++){
            for(int j=0;j<times.size();j++) {
                int lessonMin = times.get(j).getLesson();
                for (int t = j; t < times.size(); t++) {
                    if (times.get(t).getDay() == i) {
                        if (times.get(t).getLesson() < lessonMin) {
                            Time tmp = times.get(t);
                            times.set(t,times.get(j));
                            times.set(j,tmp);
                        }
                    }
                }
            }
        }
    }


    //--------------------------------------------------------------------------------
    /*
      Сортирует расписание по дням недели и парам.
      Результат - все дни и пары в каждом расписании упорядочены от понедельника до пятницы,
      от первой до пятой
     */

    private void sortAllSchedulesForDaysAndLessons(){
        for (Schedule currentScheduleForEveryGroup : scheduleForEveryGroup) {
            sortScheduleForDaysAndLessons(currentScheduleForEveryGroup);
        }
    }

    //--------------------------------------------------------------------------------

    /*
     Возвращает количество окон у всех групп
     */
    private int getAmountGaps(){
        int amountGaps=0;
        for (Schedule currentScheduleForEveryGroup : scheduleForEveryGroup) {
            ArrayList<Time> times = currentScheduleForEveryGroup.getTimes();
            for(int i=0;i<5;i++){
                int fixedDay = i + 1;
                int startLesson=1;
                for(Time time:times){
                    if(time.getDay()==fixedDay) {
                        if (time.getLesson() == startLesson + 1) {
                                  startLesson++;
                        }
                    }
                }
            }
        }
        return amountGaps;
    }

}
