package iotdevice.com.iotDevice.barchart

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter


class HourAxisValueFormatter(private val chart: BarLineChartBase<*>, enableDecimalPoint: Boolean = false) : iotdevice.com.iotDevice.barchart.ValueFormatter(
    enableDecimalPoint
) {

    override fun getFormattedValue(value: Float): String {
        val hourNumber = value.toInt()

        // Only first item display the unit name
        return if (value == 0f) {
            "$hourNumber Hour"
        } else {
            hourNumber.toString()
        }
    }
}