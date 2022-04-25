package com.android.aschat.util

import com.android.aschat.common.Constants
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by NINGMEI(shicaid) on 2022/2/14.
 * Intent: 时间工具类
 */
object TimeUtil {

    private const val DATE_TIME_PATTERN1 = "yyyy-MM-dd HH:mm:ss"

    private const val DATE_TIME_PATTERN2 = "yyyy-MM-dd HH:mm"

    private const val DATE_TIME_PATTERN3 = "yyyy-MM-dd"

    private const val DATE_TIME_PATTERN4 = "MM月dd日 HH:mm:ss"

    private const val DATE_TIME_PATTERN5 = "M月dd"

    private const val DATE_TIME_PATTERN6 = "dd"

    private val dateFormat: SimpleDateFormat = SimpleDateFormat()

    /**
     * 格式话指定时间
     *
     * @param format
     * 格式
     * @param date
     * 指定时间
     * @return
     */
    fun getDateStr(format: String, date: Date?): String {
        if(date == null){
            return ""
        }
        dateFormat.applyPattern(format)
        return dateFormat.format(date)
    }

    /**
     * 格式话指定时间
     *
     * @param format
     * 格式
     * @param timestamp
     * 时间戳
     * @return
     */
    fun getDateStr(format: String, timestamp: Long): String {
        return if (timestamp <= 0) {
            ""
        } else getDateStr(format, Date(timestamp))
    }

    /**
     * 格式化当前时间返回
     *
     * @param format
     * @return
     */
    fun getDateStrNow(format: String): String {
        return getDateStr(format, System.currentTimeMillis())
    }

    /**
     * 以yyyy-MM-dd HH:mm:ss格式返回当前时间
     *
     * @return
     */
    fun getDateStrNow1(): String {
        return getDateStrNow(DATE_TIME_PATTERN1)
    }

    /**
     * 以yyyy-MM-dd HH:mm格式返回当前时间
     *
     * @return
     */
    fun getDateStrNow2(): String {
        return getDateStrNow(DATE_TIME_PATTERN2)
    }

    /**
     * 以yyyy-MM-dd HH:mm:ss格式返回当前时间
     *
     * @return
     */
    fun getFormatDate1(timestamp: Long): String {
        return getDateStr(DATE_TIME_PATTERN1, timestamp)
    }

    /**
     * 以yyyy-MM-dd HH:mm格式返回当前时间
     *
     * @return
     */
    fun getFormatDate2(timestamp: Long): String {
        return getDateStr(DATE_TIME_PATTERN2, timestamp)
    }

    /**
     * 以yyyy-MM-dd格式返回当前时间
     *
     * @return
     */
    fun getFormatDate3(timestamp: Long): String {
        return getDateStr(DATE_TIME_PATTERN3, timestamp)
    }

    /**
     * 以MM-dd HH:mm:ss格式返回当前时间
     *
     * @return
     */
    fun getFormatDate4(timestamp: Long): String {
        return getDateStr(DATE_TIME_PATTERN4, timestamp)
    }

    /**
     * 查询前/未来pastIndex天的日期
     *
     * @param pastIndex 正数：前pastIndex天
     * 负数：未来v天
     * @return
     */
    fun getPastCal(pastIndex: Int): Calendar {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - pastIndex)
        return calendar
    }

    /**
     * 和当天相比，preTime是否是同一天
     *
     * @param preTime
     * @return
     */
    fun isSameDay(preTime: Long): Boolean {
        val lastTimeCalendar = Calendar.getInstance()
        lastTimeCalendar.time = Date(preTime)
        return Calendar.getInstance()[Calendar.DAY_OF_YEAR] == lastTimeCalendar[Calendar.DAY_OF_YEAR]
    }

    /**
     * 以MM月dd日格式返回当前时间
     *
     * @return
     */
    fun getFormatDate5(timestamp: Long): String {
        return getDateStr(DATE_TIME_PATTERN5, timestamp)
    }

    /**
     * 以dd格式返回当前时间
     *
     * @return
     */
    fun getFormatDate6(timestamp: Long): String {
        return getDateStr(DATE_TIME_PATTERN6, timestamp)
    }

    /**
     * 时长转换为 mm:ss形式
     */
    fun formatDuration(duration: Long): String{
        val secondTotal = duration / 1000
        val min = duration / 60000
        val second = secondTotal - (60 * min)
        return if(min > 0) {
            String.format("%d:%02d", min,second)
        }else {
            String.format("00:%02d",second)
        }

    }

    /**
     * 和preTime相比，现在是X天之后
     * @param preTime
     * @return 返回-1表示非法
     */
    fun dayAfter(preTime: Long): Int {
        if (preTime <= 0) return -1
        val lastTimeCalendar = Calendar.getInstance()
        lastTimeCalendar.time = Date(preTime)
        val yearsInterval = Calendar.getInstance()[Calendar.YEAR] - lastTimeCalendar[Calendar.YEAR]
        var interval = 0
        if (yearsInterval == 0) {
            //是同一年的日来比较
            interval =
                Calendar.getInstance()[Calendar.DAY_OF_YEAR] - lastTimeCalendar[Calendar.DAY_OF_YEAR]
        } else if (yearsInterval == 1) {
            //preTime是上一年，现在是今年
            interval =
                365 - lastTimeCalendar[Calendar.DAY_OF_YEAR] + Calendar.getInstance()[Calendar.DAY_OF_YEAR]
        }
        return interval
    }

    /**
     * 和preTime相比，现在是否已是X分钟之后
     * @param preTime
     * @return 如果已是minute分钟之后，返回true，否则，返回false
     */
    fun isXMinuteAfter(preTime: Long, minute: Long): Boolean {
        val milliseconds = Calendar.getInstance().timeInMillis - preTime
        return milliseconds / 1000 / 60 > minute
    }

    fun getYearMonthDay(timeStamp: Long): IntArray {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeStamp
        val y = calendar.get(Calendar.YEAR)
        val m = calendar.get(Calendar.MONTH)
        val d = calendar.get(Calendar.DAY_OF_MONTH)
        return IntArray(3).apply {
            this[0] = y
            this[1] = m
            this[2] = d
        }
    }

    /**
     * 格式化剩余时间
     */
    fun formatPromotion(millis: Long): String {
        var last = millis
        val hour = (last / Constants.HOUR_MILLIS).toInt()
        last = last - hour * Constants.HOUR_MILLIS
        val min = (last / Constants.MIN_MILLIS).toInt()
        last = last - min * Constants.MIN_MILLIS
        val second = last / Constants.SECOND_MILLIS
        return "$hour:$min:$second"
    }
}