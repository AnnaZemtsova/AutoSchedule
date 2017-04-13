package additional;

import data.*;

import java.util.ArrayList;

/**
 * Created by zemtsovaanna on 22.03.17.
 */
public class BackAlgorithm {
    ArrayList<Time> times;
    int[][] finishAuditories;
    ArrayList<Lesson> lessons;
    int[] triedAuditories;
    ArrayList<Auditory> auditories;
    int count=0;
    int amount=0;
    int f=0;


    //------------------------------------------------------------------------


    public BackAlgorithm(){
        times = new ArrayList<>();

    }

    public void setAuditories(ArrayList<Auditory> auditories) {
        this.auditories = auditories;
    }

    public ArrayList<Auditory> getAuditories() {
        return auditories;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }
//------------------------------------------------------------------------

    public boolean isAvailable(int needType, int typeAuditory, int amountOfStudents, int capacity,
                               int capacityForComputer) {
        if (((needType == typeAuditory) && (amountOfStudents <= capacity) && (capacity - amountOfStudents < 150)) ||
                ((needType == 1) && (typeAuditory == 0) && (amountOfStudents <= capacity)) ||
                ((needType == 0) && (typeAuditory == 1) && (capacity <= 90))||
                (needType==2)&&(typeAuditory==2)&&(amountOfStudents<=(capacity+capacityForComputer))||
                (needType==0)&&(typeAuditory==2)&&(amountOfStudents<=capacity))
            return true;
        return false;
    }
    //------------------------------------------------------------------------

    /*
       Поиск времени для мигалки. (Поиск времени, в которое есть мигалка у опредленной группы/групп
     */

    public Object searchFlasher(int j, Object obj) {
        boolean flag;

        /*
          Проверяем нет ли пары-мигалки, в которой однозначно соответствуют все группы
         */

        for (int i = 0; i < times.size(); i++) {
            flag = true;
            if (times.get(i).getDay() == 0) break;
            if (i != j) {
                if (lessons.get(i).getIntensity() == 2) {
                    for (int k = 0; k < lessons.get(i).getGroups().size(); k++) {
                        if (lessons.get(i).getGroups().size() == lessons.get(j).getGroups().size()) {
                            if (lessons.get(i).getGroups().get(k) != lessons.get(j).getGroups().get(k)) {
                                flag = false;
                                break;
                            }
                        }
                    }
                    if (flag) {
                        if(obj instanceof Time) {
                            obj = times.get(i);
                            return obj;
                        }
                        if(obj instanceof int[]){
                            obj = finishAuditories[i];
                            return obj;
                        }
                    }
                }
            }
        }
        /*
         Если нет пары-мигалки, в которой участвуют все группы , ищем пару-мигалку,
         в которой участвуют все группы (группы искомого занятия являются подмножеством множества групп
         даного занятия)
         */
        if(obj instanceof Time) {
            obj = new Time();
        }
        if(obj instanceof int[]){
            int [] a= new int[2];
            a[0]=-1;
            a[1]=-1;
            obj = a;
        }

        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getDay() == 0) {
                return obj;
            }
            if (i != j) {
                if (lessons.get(i).getIntensity() == 2) {
                    if (lessons.get(j).getGroups().size() > lessons.get(i).getGroups().size()) {
                        for (int k = 0; k < lessons.get(j).getGroups().size(); k++) {
                            flag = false;
                            for (int b = 0; b < lessons.get(i).getGroups().size(); b++) {
                                if (lessons.get(j).getGroups().get(k) == lessons.get(i).getGroups().get(b)) {
                                    flag = true;
                                }
                            }
                            if (!flag) continue;
                        }
                        if(obj instanceof Time) {
                            obj = times.get(i);
                            return obj;
                        }
                        if(obj instanceof int[]){
                            obj = finishAuditories[i];
                            return obj;
                        }
                    }
                }
            }
        }
        return obj;
    }


    //------------------------------------------------------------------------


    public Group getGroup(String name, int amount) {
        Group group = new Group();
        group.setName(name);
        group.setAmountOfStudents(amount);
        return group;
    }

    public ArrayList<Time> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Time> times) {
        this.times = times;
    }

    //------------------------------------------------------------------------

   /* public ArrayList<Group> getGroups(String [] names,int[] amount){
        ArrayList<Group> groups = new ArrayList<>();
        for(int i=0;i<names.length;i++){
            Group group  = new Group();
            group.setName(names[i]);
            group.setAmountOfStudents(amount[i]);
            groups.add(group);
        }
        return  groups;
    }*/

    public int[][] getFinishAuditories(){
        return  finishAuditories;
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

    public Auditory getAuditory(int type, int capacity, String name, ArrayList<Time> times,
                                boolean isMultimedia) {
        Auditory auditory = new Auditory();
        auditory.setCapacity(capacity);
        auditory.setName(name);
        auditory.setType(type);
        auditory.setTimes(times);
        auditory.setMultimedia(isMultimedia);
        return auditory;
    }

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

    //------------------------------------------------------------------------

    public String allLessonsToString() {
        String result = "";
        for (int i = 0; i < lessons.size(); i++) {
            result += toString(lessons.get(i));
        }
        return result;
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

    public int getAmountOfStudents(Lesson lesson) {
        int result = 0;
        for (int i = 0; i < lesson.getGroups().size(); i++) {
            result += lesson.getGroups().get(i).getAmountOfStudents();
        }
        return result;
    }

    //------------------------------------------------------------------------


    public boolean wasTried(int j, int[] tried) {
        for (int i = 0; i < tried.length; i++) {
            if (j == tried[i]) return true;
        }
        return false;
    }


    //------------------------------------------------------------------------


    public int getA(int[] triedTime,int max,int min) {
        int a = min + (int) (Math.random() * max);
        while (wasTried(a, triedTime)) {
            a = min + (int) (Math.random() * max);
            if (triedTime.length >= max)
                break;
        }
        return a;
    }

    //------------------------------------------------------------------------

    /*
      Возвращает true если  уже пытались поставить пару  в каждую из допустимых аудиторий,
      false - если есть аудитории, которы еще не были проверены
     */

    public boolean isAllAuditoriesWasTried(){
        boolean  isPresent = false;
        for(int i=0;i<auditories.size();i++){
            isPresent = false;
            for(int j=0;j<triedAuditories.length;j++){
                if(triedAuditories[j]==i){
                    isPresent = true;
                }
            }
            if(!isPresent) return false;
        }
        return isPresent;
    }


    //------------------------------------------------------------------------

    private int getJ() {
        int j = (int) (Math.random() * ((auditories.size() - 1) + 1));
        while (wasTried(j, triedAuditories)) {
            if (isAllAuditoriesWasTried()
                    &&!wasTried(j,triedAuditories)){
                break;
            }
        }
        return j;
    }



    //------------------------------------------------------------------------

     /*
       isFreeTeacher - проверка отсутствия накладок для преподавателей
     */

    public boolean isFreeTeacher(Teacher teacher, int[] time) {
        for (int i = 0; i < teacher.getTimes().size(); i++) {
            if ((teacher.getTimes().get(i).getDay() == time[0]) &&
                    (teacher.getTimes().get(i).getLesson() == time[1])) {
                return false;
            }
        }
        return true;
    }


    //------------------------------------------------------------------------
    /*
          isFreeGroup - проверка отсутствия накладок для группы
     */

    public boolean isFreeGroup(Group group, int[] time) {
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getLesson() == 0) break;
            for (int j = 0; j < lessons.get(i).getGroups().size(); j++) {
                if ((lessons.get(i).getGroups().get(j).getName().equals(group.getName()) && (time[0] == times.get(i).getDay())
                        && (time[1] == times.get(i).getLesson()))) {
                    return false;
                }
            }
        }
        return true;
    }

    //------------------------------------------------------------------------
     /*
          isFreeGroups - проверка отсутствия накладок для групп
     */

    public boolean isFreeGroups(ArrayList<Group> groups, int[] time) {
        for (int i = 0; i < groups.size(); i++) {
            if (!isFreeGroup(groups.get(i), time)) {
                return false;
            }
        }
        return true;
    }

    //------------------------------------------------------------------------
     /*
          isFreeGroups - проверка отсутствия накладок для преподавателей
     */

    public boolean isFreeTeachers(ArrayList<Teacher> teachers, int[] time) {
        for (int i = 0; i < teachers.size(); i++) {
            if (!isFreeTeacher(teachers.get(i), time)) {
                return false;
            }
        }
        return true;
    }
