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
import com.strongloop.android.loopback.RestAdapter
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

    private val adapter: RestAdapter by lazy { App.sInstance.loopBackAdapter }
    private val deviceStatusRepository: DeviceStatusRepository by lazy { adapter.createRepository(DeviceStatusRepository::class.java) }

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

        val mv = XYMarkerView(this, xAxisFormatter)
        mv.chartView = mBarChart // For bounds control
        mBarChart.marker = mv // Set the marker to the chart


//        getMonthStatus(deviceId)
//        getTodayStatus(deviceId)
//        getMonthOperationStatus(deviceId)
        getMonthPCSStatus(deviceId)
    }

    fun getTodayStatus(deviceId: Long?) {
        deviceStatusRepository.findTodayStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                // TODO: Put each login to different place
                val todayStatus = objects!![0]

                val deviceStatusList : HashMap<Int, Float>  = hashMapOf(
                        0 to todayStatus.productivity0.toFloat(),
                        1 to todayStatus.productivity1.toFloat(),
                        2 to todayStatus.productivity2.toFloat(),
                        3 to todayStatus.productivity3.toFloat(),
                        4 to todayStatus.productivity4.toFloat(),
                        5 to todayStatus.productivity5.toFloat(),
                        6 to todayStatus.productivity6.toFloat(),
                        7 to todayStatus.productivity7.toFloat(),
                        8 to todayStatus.productivity8.toFloat(),
                        9 to todayStatus.productivity9.toFloat(),
                        10 to todayStatus.productivity10.toFloat(),
                        11 to todayStatus.productivity11.toFloat(),
                        12 to todayStatus.productivity12.toFloat(),
                        13 to todayStatus.productivity13.toFloat(),
                        14 to todayStatus.productivity14.toFloat(),
                        15 to todayStatus.productivity15.toFloat(),
                        16 to todayStatus.productivity16.toFloat(),
                        17 to todayStatus.productivity17.toFloat(),
                        18 to todayStatus.productivity18.toFloat(),
                        19 to todayStatus.productivity19.toFloat(),
                        20 to todayStatus.productivity20.toFloat(),
                        21 to todayStatus.productivity21.toFloat(),
                        22 to todayStatus.productivity22.toFloat(),
                        23 to todayStatus.productivity23.toFloat())


                setOutputData(deviceStatusList, "HourOutput")
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
            }
        })
    }

    fun getMonthStatus(deviceId: Long?) {

        deviceStatusRepository.findMonthStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                val dayTotalList: HashMap<Int, Float> = hashMapOf()

                objects!!.forEach {

                    val dayTotal =
                            it.productivity0 +
                            it.productivity1 +
                            it.productivity2 +
                            it.productivity3 +
                            it.productivity4 +
                            it.productivity5 +
                            it.productivity6 +
                            it.productivity7 +
                            it.productivity8 +
                            it.productivity9 +
                            it.productivity10 +
                            it.productivity11 +
                            it.productivity12 +
                            it.productivity13 +
                            it.productivity14 +
                            it.productivity15 +
                            it.productivity16 +
                            it.productivity17 +
                            it.productivity18 +
                            it.productivity19 +
                            it.productivity20 +
                            it.productivity21 +
                            it.productivity22 +
                            it.productivity23

                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = it.timeStamp!!.toLong()

                    dayTotalList[calendar.get(Calendar.DAY_OF_MONTH)] = dayTotal.toFloat()
                }

                setOutputData(dayTotalList, "DayOutput")
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
            }
        })
    }

    fun getMonthOperationStatus(deviceId: Long?) {

        deviceStatusRepository.findMonthStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                val dayTotalList: HashMap<Int, Float> = hashMapOf()

                objects!!.forEach {

                    val dayTotal = it.operationTime
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = it.timeStamp!!.toLong()

                    dayTotalList[calendar.get(Calendar.DAY_OF_MONTH)] = dayTotal.toFloat()
                }

                setOutputData(dayTotalList, "DayOperationTime")
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
            }
        })
    }

    fun getMonthPCSStatus(deviceId: Long?) {

        deviceStatusRepository.findMonthStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                val dayTotalList: HashMap<Int, Float> = hashMapOf()

                objects!!.forEach {


                    val dayOutputPCS =
                            it.productivity0 +
                            it.productivity1 +
                            it.productivity2 +
                            it.productivity3 +
                            it.productivity4 +
                            it.productivity5 +
                            it.productivity6 +
                            it.productivity7 +
                            it.productivity8 +
                            it.productivity9 +
                            it.productivity10 +
                            it.productivity11 +
                            it.productivity12 +
                            it.productivity13 +
                            it.productivity14 +
                            it.productivity15 +
                            it.productivity16 +
                            it.productivity17 +
                            it.productivity18 +
                            it.productivity19 +
                            it.productivity20 +
                            it.productivity21 +
                            it.productivity22 +
                            it.productivity23

                    val dayOperationTime = it.operationTime
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = it.timeStamp!!.toLong()

                    val avgProduction = dayOutputPCS / dayOperationTime

                    dayTotalList[calendar.get(Calendar.DAY_OF_MONTH)] = avgProduction.toFloat()
                }

                setOutputData(dayTotalList, "DayAverageOutput")
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
            }
        })
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

        info("low: " + mBarChart.getLowestVisibleX() + ", high: "
                + mBarChart.highestVisibleX)

        MPPointF.recycleInstance(position)
    }

    private fun setOutputData(dataList: HashMap<Int, Float>, labelName: String) {

        val yValueList = ArrayList<BarEntry>()

        dataList.forEach { yValueList.add(BarEntry(it.key.toFloat(),it.value)) }

        val set1: BarDataSet

        if (mBarChart.data != null && mBarChart.data.dataSetCount > 0) {
            set1 = mBarChart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = yValueList
            mBarChart.data.notifyDataChanged()
            mBarChart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(yValueList, labelName)

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