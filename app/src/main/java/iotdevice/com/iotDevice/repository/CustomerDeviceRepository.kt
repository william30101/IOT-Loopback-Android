package iotdevice.com.iotDevice.repository

import com.google.common.collect.ImmutableMap
import com.strongloop.android.loopback.ModelRepository
import com.strongloop.android.loopback.callbacks.JsonArrayParser
import com.strongloop.android.loopback.callbacks.JsonObjectParser
import com.strongloop.android.loopback.callbacks.ListCallback
import com.strongloop.android.loopback.callbacks.ObjectCallback
import iotdevice.com.iotDevice.model.CustomerDeviceModel

class CustomerDeviceRepository : ModelRepository<CustomerDeviceModel>("CustomerDevice", "CustomerDevices", CustomerDeviceModel::class.java) {
    fun add(deviceId: String, customerId: String, displayName: String = "", callback: ObjectCallback<CustomerDeviceModel>) {

//        val createRelation = JSONObject("""{"customerId":$customerId, "deviceId": $deviceId }""")

        invokeStaticMethod("prototype.create", mapOf("customerId" to customerId, "deviceId" to deviceId, "displayName" to displayName),
                JsonObjectParser(this, callback))
    }

    fun filter(id: Int, callback: ListCallback<CustomerDeviceModel>) {
        invokeStaticMethod("all",
                ImmutableMap.of("filter",
                        ImmutableMap.of("where",
                                ImmutableMap.of("deviceId", id))),
                JsonArrayParser(this, callback))
    }

    fun findDevice(customerId: Int, callback: ListCallback<CustomerDeviceModel>) {
        invokeStaticMethod("all",
                ImmutableMap.of("filter",
                        ImmutableMap.of("where",
                                ImmutableMap.of("customerId", customerId))),
                JsonArrayParser(this, callback))
    }

//
//    fun findAllDevice(deviceId: Int, callback: ListCallback<Device>) {
//
//    }


}