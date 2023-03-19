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

                // Fix a bug, device status will be updated during 0759 - 0800
                // Server will create another device status record at that time,
                // We need to handle the situation
                var productivity0Total = 0L
                var productivity1Total = 0L
                var productivity2Total = 0L
                var productivity3Total = 0L
                var productivity4Total = 0L
                var productivity5Total = 0L
                var productivity6Total = 0L
                var productivity7Total = 0L
                var productivity8Total = 0L
                var productivity9Total = 0L
                var productivity10Total = 0L
                var productivity11Total = 0L
                var productivity12Total = 0L
                var productivity13Total = 0L
                var productivity14Total = 0L
                var productivity15Total = 0L
                var productivity16Total = 0L
                var productivity17Total = 0L
                var productivity18Total = 0L
                var productivity19Total = 0L
                var productivity20Total = 0L
                var productivity21Total = 0L
                var productivity22Total = 0L
                var productivity23Total = 0L

                objects?.forEach {
                    productivity0Total += it.productivity0
                    productivity1Total += it.productivity1
                    productivity2Total += it.productivity2
                    productivity3Total += it.productivity3
                    productivity4Total += it.productivity4
                    productivity5Total += it.productivity5
                    productivity6Total += it.productivity6
                    productivity7Total += it.productivity7
                    productivity8Total += it.productivity8
                    productivity9Total += it.productivity9
                    productivity10Total += it.productivity10
                    productivity11Total += it.productivity11
                    productivity12Total += it.productivity12
                    productivity13Total += it.productivity13
                    productivity14Total += it.productivity14
                    productivity15Total += it.productivity15
                    productivity16Total += it.productivity16
                    productivity17Total += it.productivity17
                    productivity18Total += it.productivity18
                    productivity19Total += it.productivity19
                    productivity20Total += it.productivity20
                    productivity21Total += it.productivity21
                    productivity22Total += it.productivity22
                    productivity23Total += it.productivity23
                }

                totalOfDay.postValue(productivity0Total +
                        productivity1Total +
                        productivity2Total +
                        productivity3Total +
                        productivity4Total +
                        productivity5Total +
                        productivity6Total +
                        productivity7Total +
                        productivity8Total +
                        productivity9Total +
                        productivity10Total +
                        productivity11Total +
                        productivity12Total +
                        productivity13Total +
                        productivity14Total +
                        productivity15Total +
                        productivity16Total +
                        productivity17Total +
                        productivity18Total +
                        productivity19Total +
                        productivity20Total +
                        productivity21Total +
                        productivity22Total +
                        productivity23Total)
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