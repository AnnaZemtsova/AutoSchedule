package allowableSchedule;

import additional.BackAlgorithm;
import data.*;

import java.util.ArrayList;

/**
 * Created by zemtsovaanna on 04.04.17.
 */
public class Schedule {
    private  ArrayList<Time> times;
    private  int[][] finishAuditories;
    private  ArrayList<Lesson> lessons;
    private  int[] triedAuditories;
    private  ArrayList<Auditory> auditories;
    private  ObjectsForLesson objects;
    public static  int count=0;
    /*
           CONSTRUCTOR & GET & SET
     */
    //------------------------------------------------------------------------

    public Schedule(){
        times = new ArrayList<>();
        lessons = new ArrayList<>();
        auditories = new ArrayList<>();
        objects = new ObjectsForLesson();
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

    public ArrayList<Time> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Time> times) {
        this.times = times;
    }

    public int[][] getFinishAuditories(){
        return  finishAuditories;
    }

    public void setTime(int i,Time time){
        times.set(i,time);
    }

    public void addTime(Time time){
        times.add(time);
    }

    public void addLesson(Lesson lesson){
        lessons.add(lesson);
    }

    public void setFinishAuditory(int i,int[]a){
        finishAuditories[i]=a;
    }

    public void setFinishAuditories(int[][] finishAuditories){
        this.finishAuditories = finishAuditories;
    }

    public void setLesson(int i,Lesson lesson){
        lessons.set(i,lesson);
    }


    //------------------------------------------------------------------------

    /*
           Работа со степенями свободы
     */


    //------------------------------------------------------------------------

    private double getDegreeOfFreedom(Lesson lesson) { // ]
        double result = 0;
        if (getAmountOfStudents(lesson) > 30) result += 1;
        if (getAmountOfStudents(lesson) > 60) result += 1;
        if (getAmountOfStudents(lesson) > 110) result += 1;
        if (getAmountOfStudents(lesson) > 150) result += 1;
        if (lesson.getCodeAuditory() == 1) result += 1;
        if (lesson.getCodeAuditory() == 2) result += 2;
        for (int i = 0; i < lesson.getTeachers().size(); i++) {
            result += lesson.getTeachers().get(i).getTimes().size();
            if(lesson.getTeachers().get(i).getTimes().size()>15) result+=10;
        }

        if(lesson.getIntensity()==2) result+=20;
        if(lesson.isNeedMultimedia())result+=5;
        return result;
    }
    /*
       Сортирует массив по степеням свободы. Те которые расставить сложнее всего окажутся в начале массива
       (лекции, занятия требующие компьютерных аудторий и тд)
     */

    public void sortForFreedom() { //необходимо упростить!!!!! НАЙТИ сначала все степени свободы, а потом сортировать!!!
        for (int i = 0; i < lessons.size(); i++) {
            for (int j = i + 1; j < lessons.size(); j++) {
                if (getDegreeOfFreedom(lessons.get(j)) > getDegreeOfFreedom(lessons.get(i))) {
                    Lesson tmp = lessons.get(i);
                    lessons.set(i, lessons.get(j));
                    lessons.set(j, tmp);
                }
            }
        }
    }


    //------------------------------------------------------------------------
    /*
         Формирует все занятия в строку
     */

    public String allLessonsToString() {
        String result = "";
        for (Lesson lesson : lessons) {
            result += objects.toString(lesson);
        }
        return result;
    }

    //------------------------------------------------------------------------
    //------------------------------------------------------------------------
    //------------------------------------------------------------------------
    //------------------------------------------------------------------------

    /*
      Метод возвращает количество расставленых занятий
     */

    private   int amountAlingmentLessons(){
        int amount =0;
        for (Lesson lesson : lessons) {
            if (lesson.isSet()) {
                amount++;
            }
        }
        return  amount;
    }


    //------------------------------------------------------------------------

    /*
     удалить если все ок!
     */
    public boolean notSetYet(int i){
        return !lessons.get(i).isSet();
    }



    //------------------------------------------------------------------------
    //------------------------------------------------------------------------
    //------------------------------------------------------------------------



