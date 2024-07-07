package iotdevice.com.iotDevice.barchart

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import iotdevice.com.iotDevice.R

import java.text.DecimalFormat

class XYMarkerView(context: Context, private val xAxisValueFormatter: IAxisValueFormatter, val enablePointDecimal: Boolean) : MarkerView(context, R.layout.custom_marker_view) {

    private val format =  if (enablePointDecimal) {
        DecimalFormat("###.0")
    } else {
        DecimalFormat("###")
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry?, highlight: Highlight?) {

        val tvText = xAxisValueFormatter.getFormattedValue(e!!.x, null) + ", " +
                format.format(e.y.toDouble())

        val tvContent = findViewById<TextView>(R.id.tvContent)
        tvContent.text = tvText

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

}
