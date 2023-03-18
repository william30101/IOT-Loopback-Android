package iotdevice.com.iotDevice.chart

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strongloop.android.loopback.callbacks.ListCallback
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.common.ChartUtils.Companion.calHourAndMinutes
import iotdevice.com.iotDevice.model.DeviceStatusModel
import iotdevice.com.iotDevice.repository.DeviceStatusRepository
import iotdevice.com.iot_device.R
import java.util.*

class ChartViewModel : ViewModel() {

    val errorGetChart: MutableLiveData<Any> = MutableLiveData()

    val headerItemLiveData: MutableLiveData<ChartHeaderItem> = MutableLiveData()
    val currentDataLiveData: MutableLiveData<Map<String, Long>> = MutableLiveData()
    val adapter = App.sInstance.loopBackAdapter
    val repository: DeviceStatusRepository? = adapter.createRepository(DeviceStatusRepository::class.java)
    private val resource: Resources by lazy { App.sInstance.resources }

    var totalOfDay: MutableLiveData<Long> = MutableLiveData()
    var totalOfMonth: MutableLiveData<Long> = MutableLiveData()
    var totalOfOperationTime: MutableLiveData<Long> = MutableLiveData()
    var totalOfPCS: MutableLiveData<Long> = MutableLiveData()


    fun fetchCurrentListItem(deviceId: Long) {

        repository?.findTodayStatus(deviceId, object : ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                val todayStatus = objects!![0]

                totalOfDay.postValue(todayStatus.productivity0 +
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
                        todayStatus.productivity23)
            }

            override fun onError(t: Throwable?) {
            }
        })


        repository?.findMonthStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                val dayTotalList: MutableMap<Int, Float> = mutableMapOf()

                var totalMonth = 0L

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

                    totalMonth += dayTotal
                }

                totalOfMonth.postValue(totalMonth)
            }

            override fun onError(t: Throwable?) {
            }
        })


        repository?.findMonthStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                var operationTimeTotal = 0L


                objects?.forEach {
                    val dayTotal = it.operationTime / 60
                    operationTimeTotal += dayTotal
                }

                totalOfOperationTime.postValue(operationTimeTotal)
            }

            override fun onError(t: Throwable?) {
            }
        })



        repository?.findMonthStatus(deviceId, object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {

                val dayTotalList: MutableMap<Int, Float> = mutableMapOf()

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

                totalOfPCS.postValue(maxDate?.value?.toLong())
            }

            override fun onError(t: Throwable?) {
            }
        })

    }

    fun fetchHeaderItem(deviceId: Long) {

        repository?.findTodayStatus(deviceId, object : ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {
                val todayStatus = objects!![0]
                val bootTime = todayStatus.bootTime
                val bootTimeSoFar = todayStatus.bootTimeSoFar


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


                var operationTime = 0L
                var productivity = 0L
                if (todayStatus.operationTime > 0L) {
                    operationTime = todayStatus.operationTime
                    productivity = totalOfDay / operationTime
                }

                headerItemLiveData.value = ChartHeaderItem(
                        calHourAndMinutes(bootTime),
                        calHourAndMinutes(bootTimeSoFar),
                        calHourAndMinutes(operationTime),
                        productivity.toString() + resource.getString(R.string.average_productvity_unit)

                )
            }

            override fun onError(t: Throwable?) {
                Log.i(tag, "error : $t")
                headerItemLiveData.value = ChartHeaderItem(
                        calHourAndMinutes(0),
                        calHourAndMinutes(0),
                        calHourAndMinutes(0)
                        , "0" + resource.getString(R.string.average_productvity_unit))
                errorGetChart.value = t
            }
        })
    }

    companion object {
        const val tag = "ChartViewModel"
    }
}