//------------------------------------------------------------------------
    /*
       isFreeAuditory - проверка отсутствия накладок для аудиторий
     */

    public boolean isFreeAuditory(int auditory, int[]time) {
        for (int i = 0; i < auditories.get(auditory).getTimes().size(); i++) {
            if ((auditories.get(auditory).getTimes().get(i).getDay() == time[0]) &&
                    (auditories.get(auditory).getTimes().get(i).getLesson() == time[1])) {
                return false;
            }
        }
        return true;
    }

    //------------------------------------------------------------------------
     /*
          isFreeGroups - проверка отсутствия накладок для аудиторий
     */

    public boolean isFreeAuditories(int[] auditories, int[] time) {
        for (int i = 0; i < 2; i++) {
            if(auditories[i]>=0)
                if (!isFreeAuditory(auditories[i], time)) {
                    return false;
                }
        }
        return true;
    }

    //------------------------------------------------------------------------

    //------------------------------------------------------------------------

    /*
       Проверяем если преподаватели во время time имеют занятия - мигалку, причем одно, то считаем,
       что преподаватели свободны для установления занятия - мигалки
     */

    private boolean isFreeTeachersForFlashes(ArrayList<Teacher> teachers, int[] time) {
        for (int i = 0; i < teachers.size(); i++) {
            if (!isFreeTeacherForFlashes(teachers.get(i), time)) return false;
        }
        return true;
    }

    //------------------------------------------------------------------------

    /*
       Проверяем если преподаватель во время time имеет занятие - мигалку, причем одно, то считаем,
       что преподаватель свободен для установления занятия - мигалки
     */
    //true - если найдена 1 мигалка, false - в другом случае

    /*  private boolean isFreeTeacherForFlashes(Teacher teacher, int[] time) {
          return  true;
      }*/
    private boolean isFreeTeacherForFlashes(Teacher teacher, int[] time) {
        int count = 0;
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getLesson() == time[1] && times.get(i).getDay() == time[0]) {
                for (int j = 0; j < lessons.get(i).getTeachers().size(); j++) {
                    if (lessons.get(i).getTeachers().get(j) == teacher) {
                        if (lessons.get(i).getIntensity() == 2) {
                            count++;
                            if (count > 1) return false;
                        }
                    }
                }
            }
        }
        if (count == 1) {
            return true;
        }
        return false;
    }

    //------------------------------------------------------------------------

    /*
       Проверяем если аудитории во время time имеют занятие - мигалкe, причем одно, то считаем,
       что аудитория свободна для установления занятия - мигалки
     */

    private boolean isFreeAuditoriesForFlashes(int[] auditories, int[] time) {
        for (int i = 0; i < auditories.length; i++) {
            if (!isFreeAuditoryForFlashes(auditories[i], time)) return false;
        }
        return true;
    }

    //------------------------------------------------------------------------

    /*
       Проверяем если аудитория во время time имеет занятие - мигалку, причем одно, то считаем,
       что аудитория свободна для установления занятия - мигалки
     */

    private boolean isFreeAuditoryForFlashes(int a, int[] time) {
        int count = 0;
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getLesson() == time[0] && times.get(i).getDay() == time[0]) {
                for (int j = 0; j < 2; j++) {
                    if (finishAuditories[i][j] == a) {
                        if (lessons.get(i).getIntensity() == 2) {
                            count++;
                            if (count > 1) return false;
                        }
                    }
                }
            }
        }
        if (count == 1) return true;
        return false;
    }


    //------------------------------------------------------------------------

    /*
       Проверяем если группы во время time имеют занятия - мигалку, причем одно, то считаем,
       что группы свободны для установления занятия - мигалки
     */

    private boolean isFreeGroupsForFlashes(ArrayList<Group> groups, int[] time) {
        for (int i = 0; i < groups.size(); i++) {
            if (!isFreeGroupForFlashes(groups.get(i), time)) return false;
        }
        return true;
    }

    //------------------------------------------------------------------------

    /*
       Проверяем если группа во время time имеет занятие - мигалку, причем одно, то считаем,
       что группа свободен для установления занятия - мигалки
     */

    private boolean isFreeGroupForFlashes(Group group, int[] time) {
        int count = 0;
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getLesson() == time[1] && times.get(i).getDay() == time[0]) {
                for (int j = 0; j < lessons.get(i).getGroups().size(); j++) {
                    if (lessons.get(i).getGroups().get(j) == group) {
                        if ((lessons.get(i).getIntensity() == 2)&&!isFreeGroup(group,time)) {
                            count++;
                            if (count > 1) return false;
                        }
                    }
                }
            }
        }
        if (count == 1) return true;
        return false;
    }

    //------------------------------------------------------------------------

    /*
      Проверяет свобдны ли преподаватели, группы и аудитории для данного времени
     */

    public boolean isFree(int[] time, int i,int [] a) {
        ArrayList<Teacher> teachers = lessons.get(i).getTeachers();
        ArrayList<Group> groups = lessons.get(i).getGroups();
        /*
          Если это занятие мигалка - смотрим, нет ли установленной мигалки на даное время.
               Если есть, и группа в это время свободна, ставим туда пару
               Если нет, как обычно ставим занятие
          Иначе как обычно выбираем занятие
         */
        if (lessons.get(i).getIntensity() == 2) {
            //если это занятие мигалка
            if (((isFreeAuditories(a, time)) &&
                    (isFreeGroups(groups, time)) &&
                    (isFreeTeachers(teachers, time))) ||      // или делаем проверку как обычно
                    //или проверяем вдруг в это время есть мигалка
                    ((isFreeTeachersForFlashes(teachers, time) || isFreeTeachers(teachers, time)) && // преподаватели или свободны для мигалки или свободны в принципе
                            (isFreeAuditoriesForFlashes(a, time) || isFreeAuditories(a, time)) &&// аудитории или свободны для мигалки или свободны в принципе
                            (isFreeGroupsForFlashes(groups, time) || isFreeGroups(groups, time)))) // группы или свободны для мигалки или свободны в принципе
                return true;
        } else                                               //иначе как обычно проверяем свободны ли
            if ((isFreeAuditories(a, time)) &&          //преподаватели, аудитории и группы в даное время
                    (isFreeGroups(groups, time)) &&
                    (isFreeTeachers(teachers, time)))
                return true;
        return false;
    }

    //------------------------------------------------------------------------
    public boolean notSetYet(int i){
        if(lessons.get(i).isSet()) return  false;
        return true;
    }
    //------------------------------------------------------------------------

    /*
      очень странный метод! Возможно работает неправильно
     */

    public int chooseAuditoryForHalfPair(int a,int codeAuditory,int amountOfStudents,int[]time){
        for(int i=0;i<auditories.size();i++){
            if(i!=a) {
                if (isAvailable(codeAuditory, auditories.get(i).getType(), amountOfStudents,
                        auditories.get(i).getCapacity(), auditories.get(i).getCapacityForComputer())&&
                        (isFreeAuditory(i, time) || isFreeAuditoryForFlashes(i, time))) {

                    return i;
                }
            }
        }
        // System.out.println("ERROR");
        return -1;
    }
    //------------------------------------------------------------------------

    /*
      на даное время в даной аудитории подбираем занятие, которое бы лучше всего подходило сюда

     */

    public void chooseLesson(int[] time,int [] a){
        for(int i=0;i<lessons.size();i++){
            boolean setFlashes=false;
            if(notSetYet(i)&&lessons.get(i).getIntensity()==2){
                //f++;
                int[] aud = new int[2];
                aud = (int[]) searchFlasher(i, aud);
                boolean isSetAuditory=false;
                for (int h = 0; h < 2; h++){
                    if (aud[h] >= 0){
                        f++;
                        if (isAvailable(lessons.get(i).getCodeAuditory(), auditories.get(aud[h]).getType(),
                                getAmountOfStudents(lessons.get(i)), auditories.get(aud[h]).getCapacity(),
                                auditories.get(aud[h]).getCapacityForComputer())) {
                            //  amount++;
                            finishAuditories[i][h] = aud[h];
                            isSetAuditory = true;
                        }
                    }
                    //возможно нужен  else если aud[h]<0 так как если не подошла одна аудитория,
                    //нужно искать обе другие

                }
                if(isSetAuditory){
                    //  System.out.print(i+" .");
                    Time time1 = new Time();
                    time1 = (Time)searchFlasher(i,time1);
                    int [] tmp = new int[2];
                    tmp[0]=time1.getDay();
                    tmp[1]=time1.getLesson();
                    if(isFree(tmp,i,aud)){
                        times.set(i,time1);
                        lessons.get(i).setSet(true);
                        lessons.get(i).getTeachers().get(0).addTime(time1);
                        auditories.get(aud[0]).addTime(time1);
                        setFlashes=true;
                        //   amount++;
                    }
                }
            }
            /*
           -------------------------------------------------------------------------------------------
             */
            if(!setFlashes&&
                    notSetYet(i)&&
                    isFree(time,i,a)&&isAvailable(lessons.get(i).getCodeAuditory(),auditories.get(a[0]).getType(),
                    getAmountOfStudents(lessons.get(i)),auditories.get(a[0]).getCapacity(),
                    auditories.get(a[0]).getCapacityForComputer())){
                count++;
                Time time1 = new Time();
                time1.setDay(time[0]);
                time1.setLesson(time[1]);
                times.set(i,time1);
                lessons.get(i).getTeachers().get(0).addTime(time1);
                auditories.get(a[0]).addTime(time1);
                finishAuditories[i][0]=a[0];
                lessons.get(i).setSet(true);
                if(lessons.get(i).getHalfPair()) {
                    a[1]= chooseAuditoryForHalfPair(a[0],lessons.get(i).getCodeAuditory(),
                            getAmountOfStudents(lessons.get(i)),time);
                    lessons.get(i).getTeachers().get(1).addTime(time1);
                    if(a[1]>=0) {
                        auditories.get(a[1]).addTime(time1);
                        finishAuditories[i][1]=a[1];
                    }else{
                        finishAuditories[i][0] = -1;
                        lessons.get(i).getTeachers().get(0).getTimes().
                                remove(lessons.get(i).getTeachers().get(0).getTimes().size()-1);
                        lessons.get(i).getTeachers().get(1).getTimes().
                                remove(lessons.get(i).getTeachers().get(1).getTimes().size()-1);
                        auditories.get(a[0]).getTimes().remove(auditories.get(a[0]).getTimes().size()-1);
                        lessons.get(i).setSet(false);
                    }
                }
            }
        }
    }


    //------------------------------------------------------------------------
    /*
      Выбираем любую аудиторию, свободную во время time
     */

    private void chooseAuditory(int[] time){
        for(int a=0;a<auditories.size();a++){
            triedAuditories[a]=-1;
        }
        for(int i=0;i<auditories.size();i++) {
            int tA = getJ();
            triedAuditories[i]= tA;
            int[] a = new int[2];
            a[0] = tA;
            a[1]=-1;
            chooseLesson(time, a);
        }
    }

    //------------------------------------------------------------------------

    public  int howMuchAllLessonsAlingment(){
        int amount =0;
        for(int i=0;i<lessons.size();i++){
            if(lessons.get(i).isSet()) {
                amount++;
            }
        }
        return  amount;
    }

    //------------------------------------------------------------------------

    /*
     Пробуем посавить удобное для нас время (2 , 3 пары любого дня)
     */

    private void chooseTime(){
        int[] triedDays = new int[5];
        int[] resTime = new int[2];
        for(int i=0;i<5;i++){
            int d = getA(triedDays,5,1);
            int []triedLessons = new int[2];
            for(int j=0;j<2;j++){
                int l = getA(triedLessons,2,2);
                resTime[0]=d;
                resTime[1]=l;
                chooseAuditory(resTime);
            }
        }
        if(howMuchAllLessonsAlingment()<lessons.size()) {
            triedDays = new int[5];
            resTime = new int[2];
            for (int i = 0; i < 5; i++) {
                int d = getA(triedDays, 5, 1);
                resTime[0] = d;
                resTime[1] = 4;
                chooseAuditory(resTime);

            }
        }
        if(howMuchAllLessonsAlingment()<lessons.size()) {
            triedDays = new int[5];
            resTime = new int[2];
            for (int i = 0; i < 5; i++) {
                int d = getA(triedDays, 5, 1);
                resTime[0] = d;
                resTime[1] = 1;
                chooseAuditory(resTime);
            }
        }

        if(howMuchAllLessonsAlingment()<lessons.size()) {
            triedDays = new int[5];
            resTime = new int[2];
            for(int i=0;i<5;i++){
                int d = getA(triedDays,5,1);
                resTime[0]=d;
                resTime[1]=5;
                chooseAuditory(resTime);
            }
        }
    }


    //------------------------------------------------------------------------


    public void chooseCorrectLesson(){
        finishAuditories = new int[lessons.size()][2];
        triedAuditories = new int[auditories.size()];
        times = new ArrayList<>();
        for (int l = 0; l < lessons.size(); l++) {
            finishAuditories[l][0]=-1;
            finishAuditories[l][1]=-1;
            times.add(l,new Time());
        }
        for(int i=0;i<auditories.size();i++){
            chooseTime();
        }
    }
    //------------------------------------------------------------------------

    //------------------------------------------------------------------------

    public double getDegreeOfFreedom(Lesson lesson) { // ]
        double result = 0;
        if (getAmountOfStudents(lesson) > 30) result += 1;
        if (getAmountOfStudents(lesson) > 60) result += 1;
        if (getAmountOfStudents(lesson) > 110) result += 1;
        if (getAmountOfStudents(lesson) > 150) result += 1;
        if (lesson.getCodeAuditory() == 1) result += 1;
        if (lesson.getCodeAuditory() == 2) result += 2;
        for (int i = 0; i < lesson.getTeachers().size(); i++) {
            result += lesson.getTeachers().get(i).getTimes().size();
        }
        if(lesson.getIntensity()==2) result+=20;
        return result;
    }
    /*
       Сортирует массив по степеням свободы. Те которые расставить сложнее всего окажутся в начале массива
       (лекции, занятия требующие компьютерных аудторий и тд)
     */

    public void sortForFreedom() {
        for (int i = 0; i < lessons.size(); i++) {
            int min = i;
            for (int j = i + 1; j < lessons.size(); j++) {
                if (getDegreeOfFreedom(lessons.get(j)) > getDegreeOfFreedom(lessons.get(min))) {
                    Lesson tmp = lessons.get(min);
                    lessons.set(min, lessons.get(j));
                    lessons.set(j, tmp);
                }
            }
        }
    }

    public static  void main(String [] args){
        int count=0;
        int count1 = 0;
        int count2=0;
        ArrayList<Lesson> lessons;
        BackAlgorithm backAlgorithm = new BackAlgorithm();
        backAlgorithm.fullAll();
        backAlgorithm.sortForFreedom();
        //lessons = backAlgorithm.lessons;

        backAlgorithm.chooseCorrectLesson();
        System.out.println(count+" -5");
        System.out.println(count2+" -1");
        System.out.println(count1+" -0");
        System.out.println(backAlgorithm.count);
        System.out.println(backAlgorithm.amount);
        System.out.println(backAlgorithm.f);
    }



    public void testFullLesson() {
        auditories = new ArrayList<>();
        lessons = new ArrayList<>();
        //---------------------------------------------------------
//busyness time<
        //1 week monday<
        Time time111 = new Time();
        // time111.setWeek(1);
        time111.setDay(1);
        time111.setLesson(1);

        Time time112 = new Time();
        //time112.setWeek(1);
        time112.setDay(1);
        time112.setLesson(2);

        Time time113 = new Time();
        // time113.setWeek(1);
        time113.setDay(1);
        time113.setLesson(3);

        Time time114 = new Time();
        //time114.setWeek(1);
        time114.setDay(1);
        time114.setLesson(4);

        Time time115 = new Time();
        // time115.setWeek(1);
        time115.setDay(1);
        time115.setLesson(5);
        //>

        //1 week tuesday<
        Time time121 = new Time();
        //time121.setWeek(1);
        time121.setDay(2);
        time121.setLesson(1);

        Time time122 = new Time();
        //time122.setWeek(1);
        time122.setDay(2);
        time122.setLesson(2);

        Time time123 = new Time();
        //time123.setWeek(1);
        time123.setDay(2);
        time123.setLesson(3);

        Time time124 = new Time();
        // time124.setWeek(1);
        time124.setDay(2);
        time124.setLesson(4);

        Time time125 = new Time();
        //time125.setWeek(1);
        time125.setDay(2);
        time125.setLesson(5);
        //>

        //1 week wednesday<
        Time time131 = new Time();
        // time131.setWeek(1);
        time131.setDay(3);
        time131.setLesson(1);

        Time time132 = new Time();
        //time132.setWeek(1);
        time132.setDay(3);
        time132.setLesson(2);

        Time time133 = new Time();
        //time133.setWeek(1);
        time133.setDay(3);
        time133.setLesson(3);

        Time time134 = new Time();
        // time134.setWeek(1);
        time134.setDay(3);
        time134.setLesson(4);

        Time time135 = new Time();
        // time135.setWeek(1);
        time135.setDay(3);
        time135.setLesson(5);
        //>

        //1 week thursday<
        Time time141 = new Time();
        //time141.setWeek(1);
        time141.setDay(4);
        time141.setLesson(1);

        Time time142 = new Time();
        // time142.setWeek(1);
        time142.setDay(4);
        time142.setLesson(2);

        Time time143 = new Time();
        // time143.setWeek(1);
        time143.setDay(4);
        time143.setLesson(3);

        Time time144 = new Time();
        //time144.setWeek(1);
        time144.setDay(4);
        time144.setLesson(4);

        Time time145 = new Time();
        //time145.setWeek(1);
        time145.setDay(4);
        time145.setLesson(5);
        //>

        //1 week friday<
        Time time151 = new Time();
        //time151.setWeek(1);
        time151.setDay(5);
        time151.setLesson(1);

        Time time152 = new Time();
        //time152.setWeek(1);
        time152.setDay(5);
        time152.setLesson(2);

        Time time153 = new Time();
        //time153.setWeek(1);
        time153.setDay(5);
        time153.setLesson(3);

        Time time154 = new Time();
        //time154.setWeek(1);
        time154.setDay(5);
        time154.setLesson(4);

        Time time155 = new Time();
        // time155.setWeek(1);
        time155.setDay(5);
        time155.setLesson(5);
        //>
//>

//busyness for teachers<
        //makarov<
        ArrayList<Time> timesTmp = new ArrayList<>();
        timesTmp.add(time115);

        timesTmp.add(time125);

        timesTmp.add(time135);

        timesTmp.add(time141);
        timesTmp.add(time142);

        timesTmp.add(time155);
        //>

        //litvinov<
        ArrayList<Time> timesTmp1 = new ArrayList<>();
        //>

        //zinoviev<
        ArrayList<Time> timesTmp2 = new ArrayList<>();
        //>

        //karas<
        ArrayList<Time> timesTmp3 = new ArrayList<>();
        //>

        //dolya<
        ArrayList<Time> timesTmp4 = new ArrayList<>();
        timesTmp4.add(time113);
        timesTmp4.add(time114);
        timesTmp4.add(time115);

        timesTmp4.add(time123);
        timesTmp4.add(time124);
        timesTmp4.add(time125);

        timesTmp4.add(time133);
        timesTmp4.add(time134);
        timesTmp4.add(time135);

        timesTmp4.add(time143);
        timesTmp4.add(time144);
        timesTmp4.add(time145);

        timesTmp4.add(time153);
        timesTmp4.add(time154);
        timesTmp4.add(time155);
        //>

        //litvinova<
        ArrayList<Time> timesTmp5 = new ArrayList<>();
        //>

        //kuklin<
        ArrayList<Time> timesTmp6 = new ArrayList<>();
        timesTmp6.add(time111);
        timesTmp6.add(time112);
        timesTmp6.add(time113);
        timesTmp6.add(time114);
        timesTmp6.add(time115);

        timesTmp6.add(time121);
        timesTmp6.add(time122);
        timesTmp6.add(time123);
        timesTmp6.add(time124);
        timesTmp6.add(time125);

        timesTmp6.add(time131);
        timesTmp6.add(time132);
        timesTmp6.add(time133);
        timesTmp6.add(time134);
        timesTmp6.add(time135);

        timesTmp6.add(time141);
        timesTmp6.add(time142);
        timesTmp6.add(time143);
        timesTmp6.add(time144);
        timesTmp6.add(time145);
        //>

        //vasileva<
        ArrayList<Time> timesTmp7 = new ArrayList<>();
        //>

        //podtsikin<
        ArrayList<Time> timesTmp8 = new ArrayList<>();
        //>

        //yanovsky<
        ArrayList<Time> timesTmp9 = new ArrayList<>();
        timesTmp9.add(time111);
        timesTmp9.add(time112);
        timesTmp9.add(time113);

        timesTmp9.add(time131);
        timesTmp9.add(time132);
        timesTmp9.add(time133);

        timesTmp9.add(time141);
        timesTmp9.add(time142);
        timesTmp9.add(time143);
        //>

        //likova<
        ArrayList<Time> timesTmp10 = new ArrayList<>();
        timesTmp10.add(time111);
        timesTmp10.add(time112);
        timesTmp10.add(time113);
        timesTmp10.add(time114);
        timesTmp10.add(time115);

        timesTmp10.add(time121);
        timesTmp10.add(time122);
        timesTmp10.add(time123);
        timesTmp10.add(time124);
        timesTmp10.add(time125);

        timesTmp10.add(time131);

        timesTmp10.add(time141);
        timesTmp10.add(time142);
        timesTmp10.add(time143);
        timesTmp10.add(time144);
        timesTmp10.add(time145);

        timesTmp10.add(time151);
        timesTmp10.add(time154);
        timesTmp10.add(time155);
        //>

        //chervinko<
        ArrayList<Time> timesTmp11 = new ArrayList<>();
        //>

        //turenko<
        ArrayList<Time> timesTmp12 = new ArrayList<>();
        timesTmp12.add(time111);

        timesTmp12.add(time121);

        timesTmp12.add(time131);

        timesTmp12.add(time141);

        timesTmp12.add(time151);
        //>

        //osipchuk<
        ArrayList<Time> timesTmp13 = new ArrayList<>();
        //>

        //artyuh<
        ArrayList<Time> timesTmp14 = new ArrayList<>();
        //>

        //zamula<
        ArrayList<Time> timesTmp15 = new ArrayList<>();
        timesTmp15.add(time111);
        timesTmp15.add(time112);
        timesTmp15.add(time113);
        timesTmp15.add(time114);
        timesTmp15.add(time115);

        timesTmp15.add(time131);
        timesTmp15.add(time134);
        timesTmp15.add(time135);

        timesTmp15.add(time141);
        timesTmp15.add(time144);
        timesTmp15.add(time145);

        timesTmp15.add(time151);
        timesTmp15.add(time152);
        timesTmp15.add(time153);
        timesTmp15.add(time154);
        timesTmp15.add(time155);
        //>

        //berdnikov<
        ArrayList<Time> timesTmp16 = new ArrayList<>();
        timesTmp16.add(time111);
        timesTmp16.add(time115);

        timesTmp16.add(time123);
        timesTmp16.add(time124);
        timesTmp16.add(time125);

        timesTmp16.add(time133);
        timesTmp16.add(time134);
        timesTmp16.add(time135);

        timesTmp16.add(time143);
        timesTmp16.add(time144);
        timesTmp16.add(time145);

        timesTmp16.add(time151);
        timesTmp16.add(time152);
        timesTmp16.add(time153);
        timesTmp16.add(time154);
        timesTmp16.add(time155);
        //>

        //kabalyants<
        ArrayList<Time> timesTmp17 = new ArrayList<>();
        timesTmp17.add(time123);
        timesTmp17.add(time124);
        timesTmp17.add(time125);

        timesTmp17.add(time141);
        timesTmp17.add(time142);
        timesTmp17.add(time145);

        timesTmp17.add(time151);
        timesTmp17.add(time152);
        timesTmp17.add(time153);
        timesTmp17.add(time154);
        timesTmp17.add(time155);
        //>

        //rudichev<
        ArrayList<Time> timesTmp18 = new ArrayList<>();
        //>

        //pavlov<
        ArrayList<Time> timesTmp19 = new ArrayList<>();
        //>

        //gorbenko<
        ArrayList<Time> timesTmp20 = new ArrayList<>();
        //>

        //lazurikV<
        ArrayList<Time> timesTmp21 = new ArrayList<>();
        //>

        //losev<
        ArrayList<Time> timesTmp22 = new ArrayList<>();
        timesTmp22.add(time111);

        timesTmp22.add(time121);

        timesTmp22.add(time131);

        timesTmp22.add(time141);

        timesTmp22.add(time151);
        //>

        //bulavin<
        ArrayList<Time> timesTmp23 = new ArrayList<>();
        //>

        //krasnobaev<
        ArrayList<Time> timesTmp24 = new ArrayList<>();
        timesTmp24.add(time111);
        timesTmp24.add(time112);
        timesTmp24.add(time113);
        timesTmp24.add(time114);
        timesTmp24.add(time115);

        timesTmp24.add(time121);
        timesTmp24.add(time122);
        timesTmp24.add(time125);

        timesTmp24.add(time131);
        timesTmp24.add(time133);
        timesTmp24.add(time134);
        timesTmp24.add(time135);

        timesTmp24.add(time141);
        timesTmp24.add(time143);
        timesTmp24.add(time144);
        timesTmp24.add(time145);

        timesTmp24.add(time151);
        timesTmp24.add(time152);
        timesTmp24.add(time154);
        timesTmp24.add(time155);
        //>

        //vladimirova<
        ArrayList<Time> timesTmp25 = new ArrayList<>();
        timesTmp25.add(time111);

        timesTmp25.add(time121);
        timesTmp25.add(time124);
        timesTmp25.add(time125);

        timesTmp25.add(time131);

        timesTmp25.add(time141);

        timesTmp25.add(time151);
        //>

        //shmatkov<
        ArrayList<Time> timesTmp26 = new ArrayList<>();
        timesTmp26.add(time111);

        timesTmp26.add(time121);

        timesTmp26.add(time131);

        timesTmp26.add(time141);

        timesTmp26.add(time151);
        //>

        //mishchenko<
        ArrayList<Time> timesTmp27 = new ArrayList<>();
        //>

        //tolstoluzskaya<
        ArrayList<Time> timesTmp28 = new ArrayList<>();
        //>

        //kolovanova<
        ArrayList<Time> timesTmp29 = new ArrayList<>();
        //>

        //kuznetsova<
        ArrayList<Time> timesTmp30 = new ArrayList<>();
        timesTmp30.add(time111);
        timesTmp30.add(time112);
        timesTmp30.add(time113);
        timesTmp30.add(time114);
        timesTmp30.add(time115);

        timesTmp30.add(time125);

        timesTmp30.add(time131);
        timesTmp30.add(time132);
        timesTmp30.add(time133);
        timesTmp30.add(time134);
        timesTmp30.add(time135);

        timesTmp30.add(time142);
        timesTmp30.add(time145);

        timesTmp30.add(time151);
        timesTmp30.add(time152);
        timesTmp30.add(time153);
        timesTmp30.add(time154);
        timesTmp30.add(time155);
        //>

        //kropotov<
        ArrayList<Time> timesTmp41_1 = new ArrayList<>();
        //>

        //sporov<
        ArrayList<Time> timesTmp31 = new ArrayList<>();
        timesTmp31.add(time131);
        timesTmp31.add(time132);
        timesTmp31.add(time133);
        timesTmp31.add(time134);
        timesTmp31.add(time135);
        //>

        //sharun<
        ArrayList<Time> timesTmp32 = new ArrayList<>();
        timesTmp32.add(time115);

        timesTmp32.add(time125);

        timesTmp32.add(time131);
        timesTmp32.add(time135);

        timesTmp32.add(time145);

        timesTmp32.add(time151);
        timesTmp32.add(time155);
        //>

        //didenko<
        ArrayList<Time> timesTmp33 = new ArrayList<>();
        //>

        //staroselska<
        ArrayList<Time> timesTmp35 = new ArrayList<>();
        //>

        //andreev<
        ArrayList<Time> timesTmp36 = new ArrayList<>();
        //>

        //stervoedov<
        ArrayList<Time> timesTmp37 = new ArrayList<>();
        //>

        //ralo<
        ArrayList<Time> timesTmp38 = new ArrayList<>();
        //>

        //kuznetsov<
        ArrayList<Time> timesTmp39 = new ArrayList<>();
        timesTmp39.add(time121);
        timesTmp39.add(time122);
        timesTmp39.add(time123);
        timesTmp39.add(time124);
        timesTmp39.add(time125);
        //>

        //tkachuk<
        ArrayList<Time> timesTmp40 = new ArrayList<>();
        timesTmp40.add(time111);
        timesTmp40.add(time112);
        timesTmp40.add(time114);
        timesTmp40.add(time115);

        timesTmp40.add(time123);
        //>

        //potiy<
        ArrayList<Time> timesTmp41 = new ArrayList<>();
        //>

        //lisitskaya<
        ArrayList<Time> timesTmp42 = new ArrayList<>();
        timesTmp42.add(time111);

        timesTmp42.add(time121);

        timesTmp42.add(time131);
        timesTmp42.add(time132);
        timesTmp42.add(time133);
        timesTmp42.add(time134);
        timesTmp42.add(time135);

        timesTmp42.add(time141);
        timesTmp42.add(time142);
        timesTmp42.add(time143);
        timesTmp42.add(time144);
        timesTmp42.add(time145);

        timesTmp42.add(time151);
        timesTmp42.add(time152);
        timesTmp42.add(time153);
        timesTmp42.add(time154);
        timesTmp42.add(time155);
        //>

        //hruslov<
        ArrayList<Time> timesTmp43 = new ArrayList<>();
        //>

        //narejniy<
        ArrayList<Time> timesTmp44 = new ArrayList<>();
        //>

        //dolgov<
        ArrayList<Time> timesTmp45 = new ArrayList<>();
        //>

        //svatovsky<
        ArrayList<Time> timesTmp46 = new ArrayList<>();
        //>

        //rassomahin<
        ArrayList<Time> timesTmp47 = new ArrayList<>();
        //>

        //esin<
        ArrayList<Time> timesTmp48 = new ArrayList<>();
        //>

        //reva<
        ArrayList<Time> timesTmp49 = new ArrayList<>();
        timesTmp49.add(time151);
        timesTmp49.add(time152);
        timesTmp49.add(time153);
        timesTmp49.add(time154);
        timesTmp49.add(time155);
        //>

        //gorban<
        ArrayList<Time> timesTmp50 = new ArrayList<>();
        timesTmp50.add(time111);

        timesTmp50.add(time121);

        timesTmp50.add(time131);

        timesTmp50.add(time141);

        timesTmp50.add(time151);
        //>

        //kompaniets<
        ArrayList<Time> timesTmp51 = new ArrayList<>();
        //>

        //politolog<
        ArrayList<Time> timesTmp52 = new ArrayList<>();
        //>

        //zolotuhina<
        ArrayList<Time> timesTmp53 = new ArrayList<>();
        //>

        //gromiko<
        ArrayList<Time> timesTmp54 = new ArrayList<>();
        //>

        //lazurikVT<
        ArrayList<Time> timesTmp55 = new ArrayList<>();
        //>

        //buts<
        ArrayList<Time> timesTmp56 = new ArrayList<>();
        timesTmp56.add(time111);
        timesTmp56.add(time112);
        timesTmp56.add(time113);
        timesTmp56.add(time114);
        timesTmp56.add(time115);

        timesTmp56.add(time131);
        timesTmp56.add(time132);
        timesTmp56.add(time133);
        timesTmp56.add(time134);
        timesTmp56.add(time135);

        timesTmp56.add(time151);
        timesTmp56.add(time152);
        timesTmp56.add(time153);
        timesTmp56.add(time154);
        timesTmp56.add(time155);
        //>

        //bogucharski<
        ArrayList<Time> timesTmp57 = new ArrayList<>();
        //>

        //kurinii<
        ArrayList<Time> timesTmp58 = new ArrayList<>();
        //>

        //starovoitov<
        ArrayList<Time> timesTmp59 = new ArrayList<>();
        //>

        //moroz<
        ArrayList<Time> timesTmp60 = new ArrayList<>();
        timesTmp60.add(time111);

        timesTmp60.add(time121);

        timesTmp60.add(time131);

        timesTmp60.add(time141);
        timesTmp60.add(time142);
        timesTmp60.add(time143);
        timesTmp60.add(time144);
        timesTmp60.add(time145);

        timesTmp60.add(time151);
        //>

        //denisenko<
        ArrayList<Time> timesTmp61 = new ArrayList<>();
        //>

        //oleshko<
        ArrayList<Time> timesTmp62 = new ArrayList<>();
        //>

        ArrayList<Time> timesTmp65 = new ArrayList<>();
//>

//busyness for auditories<
        //715<
        ArrayList<Time> timeTmp9 = new ArrayList<Time>();
        timeTmp9.add(time111); //*
        timeTmp9.add(time112);

        timeTmp9.add(time123); //*

        timeTmp9.add(time132); //*

        timeTmp9.add(time142);

        timeTmp9.add(time151); //*
        timeTmp9.add(time153);
        //>
//>


        Group ks11 = getGroup("KS11", 30);
        Group ks12 = getGroup("KS12", 21);
        Group ks13 = getGroup("KS13", 24);
        Group ku11 = getGroup("KU11", 16);
        Group kb11 = getGroup("KB11", 27);
        Group kb12 = getGroup("KB12",21);
        Group ki11 = getGroup("KI11",16);

        Group ks21 = getGroup("KS21", 15);
        Group ks22 = getGroup("KS22", 19);
        Group ku21 = getGroup("KU21", 23);
        Group kb21 = getGroup("KB21", 26);
        Group kb22 = getGroup("KB22", 21);

        Group ks31 = getGroup("KS31", 25);
        Group ks32 = getGroup("KS32", 19);
        Group ku31 = getGroup("KU31", 20);
        Group kb31 = getGroup("KB31", 26);

        Group ks41 = getGroup("KS41", 25);
        Group ks42 = getGroup("KS42", 29);
        Group ku41 = getGroup("KU41", 13);
        Group kb41 = getGroup("KB41", 16);

        Group ks51 = getGroup("KS51", 15);
        Group ks52 = getGroup("KS52", 12);
        Group ku51 = getGroup("KU51", 16);
        Group ku52 = getGroup("KU52", 14);
        Group kb51 = getGroup("KB51",18);
        Group kya51 = getGroup("KYA51", 10);
        Group kb52 = getGroup("KB52", 15);

        // teachers<
        Teacher makarov = getTeacher("Makarov", timesTmp);                  //*
        Teacher litvinov = getTeacher("Litvinov", timesTmp1);
        Teacher zinoviev = getTeacher("Zinoviev", timesTmp2);
        Teacher karas = getTeacher("Karas", timesTmp3);
        Teacher dolya = getTeacher("Dolya", timesTmp4);                     //*
        Teacher litvinova = getTeacher("Litvinova", timesTmp5);
        Teacher kuklin = getTeacher("Kuklin", timesTmp6);                   //*
        Teacher vasileva = getTeacher("Vasileva", timesTmp7);
        Teacher podtsikin = getTeacher("Podtsikin", timesTmp8);
        Teacher yanovsky = getTeacher("Yanovsky", timesTmp9);               //*
        Teacher likova = getTeacher("Likova", timesTmp10);                  //*
        Teacher chervinko = getTeacher("Chervinko", timesTmp11);
        Teacher turenko = getTeacher("Turenko", timesTmp12);                //*
        Teacher osipchuk = getTeacher("Osipchuk", timesTmp13);
        Teacher artyuh = getTeacher("Artyuh", timesTmp14);
        Teacher zamula = getTeacher("Zamula", timesTmp15);                  //*
        Teacher berdnikov = getTeacher("Berdnikov", timesTmp16);            //*
        Teacher kabalyants = getTeacher("Kabalyants", timesTmp17);          //*
        Teacher rudichev = getTeacher("Rudichev", timesTmp18);
        Teacher pavlov = getTeacher("Pavlov", timesTmp19);
        Teacher gorbenko = getTeacher("Gorbenko", timesTmp20);
        Teacher lazurikV = getTeacher("LazurikV", timesTmp21);
        Teacher losev = getTeacher("Losev", timesTmp22);                    //*
        Teacher bulavin = getTeacher("Bulavin", timesTmp23);
        Teacher krasnobaev = getTeacher("Krasnobaev", timesTmp24);          //*
        Teacher vladimirova = getTeacher("Vladimirova", timesTmp25);        //*
        Teacher shmatkov = getTeacher("Shmatkov", timesTmp26);              //*
        Teacher mishchenko = getTeacher("Mishchenko", timesTmp27);
        Teacher tolstoluzskaya = getTeacher("Tolstoluzskaya", timesTmp28);
        Teacher kolovanova = getTeacher("Kolovanova", timesTmp29);
        Teacher kuznetsova = getTeacher("Kuznetsova", timesTmp30);          //*
        Teacher kropotov = getTeacher("Kropotov", timesTmp41_1);
        Teacher sporov = getTeacher("Sporov",timesTmp31);                   //*
        Teacher sharun = getTeacher("Sharun",timesTmp32);                   //*
        Teacher didenko = getTeacher("Didenko",timesTmp33);
        Teacher staroselska = getTeacher("Staroselska",timesTmp35);
        Teacher andreev = getTeacher("Andreev",timesTmp36);
        Teacher stervoedov = getTeacher("Stervoedov",timesTmp37);
        Teacher ralo = getTeacher("Ralo",timesTmp38);
        Teacher kuznetsov = getTeacher("Kuznetsov",timesTmp39);             //*
        Teacher tkachuk = getTeacher("Tkachuk",timesTmp40);                 //*
        Teacher potiy = getTeacher("Potiy",timesTmp41);
        Teacher lisitskaya = getTeacher("Lisitskaya",timesTmp42);           //*
        Teacher hruslov = getTeacher("Hruslov",timesTmp43);
        Teacher narejniy = getTeacher("Narejniy",timesTmp44);
        Teacher dolgov = getTeacher("Dolgov",timesTmp45);
        Teacher svatovsky = getTeacher("Svatovsky",timesTmp46);
        Teacher rassomahin = getTeacher("Rassomahin",timesTmp47);
        Teacher esin = getTeacher("Esin", timesTmp48);
        Teacher reva = getTeacher("Reva", timesTmp49);                      //*
        Teacher gorban = getTeacher("Gorban", timesTmp50);                  //*
        Teacher kompaniets = getTeacher("Kompaniets", timesTmp51);
        Teacher politolog = getTeacher("Politolog", timesTmp52);
        Teacher zolotuhina = getTeacher("Zolotuhina", timesTmp53);
        Teacher gromiko = getTeacher("Gromiko", timesTmp54);
        Teacher lazurikVT = getTeacher("LazurikVT", timesTmp55);
        Teacher buts = getTeacher("Buts", timesTmp56);                      //*
        Teacher bogucharski = getTeacher("Bogucharski",timesTmp57);
        Teacher kurinii = getTeacher("Kurinii", timesTmp58);
        Teacher starovoitov = getTeacher("Starovoitov", timesTmp59);
        Teacher moroz = getTeacher("Moroz", timesTmp60);                    //*
        Teacher denisenko = getTeacher("Denisenko", timesTmp61);
        Teacher oleshko = getTeacher("Oleshko", timesTmp62);
        Teacher derid = getTeacher("Derid", timesTmp65);

        ArrayList<Teacher> teachers = new ArrayList<>();
        teachers.add(makarov);
        Subject math = getSubject(teachers, "Math");

        ArrayList<Teacher> teachers1 = new ArrayList<>();
        teachers1.add(litvinov);
        Subject oop = getSubject(teachers1, "OOP");

        ArrayList<Teacher> teachers2 = new ArrayList<>();
        teachers2.add(zinoviev);
        Subject ogti = getSubject(teachers2, "OGTI");

        ArrayList<Teacher> teachers3 = new ArrayList<>();
        teachers3.add(karas);
        Subject physic = getSubject(teachers3, "Physic");

        ArrayList<Teacher> teachers4 = new ArrayList<>();
        teachers4.add(osipchuk);
        Subject comp_graphicaOsip = getSubject(teachers4, "Comp.graphica");

        ArrayList<Teacher> teachers5 = new ArrayList<>();
        teachers5.add(likova);
        Subject discreateMath = getSubject(teachers5, "DiscreateMath");


        ArrayList<Teacher> teachers6 = new ArrayList<>();
        teachers6.add(litvinova);
        Subject algorithms = getSubject(teachers6, "Algorithms");


        ArrayList<Teacher> teachers7 = new ArrayList<>();
        teachers7.add(artyuh);
        Subject comp_seti = getSubject(teachers7, "Comp.seti");


        ArrayList<Teacher> teachers8 = new ArrayList<>();
        teachers8.add(zamula);
        Subject bjd = getSubject(teachers8, "BJD");

        ArrayList<Teacher> teachers9 = new ArrayList<>();
        teachers9.add(kuklin);
        Subject ai = getSubject(teachers9, "AI");


        teachers = new ArrayList<>();
        teachers.add(zinoviev);
        Subject plazma = getSubject(teachers, "Plazma");

        teachers = new ArrayList<>();
        teachers.add(vasileva);
        Subject metrologia = getSubject(teachers, "Metrologia");

        ArrayList<Teacher> teachers10 = new ArrayList<>();
        teachers10.add(yanovsky);
        Subject mozgechok = getSubject(teachers10, "Mozgechok");

        teachers = new ArrayList<>();
        teachers.add(berdnikov);
        Subject comp_seti_Berdnikov = getSubject(teachers, "Comp.seti");


        ArrayList<Teacher> teachers11 = new ArrayList<>();
        teachers11.add(chervinko);
        Subject anglCh = getSubject(teachers11, "Angl");


        ArrayList<Teacher> teachers12 = new ArrayList<>();
        teachers12.add(turenko);
        Subject anglTu = getSubject(teachers12, "Angl");

        ArrayList<Teacher> teachers13 = new ArrayList<>();
        teachers13.add(kabalyants);
        Subject mathKab = getSubject(teachers13, "Math");

        ArrayList<Teacher> teachers14 = new ArrayList<>();
        teachers14.add(rudichev);
        Subject ypp = getSubject(teachers14, "YPP");

        ArrayList<Teacher> teachers15 = new ArrayList<>();
        teachers15.add(pavlov);
        Subject tpi = getSubject(teachers15, "TPI");

        ArrayList<Teacher> teachers16 = new ArrayList<>();
        teachers16.add(lazurikV);
        Subject db  = getSubject(teachers16, "DB");

        ArrayList<Teacher> teachers17 = new ArrayList<>();
        teachers17.add(gorbenko);
        Subject kriptologia = getSubject(teachers17, "Kriptologia");

        ArrayList<Teacher> teachers18 = new ArrayList<>();
        teachers18.add(podtsikin);
        Subject model = getSubject(teachers18, "Model");

        ArrayList<Teacher> teachers19 = new ArrayList<>();
        teachers19.add(dolya);
        Subject optoinform = getSubject(teachers19, "Optoinform");

        ArrayList<Teacher> teachers20 = new ArrayList<>();
        teachers20.add(bulavin);
        Subject susks = getSubject(teachers20, "SUSKS");

        ArrayList<Teacher> teachers21 = new ArrayList<>();
        teachers21.add(krasnobaev);
        Subject smodtc = getSubject(teachers21, "SMODTC");

        ArrayList<Teacher> teachers22 = new ArrayList<>();
        teachers22.add(vladimirova);
        Subject upravlenieIT = getSubject(teachers22, "UpravlenieIT");


        ArrayList<Teacher> teachers23 = new ArrayList<>();
        teachers23.add(zamula);
        Subject uprBezopasnost = getSubject(teachers23, "UprBezopasnost");


        ArrayList<Teacher> teachers24 = new ArrayList<>();
        teachers24.add(shmatkov);
        Subject sistemiAI = getSubject(teachers24, "SistemiAI");

        ArrayList<Teacher> teachers25 = new ArrayList<>();
        teachers25.add(krasnobaev);
        Subject patentoznavstvo = getSubject(teachers25, "Patentoznavstvo");

        ArrayList<Teacher> teachers26 = new ArrayList<>();
        teachers26.add(mishchenko);
        Subject mpc = getSubject(teachers26, "MPC");

        ArrayList<Teacher> teachers27 = new ArrayList<>();
        teachers27.add(berdnikov);
        Subject up = getSubject(teachers27, "UP");

        ArrayList<Teacher> teachers28 = new ArrayList<>();
        teachers28.add(tolstoluzskaya);
        Subject openMp = getSubject(teachers28, "OpenMp");

        ArrayList<Teacher> teachers29 = new ArrayList<>();
        teachers29.add(kolovanova);
        Subject monitoring = getSubject(teachers29, "Monitoring");

        ArrayList<Teacher> teachers30 = new ArrayList<>();
        teachers30.add(sporov);
        Subject kpp = getSubject(teachers30, "KPP");

        ArrayList<Teacher> teachers31 = new ArrayList<>();
        teachers31.add(kropotov);
        Subject comp_graphicsKropotov = getSubject(teachers31, "Comp.graphics");

        ArrayList<Teacher> teachers32 = new ArrayList<>();
        teachers32.add(didenko);
        Subject veb_designDidenko = getSubject(teachers32, "Veb.disain");

        ArrayList<Teacher> teachers33 = new ArrayList<>();
        teachers33.add(rudichev);
        Subject veb_designRudichev = getSubject(teachers33, "Veb.disain");

        ArrayList<Teacher> teachers34 = new ArrayList<>();
        teachers34.add(kuznetsov);
        Subject teoriaKilKuznetsov = getSubject(teachers34, "TeoriaKil");

        ArrayList<Teacher> teachers35 = new ArrayList<>();
        teachers35.add(kuznetsova);
        Subject mathKuz = getSubject(teachers35, "math");


        ArrayList<Teacher> teachers40 = new ArrayList<>();
        teachers40.add(vasileva);
        Subject ohranaTruda = getSubject(teachers40, "OhranaTruda");


        ArrayList<Teacher> teachers36 = new ArrayList<>();
        teachers36.add(andreev);
        Subject osnoviTheoriKilAndreev = getSubject(teachers36, "OsnoviTheoriKil");

        ArrayList<Teacher> teachers37 = new ArrayList<>();
        teachers37.add(kuklin);
        Subject economic = getSubject(teachers37, "Economic");


        ArrayList<Teacher> teachers38 = new ArrayList<>();
        teachers38.add(mishchenko);
        Subject proectIC = getSubject(teachers38, "ProectIC");


        ArrayList<Teacher> teachers39 = new ArrayList<>();
        teachers39.add(stervoedov);
        Subject schemeSterv = getSubject(teachers39, "Scheme");

        ArrayList<Teacher> teachers44 = new ArrayList<>();
        teachers44.add(stervoedov);
        Subject schemeRalo = getSubject(teachers44, "SchemeRalo");


        ArrayList<Teacher> teachers41 = new ArrayList<>();
        teachers41.add(sharun);
        Subject anglShar = getSubject(teachers41, "Angl");

        ArrayList<Teacher> teachers42 = new ArrayList<>();
        teachers42.add(didenko);
        Subject compGraphicaDid = getSubject(teachers42, "Comp_graphicaDid");

        ArrayList<Teacher> teachers45 = new ArrayList<>();
        teachers45.add(zamula);
        Subject infBezDerj = getSubject(teachers45, "InfBezDerj");

        ArrayList<Teacher> teachers46 = new ArrayList<>();
        teachers46.add(tkachuk);
        Subject rspopsTkachuk = getSubject(teachers46, "RSPOPS");

        ArrayList<Teacher> teachers47 = new ArrayList<>();
        teachers47.add(tkachuk);
        Subject psiusTkachuk = getSubject(teachers47, "PSIUS");

        ArrayList<Teacher> teachers48 = new ArrayList<>();
        teachers48.add(kuznetsov);
        Subject matOsnovi = getSubject(teachers48, "matOsnovi");

        ArrayList<Teacher> teachers49 = new ArrayList<>();
        teachers49.add(potiy);
        Subject ttypts = getSubject(teachers49, "TTYPTS");

        ArrayList<Teacher> teachers50 = new ArrayList<>();
        teachers50.add(podtsikin);
        Subject matMetModel = getSubject(teachers50, "matMetModel");

        ArrayList<Teacher> teachers51 = new ArrayList<>();
        teachers51.add(lisitskaya);
        Subject bezpekaInfSyst = getSubject(teachers51, "bezpekaInfSyst");

        ArrayList<Teacher> teachers52 = new ArrayList<>();
        teachers52.add(shmatkov);
        Subject ksu= getSubject(teachers52, "KSU");

        ArrayList<Teacher> teachers53 = new ArrayList<>();
        teachers53.add(hruslov);
        Subject psiusHruslov= getSubject(teachers53, "psius");


        ArrayList<Teacher> teachers54 = new ArrayList<>();
        teachers54.add(hruslov);
        Subject rspopsHruslov= getSubject(teachers54, "rspops");


        ArrayList<Teacher> teachers55 = new ArrayList<>();
        teachers55.add(losev);
        Subject srs= getSubject(teachers55, "SRS");


        ArrayList<Teacher> teachers56 = new ArrayList<>();
        teachers56.add(narejniy);
        Subject monitoringTaAudit= getSubject(teachers56, "monitoringTaAudit");

        ArrayList<Teacher> teachers57 = new ArrayList<>();
        teachers57.add(dolgov);
        Subject kriptoMathods= getSubject(teachers57, "KriptoMathods");

        ArrayList<Teacher> teachers58 = new ArrayList<>();
        teachers58.add(vasileva);
        Subject chinnikiUspPr= getSubject(teachers58, "ChinnikiUspPr");


        ArrayList<Teacher> teachers59 = new ArrayList<>();
        teachers59.add(vasileva);
        Subject bezpekaBezdrotMerej= getSubject(teachers59, "BezpekaBezdrotMerej");


        ArrayList<Teacher> teachers60 = new ArrayList<>();
        teachers60.add(staroselska);
        Subject bjdStar= getSubject(teachers60, "bjdStar");

        ArrayList<Teacher> teachers61 = new ArrayList<>();
        teachers61.add(rassomahin);
        Subject matOsnoviProektuv= getSubject(teachers61, "matOsnoviProektuv");


        ArrayList<Teacher> teachers62 = new ArrayList<>();
        teachers62.add(karas);
        Subject systModelObrData = getSubject(teachers62, "systModelObrData");


        ArrayList<Teacher> teachers100 = new ArrayList<>();
        teachers100.add(rassomahin);
        Subject tau = getSubject(teachers100, "TAU");



        ArrayList<Teacher> teachers101 = new ArrayList<>();
        teachers101.add(esin);
        Subject zashita_v_info_kom_setyah = getSubject(teachers101, "Zashita_v_info_kom_setyah");


        ArrayList<Teacher> teachers102 = new ArrayList<>();
        teachers102.add(reva);
        Subject sistemi_avt_control = getSubject(teachers102, "Sistemi_avt_control");



        ArrayList<Teacher> teachers103 = new ArrayList<>();
        teachers103.add(yanovsky);
        Subject alternative_metods_obcislinya = getSubject(teachers103, "Alternative_metods_obcislinya");

        ArrayList<Teacher> teachers104 = new ArrayList<>();
        teachers104.add(gorban);
        Subject systAnalis = getSubject(teachers104, "SystAnalis");


        ArrayList<Teacher> teachers105 = new ArrayList<>();
        teachers105.add(kompaniets);
        Subject religieznavstvo = getSubject(teachers105, "Religieznavstvo");


        ArrayList<Teacher> teachers106 = new ArrayList<>();
        teachers106.add(politolog);
        Subject politologia = getSubject(teachers106, "Politologia");

        ArrayList<Teacher> teachers107 = new ArrayList<>();
        teachers107.add(gromiko);
        Subject complexSystem = getSubject(teachers107, "ComplexSystem");


        ArrayList<Teacher> teachers108 = new ArrayList<>();
        teachers108.add(zolotuhina);
        Subject menejment = getSubject(teachers108, "Menejment");



        ArrayList<Teacher> teachers109 = new ArrayList<>();
        teachers109.add(lisitskaya);
        Subject tehnologiiZahInf = getSubject(teachers109, "tehnologiiZahInf");


        ArrayList<Teacher> teachers110 = new ArrayList<>();
        teachers110.add(tolstoluzskaya);
        Subject paralelSystemObch = getSubject(teachers110, "ParalelSystemObch");


        ArrayList<Teacher> teachers111 = new ArrayList<>();
        teachers111.add(tolstoluzskaya);
        Subject tehnologiiParalelSystemObch = getSubject(teachers111, "tehnologiiParalelSystemObch");


        ArrayList<Teacher> teachers112 = new ArrayList<>();
        teachers112.add(svatovsky);
        Subject zahistInfVIKS = getSubject(teachers112, "zahistInfVIKS");

        ArrayList<Teacher> teachers113 = new ArrayList<>();
        teachers113.add(berdnikov);
        Subject proectCompSystUprBerd = getSubject(teachers113, "proectCompSystUpr");


        ArrayList<Teacher> teachers114 = new ArrayList<>();
        teachers114.add(pavlov);
        Subject proectCompSystUprPavlov = getSubject(teachers114, "proectCompSystUpr");

        ArrayList<Teacher> teachers115 = new ArrayList<>();
        teachers115.add(lazurikVT);
        Subject compErgonomika = getSubject(teachers115, "CompErgonomika");


        ArrayList<Teacher> teachers116 = new ArrayList<>();
        teachers116.add(yanovsky);
        Subject kvantumComputer = getSubject(teachers116, "KvantumComputer");


        ArrayList<Teacher> teachers117 = new ArrayList<>();
        teachers117.add(buts);
        Subject compPhisics = getSubject(teachers117, "CompPhisics");


        ArrayList<Teacher> teachers118 = new ArrayList<Teacher>();
        teachers118.add(bogucharski);
        Subject algorithmsBogucharski = getSubject(teachers118, "Algorithms");

        ArrayList<Teacher> teachers119 = new ArrayList<Teacher>();
        teachers119.add(kurinii);
        Subject discreateMathKurinii = getSubject(teachers119, "DiscreateMath");

        ArrayList<Teacher> teachers120 = new ArrayList<Teacher>();
        teachers120.add(starovoitov);
        Subject physicStrovoitov = getSubject(teachers120, "Physic");

        ArrayList<Teacher> teachers121 = new ArrayList<Teacher>();
        teachers121.add(moroz);
        Subject compOsnovi = getSubject(teachers121, "Computer osnovi");

        ArrayList<Teacher> teachers122 = new ArrayList<Teacher>();
        teachers122.add(denisenko);
        Subject physicDenisenko = getSubject(teachers122, "Physic");

        ArrayList<Teacher> teachers123 = new ArrayList<Teacher>();
        teachers123.add(oleshko);
        Subject oopOlechko = getSubject(teachers123, "OOP");

        ArrayList<Teacher> teachers124 = new ArrayList<Teacher>();
        teachers124.add(vladimirova);
        Subject oopVl = getSubject(teachers124, "OOPVl");


        ArrayList<Teacher> teachers125 = new ArrayList<Teacher>();
        teachers125.add(moroz);
        Subject infTeh = getSubject(teachers125, "InfTeh");

        ArrayList<Teacher> teachers126 = new ArrayList<Teacher>();
        teachers126.add(derid);
        Subject economTeor = getSubject(teachers126, "Ecomom");


        ArrayList<Teacher> teachers128 = new ArrayList<Teacher>();
        teachers128.add(bulavin);
        Subject isup = getSubject(teachers128, "ISUP");



        //----------------------------------------------------------
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(ks11);
        groups.add(ks13);
        groups.add(kb12);
        groups.add(ki11);
        groups.add(ks12);
        groups.add(kb11);
        groups.add(ku11);
        Lesson lesson = getLesson(groups, math, makarov, 2, 1, 1, 1,false);
        lessons.add(lesson);


        //----------------------------------------------------------
        ArrayList<Group> groups300 = new ArrayList<>();
        groups300.add(ks11);
        groups300.add(ks13);
        groups300.add(kb12);
        groups300.add(ki11);
        groups300.add(ks12);
        groups300.add(kb11);
        groups300.add(ku11);
        lesson = getLesson(groups300, algorithms, litvinova, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups2 = new ArrayList<>();
        groups2.add(ks11);
        lesson = getLesson(groups2, discreateMath, likova, 2, 1,2 , 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups209 = new ArrayList<>();
        groups209.add(ks12);
        lesson = getLesson(groups209, discreateMath, likova, 2, 1, 2 , 0,false);
        lessons.add(lesson);
        //----------------------------------------------------------

        ArrayList<Group> groups210 = new ArrayList<>();
        groups210.add(ks13);
        lesson = getLesson(groups210, discreateMath, likova, 2, 1, 2 , 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups2094 = new ArrayList<>();
        groups2094.add(kb12);
        lesson = getLesson(groups2094, discreateMath, likova, 2, 1, 2 , 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups3 = new ArrayList<>();
        ArrayList<Teacher> teachers347 = new ArrayList<>();
        teachers347.add(chervinko);
        teachers347.add(sharun);
        ArrayList<Subject> subjects20 = new ArrayList<>();
        subjects20.add(anglCh);
        subjects20.add(anglShar);
        groups3.add(ku11);
        lesson = getLesson(groups3, subjects20, teachers347, true, 2, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups4 = new ArrayList<>();
        groups4.add(ks11);
        lesson = getLesson(groups4, math, makarov, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups5 = new ArrayList<>();
        groups5.add(ks12);
        lesson = getLesson(groups5, anglTu, turenko, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups6 = new ArrayList<>();
        groups6.add(ku11);
        lesson = getLesson(groups6, math, makarov, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups7 = new ArrayList<>();
        ArrayList<Teacher> teachers791 = new ArrayList<>();
        teachers791.add(litvinova);
        teachers791.add(litvinov);
        ArrayList<Subject> subjects21 = new ArrayList<>();
        subjects21.add(algorithms);
        subjects21.add(oop);
        groups7.add(kb11);
        lesson = getLesson(groups7, subjects21, teachers791, true, 2, 1, 2,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups8 = new ArrayList<>();
        groups8.add(ks21);
        lesson = getLesson(groups8, comp_graphicaOsip, osipchuk, 2, 1, 2, 2,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups9 = new ArrayList<>();
        groups9.add(ks22);
        lesson = getLesson(groups9, comp_seti, artyuh, 2, 1, 1, 2,true);
        lessons.add(lesson);

        //----------------------------------------------------------
        ArrayList<Group> groups10 = new ArrayList<>();
        groups10.add(kb22);
        lesson = getLesson(groups10, comp_graphicaOsip, osipchuk, 2, 1, 2, 2,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups11 = new ArrayList<>();
        groups11.add(ks21);
        lesson = getLesson(groups11, comp_seti, artyuh, 2, 1, 1, 2,true);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups12 = new ArrayList<>();
        groups12.add(kb22);
        lesson = getLesson(groups12, bjd, zamula, 2, 1, 2, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------
        ArrayList<Group> groups13_1 = new ArrayList<>();
        groups13_1.add(kb21);
        groups13_1.add(kb22);
        lesson = getLesson(groups13_1, bjd, zamula, 2, 1, 2, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups13 = new ArrayList<>();
        groups13.add(ks21);
        groups13.add(ks22);
        lesson = getLesson(groups13, comp_seti_Berdnikov, berdnikov, 2, 1, 1, 1,true);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups14 = new ArrayList<>();
        groups14.add(ku21);
        lesson = getLesson(groups14, comp_graphicaOsip, osipchuk, 2, 1, 2, 2,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups15 = new ArrayList<>();
        groups15.add(ku21);
        lesson = getLesson(groups15, mathKab, kabalyants, 2, 1, 2, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------
        ArrayList<Group> groups16 = new ArrayList<>();
        groups16.add(kb22);
        lesson = getLesson(groups16, mathKab ,kabalyants, 2, 1, 2, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------
        ArrayList<Group> groups17 = new ArrayList<>();
        groups17.add(ks21);
        groups17.add(ks22);
        groups17.add(ku21);
        groups17.add(kb21);
        groups17.add(kb22);
        lesson = getLesson(groups17, mathKab , kabalyants, 2, 1, 2, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups18 = new ArrayList<>();
        groups18.add(ks31);
        groups18.add(ks32);
        lesson = getLesson(groups18, ypp , rudichev, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups19 = new ArrayList<>();
        groups19.add(ku31);
        lesson = getLesson(groups19, tpi, pavlov , 2, 1, 2, 2,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups20 = new ArrayList<>();
        groups20.add(kb31);
        lesson = getLesson(groups20, kriptologia, gorbenko , 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups21 = new ArrayList<>();
        groups21.add(ks31);
        groups21.add(ks32);
        groups21.add(ku31);
        lesson = getLesson(groups21, db, lazurikV, 2, 1, 1, 1,true);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups22 = new ArrayList<>();
        groups22.add(kb31);
        lesson = getLesson(groups22, kriptologia, gorbenko, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups23 = new ArrayList<>();
        groups23.add(ks31);
        groups23.add(ks32);
        lesson = getLesson(groups23, ai, kuklin, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups24 = new ArrayList<>();
        groups24.add(ks31);
        groups24.add(ks32);
        lesson = getLesson(groups24, ai, kuklin , 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups25 = new ArrayList<>();
        groups25.add(ku31);
        groups25.add(kb31);
        lesson = getLesson(groups25, tpi, losev, 2, 1, 1, 1,false);
        lessons.add(lesson);
        //----------------------------------------------------------

        ArrayList<Group> groups26 = new ArrayList<>();
        groups26.add(ku31);
        lesson = getLesson(groups26, db, lazurikV, 2, 1, 1, 2,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups27 = new ArrayList<>();
        groups27.add(ks41);
        lesson = getLesson(groups27, model, podtsikin, 2, 1, 1, 2,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups28 = new ArrayList<>();
        groups28.add(ks42);
        lesson = getLesson(groups28, optoinform , dolya, 2, 1, 1, 0,false);
        lessons.add(lesson);
        //----------------------------------------------------------

        ArrayList<Group> groups29 = new ArrayList<>();
        groups29.add(ks41);
        groups29.add(ks42);
        lesson = getLesson(groups29, model, podtsikin, 2, 1, 1, 0,false);
        lessons.add(lesson);
        //----------------------------------------------------------

        ArrayList<Group> groups30 = new ArrayList<>();
        groups30.add(ku41);
        lesson = getLesson(groups30, susks, bulavin, 2, 1, 1, 0,true);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups31 = new ArrayList<>();
        groups31.add(kb41);
        lesson = getLesson(groups31, smodtc, krasnobaev , 2, 1, 1, 0,false);
        lessons.add(lesson);
        //----------------------------------------------------------

        ArrayList<Group> groups32 = new ArrayList<>();
        groups32.add(ks41);
        groups32.add(ks42);
        lesson = getLesson(groups32, upravlenieIT , vladimirova, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups33 = new ArrayList<>();
        groups33.add(ku41);
        lesson = getLesson(groups33, susks , bulavin, 2, 1, 1, 2,true);
        lessons.add(lesson);

        ArrayList<Group> groups631 = new ArrayList<>();
        groups631.add(ku41);
        lesson = getLesson(groups631, susks , bulavin, 2, 1, 1, 0,true);
        lessons.add(lesson);


        //----------------------------------------------------------

        ArrayList<Group> groups34 = new ArrayList<>();
        groups34.add(kb41);
        lesson = getLesson(groups34, uprBezopasnost, zamula, 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups632 = new ArrayList<>();
        groups632.add(kb41);
        lesson = getLesson(groups632, uprBezopasnost, zamula, 2, 1, 1, 0,false);
        lessons.add(lesson);


        //----------------------------------------------------------

        ArrayList<Group> groups35 = new ArrayList<>();
        groups35.add(ku41);
        lesson = getLesson(groups35, sistemiAI, shmatkov, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups36 = new ArrayList<>();
        groups36.add(ks41);
        lesson = getLesson(groups36, upravlenieIT, vladimirova, 2, 1, 1, 2,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups37 = new ArrayList<>();
        groups37.add(ks51);
        lesson = getLesson(groups37, patentoznavstvo, artyuh, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups38 = new ArrayList<>();
        groups38.add(ks52);
        lesson = getLesson(groups38, patentoznavstvo, artyuh, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups39 = new ArrayList<>();
        groups39.add(ks51);
        lesson = getLesson(groups39, mpc, mishchenko, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups40 = new ArrayList<>();
        groups40.add(ks51);
        groups40.add(ks52);
        lesson = getLesson(groups40, mpc, mishchenko, 2, 1, 1, 1,false);//THIS LECTION
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups41 = new ArrayList<>();
        groups41.add(ks52);
        lesson = getLesson(groups41, mpc, mishchenko, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups42 = new ArrayList<>();
        groups42.add(ku51);
        groups42.add(ku52);
        lesson = getLesson(groups42, up, berdnikov, 2, 1, 1, 1,false);//LECTION
        lessons.add(lesson);

        //----------------------------------------------------------

        //----------------------------------------------------------

        ArrayList<Group> groups43 = new ArrayList<>();
        groups43.add(ku51);
        groups43.add(ku52);
        lesson = getLesson(groups43, up, berdnikov, 2, 1, 1, 0,true);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups44 = new ArrayList<>();
        groups44.add(ku51);
        groups44.add(ku52);
        lesson = getLesson(groups44, openMp, tolstoluzskaya, 2, 1, 1, 1,true);
        lessons.add(lesson);

        //----------------------------------------------------------
        ArrayList<Group> groups633 = new ArrayList<>();
        groups633.add(ku51);
        groups633.add(ku52);
        lesson = getLesson(groups633, openMp, tolstoluzskaya, 2, 1, 1, 0,true);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups45 = new ArrayList<>();
        groups45.add(kya51);
        lesson = getLesson(groups45, monitoring, kolovanova, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups46 = new ArrayList<>();
        groups46.add(kya51);
        lesson = getLesson(groups46, monitoring, kolovanova, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups47 = new ArrayList<>();
        groups47.add(ks22);
        lesson = getLesson(groups47, mathKab, kabalyants, 2, 1, 2, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups48 = new ArrayList<>();
        ArrayList<Teacher> teachers562 = new ArrayList<>();
        teachers562.add(sharun);
        teachers562.add(chervinko);
        ArrayList<Subject> subjects22 = new ArrayList<>();
        subjects22.add(anglShar);
        subjects22.add(anglCh);
        groups48.add(kb11);

        lesson = getLesson(groups48, subjects22, teachers562, true, 2, 1, 0,false);
        lessons.add(lesson);
        //----------------------------------------------------------

        ArrayList<Group> groups49 = new ArrayList<>();
        groups49.add(kb21);
        lesson = getLesson(groups49,mathKab, kabalyants, 2, 1, 2, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups50 = new ArrayList<>();
        groups50.add(kb22);
        lesson = getLesson(groups50,mathKuz, kuznetsova, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups51 = new ArrayList<>();
        groups51.add(ks21);
        groups51.add(ks22);
        groups51.add(ku21);
        groups51.add(kb21);
        groups51.add(kb22);
        lesson = getLesson(groups51,mathKuz, kuznetsova, 2, 1, 1, 1,false);//NOT MIGALKA
        lessons.add(lesson);


        //----------------------------------------------------------

        ArrayList<Group> groups52 = new ArrayList<>();
        groups52.add(ks21);
        groups52.add(ks22);
        groups52.add(ku21);
        groups52.add(kb21);
        groups52.add(kb22);
        lesson = getLesson(groups52,kpp, sporov, 2, 1, 2, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups53 = new ArrayList<>();
        groups53.add(ks22);
        lesson = getLesson(groups53,anglShar, sharun, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups54 = new ArrayList<>();
        groups54.add(kb21);
        lesson = getLesson(groups54,mathKuz, kuznetsova, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups55 = new ArrayList<>();
        groups55.add(ks21);
        lesson = getLesson(groups55,veb_designDidenko, didenko, 2, 1, 2, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups56 = new ArrayList<>();
        groups56.add(ks21);
        groups56.add(ks22);
        lesson = getLesson(groups56,veb_designRudichev, rudichev, 2, 1, 2, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups57 = new ArrayList<>();
        groups57.add(ku21);
        lesson = getLesson(groups57,mathKuz, kuznetsova, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups58 = new ArrayList<>();
        groups58.add(kb21);
        lesson = getLesson(groups58,ohranaTruda, vasileva, 2, 1, 2, 0,false);
        lessons.add(lesson);

//----------------------------------------------------------

        ArrayList<Group> groups59 = new ArrayList<>();
        groups59.add(kb22);
        lesson = getLesson(groups59,kpp, sporov, 2, 1, 2, 2,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups60 = new ArrayList<>();
        groups60.add(ks21);
        lesson = getLesson(groups60,kpp, sporov, 2, 1, 2, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups61 = new ArrayList<>();
        groups61.add(ku21);
        lesson = getLesson(groups61,bjdStar, staroselska, 2, 1, 2, 0,true);
        lessons.add(lesson);
        //----------------------------------------------------------

        ArrayList<Group> groups62 = new ArrayList<>();
        groups62.add(kb21);
        groups62.add(kb22);
        lesson = getLesson(groups62,teoriaKilKuznetsov, kuznetsov, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups63 = new ArrayList<>();
        groups63.add(ks21);
        lesson = getLesson(groups63,mathKuz,kuznetsova, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups67 = new ArrayList<>();
        groups67.add(ks22);
        lesson = getLesson(groups67,mathKuz,kuznetsova, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups64 = new ArrayList<>();
        groups64.add(ks22);
        lesson = getLesson(groups64,bjd,vasileva, 2, 1, 2, 0,true);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups65 = new ArrayList<>();
        groups65.add(ku21);
        lesson = getLesson(groups65,osnoviTheoriKilAndreev,andreev, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------
        ArrayList<Group> groups66 = new ArrayList<>();
        groups66.add(kb21);
        groups66.add(kb22);
        lesson = getLesson(groups66,teoriaKilKuznetsov, kuznetsov, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------
        ArrayList<Group> groups68 = new ArrayList<>();
        groups68.add(ku21);
        groups68.add(kb21);
        groups68.add(kb22);
        lesson = getLesson(groups68,osnoviTheoriKilAndreev,andreev, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups69 = new ArrayList<>();
        ArrayList<Teacher> teachers565 = new ArrayList<>();
        teachers565.add(sharun);
        teachers565.add(chervinko);
        ArrayList<Subject> subjects23 = new ArrayList<>();
        subjects23.add(anglShar);
        subjects23.add(anglCh);
        groups69.add(ks21);
        lesson = getLesson(groups69,subjects23,teachers565, true, 2, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups70 = new ArrayList<>();
        groups70.add(ku21);
        lesson = getLesson(groups70,schemeSterv,stervoedov, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups71 = new ArrayList<>();
        groups71.add(ks21);
        groups71.add(ks22);
        lesson = getLesson(groups71,economic,kuklin, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups72 = new ArrayList<>();
        groups72.add(ku21);
        lesson = getLesson(groups72,schemeRalo,ralo, 2, 1, 1, 0,true);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups73 = new ArrayList<>();
        groups73.add(ks21);
        lesson = getLesson(groups73,mathKab,kabalyants, 2, 1, 2, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups74 = new ArrayList<>();
        groups74.add(ks22);
        lesson = getLesson(groups74,proectIC,mishchenko, 2, 1, 2, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups75 = new ArrayList<>();
        groups75.add(ks21);
        groups75.add(ks22);
        lesson = getLesson(groups75,proectIC,mishchenko, 2, 1, 2, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups76 = new ArrayList<>();
        groups76.add(ks21);
        lesson = getLesson(groups76,proectIC,mishchenko, 2, 1, 2, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups77 = new ArrayList<>();
        groups77.add(ks22);
        lesson = getLesson(groups77,anglTu,turenko, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups78 = new ArrayList<>();
        groups78.add(kb21);
        lesson = getLesson(groups78,infBezDerj,zamula, 2, 1, 2, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups80 = new ArrayList<>();
        groups80.add(kb21);
        lesson = getLesson(groups80,osnoviTheoriKilAndreev,andreev, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups81 = new ArrayList<>();
        ArrayList<Teacher> teachers566 = new ArrayList<>();
        teachers566.add(sharun);
        teachers566.add(chervinko);
        ArrayList<Subject> subjects24 = new ArrayList<>();
        subjects24.add(anglShar);
        subjects24.add(anglCh);
        groups81.add(kb21);
        lesson = getLesson(groups81,subjects24,teachers566, true, 2, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups82 = new ArrayList<>();
        ArrayList<Teacher> teachers567 = new ArrayList<>();
        teachers567.add(sharun);
        teachers567.add(chervinko);
        ArrayList<Subject> subjects25 = new ArrayList<>();
        subjects25.add(anglShar);
        subjects25.add(anglCh);
        groups82.add(kb22);
        lesson = getLesson(groups82,subjects25,teachers567, true, 2, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups83 = new ArrayList<>();
        groups83.add(kb22);
        lesson = getLesson(groups83,osnoviTheoriKilAndreev,andreev, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups84 = new ArrayList<>();
        groups84.add(ku51);
        groups84.add(ku52);
        lesson = getLesson(groups84,patentoznavstvo,artyuh, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups85 = new ArrayList<>();
        groups85.add(ku51);
        groups85.add(ku52);
        groups85.add(ks51);
        groups85.add(ks52);
        groups85.add(kb51);
        groups85.add(kya51);
        groups85.add(kb51);
        lesson = getLesson(groups85,patentoznavstvo,artyuh, 2, 1, 1, 1,true);
        lessons.add(lesson);

        //----------------------------------------------------------

        ArrayList<Group> groups87 = new ArrayList<>();
        groups87.add(ku51);
        groups87.add(ku52);
        lesson = getLesson(groups87,psiusTkachuk,tkachuk, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups86 = new ArrayList<>();
        groups86.add(ks51);
        groups86.add(ks52);
        lesson = getLesson(groups86,rspopsTkachuk,tkachuk, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups88 = new ArrayList<>();
        groups88.add(kya51);
        groups88.add(kb52);
        groups88.add(kb51);
        lesson = getLesson(groups88,matOsnovi,kuznetsov, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups89 = new ArrayList<>();
        groups89.add(kya51);
        groups89.add(kb52);
        groups89.add(kb51);
        lesson = getLesson(groups89,matOsnovi,kuznetsov, 2, 1, 1, 1,false);
        lessons.add(lesson);
        //----------------------------------------------------------


        ArrayList<Group> groups90 = new ArrayList<>();
        groups90.add(ks51);
        lesson = getLesson(groups90,rspopsHruslov,hruslov, 2, 1, 2, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups91 = new ArrayList<>();
        groups91.add(kya51);
        groups91.add(kb52);
        groups91.add(kb51);
        lesson = getLesson(groups91,ttypts,potiy, 2, 1, 1, 1,true);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups92 = new ArrayList<>();
        groups92.add(ks51);
        lesson = getLesson(groups92,matMetModel,podtsikin, 2, 1, 1, 0,true);
        lessons.add(lesson);


        //----------------------------------------------------------


        ArrayList<Group> groups93 = new ArrayList<>();
        groups93.add(ks51);
        groups93.add(ks52);
        lesson = getLesson(groups93,matMetModel,podtsikin, 2, 1, 1, 1,true);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups94 = new ArrayList<>();
        groups94.add(ks51);
        lesson = getLesson(groups94,bezpekaInfSyst,lisitskaya, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups95 = new ArrayList<>();
        groups95.add(ks52);
        lesson = getLesson(groups95,matMetModel,podtsikin, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups96 = new ArrayList<>();
        groups96.add(ku51);
        groups96.add(ku52);
        lesson = getLesson(groups96,ksu,shmatkov, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups97 = new ArrayList<>();
        groups97.add(kya51);
        groups97.add(kb51);
        groups97.add(kb52);
        lesson = getLesson(groups97,matOsnoviProektuv,rassomahin, 2, 1, 1, 1,true);
        lessons.add(lesson);
        //----------------------------------------------------------


        ArrayList<Group> groups98 = new ArrayList<>();
        groups98.add(ks52);
        groups98.add(ks51);
        lesson = getLesson(groups98,bezpekaInfSyst,lisitskaya, 2, 1, 1, 1,false);
        lessons.add(lesson);
        //----------------------------------------------------------


        ArrayList<Group> groups99 = new ArrayList<>();
        groups99.add(ku51);
        groups99.add(ku52);
        lesson = getLesson(groups99,ksu,shmatkov, 2, 1, 1, 1,false);
        lessons.add(lesson);
//----------------------------------------------------------


        ArrayList<Group> groups200 = new ArrayList<>();
        groups200.add(kya51);
        groups200.add(kb51);
        groups200.add(kb52);
        lesson = getLesson(groups200,matOsnoviProektuv,rassomahin, 2, 1, 1, 1,true);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups201 = new ArrayList<>();
        groups201.add(kya51);
        groups201.add(kb51);
        groups201.add(kb52);
        groups201.add(ks51);
        groups201.add(ks52);
        groups201.add(ku51);
        groups201.add(ku52);
        lesson = getLesson(groups201,chinnikiUspPr,vasileva, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups202 = new ArrayList<>();
        groups202.add(ku51);
        groups202.add(ku52);
        lesson = getLesson(groups202,psiusHruslov,hruslov, 2, 1, 1, 2,true);
        lessons.add(lesson);
        //----------------------------------------------------------


        ArrayList<Group> groups203 = new ArrayList<>();
        groups203.add(ku51);
        groups203.add(ku52);
        lesson = getLesson(groups203,psiusHruslov,hruslov, 2, 1, 1, 2,true);
        lessons.add(lesson);

//----------------------------------------------------------


        ArrayList<Group> groups204 = new ArrayList<>();
        groups204.add(kya51);
        groups204.add(kb51);
        groups204.add(kb52);
        lesson = getLesson(groups204,patentoznavstvo,artyuh, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups205 = new ArrayList<>(); // 536
        groups205.add(ks51);
        groups205.add(ks52);
        lesson = getLesson(groups205,rspopsHruslov,hruslov, 2, 1, 1, 2,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups206 = new ArrayList<>();
        groups206.add(ku51);
        groups206.add(ku52);
        lesson = getLesson(groups206,srs,losev, 2, 1, 1, 0,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups207 = new ArrayList<>();
        groups207.add(ku51);
        groups207.add(ku52);
        lesson = getLesson(groups207,srs,losev, 2, 1, 1, 0,false);
        lessons.add(lesson);
        //----------------------------------------------------------


        ArrayList<Group> groups208 = new ArrayList<>();
        groups208.add(ks51);
        groups208.add(ks52);
        lesson = getLesson(groups208,systModelObrData,karas, 2, 1, 1, 1,false);
        lessons.add(lesson);


        //----------------------------------------------------------


        ArrayList<Group> groups299 = new ArrayList<>();
        groups299.add(kya51);
        lesson = getLesson(groups299,monitoringTaAudit,narejniy, 2, 1, 1, 0,true);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups290 = new ArrayList<>();
        groups290.add(kya51);
        lesson = getLesson(groups290,monitoringTaAudit,narejniy, 2, 1, 1, 0,true);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups211 = new ArrayList<>();
        groups211.add(kb51);
        groups211.add(kb52);
        lesson = getLesson(groups211,kriptoMathods,dolgov, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups212 = new ArrayList<>();
        groups212.add(kb51);
        groups212.add(kb52);
        lesson = getLesson(groups212,kriptoMathods,dolgov, 2, 1, 1, 1,false);
        lessons.add(lesson);

        //----------------------------------------------------------


        ArrayList<Group> groups213 = new ArrayList<>();
        groups213.add(kya51);
        groups213.add(kb51);
        groups213.add(kb52);
        groups213.add(ks51);
        groups213.add(ks52);
        groups213.add(ku51);
        groups213.add(ku52);
        lesson = getLesson(groups213,chinnikiUspPr,vasileva, 2, 1, 1, 1,false);
        lessons.add(lesson);
        //----------------------------------------------------------


        ArrayList<Group> groups214 = new ArrayList<>();
        groups214.add(kb51);
        groups214.add(kb52);
        lesson = getLesson(groups214,bezpekaBezdrotMerej,svatovsky, 2, 1, 1, 1,false);
        lessons.add(lesson);
        //----------------------------------------------------------


        ArrayList<Group> groups215 = new ArrayList<>();
        groups215.add(kb51);
        groups215.add(kb52);
        lesson = getLesson(groups215,bezpekaBezdrotMerej,svatovsky, 2, 1, 1, 1,false);
        lessons.add(lesson);

        ArrayList<Group> groups100 = new ArrayList<>();
        groups100.add(ks31);
        groups100.add(ks32);
        groups100.add(ku31);
        lesson = getLesson(groups100, mathKab, kabalyants, 2, 1, 1, 1,false);
        lessons.add(lesson);

        ArrayList<Group> groups101 = new ArrayList<>();
        groups101.add(ks31);
        lesson = getLesson(groups101, mathKab, kabalyants, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups102 = new ArrayList<>();
        groups102.add(ku31);
        lesson = getLesson(groups102, mathKab, kabalyants, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups103 = new ArrayList<>();
        groups103.add(ks32);
        lesson = getLesson(groups103, mathKab, kabalyants, 2, 1, 1, 2,false);
        lessons.add(lesson);

//---------------------------------

        ArrayList<Group> groups105 = new ArrayList<>();
        groups105.add(ks31);
        lesson = getLesson(groups105, db, lazurikV, 2, 1, 1, 2,true);
        lessons.add(lesson);

        ArrayList<Group> groups106 = new ArrayList<>();
        groups106.add(ks32);
        lesson = getLesson(groups106, db, lazurikV, 2, 1, 1, 2,true);
        lessons.add(lesson);

//------------------------------
        ArrayList<Group> groups108 = new ArrayList<>();
        groups108.add(kb31);
        groups108.add(ku31);
        lesson = getLesson(groups108, comp_seti, berdnikov, 2, 1, 1, 1,false);
        lessons.add(lesson);

        ArrayList<Group> groups109 = new ArrayList<>();
        groups109.add(ku31);
        lesson = getLesson(groups109, comp_seti, artyuh, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups110 = new ArrayList<>();
        groups110.add(kb31);
        lesson = getLesson(groups110, comp_seti, artyuh, 2, 1, 1, 2,true);
        lessons.add(lesson);
//-----------------------------
        ArrayList<Group> groups111 = new ArrayList<>();
        groups111.add(ku31);
        lesson = getLesson(groups111, isup, bulavin, 2, 1, 1, 0,true);
        lessons.add(lesson);


        ArrayList<Group> groups112 = new ArrayList<>();
        groups112.add(ku31);
        lesson = getLesson(groups112, tau, rassomahin, 2, 1, 1, 0,true);
        lessons.add(lesson);

        ArrayList<Group> groups113 = new ArrayList<>();
        groups113.add(ku31);
        lesson = getLesson(groups113, tau, rassomahin, 2, 1, 1, 2,true);
        lessons.add(lesson);
//----------------------

        ArrayList<Group> groups119 = new ArrayList<>();
        groups119.add(kb31);
        lesson = getLesson(groups119, zashita_v_info_kom_setyah, esin, 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups120 = new ArrayList<>();
        groups120.add(kb31);
        lesson = getLesson(groups120, zashita_v_info_kom_setyah, esin, 2, 1, 1, 0,false);
        lessons.add(lesson);
//---------------------------------

        ArrayList<Group> groups121 = new ArrayList<>();
        groups121.add(ks31);
        groups121.add(ks32);
        lesson = getLesson(groups121, sistemi_avt_control, reva, 2, 1, 1, 1,false);
        lessons.add(lesson);

        ArrayList<Group> groups122 = new ArrayList<>();
        groups122.add(ks31);
        lesson = getLesson(groups122, sistemi_avt_control, reva, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups123 = new ArrayList<>();
        groups123.add(ks32);
        lesson = getLesson(groups123, sistemi_avt_control, reva, 2, 1, 1, 2,false);
        lessons.add(lesson);
//---------------------------
        ArrayList<Group> groups126 = new ArrayList<>();
        groups126.add(ks32);
        lesson = getLesson(groups126, alternative_metods_obcislinya, yanovsky, 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups127 = new ArrayList<>();
        groups127.add(ks32);
        lesson = getLesson(groups127, alternative_metods_obcislinya, yanovsky, 2, 1, 1, 0,false);
        lessons.add(lesson);


        ArrayList<Group> groups129 = new ArrayList<>();
        groups129.add(ks32);
        lesson = getLesson(groups129, ypp, rudichev, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups130 = new ArrayList<>();
        groups130.add(ks31);
        lesson = getLesson(groups130, ypp, rudichev, 2, 1, 1, 0,false);
        lessons.add(lesson);


        ArrayList<Group> groups131 = new ArrayList<>();
        groups131.add(ks31);
        groups131.add(ks32);
        lesson = getLesson(groups131, systAnalis, gorban, 2, 1, 1, 1,false);
        lessons.add(lesson);

        ArrayList<Group> groups132 = new ArrayList<>();
        groups132.add(ks31);
        lesson = getLesson(groups132, systAnalis, gorban, 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups133 = new ArrayList<>();
        groups133.add(ks32);
        lesson = getLesson(groups133, systAnalis, gorban, 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups134 = new ArrayList<>();
        ArrayList<Teacher> teachers568 = new ArrayList<>();
        teachers568.add(kompaniets);
        teachers568.add(derid);
        ArrayList<Subject> subjects26 = new ArrayList<>();
        subjects26.add(religieznavstvo);
        subjects26.add(economTeor);
        groups134.add(ks32);
        groups134.add(ks31);
        groups134.add(ku31);
        groups134.add(kb31);
        lesson = getLesson(groups134, subjects26, teachers568, true, 2, 1, 1,false);
        lessons.add(lesson);


        ArrayList<Group> groups135 = new ArrayList<>();
        groups135.add(ks32);
        groups135.add(ks31);
        groups135.add(ku31);
        groups135.add(kb31);
        lesson = getLesson(groups135, politologia , politolog, 2, 1, 1, 1,false);
        lessons.add(lesson);

        ArrayList<Group> groups136 = new ArrayList<>();
        groups136.add(ks42);
        groups136.add(ks41);
        groups136.add(ku41);
        groups136.add(kb41);
        lesson = getLesson(groups136, optoinform , dolya, 2, 1, 1, 1,false);
        lessons.add(lesson);

        ArrayList<Group> groups137 = new ArrayList<>();
        groups137.add(ks41);
        lesson = getLesson(groups137, optoinform , dolya , 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups138 = new ArrayList<>();
        groups138.add(ks42);
        lesson = getLesson(groups138, tehnologiiZahInf , lisitskaya , 2, 1, 1, 0,true);
        lessons.add(lesson);

        ArrayList<Group> groups139 = new ArrayList<>();
        groups139.add(ks42);
        groups139.add(ks41);
        groups139.add(ku41);
        lesson = getLesson(groups139, tehnologiiZahInf , lisitskaya , 2, 1, 1, 1,false);
        lessons.add(lesson);


        ArrayList<Group> groups140 = new ArrayList<>();
        groups140.add(ku41);
        lesson = getLesson(groups140, susks , bulavin , 2, 1, 1, 0,true);
        lessons.add(lesson);

        ArrayList<Group> groups141 = new ArrayList<>();
        groups141.add(kb41);
        lesson = getLesson(groups141, complexSystem , gromiko , 2, 1, 1, 2,false);
        lessons.add(lesson);


        ArrayList<Group> groups142 = new ArrayList<>();
        groups142.add(kb41);
        lesson = getLesson(groups142, complexSystem , gromiko , 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups143 = new ArrayList<>();
        groups143.add(ks41);
        lesson = getLesson(groups143, tehnologiiZahInf , lisitskaya , 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups144 = new ArrayList<>();
        groups144.add(ku41);
        groups144.add(kb41);
        lesson = getLesson(groups144, menejment , zolotuhina , 2, 1, 1, 1,false);
        lessons.add(lesson);


        ArrayList<Group> groups145 = new ArrayList<>();
        groups145.add(ku41);
        lesson = getLesson(groups145, optoinform , dolya , 2, 1, 1, 0,false);
        lessons.add(lesson);


        ArrayList<Group> groups146 = new ArrayList<>();
        groups146.add(ks42);
        lesson = getLesson(groups146, tehnologiiParalelSystemObch , tolstoluzskaya , 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups147 = new ArrayList<>();
        groups147.add(ks42);
        lesson = getLesson(groups147, model , podtsikin , 2, 1, 1, 0,false);
        lessons.add(lesson);


        ArrayList<Group> groups148 = new ArrayList<>();
        groups148.add(kb41);
        lesson = getLesson(groups148, optoinform , dolya , 2, 1, 1, 0,false);
        lessons.add(lesson);


        ArrayList<Group> groups149 = new ArrayList<>();
        groups149.add(ku41);
        lesson = getLesson(groups149, tehnologiiZahInf , lisitskaya, 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups150 = new ArrayList<>();
        groups150.add(ku41);
        lesson = getLesson(groups150, paralelSystemObch , tolstoluzskaya , 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups151 = new ArrayList<>();
        groups151.add(ku41);
        lesson = getLesson(groups151, sistemiAI, shmatkov , 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups152 = new ArrayList<>();
        groups152.add(kb41);
        lesson = getLesson(groups152, zahistInfVIKS , svatovsky , 2, 1, 1, 2,true);
        lessons.add(lesson);

        ArrayList<Group> groups153 = new ArrayList<>();
        groups153.add(kb41);
        lesson = getLesson(groups153, zahistInfVIKS , svatovsky , 2, 1, 1, 0,true);
        lessons.add(lesson);


        ArrayList<Group> groups154 = new ArrayList<>();
        groups154.add(ku41);
        lesson = getLesson(groups154, proectCompSystUprBerd , berdnikov, 2, 1, 1, 0,false);
        lessons.add(lesson);



        ArrayList<Group> groups155 = new ArrayList<>();
        groups155.add(ks41);
        groups155.add(ks42);
        lesson = getLesson(groups155, compErgonomika , lazurikVT, 2, 1, 1, 1,false);
        lessons.add(lesson);



        ArrayList<Group> groups156 = new ArrayList<>();
        groups156.add(ku41);
        lesson = getLesson(groups156, proectCompSystUprPavlov, pavlov, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups157 = new ArrayList<>();
        groups157.add(kb41);
        lesson = getLesson(groups157, smodtc, krasnobaev, 2, 1, 1, 0,false);
        lessons.add(lesson);


        ArrayList<Group> groups158 = new ArrayList<>();
        groups158.add(ku41);
        groups158.add(ks41);
        groups158.add(ks42);
        lesson = getLesson(groups158, tehnologiiParalelSystemObch, tolstoluzskaya, 2, 1, 1, 1,false);
        lessons.add(lesson);


        ArrayList<Group> groups160 = new ArrayList<>();
        groups160.add(kb41);
        lesson = getLesson(groups160, uprBezopasnost, zamula, 2, 1, 1, 0,true);
        lessons.add(lesson);

        ArrayList<Group> groups161 = new ArrayList<>();
        groups161.add(ks41);
        lesson = getLesson(groups161, tehnologiiParalelSystemObch, tolstoluzskaya, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups162 = new ArrayList<>();
        groups162.add(ks42);
        lesson = getLesson(groups162,upravlenieIT, vladimirova, 2, 1, 1, 2,false);
        lessons.add(lesson);


        ArrayList<Group> groups163 = new ArrayList<>();
        groups163.add(kb41);
        lesson = getLesson(groups163, complexSystem, gromiko, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups164 = new ArrayList<>();
        groups164.add(kb41);
        lesson = getLesson(groups164, complexSystem, gromiko, 2, 1, 2, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups165 = new ArrayList<>();
        groups165.add(ks41);
        lesson = getLesson(groups165, compPhisics, buts, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups166 = new ArrayList<>();
        groups166.add(ks41);
        lesson = getLesson(groups166, compPhisics, buts, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups167 = new ArrayList<>();
        groups167.add(ks42);
        lesson = getLesson(groups167, kvantumComputer, yanovsky, 2, 1, 1, 0,true);
        lessons.add(lesson);

        ArrayList<Group> groups168 = new ArrayList<>();
        groups168.add(ks42);
        lesson = getLesson(groups168, kvantumComputer, yanovsky, 2, 1, 1, 0,true);
        lessons.add(lesson);


        ArrayList<Group> groups170 = new ArrayList<>();
        groups170.add(kb12);
        lesson = getLesson(groups170, math, makarov, 2, 1, 1, 0,false);
        lessons.add(lesson);



        ArrayList<Group> groups172 = new ArrayList<>();
        groups172.add(ks12);
        lesson = getLesson(groups172, physic, karas, 2, 1, 1, 0,false);
        lessons.add(lesson);



        ArrayList<Group> groups173 = new ArrayList<>();
        ArrayList<Teacher> teachers920 = new ArrayList<>();
        teachers920.add(litvinova);
        teachers920.add(litvinov);
        ArrayList<Subject> subjects27 = new ArrayList<>();
        subjects27.add(algorithms);
        subjects27.add(oop);
        groups173.add(ku11);
        lesson = getLesson(groups173, subjects27, teachers920, true, 2, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups174 = new ArrayList<>();
        groups174.add(ki11);
        lesson = getLesson(groups174, compOsnovi, moroz, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups175 = new ArrayList<>();
        groups175.add(kb11);
        lesson = getLesson(groups175, math, makarov, 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups176 = new ArrayList<>();
        groups176.add(ks11);
        groups176.add(ks13);
        groups176.add(kb12);
        groups176.add(ki11);
        groups176.add(ks12);
        groups176.add(kb11);
        groups176.add(ku11);
        lesson = getLesson(groups176, oopVl, vladimirova, 2, 1, 1, 1,false);
        lessons.add(lesson);

        ArrayList<Group> groups177 = new ArrayList<>();
        groups177.add(ks12);
        lesson = getLesson(groups177, anglCh, chervinko, 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups178 = new ArrayList<>();
        groups178.add(ks13);
        lesson = getLesson(groups178, physic, karas, 2, 1, 2,0 ,false);
        lessons.add(lesson);

        ArrayList<Group> groups179 = new ArrayList<>();
        groups179.add(ki11);
        lesson = getLesson(groups179, math, makarov, 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups180 = new ArrayList<>();
        groups180.add(kb11);
        lesson = getLesson(groups180,infTeh , moroz, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> gr_ks11_2m = new ArrayList<Group>();
        ArrayList<Teacher> teachers921 = new ArrayList<>();
        teachers921.add(chervinko);
        teachers921.add(sharun);
        ArrayList<Subject> subjects28 = new ArrayList<>();
        subjects28.add(anglCh);
        subjects28.add(anglShar);
        gr_ks11_2m.add(ks11);
        lessons.add(getLesson(gr_ks11_2m, subjects28, teachers921, true, 2, 1, 0,false));

        ArrayList<Group> gr_ks11_3m = new ArrayList<Group>();
        ArrayList<Teacher> teachers922 = new ArrayList<>();
        teachers922.add(bogucharski);
        teachers922.add(oleshko);
        ArrayList<Subject> subjects29 = new ArrayList<>();
        subjects29.add(algorithms);
        subjects29.add(oopOlechko);
        gr_ks11_3m.add(ks11);
        lessons.add(getLesson(gr_ks11_3m, subjects29, teachers922, true, 2, 1, 2,false));
//>

//KS_12<
        ArrayList<Group> gr_ks12_2m = new ArrayList<Group>();
        ArrayList<Teacher> teachers923 = new ArrayList<>();
        teachers923.add(bogucharski);
        teachers923.add(oleshko);
        ArrayList<Subject> subjects30 = new ArrayList<>();
        subjects30.add(algorithmsBogucharski);
        subjects30.add(oopOlechko);
        gr_ks12_2m.add(ks12);
        lessons.add(getLesson(gr_ks12_2m, subjects30, teachers923, true, 2, 1, 2,false));

        ArrayList<Group> gr_ks12_3m = new ArrayList<Group>();
        gr_ks12_3m.add(ks12);
        lessons.add(getLesson(gr_ks12_3m, math, makarov, 2, 1, 1, 0,false));
//>

//KS_13<
        ArrayList<Group> gr_ks13_2m = new ArrayList<Group>();
        gr_ks13_2m.add(ks13);
        lessons.add(getLesson(gr_ks13_2m, math, makarov, 2, 1, 1, 0,false));

        ArrayList<Group> gr_ks13_3m = new ArrayList<Group>();
        ArrayList<Teacher> teachers924 = new ArrayList<>();
        teachers924.add(turenko);
        teachers924.add(sharun);
        ArrayList<Subject> subjects31 = new ArrayList<>();
        subjects31.add(anglTu);
        subjects31.add(anglShar);
        gr_ks13_3m.add(ks13);
        lessons.add(getLesson(gr_ks13_3m, subjects31, teachers924,true, 2, 1, 0,false));
//>

//KY_11<
        ArrayList<Group> gr_ky11_1m = new ArrayList<Group>();
        gr_ky11_1m.add(ku11);
        lessons.add(getLesson(gr_ky11_1m, discreateMathKurinii, kurinii, 2, 1, 2, 0,false));

        ArrayList<Group> gr_ky11_2m = new ArrayList<Group>();
        gr_ky11_2m.add(ku11);
        lessons.add(getLesson(gr_ky11_2m, physicStrovoitov, starovoitov, 2, 1, 1, 0,false));

        ArrayList<Group> gr_ky11_ki11_kb11_kb12_3m = new ArrayList<Group>();
        gr_ky11_ki11_kb11_kb12_3m.add(ku11);
        gr_ky11_ki11_kb11_kb12_3m.add(ki11);
        // gr_ky11_ki11_kb11_kb12_3m.add(kb11);
        //gr_ky11_ki11_kb11_kb12_3m.add(kb12);
        lessons.add(getLesson(gr_ky11_ki11_kb11_kb12_3m, compOsnovi, moroz, 2, 1, 1, 1,false));

        ArrayList<Group> gr_ky11_4m = new ArrayList<Group>();
        gr_ky11_4m.add(ku11);
        lessons.add(getLesson(gr_ky11_4m, anglShar, sharun, 2, 1, 1, 0,false));
//>

//KI11<
        ArrayList<Group> gr_ki11_2m = new ArrayList<Group>();
        gr_ki11_2m.add(ki11);
        lessons.add(getLesson(gr_ki11_2m, discreateMathKurinii, kurinii, 2, 1, 2, 0,false));

        ArrayList<Group> gr_ki11_5m = new ArrayList<Group>();
        gr_ki11_5m.add(ki11);
        lessons.add(getLesson(gr_ki11_5m, anglShar, sharun, 2, 1, 1, 0,false));
//>

//KB11<
        ArrayList<Group> gr_kb11_2m = new ArrayList<Group>();
        gr_kb11_2m.add(kb11);
        lessons.add(getLesson(gr_kb11_2m, physicDenisenko, denisenko, 2, 1, 2, 0,false));

        ArrayList<Group> gr_kb11_4m = new ArrayList<Group>();
        ArrayList<Teacher> teachers64 = new ArrayList<>();
        teachers64.add(litvinova);
        teachers64.add(oleshko);
        ArrayList<Subject> subjects35 = new ArrayList<>();
        subjects35.add(algorithms);
        subjects35.add(oop);
        gr_kb11_4m.add(kb11);
        lessons.add(getLesson(gr_kb11_4m, subjects35, teachers64, true, 2, 1, 2,false));

        ArrayList<Group> gr_ki11_2m1 = new ArrayList<Group>();
        gr_ki11_2m1.add(kb11);
        lessons.add(getLesson(gr_ki11_2m1, physicStrovoitov, starovoitov , 2, 1, 2, 0,false));
//>

//KB12<
        ArrayList<Group> gr_kb12_1m = new ArrayList<Group>();
        gr_kb12_1m.add(kb12);
        lessons.add(getLesson(gr_kb12_1m, physicDenisenko, denisenko, 2, 1, 2, 0,false));

        ArrayList<Group> groups1811 = new ArrayList<>();
        ArrayList<Teacher> teachers631 = new ArrayList<>();
        teachers631.add(bogucharski);
        teachers631.add(litvinov);
        ArrayList<Subject> subjects11 = new ArrayList<>();
        subjects11.add(algorithms);
        subjects11.add(oop);
        groups1811.add(ki11);
        lesson = getLesson(groups1811, subjects11, teachers631, true, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> gr_kb12_4m = new ArrayList<Group>();
        gr_kb12_4m.add(kb12);
        lessons.add(getLesson(gr_kb12_4m, anglTu, turenko,2, 1, 1, 0,false));


        ArrayList<Group> groups181 = new ArrayList<>();
        ArrayList<Teacher> teachers63 = new ArrayList<>();
        teachers63.add(bogucharski);
        teachers63.add(litvinov);
        ArrayList<Subject> subjects1 = new ArrayList<>();
        subjects1.add(algorithms);
        subjects1.add(oop);
        groups181.add(kb12);
        lesson = getLesson(groups181, subjects1, teachers63, true, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups182 = new ArrayList<>();
        groups182.add(ki11);
        lesson = getLesson(groups182, oopOlechko,oleshko, 2, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups183 = new ArrayList<>();
        groups183.add(kb12);
        lesson = getLesson(groups183, anglCh, chervinko, 2, 1, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups185 = new ArrayList<>();
        groups185.add(ks11);
        lesson = getLesson(groups185, physic, karas, 2, 2, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups1812 = new ArrayList<>();
        ArrayList<Teacher> teachers633 = new ArrayList<>();
        teachers633.add(litvinova);
        teachers633.add(oleshko);
        ArrayList<Subject> subjects13 = new ArrayList<>();
        subjects13.add(algorithms);
        subjects13.add(oop);
        groups1812.add(ks12);
        lesson = getLesson(groups1812, subjects13, teachers633, true, 2, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups187 = new ArrayList<>();
        groups187.add(ki11);
        lesson = getLesson(groups187, algorithms, litvinova, 2, 1, 1, 2,false);
        lessons.add(lesson);


        ArrayList<Group> groups189 = new ArrayList<>();
        ArrayList<Teacher> teachers375 = new ArrayList<>();
        teachers375.add(chervinko);
        teachers375.add(sharun);
        ArrayList<Subject> subjects291 = new ArrayList<>();
        subjects291.add(anglCh);
        subjects291.add(anglShar);
        groups189.add(kb11);
        lesson = getLesson(groups189, subjects291, teachers375, true, 2, 1, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups190 = new ArrayList<>();
        groups190.add(kb12);
        lesson = getLesson(groups190,infTeh, moroz, 2, 1, 1, 2,true);
        lessons.add(lesson);


        ArrayList<Group> groups197 = new ArrayList<>();
        groups197.add(ks11);
        lesson = getLesson(groups197, ogti, zinoviev, 2, 1, 2,2,true);
        lessons.add(lesson);

        ArrayList<Group> groups198 = new ArrayList<>();
        groups198.add(ks13);
        lesson = getLesson(groups198, ogti, zinoviev, 2, 1, 2,2,true);
        lessons.add(lesson);

        ArrayList<Group> groups199 = new ArrayList<>();
        groups199.add(ks13);
        groups199.add(ks11);
        groups199.add(ks12);
        lesson = getLesson(groups199, ogti, zinoviev, 2, 1, 2,1,true);
        lessons.add(lesson);

        ArrayList<Group> groups169 = new ArrayList<>();
        groups169.add(ks12);
        lesson = getLesson(groups169, ogti, zinoviev, 2, 1, 2, 2,true);
        lessons.add(lesson);

        ArrayList<Group> groups667 = new ArrayList<Group>();
        groups667.add(kb11);
        lessons.add(getLesson(groups667, discreateMathKurinii, kurinii, 2, 1, 2, 0,false));

        ArrayList<Group> groups1814 = new ArrayList<>();
        ArrayList<Teacher> teachers6331 = new ArrayList<>();
        teachers6331.add(litvinova);
        teachers6331.add(litvinov);
        ArrayList<Subject> subjects1311 = new ArrayList<>();
        subjects1311.add(algorithms);
        subjects1311.add(oop);
        groups1814.add(ks11);
        lesson = getLesson(groups1814, subjects1311, teachers6331, true, 2, 1, 2,false);
        lessons.add(lesson);


        ArrayList<Group> groups1815 = new ArrayList<>();
        groups1815.add(ks11);
        groups1815.add(ks13);
        groups1815.add(kb12);
        groups1815.add(ki11);
        groups1815.add(ks12);
        groups1815.add(kb11);
        groups1815.add(ku11);
        lesson = getLesson(groups1815, physic, karas, 2, 1, 1, 1,false);
        lessons.add(lesson);

        ArrayList<Group> groups1816 = new ArrayList<>();
        ArrayList<Teacher> teachers644 = new ArrayList<>();
        teachers644.add(bogucharski);
        teachers644.add(litvinov);
        ArrayList<Subject> subjects1113 = new ArrayList<>();
        subjects1113.add(algorithms);
        subjects1113.add(oop);
        groups1816.add(ks13);
        lesson = getLesson(groups1816, subjects1113, teachers644, true, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups1817= new ArrayList<Group>();
        ArrayList<Teacher> teachers645 = new ArrayList<>();
        teachers645.add(bogucharski);
        teachers645.add(oleshko);
        ArrayList<Subject> subjects1111 = new ArrayList<>();
        subjects1111.add(algorithmsBogucharski);
        subjects1111.add(oopOlechko);
        groups1817.add(ku11);
        lessons.add(getLesson(groups1817, subjects1111, teachers645, true, 2, 1, 2,false));

        ArrayList<Group> groups1818 = new ArrayList<>();
        ArrayList<Teacher> teachers646 = new ArrayList<>();
        teachers646.add(litvinova);
        teachers646.add(litvinov);
        ArrayList<Subject> subjects1114 = new ArrayList<>();
        subjects1114.add(algorithms);
        subjects1114.add(oop);
        groups1818.add(ks13);
        lesson = getLesson(groups1818, subjects1114, teachers646, true, 1, 1, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups1819 = new ArrayList<>();
        groups1819.add(ks11);
        groups1819.add(ks13);
        groups1819.add(kb12);
        groups1819.add(ki11);
        groups1819.add(ks12);
        groups1819.add(kb11);
        groups1819.add(ku11);
        lesson = getLesson(groups1819, discreateMathKurinii, kurinii, 2, 1, 1, 1,false);
        lessons.add(lesson);

        ArrayList<Group> groups1820 = new ArrayList<>();
        groups1820.add(ks21);
        groups1820.add(ks22);
        groups1820.add(ku21);
        groups1820.add(kb21);
        groups1820.add(kb22);
        lesson = getLesson(groups1820, comp_graphicsKropotov , kropotov, 2, 1, 2, 1,false);
        lessons.add(lesson);

        ArrayList<Group> groups1821 = new ArrayList<>();
        groups1821.add(kb21);
        lesson = getLesson(groups1821, comp_graphicaOsip, osipchuk, 2, 1, 2,2,false);
        lessons.add(lesson);

        ArrayList<Group> groups1822 = new ArrayList<>();
        groups1822.add(ks22);
        lesson = getLesson(groups1822, comp_graphicaOsip, osipchuk, 2, 1, 2,2,false);
        lessons.add(lesson);

        ArrayList<Group> groups1823 = new ArrayList<>();
        groups1823.add(kb22);
        lesson = getLesson(groups1823, mathKab, kabalyants, 2, 1, 2,2,false);
        lessons.add(lesson);

        ArrayList<Group> groups1824 = new ArrayList<>();
        groups1824.add(ku21);
        lesson = getLesson(groups1824, mathKab, kabalyants, 2, 1, 2,2,false);
        lessons.add(lesson);


        ArrayList<Group> groups1827 = new ArrayList<>();
        groups1827.add(kb21);
        lesson = getLesson(groups1827,kpp, sporov, 2, 1, 2, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups1828 = new ArrayList<>();
        groups1828.add(ku21);
        lesson = getLesson(groups1828,kpp, sporov, 2, 1, 2, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups1829 = new ArrayList<>();
        groups1829.add(ks22);
        lesson = getLesson(groups1829,kpp, sporov, 2, 1, 2, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups1830 = new ArrayList<>();
        groups1830.add(ks22);
        lesson = getLesson(groups1830, compGraphicaDid, didenko, 2, 1, 2, 2,false);
        lessons.add(lesson);

        ArrayList<Group> groups1831 = new ArrayList<>();
        groups1831.add(kb22);
        lesson = getLesson(groups1831, ohranaTruda, vasileva, 2, 1, 2, 0,false);
        lessons.add(lesson);

        ArrayList<Group> groups1832 = new ArrayList<>();//tut bjd
        groups1832.add(ks21);
        groups1832.add(ks22);
        groups1832.add(ku21);
        groups1832.add(kb21);
        groups1832.add(kb22);
        lesson = getLesson(groups1832, ohranaTruda, vasileva, 2, 1, 2, 1,true);
        lessons.add(lesson);

        ArrayList<Group> groups1833 = new ArrayList<>();
        groups1833.add(ks21);
        lesson = getLesson(groups1833, bjdStar, staroselska, 2, 1, 2, 0,true);
        lessons.add(lesson);

        ArrayList<Group> groups1834 = new ArrayList<>();
        groups1834.add(kb31);
        lesson = getLesson(groups1834, tpi, pavlov , 2, 1, 2, 2,true);
        lessons.add(lesson);

        ArrayList<Group> groups1835 = new ArrayList<>();
        groups1835.add(kb31);
        lesson = getLesson(groups1835, zashita_v_info_kom_setyah, esin , 2, 1, 2, 0,true);
        lessons.add(lesson);


        ArrayList<Group> groups1836 = new ArrayList<>();
        groups1836.add(ks31);
        lesson = getLesson(groups1836, plazma, zinoviev , 2, 1, 1, 0,false);
        lessons.add(lesson);


        ArrayList<Group> groups1837 = new ArrayList<>();
        groups1837.add(ks31);
        lesson = getLesson(groups1837, plazma, zinoviev , 2, 1, 1, 0,false);
        lessons.add(lesson);



        ArrayList<Group> groups1838 = new ArrayList<>();
        groups1838.add(ku51);
        groups1838.add(ku52);
        lesson = getLesson(groups1838, up, berdnikov, 2, 1, 1, 1,false);
        lessons.add(lesson);



    }


    public void testFullAuditories() {
        ArrayList<Time> timeTmp = new ArrayList<>();
        ArrayList<Time> timeTmp1 = new ArrayList<>();
        ArrayList<Time> timeTmp2 = new ArrayList<>();
        ArrayList<Time> timeTmp3 = new ArrayList<>();
        ArrayList<Time> timeTmp4 = new ArrayList<>();
        ArrayList<Time> timeTmp5 = new ArrayList<>();
        ArrayList<Time> timeTmp6 = new ArrayList<>();
        ArrayList<Time> timeTmp7 = new ArrayList<>();
        ArrayList<Time> timeTmp8 = new ArrayList<>();
        ArrayList<Time> timeTmp9 = new ArrayList<>();
        ArrayList<Time> timeTmp10 = new ArrayList<>();
        ArrayList<Time> timeTmp11 = new ArrayList<>();
        ArrayList<Time> timeTmp12 = new ArrayList<>();
        ArrayList<Time> timeTmp13 = new ArrayList<>();
        ArrayList<Time> timeTmp14 = new ArrayList<>();
        ArrayList<Time> timeTmp15 = new ArrayList<>();
        ArrayList<Time> timeTmp16 = new ArrayList<>();
        ArrayList<Time> timeTmp17 = new ArrayList<>();
        ArrayList<Time> timeTmp18 = new ArrayList<>();
        ArrayList<Time> timeTmp19 = new ArrayList<>();

        Auditory auditory = getAuditory(1, 100, "519",timeTmp,false);
        auditories.add(auditory);
        Auditory auditory1 = getAuditory(2, 50, "558",timeTmp1,true);
        auditories.add(auditory1);
        Auditory auditory2 =getAuditory(0, 80, "527",timeTmp2,false);
        auditories.add(auditory2);
        Auditory auditory3 = getAuditory(3, 40, "215",timeTmp3,false);
        auditories.add(auditory3);
        Auditory auditory4 = getAuditory(2, 50, "317",timeTmp4,true);
        auditories.add(auditory4);
        Auditory auditory5 = getAuditory(2, 30, "540",timeTmp5,false);
        auditories.add(auditory5);
        Auditory auditory6 = getAuditory(1, 80, "526",timeTmp6,false);
        auditories.add(auditory6);
        Auditory auditory7 = getAuditory(0, 35, "554",timeTmp7,false);
        auditories.add(auditory7);
        Auditory auditory8 = getAuditory(1, 120, "542",timeTmp8,true);
        auditories.add(auditory8);
        Auditory auditory9 = getAuditory(1, 300, "715",timeTmp9,false);
        auditories.add(auditory9);
        Auditory auditory10 = getAuditory(2, 40, "536",timeTmp10,true);
        auditories.add(auditory10);
        Auditory auditory11 = getAuditory(2, 40, "535",timeTmp11,true);
        auditories.add(auditory11);
        Auditory auditory12 = getAuditory(0, 40, "538",timeTmp12,false);
        auditories.add(auditory12);
        Auditory auditory13 = getAuditory(1, 100, "541",timeTmp13,true);
        auditories.add(auditory13);
        Auditory auditory14 = getAuditory(2, 40, "316",timeTmp14,true);
        auditories.add(auditory14);
        Auditory auditory15 = getAuditory(0, 40, "552",timeTmp15,false);
        auditories.add(auditory15);
        Auditory auditory16 = getAuditory(0, 40, "320",timeTmp16,false);
        auditories.add(auditory16);
        Auditory auditory17 = getAuditory(0, 40, "318",timeTmp17,false);
        auditories.add(auditory17);
        Auditory auditory18 = getAuditory(0, 40, "521",timeTmp18,false);
        auditories.add(auditory18);
        Auditory auditory19 = getAuditory(0, 30, "523",timeTmp19,false);
        auditories.add(auditory19);

    }

    public void fullAll() {
        testFullLesson();
        testFullAuditories();

    }

}
