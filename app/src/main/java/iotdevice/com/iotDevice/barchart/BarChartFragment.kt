package iotdevice.com.iotDevice.barchart

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
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
import iotdevice.com.iotDevice.chart.ChartListItem
import iotdevice.com.iotDevice.common.ChartUtils
import iotdevice.com.iotDevice.common.ChartUtils.Companion.AVERAGE_OUTPUT
import iotdevice.com.iotDevice.common.ChartUtils.Companion.DAY_OUTPUT
import iotdevice.com.iotDevice.common.ChartUtils.Companion.HOUR_OUTPUT
import iotdevice.com.iotDevice.common.ChartUtils.Companion.OPERATION_TIME
import iotdevice.com.iotDevice.common.ChartUtils.Companion.averageProductvityUnit
import iotdevice.com.iotDevice.common.ChartUtils.Companion.hourUnit
import iotdevice.com.iotDevice.common.ChartUtils.Companion.productivityUnit
import iotdevice.com.iot_device.R
import iotdevice.com.iot_device.databinding.BarchartBinding
import kotlinx.android.synthetic.main.fragment_chart.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class BarChartFragment: Fragment(), AnkoLogger, OnChartValueSelectedListener {

    private var mOnValueSelectedRectF = RectF()

    lateinit var binding: BarchartBinding
    private lateinit var mTfRegular: Typeface
    private lateinit var mTfLight: Typeface
    private var deviceName: String = ""

    var itemTitle: String = ""

    var deviceId: Long = 0
    var fragmentTitle: String = ""



    private var instance: BarChartFragment? = null

    val barColor = intArrayOf(ColorTemplate.rgb("#ff3333"), ColorTemplate.rgb("#ff0088"),
            ColorTemplate.rgb("#a500cc"), ColorTemplate.rgb("#770077"))

    private lateinit var barChartViewModel: BarChartViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.barchart, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTfRegular = Typeface.createFromAsset(activity.assets, "OpenSans-Regular.ttf")
        mTfLight = Typeface.createFromAsset(activity.assets, "OpenSans-Light.ttf")

        binding.barChart.setOnChartValueSelectedListener(this)

        binding.barChart.setDrawBarShadow(false)
        binding.barChart.setDrawValueAboveBar(true)
        binding.barChart.isDoubleTapToZoomEnabled = false
        binding.barChart.description.isEnabled = false


        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        binding.barChart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        binding.barChart.setPinchZoom(false)

        binding.barChart.setDrawGridBackground(false)

        val xAxisFormatter = DayAxisValueFormatter(binding.barChart)

        val xAxis = binding.barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = mTfLight
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7
        xAxis.valueFormatter = xAxisFormatter

        val custom = MyAxisValueFormatter("H")

        val leftAxis = binding.barChart.axisLeft
        leftAxis.typeface = mTfLight
        leftAxis.setLabelCount(8, false)
        leftAxis.valueFormatter = custom
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val axisRight = binding.barChart.axisRight
        axisRight.setDrawLabels(false)
        axisRight.setDrawGridLines(false)

        val legend = binding.barChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.form = Legend.LegendForm.SQUARE
        legend.formSize = 9f
        legend.textSize = 11f
        legend.xEntrySpace = 4f
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val arguments = arguments

        val device = arguments.getParcelable<ChartListItem>("device")

        deviceId = device.deviceId
        itemTitle = device.title
        deviceName = arguments.getString("deviceName")


        barChartViewModel = ViewModelProviders.of(this).get(BarChartViewModel::class.java)

        barChartViewModel.yAxisData.observe(this, android.arch.lifecycle.Observer {

            swipeRefreshLayout.isRefreshing = false

            it?.chartName?.apply {

                when(this) {
                    HOUR_OUTPUT -> setLeftUnit(productivityUnit, it.leftAxisNumber)
                    DAY_OUTPUT -> setLeftUnit(productivityUnit, it.leftAxisNumber)
                    OPERATION_TIME -> setLeftUnit(hourUnit, it.leftAxisNumber)
                    AVERAGE_OUTPUT -> setLeftUnit(averageProductvityUnit, it.leftAxisNumber)
                }

                val axisItem = ChartUtils.getMappingAxis(this)

                axisItem?.run {
                    when(this.xAxisType) {
                        ChartUtils.AxisXType.Hour -> {
                            setAxisXFormatter(HourAxisValueFormatter(binding.barChart))
                            val mv = XYMarkerView(activity, HourAxisValueFormatter(binding.barChart), it.enablePointDecimal)
                            mv.chartView = binding.barChart // For bounds control
                            binding.barChart.marker = mv // Set the marker to the chart
                        }
                        ChartUtils.AxisXType.Month -> {
                            setAxisXFormatter(DayAxisValueFormatter(binding.barChart))
                            val mv = XYMarkerView(activity, DayAxisValueFormatter(binding.barChart), it.enablePointDecimal)
                            mv.chartView = binding.barChart // For bounds control
                            binding.barChart.marker = mv // Set the marker to the chart
                        }
                    }

                    if (binding.barChart.data != null && binding.barChart.data.dataSetCount > 0) {
                        val set1 = binding.barChart.data.getDataSetByIndex(0) as BarDataSet
                        set1.values = it.dataList
                        binding.barChart.data.notifyDataChanged()
                        binding.barChart.notifyDataSetChanged()

                    } else {
                        val set1 = BarDataSet(it.dataList, itemTitle)

                        set1.setColors(*barColor)

                        val dataSets = ArrayList<IBarDataSet>()
                        dataSets.add(set1)

                        val data = BarData(dataSets)
                        data.setValueTextSize(10f)
                        data.setValueTypeface(mTfLight)
                        data.barWidth = 0.9f
                        data.setValueFormatter(ValueFormatter(false))

                        binding.barChart.data = data
                    }

                    binding.barChartBottomInfo = it.barChartBottomInfo

                    binding.barChart.animateY(3000)
                    binding.barChart.invalidate()
                }
            }
        })

        // Don't display error msg to users
        barChartViewModel.errorRes.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = false
//            if (it?.message?.contains("size is 0") == true) {
//                DialogUtils.createAlertDialog(context, getString(R.string.chart_title), getString(R.string.no_date_str), activity::onBackPressed)
//            } else {
//                DialogUtils.createAlertDialog(context, getString(R.string.chart_title))
//            }

            info("error msg : {${it?.message}}")
        })

        swipeRefreshLayout.setOnRefreshListener({
            onRefresh(itemTitle)
        })

    }

    override fun onResume() {
        super.onResume()
        onRefresh(itemTitle)


    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        info { "test" }
//        outState?.putSerializable(STATE_ITEMS, mItems)
    }

    private fun onRefresh(itemTitle : String) {
        when(itemTitle) {
            resources.getString(R.string.hour_output_title) -> {
                val titleStr = deviceName + " " + getString(R.string.day_chart_title)
                barChartViewModel.getTodayStatus(deviceId)
            }
            resources.getString(R.string.day_output_title) -> {
                val titleStr = deviceName + " " + getString(R.string.month_chart_title)
                barChartViewModel.getMonthStatus(deviceId)
            }
            resources.getString(R.string.operation_time_title) -> {
                val titleStr = deviceName + " " + getString(R.string.operation_time_title)
                barChartViewModel.getMonthOperationStatus(deviceId)
            }
            resources.getString(R.string.average_output_title) -> {
                val titleStr = deviceName + " " + getString(R.string.average_output_title)
                barChartViewModel.getMonthPCSStatus(deviceId)
            }
        }
    }


    private fun setLeftUnit(unit: String, labelCount: Int = 6) {
        val leftAxis = binding.barChart.axisLeft
        leftAxis.labelCount = labelCount
        leftAxis.valueFormatter = MyAxisValueFormatter(unit)
    }

    private fun setAxisXFormatter(formatter: IAxisValueFormatter) {
        val xAxis = binding.barChart.xAxis
        xAxis.labelRotationAngle = -20f
        xAxis.valueFormatter = formatter
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null) {
            return
        }

        val bounds = mOnValueSelectedRectF
        binding.barChart.getBarBounds(e as BarEntry, bounds)
        val position = binding.barChart.getPosition(e, YAxis.AxisDependency.LEFT)

        info("low: " + binding.barChart.lowestVisibleX + ", high: "
                + binding.barChart.highestVisibleX)

        MPPointF.recycleInstance(position)
    }

    fun newInstance(bundle: Bundle): BarChartFragment {

        return if (instance == null) {
            instance = BarChartFragment()
            instance?.arguments = bundle
            instance!!
        } else {
            instance?.arguments = bundle
            instance!!
        }
    }
}