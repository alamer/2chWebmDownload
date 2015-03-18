/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.jeene.chwebm.worker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Работа с датой
 *
 * @author scherb
 */
public class DateUtils {

    /**
     * Вытаскиваем текущую дату строкой
     *
     * @return Текущая дата строкой
     */
    public static String CurrentDate() {
        String date_str = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy_MM_dd");
        cal.setTimeInMillis(System.currentTimeMillis());
        date_str = sd.format(cal.getTime());

        return date_str;
    }

//  Letter   Date or Time Component   Presentation       Examples
//G        Era designator             Text                AD
//y        Year                       Year                1996;    96
//M        Month in year               Month               July; Jul; 07
//w        Week in year               Number               27
//W        Week in month               Number               2
//D        Day in year               Number               189
//d        Day in month               Number               10
//F        Day of week in month       Number               2
//E        Day in week               Text               Tuesday; Tue
//a        Am/pm marker               Text               PM
//H        Hour in day (0-23)       Number               0
//k        Hour in day (1-24)       Number               24
//K        Hour in am/pm (0-11)       Number               0
//h        Hour in am/pm (1-12)       Number               12
//m        Minute in hour           Number               30
//s        Second in minute           Number               55
//S        Millisecond                Number               978
//z        Time zone                   General time zone   Pacific Standard Time; PST; GMT-08:00
//Z        Time zone                  RFC 822 time zone   -0800
    /**
     * Текущая дата строкой в необходимом формате
     *
     * @param format Необходимый формат даты вида ddmmyyyy
     * @return Текущая дата строкой
     */
    public static String CurrentDate(String format) {
        String date_str = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat(format);
        cal.setTimeInMillis(System.currentTimeMillis());
        date_str = sd.format(cal.getTime());

        return date_str;
    }

