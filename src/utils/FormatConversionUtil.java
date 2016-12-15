package utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/6.
 */
public class FormatConversionUtil {

    public static Date getDateFromString(String dateString){
        Calendar cal = Calendar.getInstance();
        String[] times = dateString.trim().split(" ");
        String[] yearMD = times[0].split("-");
        String[] hourMS = times[1].split(":");
        int[] time = new int[6];
        time[0] = Integer.valueOf(yearMD[0]);
        time[1] = Integer.valueOf(yearMD[1]);
        time[2] = Integer.valueOf(yearMD[2]);
        time[3] = Integer.valueOf(hourMS[0]);
        time[4] = Integer.valueOf(hourMS[1]);
        time[5] = Integer.valueOf(hourMS[2]);
        cal.set(time[0],time[1],time[2],time[3],time[4],time[5]);
        return cal.getTime();
    }

    public static int getInternalDaysOfTwo(Date fDate, Date oDate) {

        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }
}
