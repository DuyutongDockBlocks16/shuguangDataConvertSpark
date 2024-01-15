package cn.jj.simulation.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-06-01 10:52
 */
public class DateUtils {

    public static DateTimeFormatter datedtf = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static DateTimeFormatter fulldatedtf = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
    public static DateTimeFormatter timedtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(datedtf);
    }

    public static String getCurrentFullDate() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(fulldatedtf);
    }
}