    /**
     * Прошлый месяц дата строкой в необходимом формате
     *
     * @param format Необходимый формат даты вида ddmmyyyy
     * @return прошлый месяц дата строкой
     */
    public static String PrevMonthDate(String format) {
        String date_str = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat(format);
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.MONTH, -1);
        date_str = sd.format(cal.getTime());
        return date_str;
    }

    /**
     * Прошлый день дата строкой в необходимом формате
     *
     * @param format Необходимый формат даты вида ddmmyyyy
     * @return прошлый день дата строкой
     */
    public static String PrevDayDate(String format) {
        String date_str = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat(format);
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        date_str = sd.format(cal.getTime());
        return date_str;
    }

    /**
     * Преобразует дату в строку в необходимом формате
     *
     * @param cal Дата
     * @param format Необходимый формат даты вида ddmmyyyy
     * @return Текущая дата строкой
     */
    public static String DateToString(Calendar cal, String format) {
        String date_str = "";
        SimpleDateFormat sd = new SimpleDateFormat(format);
        date_str = sd.format(cal.getTime());
        return date_str;
    }

    /**
     * Преобразует дату в строку в необходимом формате
     *
     * @param d Дата
     * @param format Необходимый формат даты вида ddmmyyyy
     * @return Текущая дата строкой
     */
    public static String DateToString(Date d, String format) {
        return DateToString(getCalendarFromDate(d), format);
    }

    /**
     * Переводит дату из типа Date в Calendar
     *
     * @param d Дата тип Calendar
     * @return Дата типа Calendar
     */
    public static Calendar getCalendarFromDate(Date d) {
        Calendar calen = Calendar.getInstance();
        calen.setTime(d);
        return calen;
    }

    /**
     * Выдает название месяца
     *
     * @param i порядковый номер месяца, нумерация с 0
     * @return название месяца строкой
     */
    public static String getMName(int i) {
        String a = null;
        switch (i) {
            case 0:
                a = "января";
                break;
            case 1:
                a = "февраля";
                break;
            case 2:
                a = "марта";
                break;
            case 3:
                a = "апреля";
                break;
            case 4:
                a = "мая";
                break;
            case 5:
                a = "июня";
                break;
            case 6:
                a = "июля";
                break;
            case 7:
                a = "августа";
                break;
            case 8:
                a = "сентября";
                break;
            case 9:
                a = "октября";
                break;
            case 10:
                a = "ноября";
                break;
            case 11:
                a = "декабря";
                break;
        }
        return a;
    }

    /**
     * Выдает название месяца
     *
     * @param d Дата типа Date
     * @return название месяца строкой
     */
    public static String getMName(Date d) {
        String a = null;
        switch (getCalendarFromDate(d).get(Calendar.MONTH)) {
            case 0:
                a = "январь";
                break;
            case 1:
                a = "февраль";
                break;
            case 2:
                a = "март";
                break;
            case 3:
                a = "апрель";
                break;
            case 4:
                a = "май";
                break;
            case 5:
                a = "июнь";
                break;
            case 6:
                a = "июль";
                break;
            case 7:
                a = "август";
                break;
            case 8:
                a = "сентябрь";
                break;
            case 9:
                a = "октябрь";
                break;
            case 10:
                a = "ноябрь";
                break;
            case 11:
                a = "декабрь";
                break;
        }
        return a;
    }

    /**
     * Выдает дату строкой в виде "01 января 2011"
     *
     * @param date Дата типа Date
     * @return дата вида "01 января 2011"
     */
    public static String formatStr(Date date) {
        Calendar calen = Calendar.getInstance();
        calen.setTime(date);
        return calen.get(Calendar.DAY_OF_MONTH) + " " + getMName(calen.get(Calendar.MONTH)) + " " + calen.get(Calendar.YEAR);
    }

    /**
     * Выдает начало месяца
     *
     * @param d Дата типа Date
     * @return Первое число месяца
     */
    public static Date getMinDate(Date d) {
        Calendar c = (Calendar) getCalendarFromDate(d);
        c.set(Calendar.DATE, 1);
        return new Date(c.getTimeInMillis());
    }

    /**
     * Выдает последнее число месяца
     *
     * @param d Дата типа Date
     * @return последнее число месяца
     */
    public static Date getMaxDate(Date d) {
        Calendar c = (Calendar) getCalendarFromDate(d);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new Date(c.getTimeInMillis());
    }

    /**
     * Выдает год в числовом виде
     *
     * @param d Дата типа Date
     * @return год(число)
     */
    public static int getYearToInt(Date d) {
        return getCalendarFromDate(d).get(Calendar.YEAR);
    }

    /**
     * Выдает порядковый номер месяца (начиная с 1)
     *
     * @param d Дата
     * @return месяц(число)
     */
    public static int getMonthToInt(Date d) {
        return getCalendarFromDate(d).get(Calendar.MONTH) + 1;
    }

    /**
     * Выдает день месяца (начиная с 1)
     *
     * @param d Дата
     * @return день месяца(число)
     */
    public static int getDayToInt(Date d) {
        return getCalendarFromDate(d).get(Calendar.DATE);
    }

    /**
     *
     * @param day
     * @param dateps
     * @return
     */
    public static java.sql.Date getJavaDate(int day, java.util.Date dateps) {
        Calendar calen = (Calendar) getCalendarFromDate(dateps);
        calen.set(Calendar.DATE, day);
        return new java.sql.Date(calen.getTimeInMillis());
    }

    /**
     * Добавляет несколько лет к дате
     *
     * @param d Дата
     * @param y Количество лет, которые необходимо прибавить
     * @return Дата
     */
    public static Date addYearDate(Date d, int y) {
        getCalendarFromDate(d).add(Calendar.YEAR, y);
        Calendar cc = (Calendar) getCalendarFromDate(d);
        cc.add(Calendar.YEAR, y);
        return new Date(cc.getTimeInMillis());
    }

    /**
     * Считает разницу между двумя датами
     *
     * @param c1 первая дата
     * @param c2 вторая дата
     * @return разница в часах и минутах
     */
    public static String getDiffTime(Calendar c1, Calendar c2) {
        long start = c1.getTimeInMillis() + c1.getTimeZone().getOffset(c1.getTimeInMillis());
        long stop = c2.getTimeInMillis() + c2.getTimeZone().getOffset(c2.getTimeInMillis());
        long milsec = Math.round((double) (stop - start));
        long sec = milsec / 1000L;
        long min = sec / 3600L;
        long hour = sec / 60L;
        if (hour > 60L) {
            hour -= min * 60L;
        }
        if (sec > 60L) {
            sec = sec - min * 3600L - hour * 60L;
        }
        if (milsec > 1000L) {
            milsec = milsec - sec * 1000L - min * 3600L - hour * 60L;
        }
        String res = sec + "." + milsec;
        if (min > 0) {
            res = min + ":" + res;
        }
        if (hour > 0) {
            res = hour + ":" + res;
        }
        //return hour + ":" + min + ":" + sec + ":" + milsec;
        return res;
    }

    //30.11.2011 04:23
    //dd.MM.yyyy HH:mm
    public static Date getDateFromString(String dt, String formatS) throws ParseException {
        Date res;
        //String stringDate = "Thu, 6 Nov 2008 14:12:13 GMT";
        //String stringDateFormat = "EEE, dd MMM yyyy HH:mm:ss z";
        SimpleDateFormat format = new SimpleDateFormat(formatS);
        res = format.parse(dt);
        return res;
    }
}
