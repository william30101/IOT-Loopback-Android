package iotdevice.com.iotDevice.barchart

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter

class HourAxisValueFormatter(private val chart: BarLineChartBase<*>) : IAxisValueFormatter {

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {

        val hourNumber = value.toInt()

        // Only first item display the unit name
        return if (value == 0f) {
            hourNumber.toString() + " Hour"
        } else {
            hourNumber.toString()
        }
    }
}