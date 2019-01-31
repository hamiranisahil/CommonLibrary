package com.example.library.util;

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtility {


    private object DateTimeFormat {
        /**
         * Typical MySqL/SQL dateTime format with dash as separator
         */
        val DATE_TIME_PATTERN_1 = "yyyy-MM-dd HH:mm:ss"
        /**
         * Typical MySqL/SQL dateTime format with slash as seperator
         */
        val DATE_TIME_PATTERN_2 = "dd/MM/yyyy HH:mm:ss"
        /**
         * Typical MySqL/SQL date format with dash as separator
         */
        val DATE_PATTERN_1 = "yyyy-MM-dd"
        /**
         * Typical MySqL/SQL date format with slash as seperator
         */
        val DATE_PATTERN_2 = "dd/MM/yyyy"
        /**
         * Time format full
         */
        val TIME_PATTERN_1 = "HH:mm:ss"
    }

    private val TAG = DateUtility::class.java.simpleName

    fun format(format: String, date: Date): String {
        return SimpleDateFormat(format).format(date)
    }

    fun format(format: String, date: String): String {
        return SimpleDateFormat(format).format(formatDate(date))
    }

    fun format(format: String, milliseconds: Long?, locale: Locale = Locale.getDefault()): String {
        return SimpleDateFormat(format, locale).format(Date(milliseconds!! * 1000L))
    }

    private fun formatDate(dateString: String?): Date? {
        var date: Date? = null
        if (dateString != null) {
            try {
                date = SimpleDateFormat(getDatePattern(dateString)).parse(dateString.trim { it <= ' ' })
            } catch (e: ParseException) {
                e.printStackTrace()
                Log.e(TAG, "formatDate: " + e.message)
            }

        }
        return date
    }

    fun dateToUnixTimestamp(dateString: String): Long {
        try {
            return SimpleDateFormat(getDatePattern(dateString)).parse(dateString).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return -1
    }

    fun unixTimestampToDate(format: String, milliseconds: Long?): String {
        return unixTimestampToDate(format, milliseconds, Locale.getDefault())
    }

    fun unixTimestampToDate(format: String, milliseconds: Long?, locale: Locale): String {
        return SimpleDateFormat(format, locale).format(Date(milliseconds!! * 1000L))
    }

    private fun getDatePattern(dateString: String?): String {
        return if (isDateTime(dateString)) {
            if (dateString!!.contains("/")) DateTimeFormat.DATE_TIME_PATTERN_2 else DateTimeFormat.DATE_TIME_PATTERN_1
        } else {
            if (dateString!!.contains("/")) DateTimeFormat.DATE_PATTERN_2 else DateTimeFormat.DATE_PATTERN_1
        }
    }

    private fun isDateTime(dateString: String?): Boolean {
        return dateString != null && dateString.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size > 1
    }

    fun getPreviousDateInLong(daysFromToday: Int): Long {
        return System.currentTimeMillis() - daysFromToday * 24 * 60 * 60 * 1000

    }

    fun getNextDateInLong(daysFromToday: Int): Long {
        return System.currentTimeMillis() - daysFromToday * 24 * 60 * 60 * 1000

    }
}
