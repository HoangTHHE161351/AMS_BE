package vn.attendance.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class LocalDateTimeUtil {
    private static final Logger log = LoggerFactory.getLogger(LocalDateTimeUtil.class);

    public static LocalDate parse(String time, String pattern) {
        if (StringUtils.isEmpty(time))
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(time, formatter);
    }

    public static LocalDate parse(String time) {
        if (StringUtils.isEmpty(time))
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.COMMON.patternDate);
        return LocalDate.parse(time, formatter);
    }

    public static long getTimeFromString(String time, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(time).getTime();
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    public static Date sqlDateFromString(String time, String pattern) {
        if (StringUtils.isEmpty(pattern))
            pattern = Constants.COMMON.patternDate;
        if (StringUtils.isEmpty(time))
            return null;
        return new Date(getTimeFromString(time, pattern));
    }

    public static java.util.Date dateFromString(String time, String pattern) {
        if (StringUtils.isEmpty(pattern))
            pattern = Constants.COMMON.patternDate;
        if (StringUtils.isEmpty(time))
            return null;
        return new java.util.Date(getTimeFromString(time, pattern));
    }

    public static Timestamp sqlTimestampFromString(String time) {
        if (StringUtils.isEmpty(time))
            return null;

        return Timestamp.valueOf(time);
    }

    public static Integer parseIntFromString(String param) {
        if (StringUtils.isEmpty(param))
            return null;
        return Integer.parseInt(param);
    }

    public static Long parseLongFromString(String param) {
        if (StringUtils.isEmpty(param))
            return null;
        return Long.parseLong(param);
    }

    public static String getStringFromSqlDate(Date date) {
        if (null == date)
            return null;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(date);
    }

    public static String getStringFromDate(java.util.Date date) {
        if (null == date)
            return null;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(date);
    }

    public static String getStringFromDate(java.util.Date date, String pattern) {
        if (null == date)
            return null;
        if (StringUtils.isEmpty(pattern)) {
            pattern = Constants.COMMON.patternDate;
        }
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static java.util.Date addDay(java.util.Date pDate, int pintDay) {
        Calendar vcal = Calendar.getInstance();
        vcal.setTime(pDate);
        vcal.add(Calendar.DAY_OF_MONTH, pintDay);
        return vcal.getTime();
    }

    public static java.util.Date addMonth(java.util.Date pDate, int pintMonth) {
        Calendar vcal = Calendar.getInstance();
        vcal.setTime(pDate);
        vcal.add(Calendar.MONTH, pintMonth);
        return vcal.getTime();
    }

    public static java.util.Date addYear(java.util.Date pDate, int pintYear) {
        Calendar vcal = Calendar.getInstance();
        vcal.setTime(pDate);
        vcal.add(Calendar.YEAR, pintYear);
        return vcal.getTime();
    }

    public static String newDate(java.util.Date date, String pattern) {
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        return fmt.format(date);
    }

    public static java.util.Date addHoursToJavaUtilDate(java.util.Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static java.util.Date addMinuteToJavaUtilDate(java.util.Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public static java.util.Date setTimeToJavaUtilDate(java.util.Date date, int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
}
