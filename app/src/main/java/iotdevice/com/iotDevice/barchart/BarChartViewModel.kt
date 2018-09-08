package iotdevice.com.iotDevice.barchart

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
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
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

class BarChartViewModel: ViewModel(), AnkoLogger {

    private val adapter: RestAdapter by lazy { App.sInstance.loopBackAdapter }
    private val deviceStatusRepository: DeviceStatusRepository by lazy { adapter.createRepository(DeviceStatusRepository::class.java) }
    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH)


    val yAxisData: MutableLiveData<ChartData> = MutableLiveData()
    val errorRes: MutableLiveData<Throwable> = MutableLiveData()

    fun getTodayStatus(deviceId: Long?) {
        deviceStatusRepository.findTodayStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                // TODO: Put each login to different place
                val todayStatus = objects!![0]

                val deviceStatusList : Map<Int, Float> = mapOf(
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

                val totalOfDay = todayStatus.productivity0 +
                        todayStatus.productivity1 +
                        todayStatus.productivity2 +
                        todayStatus.productivity3 +
                        todayStatus.productivity4 +
                        todayStatus.productivity5 +
                        todayStatus.productivity6 +
                        todayStatus.productivity7 +
                        todayStatus.productivity8 +
                        todayStatus.productivity9 +
                        todayStatus.productivity10 +
                        todayStatus.productivity11 +
                        todayStatus.productivity12 +
                        todayStatus.productivity13 +
                        todayStatus.productivity14 +
                        todayStatus.productivity15 +
                        todayStatus.productivity16 +
                        todayStatus.productivity17 +
                        todayStatus.productivity18 +
                        todayStatus.productivity19 +
                        todayStatus.productivity20 +
                        todayStatus.productivity21 +
                        todayStatus.productivity22 +
                        todayStatus.productivity23

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
            }

            override fun onError(t: Throwable?) {
                info("error : $t")

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
                info("error : $t")

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
                info("error : $t")

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
                info("error : $t")

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