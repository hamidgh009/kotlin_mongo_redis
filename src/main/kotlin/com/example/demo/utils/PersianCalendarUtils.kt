package com.example.demo.utils

import com.ibm.icu.util.Calendar
import com.ibm.icu.util.ULocale
import java.util.*

object PersianCalendarUtils {

    var calendar: Calendar

    init {
        val locale = ULocale("@calendar=persian")

        calendar = Calendar.getInstance(locale)
        calendar.firstDayOfWeek = 7

    }

    fun getYear(date : Date) : Int{
        calendar.time = date
        return calendar.get(Calendar.YEAR)
    }

    fun getWeekOfYear(date : Date) : Int {
        calendar.time = date
        return if(calendar.get(Calendar.YEAR_WOY) == calendar.get(Calendar.YEAR)) calendar.get(Calendar.WEEK_OF_YEAR) else 53
    }

}