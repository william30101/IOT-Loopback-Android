package iotdevice.com.iotDevice.draw

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.mikephil.charting.data.BarEntry
import com.strongloop.android.loopback.RestAdapter
import com.strongloop.android.loopback.callbacks.ListCallback
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.model.DeviceStatusModel
import iotdevice.com.iotDevice.repository.DeviceStatusRepository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

class BarChartViewModel: ViewModel(), AnkoLogger {

    private val adapter: RestAdapter by lazy { App.sInstance.loopBackAdapter }
    private val deviceStatusRepository: DeviceStatusRepository by lazy { adapter.createRepository(DeviceStatusRepository::class.java) }

    val yAxisData: MutableLiveData<ChartData> = MutableLiveData()

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


                setOutputData("HourOutput", deviceStatusList)
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
            }
        })
    }

    fun getMonthStatus(deviceId: Long?) {

        deviceStatusRepository.findMonthStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                val dayTotalList: MutableMap<Int, Float> = mutableMapOf()

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

                setOutputData("DayOutput", dayTotalList)
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
            }
        })
    }

    fun getMonthOperationStatus(deviceId: Long?) {

        deviceStatusRepository.findMonthStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                val dayTotalList: MutableMap<Int, Float> = mutableMapOf()

                objects!!.forEach {

                    val dayTotal = it.operationTime
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = it.timeStamp!!.toLong()

                    dayTotalList[calendar.get(Calendar.DAY_OF_MONTH)] = dayTotal.toFloat()
                }

                setOutputData("OperationTime", dayTotalList)
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
            }
        })
    }

    fun getMonthPCSStatus(deviceId: Long?) {

        deviceStatusRepository.findMonthStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                val dayTotalList: MutableMap<Int, Float> = mutableMapOf()

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

                setOutputData("AverageOutput", dayTotalList)
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
            }
        })
    }

    private fun setOutputData(chartName: String, dataList: Map<Int, Float>) {

        val yValueList = ArrayList<BarEntry>()
        dataList.forEach { yValueList.add(BarEntry(it.key.toFloat(),it.value)) }
        yAxisData.value = ChartData(chartName, yValueList)
    }
}

data class ChartData(val chartName: String, val dataList: ArrayList<BarEntry>)