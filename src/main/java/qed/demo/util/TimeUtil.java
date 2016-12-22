package qed.demo.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 数据工具类
 */
public class TimeUtil {

	public static final String DATE_TYPE = "yyyyMMdd";
	public static final String YEAR_TYPE = "yyyy-MM-dd";
	public static final String DATE_TIME_TYPE = "yyyyMMddHHmmss";
	public static final String YEAR_TIME_TYPE = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_TIME_MILLIS_TYPE = "yyyyMMddHHmmssSSS";
	public static final String TIME_TYPE = "HHmmss";


	public static String formatDate(Long date) {
		return formatDate(date, DATE_TYPE);
	}

	public static String formatDate(Long date, String format) {
		if (null == date) {
			return "";
		}
		return DateFormatUtils.format(date, format);
	}


	public static String formatDate(Timestamp date) {
		return formatDate(date, DATE_TYPE);
	}

	public static String formatDate(Timestamp date, String format) {
		if (null == date) {
			return "";
		}
		return DateFormatUtils.format(date.getTime(), format);
	}
	public static Long parseTime(String date) {
		return parseTime(date, DATE_TYPE);
	}

	public static Long parseTime(String date, String format) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		try {
			return parseDate(date.trim(), format).getTime();
		} catch (Exception e) {
			return null;
		}
	}

	public static Date parseDate(String date, String format) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		try {
			return DateUtils.parseDate(date.trim(), format);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String getMonthDate() {
		SimpleDateFormat df = new SimpleDateFormat(DATE_TYPE);// 定义格式，不显示毫秒
		return df.format(new Date());
	}

	public static Timestamp nowTimestamp() {
		return Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp((new Date()).getTime())));
	}

	public static Timestamp addDayTimestamp(int day) {
		return Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp((TimeUtil.addDay(day)).getTime())));
	}


	/**
	 * 得到当前时间所在的年度是第几周、第几天
	 * @return
	 */
	public static int[] getWeekAndDay() {
		int[] var = new int[2];
		Calendar c = Calendar.getInstance(Locale.CHINA);
		var[0] = c.get(Calendar.WEEK_OF_YEAR);
		var[1] = c.get(Calendar.DAY_OF_WEEK) - 1;
		return var;
	}


	public static Date convertDate(int week, int day) {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setFirstDayOfWeek(Calendar.MONDAY); //将每周第一天设为星期一，默认是星期天
		c.set(Calendar.DAY_OF_WEEK, day+1);
		return c.getTime();
	}
	
	public static Date today() {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		return c.getTime();
	}

	public static Date addDay(int day) {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.add(Calendar.DAY_OF_YEAR, day);
		return c.getTime();
	}

}