    /*
        Этот метод проверяет доступна ли аудитория по следующему принципу:
        Если тип совпадает с требуемым, все студенты помещаются, и оставшееся кол-во мест
        не слишком большое, тогда считаем, что аудитория доступна.
        ИЛИ если нужна лекционная аудитория, проверяемая практическая, и все студенты помещаются,
        то считаем что аудитория доступна.
        ИЛИ если нужна практическая аудитория, а проверяемая лекционная и ее емкость не слишком большая
        мы можем провести там практику.
        ИЛИ если требуемый тип практический, а проверяемый компьютерный, то если кол-во мест НЕ компьютерных!
        достаточно, тоже можем провести практику.

     */

    public boolean isAvailable(int needType, int typeAuditory, int amountOfStudents, int capacity,
                               int capacityForComputer,
                               boolean isNeedMultimedia,boolean isMultimedia) {
        if(isNeedMultimedia) {
            return isMultimedia &&
                    (((needType == typeAuditory) && (amountOfStudents <= capacity) && (capacity - amountOfStudents < 150)) ||
                            ((needType == 1) && (typeAuditory == 0) && (amountOfStudents <= capacity)) ||
                            ((needType == 0) && (typeAuditory == 1) && (capacity <= 90)) ||
                            (needType == 2) && (typeAuditory == 2) && (amountOfStudents <= (capacity + capacityForComputer)) ||
                            (needType == 0) && (typeAuditory == 2) && (amountOfStudents <= capacity));
        }
        return ((needType == typeAuditory) && (amountOfStudents <= capacity) && (capacity - amountOfStudents < 150)) ||
                ((needType == 1) && (typeAuditory == 0) && (amountOfStudents <= capacity)) ||
                ((needType == 0) && (typeAuditory == 1) && (capacity <= 90)) ||
                (needType == 2) && (typeAuditory == 2) && (amountOfStudents <= (capacity + capacityForComputer)) ||
                (needType == 0) && (typeAuditory == 2) && (amountOfStudents <= capacity);
    }

    //------------------------------------------------------------------------

    /*
        Возвращает количество студентов, участвующих в занятии
        (сумма студентов всех групп, участвующих в занятии)
     */

    public int getAmountOfStudents(Lesson lesson) {
        int result = 0;
        for (int i = 0; i < lesson.getGroups().size(); i++) {
            result += lesson.getGroups().get(i).getAmountOfStudents();
        }
        return result;
    }


    //==================================================================================================================
    //==================================================================================================================
    //==================================================================================================================
    //==================================================================================================================
    /*
       В этом блоке делается проверка на свободность всех сущностей (аудитории , группы, преподаватели) в выбранное время
     */

    //------------------------------------------------------------------------

     /*
       isFreeTeacher - проверка отсутствия накладок для преподавателей
     */

