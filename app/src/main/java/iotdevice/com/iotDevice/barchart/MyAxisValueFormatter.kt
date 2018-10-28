package iotdevice.com.iotDevice.barchart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.DecimalFormat

class MyAxisValueFormatter(private val unitName: String) : IAxisValueFormatter {

    private val mFormat: DecimalFormat =
            DecimalFormat("###,###,###,###")

    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        return if (value == 0f) {
            mFormat.format(value.toDouble()) + " " + unitName
        } else {
            mFormat.format(value.toDouble())
        }
    }
}