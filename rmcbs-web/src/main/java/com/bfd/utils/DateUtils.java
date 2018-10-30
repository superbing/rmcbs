package com.bfd.utils;

import com.bfd.common.exception.RmcbsException;
import com.bfd.common.vo.Constants;
import com.google.common.collect.Maps;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: chong.chen
 * @Description: 日期工具类
 * @Date: Created in 17:32 2018/9/7
 * @Modified by:
 */
public class DateUtils {

    private static final int DATE = 5;

    /**
     * 获取前几天的日期
     * @param index 把日期往前减少一天，若想把日期向后推一天则将负数改为正数
     * @return
     */
    public static String getTheOtherDay(int index){

        Date date=new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        //把日期往前减少一天，若想把日期向后推一天则将负数改为正数
        calendar.add(DATE,index);
        date=calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取当前日期
     * @param dateFormat 日期格式
     * @return
     */
    public static String getToday(String dateFormat) {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        String today = df.format(new Date());
        return today;
    }

    /**
     * 获取数字与时间范围的映射
     * @return
     */
    public static Map<String,String> getTimeMap(){

        Map<String,String> timeMap = Maps.newLinkedHashMap();
        timeMap.put("0.0","00:00-00:59");
        timeMap.put("1.0","01:00-01:59");
        timeMap.put("2.0","02:00-02:59");
        timeMap.put("3.0","03:00-03:59");
        timeMap.put("4.0","04:00-04:59");
        timeMap.put("5.0","05:00-05:59");
        timeMap.put("6.0","06:00-06:59");
        timeMap.put("7.0","07:00-07:59");
        timeMap.put("8.0","08:00-08:59");
        timeMap.put("9.0","09:00-09:59");
        timeMap.put("10.0","10:00-10:59");
        timeMap.put("11.0","11:00-11:59");
        timeMap.put("12.0","12:00-12:59");
        timeMap.put("13.0","13:00-13:59");
        timeMap.put("14.0","14:00-14:59");
        timeMap.put("15.0","15:00-15:59");
        timeMap.put("16.0","16:00-16:59");
        timeMap.put("17.0","17:00-17:59");
        timeMap.put("18.0","18:00-18:59");
        timeMap.put("19.0","19:00-19:59");
        timeMap.put("20.0","20:00-20:59");
        timeMap.put("21.0","21:00-21:59");
        timeMap.put("22.0","22:00-22:59");
        timeMap.put("23.0","23:00-23:59");
        return timeMap;
    }

    public static Map<String,String> getTimeMaps(){
        Map<String,String> timeMap = Maps.newLinkedHashMap();
        timeMap.put("00","00:00-00:59");
        timeMap.put("01","01:00-01:59");
        timeMap.put("02","02:00-02:59");
        timeMap.put("03","03:00-03:59");
        timeMap.put("04","04:00-04:59");
        timeMap.put("05","05:00-05:59");
        timeMap.put("06","06:00-06:59");
        timeMap.put("07","07:00-07:59");
        timeMap.put("08","08:00-08:59");
        timeMap.put("09","09:00-09:59");
        timeMap.put("10","10:00-10:59");
        timeMap.put("11","11:00-11:59");
        timeMap.put("12","12:00-12:59");
        timeMap.put("13","13:00-13:59");
        timeMap.put("14","14:00-14:59");
        timeMap.put("15","15:00-15:59");
        timeMap.put("16","16:00-16:59");
        timeMap.put("17","17:00-17:59");
        timeMap.put("18","18:00-18:59");
        timeMap.put("19","19:00-19:59");
        timeMap.put("20","20:00-20:59");
        timeMap.put("21","21:00-21:59");
        timeMap.put("22","22:00-22:59");
        timeMap.put("23","23:00-23:59");
        return timeMap;
    }

    /**
     * 收集起始时间到结束时间之间所有的时间并以字符串集合方式返回
     *
     * @param timeStart
     * @param timeEnd
     * @return
     */
    public static List<String> collectRangeDates(String timeStart, String timeEnd) {
        return collectRangeDates(LocalDate.parse(timeStart), LocalDate.parse(timeEnd));
    }

    /**
     * 收集起始时间到结束时间之间所有的时间并以字符串集合方式返回
     *
     * @param start
     * @param end
     * @return
     */
    private static List<String> collectRangeDates(LocalDate start, LocalDate end) {
        // 用起始时间作为流的源头，按照每次加一天的方式创建一个无限流
        return Stream.iterate(start, localDate -> localDate.plusDays(1))
                // 截断无限流，长度为起始时间和结束时间的差+1个
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                // 由于最后要的是字符串，所以map转换一下
                .map(LocalDate::toString)
                // 把流收集为List
                .collect(Collectors.toList());
    }

    /**
     * 大致意思：Tim Cull碰到一个SimpleDateFormat带来的严重的性能问题，该问题主要有SimpleDateFormat引发，创建一个 SimpleDateFormat实例的开销比较昂贵，解析字符串时间时频繁创建生命周期短暂的实例导致性能低下。即使将 SimpleDateFormat定义为静态类变量，貌似能解决这个问题，但是SimpleDateFormat是非线程安全的，同样存在问题，如果用 ‘synchronized’线程同步同样面临问题，同步导致性能下降（线程之间序列化的获取SimpleDateFormat实例）。
     *
     *      Tim Cull使用Threadlocal解决了此问题，对于每个线程SimpleDateFormat不存在影响他们之间协作的状态，为每个线程创建一个SimpleDateFormat变量的拷贝或者叫做副本
     */
    private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(Constants.DATE_FORMAT);
        }
    };

    public static SimpleDateFormat getSimpleDateFormat() {
        return (SimpleDateFormat) threadLocal.get();
    }


    public static Date parse(String dateStr) throws ParseException {
        return threadLocal.get().parse(dateStr);
    }

    public static String format(Date date) {
        return threadLocal.get().format(date);
    }

    /**
     * 比较两个时间的大小 返回 0 相等 ,返回 1 date1比date2 大 ,返回 -1 反之
     * @param date1
     * @param date2
     */
    public static int compateDate(String date1,String date2) {
        try {
            Date d1 = parse(date1);
            Date d2 = parse(date2);
            if (d1.getTime() > d2.getTime()) {
                return Constants.MINUS_ONE;
            } else if (d1.getTime() < d2.getTime()) {
                return Constants.MINUS_ONE;
            } else {
                return Constants.MINUS_ONE;
            }
        } catch (ParseException e) {
           throw new RmcbsException("方法——compateDate（{}，{}）异常"+date1+","+date2);
        }
    }

    /**
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return List<String>  返回时间范围内每周一的日期
     */
    public static List<String> getFirstMondayOfTimes(String startDate, String endDate) {
        ArrayList<String> list;
        try {
            Date start = parse(startDate);
            Date end = parse(endDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            list = new ArrayList<>();

            while (true) {
                String date = format(calendar.getTime());
                if (calendar.get(Calendar.DAY_OF_WEEK) == 2) {
                    list.add(date);
                }

                if (date.equals(endDate)) {
                    break;
                } else {
                    calendar.add(Calendar.DATE, 1);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("时间的格式不对");
        }

        // 如果开始时间为周一，则不做处理，如果开始时间不是周一，则将开始时间所在的周一加入结果集
        String str = getMonday(startDate);
        if(!str.equals(startDate)){
            list.add(0 , str);
        }
        return list;
    }
    /**
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return List<String>  返回时间范围内每月一号的日期
     */
    public static List<String> getFirstDayOfMonths(String startDate, String endDate) {
        ArrayList<String> list;
        try {
            Date start = parse(startDate);
            Date end = parse(endDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            list = new ArrayList<>();

            while (true) {
                String date = format(calendar.getTime());
                if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                    list.add(date);
                }

                if (date.equals(endDate)) {
                    break;
                } else {
                    calendar.add(Calendar.DATE, 1);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("时间的格式不对");
        }
        return list;
    }

    /**
     * 获取指定日期对应的星期一的日期
     *
     * @param date 指定日期
     * @return List<String>  返回指定日期对应的星期一的日期
     */
    public static String getMonday(String date) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            Date time = sdf.parse(date);
            cal.setTime(time);

            //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
            //获得当前日期是一个星期的第几天
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (1 == dayWeek) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }

            //设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            cal.setFirstDayOfWeek(Calendar.MONDAY);

            //获得当前日期是一个星期的第几天
            int day = cal.get(Calendar.DAY_OF_WEEK);

            //根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
            result =  sdf.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取指定日期对应的星期日的日期
     *
     * @param date 指定日期
     * @return List<String>  返回指定日期对应的星期日的日期
     */
    public static String getSunday(String date) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            Date time = sdf.parse(date);
            cal.setTime(time);

            //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
            //获得当前日期是一个星期的第几天
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (1 == dayWeek) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }

            //设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            cal.setFirstDayOfWeek(Calendar.MONDAY);

            //获得当前日期是一个星期的第几天
            int day = cal.get(Calendar.DAY_OF_WEEK);

            //根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
            cal.add(Calendar.DATE, 6);
            result =  sdf.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取指定日期对应的当月一号的日期
     *
     * @param dateStr 指定日期
     * @return List<String>  返回指定日期对应的当月一号的日期
     */
    public static String getFirstDayOfMonth(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.add(Calendar.MONTH, 0);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
