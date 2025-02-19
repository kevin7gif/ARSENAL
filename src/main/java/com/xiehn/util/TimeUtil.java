package com.xiehn.util;

import com.xiehn.common.constants.JfTimeUnitEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
public class TimeUtil {
    public static String PATTERN_DATE_1 = "yyyy-MM-dd";
    public static String PATTERN_DATE_2 = "yyyyMMdd";
    public static String PATTERN_DATE_3 = "yyyyMMddhhMMss";
    public static String PATTERN_DATE_4 = "yyyy-MM-dd HH:mm:ss:SSS";
    public static String PATTERN_DATE_5 = "yyyy年MM月dd日";
    public static String PATTERN_DATE_6 = "yyyy-MM-dd HH:mm";
    public static String PATTERN_DATE_7 = "yyyy-MM-dd HH:mm:ss";
    public static String PATTERN_DATE_8 = "yyyy-MM-dd HH";
    public static String PATTERN_DATE_9 = "MMdd";
    public static String PATTERN_DATE_10 = "yyMMddHHmmss";
    public static String PATTERN_DATE_11 = "yyyyMMddHHmmss";
    public static String PATTERN_DATE_12 = "yyyy/MM/dd HH:mm:ss";
    public static String PATTERN_TIME_1 = "HH:mm";
    public static String PATTERN_TIME_2 = "HH:mm:ss";
    public static String PATTERN_YEAR_1 = "yyyy";
    public static String STARTTIME_SUFFIX = " 00:00:00";
    public static String ENDTIME_SUFFIX = " 23:59:59";
    public static String YEAR_FIRST_DAY = " 1-1";
    public static String YEAR_LAST_DAY = " 12-31";
    public static Integer MAX_CONTINIOUS_DAYS = 10000;
    public static final SimpleDateFormat formatter;
    public static final Long YEAR_OF_MILLISON_SECONDS;
    public static final Long MONTH_OF_MILLISON_SECONDS;
    public static final Integer DAY_OF_MILLISON_SECONDS;
    public static final Integer HOUR_OF_MILLISON_SECONDS;
    public static final Integer MINUTE_OF_MILLISON_SECONDS;

    public TimeUtil() {
    }

