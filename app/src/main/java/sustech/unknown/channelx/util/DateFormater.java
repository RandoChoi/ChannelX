package sustech.unknown.channelx.util;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dahao on 2017/12/20.
 */

public class DateFormater {

    public static String dateToString(int year, int month, int mday) {
        if (year > 2200) {
            return "NEVER EXPIRED";
        }
        return (year + "-" + (month + 1) + "-" + mday);
    }

    public static String calendarToString() {
        Calendar calendar = Calendar.getInstance();
        return calendarToString(calendar);
    }

    public static String calendarToString(Calendar calendar) {
        return  dateToString(
                calendar.get(calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH));
    }

    public static String longToString(Long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return calendarToString(c);
    }

    public static int getMinuteInDay() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return hour * 60 + minute;
    }

    public static String minuteOfDayToString(int minuteOfDay) {
        int hourOfDay = minuteOfDay / 60;
        int minute = minuteOfDay % 60;
        return String.format(Locale.getDefault(),
                "%02d:%02d", hourOfDay, minute);
    }

}
