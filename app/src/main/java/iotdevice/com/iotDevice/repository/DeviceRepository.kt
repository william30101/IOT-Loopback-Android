package iotdevice.com.iotDevice.repository

import com.strongloop.android.loopback.ModelRepository
import com.strongloop.android.loopback.callbacks.JsonArrayParser
import com.strongloop.android.loopback.callbacks.ListCallback
import iotdevice.com.iotDevice.model.DeviceModel
import org.json.JSONObject

class DeviceRepository: ModelRepository<DeviceModel>("Device", "Devices", DeviceModel::class.java) {
    fun filter(factoryCode: Int, factoryPassword: Int, callback: ListCallback<DeviceModel>) {

        val findJson = JSONObject("""{"where":{"and":[{"factoryCode": $factoryCode } , {"factoryPassword":$factoryPassword}]}}""")

        invokeStaticMethod("all",
                mapOf("filter" to findJson),
                JsonArrayParser<DeviceModel>(this, callback))
    }
}