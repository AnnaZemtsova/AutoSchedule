package allowableSchedule;

import data.*;

import java.util.ArrayList;

/**
 * Created by zemtsovaanna on 04.04.17.
 */
public class AdditionalMethods {


    //------------------------------------------------------------------------

    /*
      Было ли попрбовано j (есть ли оно в массиве попробованных индексов)
     */


    private boolean wasTried(int j, int[] tried) {
        for (int i = 0; i < tried.length; i++) {
            if (j == tried[i]) {
                return true;
            }
        }
        return false;
    }


    //------------------------------------------------------------------------

    /*
       Возвращает число  в диапазоне от max до  min, которого нет в triedTime(еще не было попробовано)
     */


    public int getDT(int[] triedTime, int max, int min) {
        if (triedTime[triedTime.length - 1] != -1) {
            return -1;
        }
        int a = min + (int) (Math.random() * max);
        while (wasTried(a, triedTime)) {
            a = min + (int) (Math.random() * max);
        }
        return a;
    }

    //------------------------------------------------------------------------

    /*
      Возвращает true если  уже пытались поставить пару  в каждую из допустимых аудиторий,
      false - если есть аудитории, которы еще не были проверены
     */

    private boolean isAllAuditoriesWasTried(ArrayList<Auditory> auditories, int[] triedAuditories) { //необходимо проверить!!
        boolean isPresent = false;
        for (int i = 0; i < auditories.size(); i++) {
            isPresent = false;
            for (int j = 0; j < triedAuditories.length; j++) {
                if (triedAuditories[j] == i) {
                    isPresent = true;
                }
            }
            if (!isPresent)
                return false;
        }
        return isPresent;
    }


    //------------------------------------------------------------------------

    /*
        Возвращает индекс аудитории, которая еще не была попробована для текущего времени
        (triedAuditories обновляются при каждом изменении времени)
     */


    public int getA(ArrayList<Auditory> auditories, int[] triedAuditories) { //Exeption только для тестирования!!! Удалить позже!!!!
        int j = (int) (Math.random() * ((auditories.size() - 1) + 1));
        while (wasTried(j, triedAuditories)) {
            j = (int) (Math.random() * ((auditories.size() - 1) + 1));
            if (isAllAuditoriesWasTried(auditories, triedAuditories)
                    && wasTried(j, triedAuditories)) {
                try {
                    throw new Exception("All auditories for this time was Tried! Choose other time!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                j = -1;
                break;
            }
        }
        return j;
    }


    //------------------------------------------------------------------------

}
