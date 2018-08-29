package iotdevice.com.iotDevice.repository

import com.google.common.collect.ImmutableMap
import com.strongloop.android.loopback.ModelRepository
import com.strongloop.android.loopback.callbacks.JsonArrayParser
import com.strongloop.android.loopback.callbacks.JsonObjectParser
import com.strongloop.android.loopback.callbacks.ListCallback
import com.strongloop.android.loopback.callbacks.ObjectCallback
import iotdevice.com.iotDevice.model.CustomerDeviceModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.json.JSONObject

class CustomerDeviceRepository : ModelRepository<CustomerDeviceModel>("CustomerDevice", "CustomerDevices", CustomerDeviceModel::class.java), AnkoLogger {
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

    fun editDevice(customerId: Int, deviceId: Int, newName: String, callback: ObjectCallback<CustomerDeviceModel>) {

        if (deviceId > 0) {
            val findJson = JSONObject("""{"where": { "and" : [{"customerId" : $customerId }, {"deviceId" : $deviceId}] }}""")

            invokeStaticMethod("all",
                    mapOf("filter" to findJson),
                    JsonArrayParser<CustomerDeviceModel>(this, object: ListCallback<CustomerDeviceModel> {
                        override fun onSuccess(objects: MutableList<CustomerDeviceModel>?) {
                            if (objects?.isNotEmpty() == true) {
                                invokeStaticMethod("prototype.save", mapOf("id" to objects[0].id, "customerId" to customerId, "deviceId" to deviceId, "displayName" to newName),
                                        JsonObjectParser(this@CustomerDeviceRepository, callback))
                            }
                        }

                        override fun onError(t: Throwable?) {
                            info("fail")
                        }
                    }))
        }
    }

    fun delDevice(id: Int, callback: ObjectCallback<CustomerDeviceModel>) {
        if (id > 0) {
            invokeStaticMethod("prototype.remove", mapOf("id" to id),
                    JsonObjectParser(this@CustomerDeviceRepository, callback))
        }
    }
}