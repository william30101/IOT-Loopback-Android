package iotdevice.com.iotDevice.chart

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.strongloop.android.loopback.callbacks.ListCallback
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.common.ChartUtils
import iotdevice.com.iotDevice.model.DeviceStatusModel
import iotdevice.com.iotDevice.repository.DeviceStatusRepository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

class ChartViewModel: ViewModel(), AnkoLogger {

    val errorGetChart: MutableLiveData<Any> = MutableLiveData()

    val headerItemLiveData: MutableLiveData<ChartHeaderItem> = MutableLiveData()
    val adapter = App.sInstance.loopBackAdapter
    val repository: DeviceStatusRepository? = adapter.createRepository(DeviceStatusRepository::class.java)

    fun fetchHeaderItem(deviceId: Long) {
        repository?.findTotalOperationTime(deviceId,  object: ListCallback<DeviceStatusModel> {
            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {


                val now = Calendar.getInstance()
                val itemCalendar = Calendar.getInstance()
                var operationTimeTotal: Long = 0
                var bootTimeTotal: Long = 0
                var currentProducitivity: Long = 0
                var bootTimeToday: Long = 0

                objects?.forEach {
                    operationTimeTotal += it.operationTime
                    bootTimeTotal += it.bootTime

                    itemCalendar.timeInMillis = it.timeStamp!!.toLong()
                    if (now.get(Calendar.DATE) == itemCalendar.get(Calendar.DATE) ) {
                        info("Today's boot time ${it.bootTime}")
                        bootTimeToday = it.bootTime
                        val hour = now.get(Calendar.HOUR_OF_DAY)
                        ChartUtils.combineProductivityToList(it)
                        currentProducitivity = it.productivityLit[hour]
                        if (currentProducitivity > 0) {
                            info("Today's boot time $currentProducitivity")
                        }
                    }
                }

                headerItemLiveData.value = ChartHeaderItem(bootTimeTotal.toString(),bootTimeToday.toString(),
                        operationTimeTotal.toString(),bootTimeTotal.toString())
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
                errorGetChart.value = t
            }
        })
    }

}