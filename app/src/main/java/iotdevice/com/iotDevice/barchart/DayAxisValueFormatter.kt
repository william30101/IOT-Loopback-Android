package iotdevice.com.iotDevice.barchart

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.util.*

/**
 * Created by philipp on 02/06/16.
 */
class DayAxisValueFormatter(private val chart: BarLineChartBase<*>) : IAxisValueFormatter {

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {

        val days = value.toInt()

        val year = determineYear(days)

        val month = determineMonth(days)
        val monthName = currentMonth()
        val yearName = year.toString()

        return if (chart.visibleXRange > 30 * 6) {
            "$monthName $yearName"
        } else {

            val dayOfMonth = determineDayOfMonth(days, month + 12 * (year - 2016))
            dayOfMonth.toString() + " " + monthName
        }
    }

    private fun getDaysForMonth(month: Int, year: Int): Int {

        // month is 0-based

        if (month == 1) {
            var is29Feb = false

            if (year < 1582)
                is29Feb = (if (year < 1) year + 1 else year) % 4 == 0
            else if (year > 1582)
                is29Feb = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

            return if (is29Feb) 29 else 28
        }

        return if (month == 3 || month == 5 || month == 8 || month == 10)
            30
        else
            31
    }

    private fun determineMonth(dayOfYear: Int): Int {

        var month = -1
        var days = 0

        while (days < dayOfYear) {
            month = month + 1

            if (month >= 12)
                month = 0

            val year = determineYear(days)
            days += getDaysForMonth(month, year)
        }

        return Math.max(month, 0)
    }

    private fun currentMonth(): String {
        return GregorianCalendar().getDisplayName(Calendar.MONTH, Calendar.ALL_STYLES, Locale.ENGLISH)
    }

    private fun determineDayOfMonth(days: Int, month: Int): Int {

        var count = 0
        var daysForMonths = 0

        while (count < month) {

            val year = determineYear(daysForMonths)
            daysForMonths += getDaysForMonth(count % 12, year)
            count++
        }

        return days - daysForMonths
    }

    private fun determineYear(days: Int): Int {

        return if (days <= 366)
            2016
        else if (days <= 730)
            2017
        else if (days <= 1094)
            2018
        else if (days <= 1458)
            2019
        else
            2020

    }
}
