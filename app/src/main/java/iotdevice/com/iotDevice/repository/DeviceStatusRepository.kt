package iotdevice.com.iotDevice.repository

import com.google.common.collect.ImmutableMap
import com.strongloop.android.loopback.ModelRepository
import com.strongloop.android.loopback.callbacks.JsonArrayParser
import com.strongloop.android.loopback.callbacks.ListCallback
import iotdevice.com.iotDevice.model.DeviceStatusModel
import org.json.JSONObject


class DeviceStatusRepository : ModelRepository<DeviceStatusModel>("DeviceStatus", "DeviceStatuses", DeviceStatusModel::class.java) {

    fun filter(id: Long, callback: ListCallback<DeviceStatusModel>) {
        invokeStaticMethod("all",
                ImmutableMap.of("filter",
                        ImmutableMap.of("where",
                                ImmutableMap.of("deviceId", id))),
                JsonArrayParser(this, callback))
    }

    fun findNewestStatus(id: Long?, callback: ListCallback<DeviceStatusModel>) {
        val findJson = JSONObject("""{"where":{"deviceId" : $id }, "order" : "id DESC", "limit" : "1"}""")

        invokeStaticMethod("all",
                mapOf("filter" to findJson),
                JsonArrayParser<DeviceStatusModel>(this, callback))

    }


}