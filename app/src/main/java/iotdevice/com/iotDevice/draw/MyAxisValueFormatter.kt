package iotdevice.com.iotDevice.draw

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.DecimalFormat

class MyAxisValueFormatter(val unitName: String) : IAxisValueFormatter {

    private val mFormat: DecimalFormat = DecimalFormat("###,###,###,##0.0")

    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        return mFormat.format(value.toDouble()) + " " + unitName
    }
}