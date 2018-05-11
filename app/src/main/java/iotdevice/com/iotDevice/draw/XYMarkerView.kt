package iotdevice.com.iotDevice.draw

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import iotdevice.com.iot_device.R
import java.text.DecimalFormat

class XYMarkerView(context: Context, private val xAxisValueFormatter: IAxisValueFormatter) : MarkerView(context, R.layout.custom_marker_view) {

    private val tvContent: TextView

    private val format: DecimalFormat

    init {
        tvContent = findViewById(R.id.tvContent) as TextView
        format = DecimalFormat("###.0")
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry?, highlight: Highlight?) {

        val tvText = "x: " + xAxisValueFormatter.getFormattedValue(e!!.x, null) + ", y: " +
                format.format(e.y.toDouble())

        tvContent.text = tvText

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}
