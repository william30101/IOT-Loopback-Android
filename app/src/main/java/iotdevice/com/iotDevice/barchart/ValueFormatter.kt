package iotdevice.com.iotDevice.barchart

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat

open class ValueFormatter(val enableDecimalPoint: Boolean): ValueFormatter() {


    private val mFormat: DecimalFormat =
            if (enableDecimalPoint) {
                DecimalFormat("###,###,###,##0.0")
            } else {
                DecimalFormat("###,###,###,###")
            }

    override fun getFormattedValue(value: Float, entry: Entry?, dataSetIndex: Int, viewPortHandler: ViewPortHandler?): String {
        return mFormat.format(value)
    }
}