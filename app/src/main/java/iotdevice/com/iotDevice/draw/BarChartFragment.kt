package iotdevice.com.iotDevice.draw

import android.arch.lifecycle.ViewModelProviders
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import iotdevice.com.iotDevice.common.ChartUtils
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.barchart.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class BarChartFragment: Fragment(), AnkoLogger, OnChartValueSelectedListener {

    var mOnValueSelectedRectF = RectF()

    lateinit var mTfRegular: Typeface
    lateinit var mTfLight: Typeface

    var deviceId: Long = 0

    lateinit var barChartViewModel: BarChartViewModel



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.barchart, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTfRegular = Typeface.createFromAsset(activity.assets, "OpenSans-Regular.ttf")
        mTfLight = Typeface.createFromAsset(activity.assets, "OpenSans-Light.ttf")

//        mBarChart = view!!.findViewById(R.id.chart1)
        bar_chart.setOnChartValueSelectedListener(this)

        bar_chart.setDrawBarShadow(false)
        bar_chart.setDrawValueAboveBar(true)
        bar_chart.isDoubleTapToZoomEnabled = false

        bar_chart.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        bar_chart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        bar_chart.setPinchZoom(false)

        bar_chart.setDrawGridBackground(false)


        val xAxisFormatter = DayAxisValueFormatter(bar_chart)

        val xAxis = bar_chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = mTfLight
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7
        xAxis.valueFormatter = xAxisFormatter

        val custom = MyAxisValueFormatter("H")

        val leftAxis = bar_chart.axisLeft
        leftAxis.typeface = mTfLight
        leftAxis.setLabelCount(8, false)
        leftAxis.valueFormatter = custom
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val axisRight = bar_chart.axisRight
        axisRight.setDrawLabels(false)
        axisRight.setDrawGridLines(false)

//        val rightAxis = mBarChart.axisRight
//        rightAxis.setDrawGridLines(false)
//        rightAxis.typeface = mTfLight
//        rightAxis.setLabelCount(8, false)
//        rightAxis.valueFormatter = custom
//        rightAxis.spaceTop = 15f
//        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val legend = bar_chart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.form = Legend.LegendForm.SQUARE
        legend.formSize = 9f
        legend.textSize = 11f
        legend.xEntrySpace = 4f
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        val mv = XYMarkerView(activity, xAxisFormatter)
        mv.chartView = bar_chart // For bounds control
        bar_chart.marker = mv // Set the marker to the chart
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
            it?.chartName?.apply {

                val axisItem = ChartUtils.getMappingAxis(this)

                axisItem?.let {
                    when(it.xAxisType) {
                        ChartUtils.AxisXType.Hour -> setXaxisFormatter(HourAxisValueFormatter(bar_chart))
                        ChartUtils.AxisXType.Month -> setXaxisFormatter(DayAxisValueFormatter(bar_chart))
                    }

                    val custom = MyAxisValueFormatter(it.yAxisUnit)
                    val leftAxis = bar_chart.axisLeft
                    leftAxis.valueFormatter = custom
                }
            }

            if (bar_chart.data != null && bar_chart.data.dataSetCount > 0) {
                set1 = bar_chart.data.getDataSetByIndex(0) as BarDataSet
                set1.values = it?.dataList
                bar_chart.data.notifyDataChanged()
                bar_chart.notifyDataSetChanged()

            } else {
                set1 = BarDataSet(it?.dataList, itemTitle)

//              set1.setDrawIcons(false)

                set1.setColors(*ColorTemplate.MATERIAL_COLORS)

                val dataSets = ArrayList<IBarDataSet>()
                dataSets.add(set1)

                val data = BarData(dataSets)
                data.setValueTextSize(10f)
                data.setValueTypeface(mTfLight)
                data.barWidth = 0.9f
                bar_chart.data = data
            }

            bar_chart.invalidate()
        })

        when(itemTitle) {
            resources.getString(R.string.hour_output_title) -> barChartViewModel.getTodayStatus(deviceId)
            resources.getString(R.string.day_output_title) -> barChartViewModel.getMonthStatus(deviceId)
            resources.getString(R.string.operation_time_title) -> barChartViewModel.getMonthOperationStatus(deviceId)
            resources.getString(R.string.average_output_title) -> barChartViewModel.getMonthPCSStatus(deviceId)
        }
    }

    fun setXaxisFormatter(formatter: IAxisValueFormatter) {
        val xAxis = bar_chart.xAxis
        xAxis.valueFormatter = formatter
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null) {
            return
        }

        val bounds = mOnValueSelectedRectF
        bar_chart.getBarBounds(e as BarEntry, bounds)
        val position = bar_chart.getPosition(e, YAxis.AxisDependency.LEFT)

        info(bounds.toString())
        info( position.toString())

        info("low: " + bar_chart.lowestVisibleX + ", high: "
                + bar_chart.highestVisibleX)

        MPPointF.recycleInstance(position)
    }

}