    private boolean isFreeTeacher(Teacher teacher, int[] time) {
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

    private boolean isFreeGroup(Group group, int[] time) {
        for (int i = 0; i < group.getTimes().size(); i++) {
            if ((group.getTimes().get(i).getDay() == time[0]) &&
                    (group.getTimes().get(i).getLesson() == time[1])) {
                return false;
            }
        }
        return true;
    }

  /*  private boolean isFreeGroup(Group group, int[] time) {
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getLesson() == 0)
                break;
            for (int j = 0; j < lessons.get(i).getGroups().size(); j++) {
                if ((lessons.get(i).getGroups().get(j).getName().equals(group.getName()))) {
                    if ((time[0] == times.get(i).getDay())
                            && (time[1] == times.get(i).getLesson())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
*/
    //------------------------------------------------------------------------
     /*
          isFreeGroups - проверка отсутствия накладок для групп
     */

    private boolean isFreeGroups(ArrayList<Group> groups, int[] time) {
        for (Group group : groups) {
            if (!isFreeGroup(group, time)) {
                return false;
            }
        }
        return true;
    }

    //------------------------------------------------------------------------
     /*
          isFreeGroups - проверка отсутствия накладок для преподавателей
     */

    private boolean isFreeTeachers(ArrayList<Teacher> teachers, int[] time) {
        for (Teacher teacher : teachers) {
            if (!isFreeTeacher(teacher, time)) {
                return false;
            }
        }
        return true;
    }
//------------------------------------------------------------------------
    /*
       isFreeAuditory - проверка отсутствия накладок для аудиторий
     */

    private boolean isFreeAuditory(int auditory, int[]time) {
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

    private  boolean isFreeAuditories(int[] auditories, int[] time) {
        for (int i = 0; i < 2; i++) {
            if(auditories[i]>=0)
                if (!isFreeAuditory(auditories[i], time)) {
                    return false;
                }
        }
        return true;
    }

    //------------------------------------------------------------------------

    /*
       Проверяем если преподаватели во время time имеют занятия - мигалку, причем одно, то считаем,
       что преподаватели свободны для установления занятия - мигалки
     */

    private boolean isFreeTeachersForFlashes(ArrayList<Teacher> teachers, int[] time) {
        for (Teacher teacher : teachers) {
            if (!isFreeTeacherForFlashes(teacher, time)) return false;
        }
        return true;
    }

    //------------------------------------------------------------------------

    /*
       Проверяем если преподаватель во время time имеет занятие - мигалку, причем одно, то считаем,
       что преподаватель свободен для установления занятия - мигалки
     */
    //true - если найдена 1 мигалка, false - в другом случае


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
        return count == 1;
    }

    //------------------------------------------------------------------------

    /*
       Проверяем если аудитории во время time имеют занятие - мигалкe, причем одно, то считаем,
       что аудитория свободна для установления занятия - мигалки
     */

    private boolean isFreeAuditoriesForFlashes(int[] auditories, int[] time) {
        for (int auditory : auditories) {
            if (!isFreeAuditoryForFlashes(auditory, time)) return false;
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
        if(a==-1) return false; //false/true?
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getLesson() == time[1] && times.get(i).getDay() == time[0]) {
                for (int j = 0; j < 2; j++) {
                    if ((finishAuditories[i][j] == a)) {
                        if (lessons.get(i).getIntensity() == 2) {
                            count++;
                            if (count > 1) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return count == 1;
    }


    //------------------------------------------------------------------------

    /*
       Проверяем если группы во время time имеют занятия - мигалку, причем одно, то считаем,
       что группы свободны для установления занятия - мигалки
     */

    private boolean isFreeGroupsForFlashes(ArrayList<Group> groups, int[] time) {
        for (Group group : groups) {
            if (!isFreeGroupForFlashes(group, time)) return false;
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
        return count == 1;
    }

    //------------------------------------------------------------------------

    /*
      Проверяет свобдны ли преподаватели, группы и аудитории для данного времени
     */

    private boolean isFree(int[] time, int i,int [] a) {
        ArrayList<Teacher> teachers = lessons.get(i).getTeachers();
        ArrayList<Group> groups = lessons.get(i).getGroups();
        /*
          Если это занятие мигалка - смотрим, нет ли установленной мигалки на даное время.
               Если есть, и группа в это время свободна, ставим туда пару
               Если нет, как обычно ставим занятие
          Иначе как обычно выбираем занятие
         */
        if (lessons.get(i).getIntensity() == 2) {
            if (((isFreeTeachersForFlashes(teachers, time) || isFreeTeachers(teachers, time)) &&
                    (isFreeAuditoriesForFlashes(a, time) || isFreeAuditories(a, time)) &&
                    (isFreeGroupsForFlashes(groups, time) || isFreeGroups(groups, time)))) {
                return true;
            }
        } else
        if ((isFreeAuditories(a, time)) &&
                (isFreeGroups(groups, time)) &&
                (isFreeTeachers(teachers, time))) {
            return true;
        }
        return false;
    }
/*
((isFreeAuditories(a, time)) &&          // или делаем проверку как обычно
                    (isFreeGroups(groups, time)) &&
                    (isFreeTeachers(teachers, time))) ||
 */



    //==================================================================================================================
    //==================================================================================================================
    //==================================================================================================================
    //==================================================================================================================

    //------------------------------------------------------------------------

    /*
       Поиск времени для мигалки. (Поиск времени, в которое есть мигалка у опредленной группы/групп
     */

    private Object searchFlasher(int j, Object obj) {
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
                        else {
                            flag = false;
                            break;
                        }
                    }
                    int [] tmp = new int[2];
                    tmp[0]=times.get(i).getDay();
                    tmp[1]=times.get(i).getLesson();


                    if (flag&&isAvailable(lessons.get(j).getCodeAuditory(),auditories.get(finishAuditories[i][0]).getType(),
                            getAmountOfStudents(lessons.get(j)),auditories.get(finishAuditories[i][0]).getCapacity(),
                            auditories.get(finishAuditories[i][0]).getCapacityForComputer(),
                            lessons.get(j).isNeedMultimedia(),
                            auditories.get(finishAuditories[i][0]).isMultimedia())&&
                            isFree(tmp,j,finishAuditories[i])){

                        if(obj instanceof Time) {
                            obj = times.get(i);
                            return obj;
                        }
                        if((obj instanceof int[])){
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

        flag = false;
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getDay() == 0) {
                return obj;
            }
            if (i != j) {
                if (lessons.get(i).getIntensity() == 2) {
                    for (int k = 0; k < lessons.get(j).getGroups().size(); k++) {
                        flag = false;
                        for (int b = 0; b < lessons.get(i).getGroups().size(); b++) {
                            if (lessons.get(j).getGroups().get(k) == lessons.get(i).getGroups().get(b)) {
                                flag=true;
                            }
                            if(flag)break;
                        }
                    }

                    int [] tmp = new int[2];
                    tmp[0]=times.get(i).getDay();
                    tmp[1]=times.get(i).getLesson();

                    if(flag&&isAvailable(lessons.get(j).getCodeAuditory(),auditories.get(finishAuditories[i][0]).getType(),
                            getAmountOfStudents(lessons.get(j)),auditories.get(finishAuditories[i][0]).getCapacity(),
                            auditories.get(finishAuditories[i][0]).getCapacityForComputer(), lessons.get(j).isNeedMultimedia(),
                            auditories.get(finishAuditories[i][0]).isMultimedia())&&isFree(tmp,j,finishAuditories[i])) {
                        if (obj instanceof Time) {
                            obj = times.get(i);
                            return obj;
                        }
                        if (obj instanceof int[]) {
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

    /*
        Метод подбирает аудиторию для пары, где группа разделена. Возвращает индекс аудитории, которая доступна,
        и свободна или -1 , если не найдено ничего подходящего
        а - индекс аудитории, которая поставлена для первой подгруппы
        x - индекс занятия, для которого подбираем вторую аудиторию
     */

    private int chooseAuditoryForHalfPair(int a,int codeAuditory,int amountOfStudents,int[]time,int x,boolean isNeedMultimedia){
        for(int i=0;i<auditories.size();i++){
            if(i!=a) {
                if(lessons.get(x).getIntensity()==2){
                    if (isAvailable(codeAuditory, auditories.get(i).getType(), amountOfStudents,
                            auditories.get(i).getCapacity(), auditories.get(i).getCapacityForComputer(),
                            isNeedMultimedia,auditories.get(i).isMultimedia())&&
                            (isFreeAuditory(i, time)||isFreeAuditoryForFlashes(i,time)))
                        return i;
                }else
                if (isAvailable(codeAuditory, auditories.get(i).getType(), amountOfStudents,
                        auditories.get(i).getCapacity(), auditories.get(i).getCapacityForComputer(),isNeedMultimedia,
                        auditories.get(i).isMultimedia())&&
                        (isFreeAuditory(i, time)))
                    return i;
            }
        }
        return -1;
    }
    //==================================================================================================================
    //==================================================================================================================

    /*
          ГЛАВНАЯ ЧАСТЬ АЛГОРИТМА!!! ПОДБОР ЗАНЯТИЙ ДЛЯ ОПРЕДЛЕННОГО ВРЕМЕНИ И АУДИТОИИ
     */

    //------------------------------------------------------------------------
    /*
      Дополнительный метод. Создает массив размера length, заполняя его -1
     */
    private int[] getVoidArray(int length){
        int[] tried=new int[length];
        for(int i=0;i<length;i++){
            tried[i]=-1;
        }
        return tried;
    }

    //------------------------------------------------------------------------

    /*
         устанавливает для занятия i время  time, аудтории а
     */

    private void defineLesson(int[] tmp,int[] a,int i){
        Time time = new Time();
        time.setDay(tmp[0]);
        time.setLesson(tmp[1]);
        for(int j=0;j<lessons.get(i).getTeachers().size();j++){
            lessons.get(i).getTeachers().get(j).addTime(time);
        }
        auditories.get(a[0]).addTime(time);
        for(int j=0;j<lessons.get(i).getGroups().size();j++){
            lessons.get(i).getGroups().get(j).addTime(time);
        }
        finishAuditories[i][0]=a[0];
        if(a[1]!=-1) {
            auditories.get(a[1]).addTime(time);
            finishAuditories[i][1]=a[1];
        }
        lessons.get(i).setSet(true);
        times.set(i,time);
    }

    //------------------------------------------------------------------------

    /*
       Для дня d, пары l, аудитории а выбираем занятие, которое можем поставить
       Если занятие мигалка, пытаемся ставить не d,l,a а найти уже поставленную мигалку
       и в то же время поставить текущую
     */

    private void chooseAllPossibleLessons(int d,int l,int a) {
        boolean isSetForCurrentAuditory = false;
        int[] time = new int[2];
        time[0] = d;
        time[1] = l;
        int[] aud = new int[2];
        aud[0] = a;
        aud[1] = -1;
        for (int i = 0; i < lessons.size(); i++) {
            if (!lessons.get(i).isSet()){
                boolean isSetFlashes = false;
                if(lessons.get(i).getIntensity()==2) {
                    Time tmpTime = new Time();
                    int[] tmpAud = getVoidArray(2);
                    tmpAud = (int[]) searchFlasher(i, tmpAud);
                    if (tmpAud[0] != -1) {
                        tmpTime = (Time) searchFlasher(i, tmpTime);
                        if (tmpTime.getLesson() != 0) {
                            isSetFlashes = true;
                            int[] timeRes = getVoidArray(2);
                            timeRes[0] = tmpTime.getDay();
                            timeRes[1] = tmpTime.getLesson();
                            defineLesson(timeRes, tmpAud, i);
                        }
                    }
                }
                if(!isSetFlashes){
                    if (isAvailable(lessons.get(i).getCodeAuditory(),
                            auditories.get(a).getType(), getAmountOfStudents(lessons.get(i)),
                            auditories.get(a).getCapacity(), auditories.get(a).getCapacityForComputer(),
                            lessons.get(i).isNeedMultimedia(),auditories.get(a).isMultimedia())&&isFree(time, i, aud) )
                        isSetForCurrentAuditory = true;
                    if (lessons.get(i).getHalfPair() && isSetForCurrentAuditory) {
                        int a2 = chooseAuditoryForHalfPair(aud[0], lessons.get(i).getCodeAuditory(),
                                getAmountOfStudents(lessons.get(i)), time, i,lessons.get(i).isNeedMultimedia());
                        if (a2 == -1) isSetForCurrentAuditory = false;
                        else aud[1] = a2;
                    }
                    if (isSetForCurrentAuditory)
                        defineLesson(time, aud, i);
                }
            }
            if(isSetForCurrentAuditory) break;
        }
    }

    //------------------------------------------------------------------------

    /*
     Для каждой аудитории, дня d, пары l пробуем выбрать все возможные занятия
     */
    private void chooseAuditory(AdditionalMethods additionalMethods,int d, int l) {
        triedAuditories = getVoidArray(auditories.size());
        for(int i=0;i<auditories.size();i++){
            int a = additionalMethods.getA(auditories,triedAuditories);
            triedAuditories[i]=a;
            chooseAllPossibleLessons(d,l,a);
        }
    }

    //------------------------------------------------------------------------

    /*
      Для всех возможных времен с приоритето 2,3-4-1-5 пробуем все возможные аудитории и занятия
     */

    private void chooseTime(){
        int l;
        AdditionalMethods additionalMethods = new AdditionalMethods();
        int[] triedDays = getVoidArray(5);
        for(int i=0;i<5;i++){
            int[] triedLesson = getVoidArray(2);
            int d = additionalMethods.getDT(triedDays,5,1);
            triedDays[i]=d;
            for(int j=0;j<2;j++){
                l = additionalMethods.getDT(triedLesson,2,2);
                triedLesson[j]=l;
                chooseAuditory(additionalMethods,d,l);
            }
        }

        if(amountAlingmentLessons()<lessons.size()) {
            triedDays = getVoidArray(5);
            for (int i = 0; i < 5; i++) {
                int d = additionalMethods.getDT(triedDays, 5, 1);
                triedDays[i] = d;
                l = 4;
                chooseAuditory(additionalMethods,d, l);
            }
        }

        if(amountAlingmentLessons()<lessons.size()) {
            triedDays = getVoidArray(5);
            for (int i = 0; i < 5; i++) {
                int d = additionalMethods.getDT(triedDays, 5, 1);
                triedDays[i] = d;
                l = 1;
                chooseAuditory(additionalMethods,d, l);
            }
        }

        if(amountAlingmentLessons()<lessons.size()){
            triedDays = getVoidArray(5);
            for(int i=0;i<5;i++) {
                int d = additionalMethods.getDT(triedDays, 5, 1);
                triedDays[i] = d;
                l = 5;
                chooseAuditory(additionalMethods,d, l);
            }
        }

        if(amountAlingmentLessons()<lessons.size()){
            System.out.println("ERROR");
        }
    }

    //------------------------------------------------------------------------

    /*
     Главный метод. Создает расписание. Для всех занятий подбор времени и аудиторий
     */

    public void makeShedule(){
        finishAuditories = new int[lessons.size()][2];
        for(int i=0;i<lessons.size();i++){
            times.add(new Time());
            for(int j=0;j<2;j++){
                finishAuditories[i][j]=-1;
            }
        }
        chooseTime();
    }

    //==================================================================================================================
    //==================================================================================================================



    public void printSchedule(Schedule schedule){
        System.out.println(schedule.getLessons().size());
        for (int i = 0; i < schedule.getLessons().size(); i++) {
            if(schedule.getLessons().get(i).getGroups().size()==0)break;
            count++;
            System.out.println(i + ".");
            if(schedule.getLessons().get(i).isNeedMultimedia()) System.out.println("Need multimedia");
            if (schedule.getLessons().get(i).getIntensity() == 2)
                System.out.println("Migalka");
            for (int j = 0; j < schedule.getLessons().get(i).getSubjects().size(); j++) {
                System.out.println(schedule.getLessons().get(i).getSubjects().get(j).getName());
                System.out.println(schedule.getLessons().get(i).getTeachers().get(j).getName());
            }
            for (int j = 0; j < schedule.getLessons().get(i).getGroups().size(); j++) {
                System.out.println(schedule.getLessons().get(i).getGroups().get(j).getName() + " ");
            }
            for (int j = 0; j < 2; j++)
                if (schedule.getFinishAuditories()[i][j] >= 0)
                    System.out.println(schedule.getAuditories().get(schedule.getFinishAuditories()[i][j]).getName());
            System.out.println(schedule.getTimes().get(i).getDay() + " " +
                    schedule.getTimes().get(i).getLesson());
            System.out.println("---------------------------------------------");
        }
    }

    //==================================================================================================================

    public  Schedule getSchedule(){
        BackAlgorithm backAlgorithm = new BackAlgorithm();
        backAlgorithm.fullAll();
        backAlgorithm.sortForFreedom();
        Schedule schedule = new Schedule();
        schedule.setLessons(backAlgorithm.getLessons());
        schedule.setAuditories(backAlgorithm.getAuditories());
        schedule.makeShedule();
        return schedule;
    }
    //===============================================================================================================
    public static void main(String []args) {
        BackAlgorithm backAlgorithm = new BackAlgorithm();
        backAlgorithm.fullAll();
        backAlgorithm.sortForFreedom();
        Schedule schedule = new Schedule();
        schedule.setLessons(backAlgorithm.getLessons());
        schedule.setAuditories(backAlgorithm.getAuditories());
        schedule.makeShedule();
        schedule.printSchedule(schedule);
    }
}


