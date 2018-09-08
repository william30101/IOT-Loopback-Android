package iotdevice.com.iotDevice.chart

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.res.Resources
import com.strongloop.android.loopback.callbacks.ListCallback
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.common.ChartUtils.Companion.calHourAndMinutes
import iotdevice.com.iotDevice.model.DeviceStatusModel
import iotdevice.com.iotDevice.repository.DeviceStatusRepository
import iotdevice.com.iot_device.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class ChartViewModel : ViewModel(), AnkoLogger {

    val errorGetChart: MutableLiveData<Any> = MutableLiveData()

    val headerItemLiveData: MutableLiveData<ChartHeaderItem> = MutableLiveData()
    val adapter = App.sInstance.loopBackAdapter
    val repository: DeviceStatusRepository? = adapter.createRepository(DeviceStatusRepository::class.java)
    private val resource: Resources by lazy { App.sInstance.resources }


    fun fetchHeaderItem(deviceId: Long) {

        repository?.findTodayStatus(deviceId, object : ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {
                val todayStatus = objects!![0]
                val bootTime = todayStatus.bootTime
                val bootTimeSoFar = todayStatus.bootTimeSoFar
                val operationTime = todayStatus.operationTime

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

                headerItemLiveData.value = ChartHeaderItem(
                        calHourAndMinutes(bootTime),
                        calHourAndMinutes(bootTimeSoFar),
                        calHourAndMinutes(operationTime),
                        (totalOfDay / operationTime).toString() + resource.getString(R.string.average_productvity_unit)

                )
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
                headerItemLiveData.value = ChartHeaderItem(
                        calHourAndMinutes(0),
                        calHourAndMinutes(0),
                        calHourAndMinutes(0)
                        ,"0" + resource.getString(R.string.average_productvity_unit))
                errorGetChart.value = t
            }
        })
    }
}