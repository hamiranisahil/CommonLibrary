package com.example.library.util;

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.util.*

class DatePickerDialogUtility {

    /**
     * @param context
     * @param disablePreviousDates : pass in int
     *      for ex: -1 don't disable in datepicker
     *              0- it means disable from today.
     *              1- means to yesterday..
     *              ... next next....
     *              -1 - means tommorow
     *              ... next next..
     *@param datePickerListener : it is listener. when date select it will gives the call back in activity.
     */
    fun pickDate(context: Context, disableDates: Int, datePickerListener: DatePickerListener) {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val datePickerDialog = DatePickerDialog(context, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                datePickerListener.onDatePick(view, year, month + 1, dayOfMonth)
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        if (disableDates != -1) {
            datePickerDialog.datePicker.minDate =
                if (disableDates > -1) DateUtility().getPreviousDateInLong(disableDates) else DateUtility().getNextDateInLong(
                    disableDates
                )
        }
        datePickerDialog.show()
    }


    /**
     * @param context
     * @param disablePreviousDates (disableFromPassedYearMonthDay) : pass in int
     *      for ex: -1 don't disable in datepicker
     *              0- it means disable from today.
     *              1- means to yesterday..
     *              ... next next....
     *              -1 - means tommorow
     *              ... next next..
     *@param year : pass year
     * @param month : pass month
     * @param dayOfMonth : pass dayofMonth
     *@param datePickerListener : it is listener. when date select it will gives the call back in activity.
     */
    fun pickDate(
        context: Context,
        year: Int,
        month: Int,
        dayOfMonth: Int,
        disableDates: Int,
        datePickerListener: DatePickerListener
    ) {
        val datePickerDialog = DatePickerDialog(context, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                datePickerListener.onDatePick(view, year, month + 1, dayOfMonth)
            }

        }, year, month, dayOfMonth)

        if (disableDates != -1) {
            datePickerDialog.datePicker.minDate =
                if (disableDates > -1) DateUtility().getPreviousDateInLong(disableDates) else DateUtility().getNextDateInLong(
                    disableDates
                )
        }
        datePickerDialog.show()
    }

    interface DatePickerListener {
        fun onDatePick(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)
    }


}