    /**
     * 获取当前月份的最后一天
     * @return
     */
    public static String getLastDayForMonth() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return convert2Str(calendar.getTime(),TimeUtil.PATTERN_DATE_1);
    }

    /**
     * 获取上个月的最后一天
     * @return
     */
    public static String getLastDayForBeforeMonth() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        // 如果结果是0，说明是1月份，需要将年份减去1，月份设置为11（即去年的12月）
        if (month == 0) {
            calendar.set(Calendar.YEAR, year - 1);
            calendar.set(Calendar.MONTH, 11);
        } else {
            calendar.set(Calendar.MONTH, month - 1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return convert2Str(calendar.getTime(),TimeUtil.PATTERN_DATE_1);
    }

    /**
     * 获取当天的最后一秒时间：2024-01-01 23:59:59
     * @param date
     * @return
     */
    public static String getLastMillisForDay(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToDate(date));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999); // 设置毫秒为999

        Date lastSecondDate = calendar.getTime();
        return convert2Str(lastSecondDate);
    }

    /**
     * 获取当天的第一秒时间：2024-01-01 00:00:01
     * @param date
     * @return
     */
    public static String getFirstSecondForDay(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToDate(date));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);

        Date firstSecondDate = calendar.getTime();
        return convert2Str(firstSecondDate);
    }

    /**
     * 返回给定日期所在月份的下一个月的开始时间。
     * @param date 输入的日期。
     * @return 返回下一个月的第一天的Date对象。
     */
    public static Date getFirstDayOfNextMonth(Date date) {
        // 创建一个Calendar实例并设置为输入的日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 将日期设置为当前月的最后一天
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 将时间设置为00:00:00.000，即当天的开始
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 从Calendar实例中获取Date对象
        return calendar.getTime();
    }

    // 判断该用户属于P1M区间还是P12M区间
    public static Boolean isP1M(String timeRange){
        String[] split = timeRange.split("_");
        String startDate = split[0];
        String endDate = split[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, formatter);

        // 计算两个日期差
        long daysBetween = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);

        return daysBetween >= 27 && daysBetween <= 31;
    }

    // 判断两个时间段之间间隔是否大于12个月
    public static boolean isIntervalGreaterThanOneYear(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 将字符串转换为 LocalDate
        LocalDate startDate = LocalDate.parse(startTime, formatter);
        LocalDate endDate = LocalDate.parse(endTime, formatter);

        // 计算两个日期之间的月差
        long monthsBetween = ChronoUnit.MONTHS.between(startDate, endDate);
        // 判断间隔是否大于等于12个月
        return monthsBetween >= 12;
    }

    // 判断两个时间段之间间隔是否大于一个月
    public static boolean isIntervalGreaterThanOneMonth(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 将字符串转换为 LocalDate
        LocalDate startDate = LocalDate.parse(startTime, formatter);
        LocalDate endDate = LocalDate.parse(endTime, formatter);

        // 计算两个日期之间的月差
        long monthsBetween = ChronoUnit.MONTHS.between(startDate, endDate);
        // 判断间隔是否大于等于1个月
        return monthsBetween >= 1;
    }

    // 转换日期格式
    public static Date stringToDate(String dateStr) {
        SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd");
        // 直接将字符串转为date类型
        Date date = null;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            log.info("转换日期出现错误，msg==> [{}]",e.getMessage());
        }
        return date;
    }

    public static Date stringToDate2(String dateStr) {
        SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM");
        // 直接将字符串转为date类型
        Date date = null;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            log.info("转换日期出现错误，msg==> [{}]",e.getMessage());
        }
        return date;
    }

    // 获取当前时间往后一年的时间
    public static Date addOneYearAndAdjust(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, 1); // 往后添加一年
        calendar.add(Calendar.DAY_OF_MONTH, -1);// 往前减一天，确保是上一年的最后一天

        // 获取下一年的2月的最后一天
        int endDayOfFebruary = isLeapYear(calendar.get(Calendar.YEAR)) ? 29 : 28;

        // 如果开始日期是3月或更晚，直接返回加上一年的结果
        // 如果开始日期是2月，则需要减去一天，以确保是2月的最后一天
        if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) {
            calendar.set(Calendar.DAY_OF_MONTH, Math.min(calendar.get(Calendar.DAY_OF_MONTH), endDayOfFebruary));
        }
        return calendar.getTime();
    }

    // 获取当前时间往后推一个月的时间
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1); // 往后添加一个月
        calendar.add(Calendar.DAY_OF_MONTH, -1);// 往前减一天，确保是下一个月最后一天

        // 如果是2月，需要检查是否为闰年，并相应地设置日期
        if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) {
            int year = calendar.get(Calendar.YEAR);
            int maxDayOfMonth = isLeapYear(year) ? 29 : 28;
            calendar.set(Calendar.DAY_OF_MONTH, Math.min(calendar.get(Calendar.DAY_OF_MONTH), maxDayOfMonth));
        }

        return calendar.getTime();
    }

    // 判断是否为闰年
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }



    public static Date parseDate(String dateTime, String pattern) {
        Date date = null;
        if (!CollectionUtil.isEmpty(dateTime)) {
            try {
                date = (new SimpleDateFormat(pattern)).parse(dateTime);
            } catch (ParseException var4) {
                var4.printStackTrace();
            }
        }

        return date;
    }

    public static Date parseDate(String dateTime) {
        Date date = null;
        String format = PATTERN_DATE_7;

        try {
            date = (new SimpleDateFormat(format)).parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convert2Str(Date date, String format) {
        if (date != null) {
            if (StringUtils.isBlank(format)) {
                format = PATTERN_DATE_7;
            }
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(date);
        }

        return null;
    }

    public static String convert2Str(Date date) {
        if (date != null) {
            String format = PATTERN_DATE_7;
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(date);
        }

        return null;
    }

    public static Date formatToDate(String dateTime, String pattern) {
        Date date = null;
        if (!CollectionUtil.isEmpty(dateTime)) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                String time = format.format(Long.parseLong(dateTime));
                date = format.parse(time);
            } catch (ParseException var5) {
                var5.printStackTrace();
            }
        }

        return date;
    }

    public static String getDefaultCurrentDateTime() {
        return getTimeStr(new Date(), PATTERN_DATE_7);
    }

    public static String getTimeStr(Date date, String pattern) {
        return null == date ? "" : (new SimpleDateFormat(pattern)).format(date);
    }

    public static String getTimeStr(String pattern) {
        return getTimeStr(new Date(), pattern);
    }

    public static String getTimeStrExcpBeginZero(String pattern) {
        String result = getTimeStr(new Date(), pattern);
        if (!CollectionUtil.isEmpty(result) && "0".equals(result.substring(0, 1))) {
            result = result.substring(1);
        }

        return result;
    }

    public static String getTimeStrExcpBeginZero(Date date, String pattern) {
        String result = getTimeStr(date, pattern);
        if (!CollectionUtil.isEmpty(result) && "0".equals(result.substring(0, 1))) {
            result = result.substring(1);
        }

        return result;
    }

    public static String getTimeStrExcpBeginZero(Date date, String pattern, boolean isTrue) {
        String result = getTimeStr(date, pattern);
        if (!CollectionUtil.isEmpty(result) && isTrue && "0".equals(result.substring(0, 1))) {
            result = result.substring(1);
        }

        return result;
    }



    public static String Date2String(Date date, String patten) {
        if (CollectionUtil.isEmpty(date)) {
            return "";
        } else {
            if (CollectionUtil.isEmpty(patten)) {
                patten = PATTERN_DATE_1;
            }

            SimpleDateFormat sdf = new SimpleDateFormat(patten);
            return sdf.format(date);
        }
    }

    public static Date convert2Date(String str, String format) throws ParseException {
        if (StringUtils.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(str);
    }

    public static int getRandom(int start, int end) {
        return start + (int)(Math.random() * 10.0D * (double)(end - start + 2)) % (end - start + 1);
    }

    public static long getQuot(String time1, String time2, String patten) {
        long quot = 0L;
        SimpleDateFormat ft = new SimpleDateFormat(patten);

        try {
            Date date1 = ft.parse(time1);
            Date date2 = ft.parse(time2);
            quot = date2.getTime() - date1.getTime();
            quot = quot / 1000L / 60L / 60L / 24L;
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return quot;
    }

    public static String getTimestampStr(Timestamp timestamp, String pattern) {
        if (null == timestamp) {
            return "";
        } else {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(timestamp.getTime());
            return (new SimpleDateFormat(pattern)).format(c.getTime());
        }
    }

    public static Date getTimestampDateFromString(String string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_DATE_4);

        try {
            Date date = dateFormat.parse(string);
            Timestamp timestamp = new Timestamp(date.getTime());
            return timestamp;
        } catch (ParseException var4) {
            return new Date();
        }
    }

    public static Date getTimestampDateFromString(String string, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        try {
            Date date = dateFormat.parse(string);
            Timestamp timestamp = new Timestamp(date.getTime());
            return timestamp;
        } catch (ParseException var5) {
            return new Date();
        }
    }

    public static int getMaxDay(String year, String month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(1, Integer.valueOf(year));
        time.set(2, Integer.valueOf(month) - 1);
        int day = time.getActualMaximum(5);
        return day;
    }

    public static Timestamp getTimeStamp(String timeStr) {
        Date date = getTimestampDateFromString(timeStr);
        Timestamp time = new Timestamp(date.getTime());
        return time;
    }

    public static Timestamp getTimeStamp(Date date) {
        Timestamp time = new Timestamp(date.getTime());
        return time;
    }

    public static long getDays(Timestamp timestamp) {
        long currentTime = System.currentTimeMillis();
        long temp = timestamp.getTime();
        long dayslong = currentTime - temp;
        long days = (dayslong >> 10) / 84375L;
        return days;
    }

    public static Timestamp getTimeStamp(String timeStr, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);

        try {
            Timestamp ts = new Timestamp(format.parse(timeStr).getTime());
            return ts;
        } catch (ParseException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static Date getDateFromString(String date, String fmt) throws Exception {
        if (date != null && date.trim().length() != 0) {
            DateFormat df = new SimpleDateFormat(fmt);
            return df.parse(date);
        } else {
            return null;
        }
    }

    public static Date getDateFormatString(String str, String pattern) {
        if (str != null && !str.trim().equals("")) {
            try {
                return (new SimpleDateFormat(pattern)).parse(str);
            } catch (ParseException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static String getTwoDateDiff(Date startDate, Date endDate) {
        long l = endDate.getTime() - startDate.getTime();
        long day = l / 86400000L;
        long hour = l / 3600000L - day * 24L;
        long min = l / 60000L - day * 24L * 60L - hour * 60L;
        long sed = l / 1000L - day * 24L * 60L * 60L - hour * 60L * 60L - min * 60L;
        return "" + day + "天" + hour + "小时" + min + "分" + sed + "秒";
    }

    public static Long getTwoDay(Date startDate, Date endDate) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String endTempDate = myFormatter.format(endDate.clone());
        String startTempDate = myFormatter.format(startDate.clone());

        try {
            return (myFormatter.parse(endTempDate).getTime() - myFormatter.parse(startTempDate).getTime()) / 86400000L;
        } catch (ParseException var6) {
            return -1L;
        }
    }

    public static List<Date> getBeginAndEndDate() {
        List<Date> preDates = new ArrayList();
        preDates.add(getTodayBeginTime());
        preDates.add(getTodayEndTime());
        return preDates;
    }

    public static String getYesterdayBeginTimeStr() {
        Calendar cal = Calendar.getInstance();
        cal.add(5, -1);
        return getSomeDayBeginTime(cal.getTime());
    }

    public static Date getTodayBeginTime() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DECEMBER, 0);
        now.set(12, 0);
        now.set(13, 0);
        now.set(14, 0);
        return now.getTime();
    }

    public static Date getTodayEndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE_7);
        Date end = null;

        try {
            String baseTimeStr = getBaseDateStr();
            String endTime = baseTimeStr + ENDTIME_SUFFIX;
            end = sdf.parse(endTime);
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return end;
    }

    public static String getSomeDayBeginTime(Date date) {
        String baseTimeStr = getBaseDateStr(date);
        return baseTimeStr + STARTTIME_SUFFIX;
    }

    public static String getNextDayBeginTime() {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        result = formatter.format(calendar.getTime());
        return result;
    }

    public static Date getIntervalTimeOfDays(Date beginDate, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        calendar.add(6, days);
        return calendar.getTime();
    }

    public static Date getIntervalTimeOfMonths(Date beginDate, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        calendar.add(2, months);
        return calendar.getTime();
    }

    public static String getSomeDayEndTime(Date date) {
        String baseTimeStr = getBaseDateStr(date);
        return baseTimeStr + ENDTIME_SUFFIX;
    }

    public static String getSomeDayEndTime(int day) {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, day + 1);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        result = formatter.format(calendar.getTime());
        return result;
    }

    public static Date getIntervalDayEndTime(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, day);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        calendar.set(14, 999);
        return calendar.getTime();
    }

    public static String getSomeDayEndTime(String baseDateStr, int day) {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDateFormatString(baseDateStr, PATTERN_DATE_7));
        calendar.add(5, day);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        result = formatter.format(calendar.getTime());
        return result;
    }

    public static String getSomeMonthEndTime(int month) {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, month);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        result = formatter.format(calendar.getTime());
        return result;
    }

    /**
     * 获取指定月份的第一天00:00:00
     * @param month 月份（1-12）
     * @return 指定月份第一天的Date对象
     */
    public static Date getFirstDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, month - 1); // Calendar月份从0开始
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定月份的最后一天23:59:59
     * @param month 月份（1-12）
     * @return 指定月份最后一天的Date对象
     */
    public static Date getLastDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, month - 1); // Calendar月份从0开始
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static void main(String[] args) {

        System.out.println(getFirstDayOfMonth(8));
        System.out.println(getLastDayOfMonth(8));
    }
    public static String getSomeMonthEndTime(String baseDateStr, int month) {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDateFormatString(baseDateStr, PATTERN_DATE_7));
        calendar.add(2, month);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        result = formatter.format(calendar.getTime());
        return result;
    }

    public static String getBaseDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE_1);
        return sdf.format(new Date());
    }

    public static String getBaseDateStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE_1);
        return sdf.format(date);
    }

    public static String getBaseDateStr(String patten) {
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        return sdf.format(new Date());
    }

    public static String getDateOfWeek(Date dt) {
        String[] weekDays = new String[]{"7", "1", "2", "3", "4", "5", "6"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(7) - 1;
        if (w < 0) {
            w = 0;
        }

        return weekDays[w];
    }

    public static Integer getDateOfDay(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int day = cal.get(5);
        return day;
    }

    public static Integer getDateOfMonth(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int month = cal.get(2) + 1;
        return month;
    }

    public static Integer getDateOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(1);
        return month;
    }

    public static Integer getDayOfSuffix(Date dt) {
        int day = getDateOfDay(dt);
        return day % 10;
    }

    public static String getCurrentDateOfWeek() {
        Date date = new Date();
        return getDateOfWeek(date);
    }

    public static Integer getCurrentDateOfDay() {
        Date date = new Date();
        return getDateOfDay(date);
    }

    public static Integer getCurrentDateOfMonth() {
        Date date = new Date();
        return getDateOfMonth(date);
    }

    public static Integer getSpecialDateOfMonth(Date date) {
        return getDateOfMonth(date);
    }

    public static Integer getCurrentDayOfSuffix() {
        Date date = new Date();
        return getDayOfSuffix(date);
    }

    public static String getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(1);
        return year + "";
    }

    public static Date getCurrentYearFirstDay() {
        String datetimeStr = getCurrentYear() + "-" + YEAR_FIRST_DAY + "" + STARTTIME_SUFFIX;
        return getDateFormatString(datetimeStr, PATTERN_DATE_7);
    }

    public static Date getCurrentYearLastDay() {
        String datetimeStr = getCurrentYear() + "-" + YEAR_LAST_DAY + "" + ENDTIME_SUFFIX;
        return getDateFormatString(datetimeStr, PATTERN_DATE_7);
    }

    public static Date getSomeYearFirstDay(Integer year) {
        String datetimeStr = year + "-" + YEAR_FIRST_DAY + "" + STARTTIME_SUFFIX;
        return getDateFormatString(datetimeStr, PATTERN_DATE_7);
    }

    public static Date getSomeYearLastDay(Integer year) {
        String datetimeStr = year + "-" + YEAR_LAST_DAY + "" + ENDTIME_SUFFIX;
        return getDateFormatString(datetimeStr, PATTERN_DATE_7);
    }

    public static String getCurrentPreviousYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(1);
        return year - 1 + "";
    }

    public static String getCurrentNextYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(1);
        return year + 1 + "";
    }

    public static Date getCurrentMonthDayBeginTime() {
        Calendar ca = Calendar.getInstance();
        ca.add(2, 0);
        ca.set(5, 1);
        ca.set(11, 0);
        ca.set(12, 0);
        ca.set(13, 0);
        return ca.getTime();
    }

    public static Date getCurrentMonthDayEndTime() {
        Calendar ca = Calendar.getInstance();
        ca.set(5, ca.getActualMaximum(5));
        ca.set(11, 23);
        ca.set(12, 59);
        ca.set(13, 59);
        return ca.getTime();
    }

    public static boolean validateTimeIsExpired(Date compareDate) {
        if (compareDate != null) {
            return compareDate.getTime() < System.currentTimeMillis();
        } else {
            return false;
        }
    }

    public static int getcontinueDates(Set<String> dateSet, String patten) {
        int count = 0;
        if (!CollectionUtil.isEmpty(dateSet)) {
            Calendar calendar = Calendar.getInstance();

            for(int i = 0; i <= MAX_CONTINIOUS_DAYS; ++i) {
                calendar.setTime(new Date());
                calendar.add(5, -i);
                String tempTimeStr = getTimeStr(calendar.getTime(), PATTERN_DATE_1);
                if (getTimeStr(new Date(), PATTERN_DATE_1).equals(tempTimeStr) && dateSet.contains(tempTimeStr)) {
                    ++count;
                } else if (i != 0) {
                    if (!dateSet.contains(tempTimeStr)) {
                        break;
                    }

                    ++count;
                }
            }
        }

        return count;
    }

    public static int getContinueDates(List<Date> dates) {
        if (CollectionUtil.isEmpty(dates)) {
            return 0;
        } else {
            int size = dates.size();
            Date now = new Date();
            Date prev = now;
            int count = 0;

            for(int i = size - 1; i >= 0; --i) {
                Date curr = (Date)dates.get(i);
                if (curr != null) {
                    long dayInterval = (prev.getTime() + 28800000L) / 86400000L - (curr.getTime() + 28800000L) / 86400000L;
                    if (dayInterval == 0L) {
                        if (prev == now) {
                            count = 1;
                        }
                    } else {
                        if (dayInterval != 1L) {
                            break;
                        }

                        ++count;
                        prev = curr;
                    }
                }
            }

            return count;
        }
    }

    public static int getMaxContinueDays(List<Date> dates) {
        if (CollectionUtil.isEmpty(dates)) {
            return 0;
        } else {
            Date prev = (Date)dates.get(0);
            int mix = 0;
            int tempMix = 0;
            if (prev != null) {
                mix = 1;
                tempMix = 1;
            }

            for(int i = 1; i < dates.size(); ++i) {
                Date curr = (Date)dates.get(i);
                if (curr != null) {
                    if (prev == null) {
                        mix = 1;
                        tempMix = 1;
                        prev = curr;
                    } else {
                        long dayInterval = (curr.getTime() + 28800000L) / 86400000L - (prev.getTime() + 28800000L) / 86400000L;
                        if (dayInterval == 1L) {
                            ++tempMix;
                        } else if (dayInterval > 1L) {
                            tempMix = 1;
                        }

                        if (tempMix > mix) {
                            ++mix;
                        }

                        prev = curr;
                    }
                }
            }

            return mix;
        }
    }

    public static int getAgeByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        } else {
            int yearNow = cal.get(1);
            int monthNow = cal.get(2) + 1;
            int dayOfMonthNow = cal.get(5);
            cal.setTime(birthday);
            int yearBirth = cal.get(1);
            int monthBirth = cal.get(2) + 1;
            int dayOfMonthBirth = cal.get(5);
            int age = yearNow - yearBirth;
            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        --age;
                    }
                } else {
                    --age;
                }
            }

            return age;
        }
    }

    public static Date someYearBeforeAssignDate(Integer unitValue, Date beginTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginTime);
        cal.set(1, cal.get(1) - unitValue);
        return cal.getTime();
    }

    public static Date someMonthBeforeAssignDate(Integer unitValue, Date beginTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginTime);
        cal.set(2, cal.get(2) - unitValue);
        return cal.getTime();
    }

    public static Date someDayBeforeAssignDate(Integer unitValue, Date beginTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginTime);
        cal.set(5, cal.get(5) - unitValue);
        return cal.getTime();
    }

    public static Date getIntervalDayBeginTime(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, day);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static Date getLatestDate(JfTimeUnitEnums timeUnitEnums, Integer unitValue, Date baseTime) {
        Date date = null;
        if (!CollectionUtil.isEmpty(timeUnitEnums)) {
            switch(timeUnitEnums) {
                case Y:
                    Calendar calendarY = Calendar.getInstance();
                    calendarY.setTime(baseTime);
                    calendarY.add(1, -unitValue);
                    date = calendarY.getTime();
                    break;
                case M:
                    Calendar calendarM = Calendar.getInstance();
                    calendarM.setTime(baseTime);
                    calendarM.add(2, -unitValue);
                    date = calendarM.getTime();
                    break;
                case D:
                    Calendar calendarD = Calendar.getInstance();
                    calendarD.setTime(baseTime);
                    calendarD.add(5, -unitValue);
                    date = calendarD.getTime();
                    break;
                case H:
                    Calendar calendarH = Calendar.getInstance();
                    calendarH.setTime(baseTime);
                    calendarH.add(11, -unitValue);
                    date = calendarH.getTime();
                    break;
                case MINUTE:
                    Calendar calendarm = Calendar.getInstance();
                    calendarm.setTime(baseTime);
                    calendarm.add(12, -unitValue);
                    date = calendarm.getTime();
            }
        }

        return date;
    }

    public static Date getAssignDate(Date time, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(11, hour);
        calendar.set(12, 0);
        calendar.set(13, 0);
        Date date = calendar.getTime();
        return date;
    }

    static {
        formatter = new SimpleDateFormat(PATTERN_DATE_7);
        YEAR_OF_MILLISON_SECONDS = 31536000000L;
        MONTH_OF_MILLISON_SECONDS = 2592000000L;
        DAY_OF_MILLISON_SECONDS = 86400000;
        HOUR_OF_MILLISON_SECONDS = 3600000;
        MINUTE_OF_MILLISON_SECONDS = 60000;
    }
}
