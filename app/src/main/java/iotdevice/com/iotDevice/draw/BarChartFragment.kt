package iotdevice.com.iotDevice.draw

import android.arch.lifecycle.ViewModelProviders
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.strongloop.android.loopback.RestAdapter
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.repository.DeviceStatusRepository
import iotdevice.com.iot_device.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

class BarChartFragment: Fragment(), AnkoLogger, OnChartValueSelectedListener {

    var mOnValueSelectedRectF = RectF()
    lateinit var mBarChart: BarChart

    lateinit var mTfRegular: Typeface
    lateinit var mTfLight: Typeface

    var deviceId: Long = 0

    private val adapter: RestAdapter by lazy { App.sInstance.loopBackAdapter }
    private val deviceStatusRepository: DeviceStatusRepository by lazy { adapter.createRepository(DeviceStatusRepository::class.java) }
    lateinit var barChartViewModel: BarChartViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.barchart, container, false)

        mTfRegular = Typeface.createFromAsset(activity.assets, "OpenSans-Regular.ttf")
        mTfLight = Typeface.createFromAsset(activity.assets, "OpenSans-Light.ttf")


        mBarChart = view!!.findViewById(R.id.chart1)
        mBarChart.setOnChartValueSelectedListener(this)

        mBarChart.setDrawBarShadow(false)
        mBarChart.setDrawValueAboveBar(true)
        mBarChart.isDoubleTapToZoomEnabled = false

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

        val mv = XYMarkerView(activity, xAxisFormatter)
        mv.chartView = mBarChart // For bounds control
        mBarChart.marker = mv // Set the marker to the chart

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val arguments = arguments

        val deviceId = arguments.getLong("deviceId")
        val itemTitle = arguments.getString("itemTitle")

        info("deviceId : $deviceId")

        barChartViewModel = ViewModelProviders.of(this).get(BarChartViewModel::class.java)

        barChartViewModel.yAxisData.observe(this, android.arch.lifecycle.Observer {
            info("return data : $it")

            val set1: BarDataSet

            if (mBarChart.data != null && mBarChart.data.dataSetCount > 0) {
                set1 = mBarChart.data.getDataSetByIndex(0) as BarDataSet
                set1.values = it
                mBarChart.data.notifyDataChanged()
                mBarChart.notifyDataSetChanged()

            } else {
                set1 = BarDataSet(it, itemTitle)

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

            mBarChart.invalidate()
        })

        when(itemTitle) {
            resources.getString(R.string.hour_output_title) -> barChartViewModel.getTodayStatus(deviceId)
            resources.getString(R.string.day_output_title) -> barChartViewModel.getMonthStatus(deviceId)
            resources.getString(R.string.operation_time_title) -> barChartViewModel.getMonthOperationStatus(deviceId)
            resources.getString(R.string.average_output_title) -> barChartViewModel.getMonthPCSStatus(deviceId)
        }
    }

    override fun onNothingSelected() {
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

        info("low: " + mBarChart.lowestVisibleX + ", high: "
                + mBarChart.highestVisibleX)

        MPPointF.recycleInstance(position)
    }

}