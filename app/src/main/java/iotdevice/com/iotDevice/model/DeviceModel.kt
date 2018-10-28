package iotdevice.com.iotDevice.model

import com.strongloop.android.loopback.Model

class DeviceModel : Model() {
    var factoryCode: String = ""
    var factoryPassword: Number = 0
    var id: Number = 0
}