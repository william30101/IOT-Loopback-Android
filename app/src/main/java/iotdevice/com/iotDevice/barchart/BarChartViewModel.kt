package iotdevice.com.iotDevice.barchart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.BarEntry
import com.strongloop.android.loopback.RestAdapter
import com.strongloop.android.loopback.callbacks.ListCallback
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.common.ChartUtils.Companion.averageProductvityUnit
import iotdevice.com.iotDevice.common.ChartUtils.Companion.dayUnit
import iotdevice.com.iotDevice.common.ChartUtils.Companion.hourUnit
import iotdevice.com.iotDevice.common.ChartUtils.Companion.productivityUnit
import iotdevice.com.iotDevice.common.ChartUtils.Companion.resource
import iotdevice.com.iotDevice.model.DeviceStatusModel
import iotdevice.com.iotDevice.repository.DeviceStatusRepository
import iotdevice.com.iot_device.R
import java.util.*

class BarChartViewModel: ViewModel() {

    private val adapter: RestAdapter by lazy { App.sInstance.loopBackAdapter }
    private val deviceStatusRepository: DeviceStatusRepository by lazy { adapter.createRepository(DeviceStatusRepository::class.java) }
    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH)


    val yAxisData: MutableLiveData<ChartData> = MutableLiveData()
    val errorRes: MutableLiveData<Throwable> = MutableLiveData()

    fun getTodayStatus(deviceId: Long?) {
        deviceStatusRepository.findTodayStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                // TODO: Put each login to different place

                // Fix a bug, Server will generate two records if
                val todayStatus = objects?.last()

                todayStatus?.run {
                    val deviceStatusList : Map<Int, Float> = mapOf(
                        0 to productivity0.toFloat(),
                        1 to productivity1.toFloat(),
                        2 to productivity2.toFloat(),
                        3 to productivity3.toFloat(),
                        4 to productivity4.toFloat(),
                        5 to productivity5.toFloat(),
                        6 to productivity6.toFloat(),
                        7 to productivity7.toFloat(),
                        8 to productivity8.toFloat(),
                        9 to productivity9.toFloat(),
                        10 to productivity10.toFloat(),
                        11 to productivity11.toFloat(),
                        12 to productivity12.toFloat(),
                        13 to productivity13.toFloat(),
                        14 to productivity14.toFloat(),
                        15 to productivity15.toFloat(),
                        16 to productivity16.toFloat(),
                        17 to productivity17.toFloat(),
                        18 to productivity18.toFloat(),
                        19 to productivity19.toFloat(),
                        20 to productivity20.toFloat(),
                        21 to productivity21.toFloat(),
                        22 to productivity22.toFloat(),
                        23 to productivity23.toFloat())

                    val totalOfDay =
                            productivity0 +
                            productivity1 +
                            productivity2 +
                            productivity3 +
                            productivity4 +
                            productivity5 +
                            productivity6 +
                            productivity7 +
                            productivity8 +
                            productivity9 +
                            productivity10 +
                            productivity11 +
                            productivity12 +
                            productivity13 +
                            productivity14 +
                            productivity15 +
                            productivity16 +
                            productivity17 +
                            productivity18 +
                            productivity19 +
                            productivity20 +
                            productivity21 +
                            productivity22 +
                            productivity23

                    val maxData = deviceStatusList.maxBy { it.value }

                    val currentHour = GregorianCalendar().get(Calendar.HOUR_OF_DAY)

                    val bottomInfo = BarChartBottomInfo(
                        resource.getString(R.string.total_of_day_title),
                        totalOfDay.toString() + productivityUnit,
                        resource.getString(R.string.maximum_productivity_of_day_title),
                        maxData?.value.toString()  + productivityUnit,
                        resource.getString(R.string.maximum_hour_of_day_title),
                        maxData?.key.toString()  + hourUnit,
                        resource.getString(R.string.current_productivity_of_day_title),
                        deviceStatusList[currentHour].toString()  + productivityUnit
                    )

                    setOutputData("HourOutput", deviceStatusList, bottomInfo, true, getLeftAxisNumber(maxData))
                } ?: run {
                    Log.e(tag, "No Data")
                }
            }

            override fun onError(t: Throwable?) {
                Log.e(tag, "error : $t")

                val deviceStatusList = mutableMapOf<Int, Float>()

                for (i in 0..23)
                {
                    deviceStatusList[i] = 0f
                }

                val bottomInfo = BarChartBottomInfo(
                        resource.getString(R.string.total_of_day_title),
                        "0 $productivityUnit",
                        resource.getString(R.string.maximum_productivity_of_day_title),
                        "0 $productivityUnit",
                        resource.getString(R.string.maximum_hour_of_day_title),
                        "0 $hourUnit",
                        resource.getString(R.string.current_productivity_of_day_title),
                        "0 $productivityUnit"
                )

                val maxData = deviceStatusList.maxBy { it.value }

                setOutputData("HourOutput", deviceStatusList, bottomInfo, true, getLeftAxisNumber(maxData))

                errorRes.value = t
            }
        })
    }

    fun getMonthStatus(deviceId: Long?) {

        deviceStatusRepository.findMonthStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                val dayTotalList: MutableMap<Int, Float> = mutableMapOf()

                initCurrentMonthDays(dayTotalList)

                var totalOfMonth = 0f

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

                    totalOfMonth += dayTotal.toFloat()

                    dayTotalList[calendar.get(Calendar.DAY_OF_MONTH)] = dayTotal.toFloat()
                }

                val maxDate = dayTotalList.maxBy { it.value }

                val currentDate = GregorianCalendar().get(Calendar.DAY_OF_MONTH)

                val bottomInfo = BarChartBottomInfo(resource.getString(R.string.total_of_month_title),
                        totalOfMonth.toString() + productivityUnit,
                        resource.getString(R.string.maximum_productivity_of_month_title),
                        maxDate?.value.toString() + productivityUnit,
                        resource.getString(R.string.maximum_day_of_month_title),
                        maxDate?.key.toString() +dayUnit,
                        resource.getString(R.string.current_productivity_of_month_title),
                        dayTotalList[currentDate].toString() + productivityUnit)

                setOutputData("DayOutput", dayTotalList, bottomInfo, true, getLeftAxisNumber(maxDate))
            }

            override fun onError(t: Throwable?) {
                Log.i(tag, "error : $t")

                val dayTotalList: MutableMap<Int, Float> = mutableMapOf()
                initCurrentMonthDays(dayTotalList)

                val maxDate = dayTotalList.maxBy { it.value }

                val bottomInfo = BarChartBottomInfo(resource.getString(R.string.total_of_month_title),
                        "0 $productivityUnit",
                        resource.getString(R.string.maximum_productivity_of_month_title),
                        "0 $productivityUnit",
                        resource.getString(R.string.maximum_day_of_month_title),
                        "0 $dayUnit",
                        resource.getString(R.string.current_productivity_of_month_title),
                        "0 $productivityUnit")

                setOutputData("DayOutput", dayTotalList, bottomInfo, true, getLeftAxisNumber(maxDate))

                errorRes.value = t
            }
        })
    }

    fun getMonthOperationStatus(deviceId: Long?) {

        deviceStatusRepository.findMonthStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                val dayTotalList: MutableMap<Int, Float> = mutableMapOf()
                var operationTimeTotal = 0f

                initCurrentMonthDays(dayTotalList)

                objects?.forEach {

                    val dayTotal = it.operationTime / 60
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = it.timeStamp!!.toLong()
                    operationTimeTotal += Math.round(dayTotal.toFloat())

                    dayTotalList[calendar.get(Calendar.DAY_OF_MONTH)] = dayTotal.toFloat()
                }

                val maxDate = dayTotalList.maxBy { it.value }
                val currentDate = GregorianCalendar().get(Calendar.DAY_OF_MONTH)

                val bottomInfo = BarChartBottomInfo(
                        resource.getString(R.string.total_operation_time_of_month_title),
                        Math.round(operationTimeTotal).toString() + hourUnit,
                        resource.getString(R.string.maximum_operation_time_of_month_title),
                        Math.round(maxDate?.value ?: 0f).toString() + hourUnit,
                        resource.getString(R.string.maximum_day_operation_time_of_month_title),
                        maxDate?.key.toString() + dayUnit,
                        resource.getString(R.string.current_operation_time_of_month_title),
                        Math.round(dayTotalList[currentDate] ?: 0f).toString() + hourUnit)

                setOutputData("OperationTime", dayTotalList, bottomInfo, false, getLeftAxisNumber(maxDate))
            }

            override fun onError(t: Throwable?) {
                Log.i(tag, "error : $t")

                val dayTotalList: MutableMap<Int, Float> = mutableMapOf()

                initCurrentMonthDays(dayTotalList)

                val maxDate = dayTotalList.maxBy { it.value }

                val bottomInfo = BarChartBottomInfo(
                        resource.getString(R.string.total_operation_time_of_month_title),
                        "0 $hourUnit",
                        resource.getString(R.string.maximum_operation_time_of_month_title),
                        "0 $hourUnit",
                        resource.getString(R.string.maximum_day_operation_time_of_month_title),
                        "0 $dayUnit",
                        resource.getString(R.string.current_operation_time_of_month_title),
                        "0 $hourUnit")

                setOutputData("OperationTime", dayTotalList, bottomInfo, false, getLeftAxisNumber(maxDate))


                errorRes.value = t
            }
        })
    }

    fun getMonthPCSStatus(deviceId: Long?) {

        deviceStatusRepository.findMonthStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                val dayTotalList: MutableMap<Int, Float> = mutableMapOf()

                initCurrentMonthDays(dayTotalList)

                objects?.forEach {
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

                    var avgProduction: Long = 0

                    if (dayOperationTime != 0L) {
                        avgProduction = dayOutputPCS / dayOperationTime
                    }

                    dayTotalList[calendar.get(Calendar.DAY_OF_MONTH)] = avgProduction.toFloat()
                }

                val maxDate = dayTotalList.maxBy { it.value }

                val currentDate = GregorianCalendar().get(Calendar.DAY_OF_MONTH)

                val bottomInfo = BarChartBottomInfo(
                        resource.getString(R.string.maximum_operation_speed_of_month_title),
                        maxDate?.value.toString() + averageProductvityUnit,
                        resource.getString(R.string.maximum_operation_speed_day_of_month_title),
                        maxDate?.key.toString() + dayUnit,
                        resource.getString(R.string.current_operation_speed_of_month_title),
                        dayTotalList[currentDate].toString() + averageProductvityUnit)

                setOutputData("AverageOutput", dayTotalList, bottomInfo, true, getLeftAxisNumber(maxDate))
            }

            override fun onError(t: Throwable?) {
                Log.i(tag, "error : $t")

                val dayTotalList: MutableMap<Int, Float> = mutableMapOf()

                initCurrentMonthDays(dayTotalList)

                val maxDate = dayTotalList.maxBy { it.value }

                val bottomInfo = BarChartBottomInfo(
                        resource.getString(R.string.maximum_operation_speed_of_month_title),
                        "0 $averageProductvityUnit",
                        resource.getString(R.string.maximum_operation_speed_day_of_month_title),
                        "0 $dayUnit",
                        resource.getString(R.string.current_operation_speed_of_month_title),
                        "0 $averageProductvityUnit")


                setOutputData("AverageOutput", dayTotalList, bottomInfo, true, getLeftAxisNumber(maxDate))

                errorRes.value = t
            }
        })
    }

    private fun setOutputData(
            chartName: String,
            dataList: Map<Int, Float>,
            bottomInfo: BarChartBottomInfo,
            enablePointDecimal: Boolean,
            leftAxisNumber: Int) {

        val yValueList = ArrayList<BarEntry>()
        dataList.forEach { yValueList.add(BarEntry(it.key.toFloat(),it.value)) }
        yAxisData.value = ChartData(chartName, yValueList, bottomInfo, enablePointDecimal, leftAxisNumber)
    }

    fun initCurrentMonthDays(monthMap: MutableMap<Int, Float>) {
        val year =  Calendar.getInstance().get(Calendar.YEAR)
        Calendar.getInstance().set(year, currentMonth, 1)
        val days = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..days) {
            monthMap[i] = 0f
        }
    }

    companion object {
        const val tag = "BarChartViewModel"
    }
}

fun getLeftAxisNumber(maxData: Map.Entry<Int, Float>?): Int {

    var leftAxisNumber = 2

    maxData?.apply {
        leftAxisNumber = if (value > 6) {
            6
        } else {
            Math.round(value)
        }
    }

    return leftAxisNumber
}



data class ChartData(
        val chartName: String,
        val dataList: ArrayList<BarEntry>,
        val barChartBottomInfo: BarChartBottomInfo,
        val enablePointDecimal: Boolean,
        val leftAxisNumber: Int)
data class BarChartBottomInfo(val line1Title: String,val line1Value: String,
                              val line2Title: String,val line2Value: String,
                              val line3Title: String,val line3Value: String,
                              val line4Title: String = "",val line4Value: String = "")