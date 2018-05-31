package iotdevice.com.iotDevice.draw

import android.graphics.RectF
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.strongloop.android.loopback.callbacks.ListCallback
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.model.DeviceStatusModel
import iotdevice.com.iotDevice.repository.DeviceStatusRepository
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.barchart.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

class BarChartActivity: AppCompatActivity(), AnkoLogger, OnChartValueSelectedListener {

    var mOnValueSelectedRectF = RectF()
    lateinit var mBarChart: BarChart

    lateinit var mTfRegular: Typeface
    lateinit var mTfLight: Typeface

    var deviceId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.barchart)


        val deviceId = this.intent.getBundleExtra("homeBundle").getLong("deviceId")
        info("deviceId : $deviceId")


        mTfRegular = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")
        mTfLight = Typeface.createFromAsset(assets, "OpenSans-Light.ttf")


        mBarChart = chart1 as BarChart
        mBarChart.setOnChartValueSelectedListener(this)

        mBarChart.setDrawBarShadow(false)
        mBarChart.setDrawValueAboveBar(true)

        mBarChart.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mBarChart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false)

        mBarChart.setDrawGridBackground(false)


        val xAxisFormatter = DayAxisValueFormatter(mBarChart)

        val xAxis = mBarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = mTfLight
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7
        xAxis.valueFormatter = xAxisFormatter

        val custom = MyAxisValueFormatter()

        val leftAxis = mBarChart.axisLeft
        leftAxis.typeface = mTfLight
        leftAxis.setLabelCount(8, false)
        leftAxis.valueFormatter = custom
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val rightAxis = mBarChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.typeface = mTfLight
        rightAxis.setLabelCount(8, false)
        rightAxis.valueFormatter = custom
        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val l = mBarChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = Legend.LegendForm.SQUARE
        l.formSize = 9f
        l.textSize = 11f
        l.xEntrySpace = 4f
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        val mv = XYMarkerView(this, xAxisFormatter)
        mv.chartView = mBarChart // For bounds control
        mBarChart.marker = mv // Set the marker to the chart

        getDeviceStatus(deviceId)
    }

    fun getDeviceStatus(deviceId: Long?) {
        val adapter = App.sInstance.loopBackAdapter
        val deviceStatusRepository = adapter.createRepository(DeviceStatusRepository::class.java)

        deviceStatusRepository.findNewestStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {
                info("bootime is :" + objects!![0].bootTime)
                setData(12, 50f, objects[0])
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
            }
        })

    }

    override fun onNothingSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null) {
            return
        }

        val bounds = mOnValueSelectedRectF
        mBarChart.getBarBounds(e as BarEntry, bounds)
        val position = mBarChart.getPosition(e, YAxis.AxisDependency.LEFT)

        info(bounds.toString())
        info( position.toString())

        info("low: " + mBarChart.getLowestVisibleX() + ", high: "
                + mBarChart.highestVisibleX)

        MPPointF.recycleInstance(position)
    }

    private fun setData(count: Int, range: Float, deviceStatus: DeviceStatusModel) {

        val start = 1f

        val yVals1 = ArrayList<BarEntry>()

        var i = start.toInt()
        while (i < start + count.toFloat()) {

            yVals1.add(BarEntry(i.toFloat(), deviceStatus.productivity0.toFloat(), null, null))


//            val mult = range + 1
//            val dataVal = (Math.random() * mult).toFloat()
//
//            if (Math.random() * 100 < 25) {
//                yVals1.add(BarEntry(i.toFloat(), dataVal, resources.getDrawable(R.drawable.star, null)))
//            } else {
//                yVals1.add(BarEntry(i.toFloat(), dataVal))
//            }
            i++
        }

        val set1: BarDataSet

        if (mBarChart.data != null && mBarChart.data.dataSetCount > 0) {
            set1 = mBarChart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = yVals1
            mBarChart.data.notifyDataChanged()
            mBarChart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(yVals1, "The year 2017")

//            set1.setDrawIcons(false)

            set1.setColors(*ColorTemplate.MATERIAL_COLORS)

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)

            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setValueTypeface(mTfLight)
            data.barWidth = 0.9f

            mBarChart.data = data
        }
    }


}