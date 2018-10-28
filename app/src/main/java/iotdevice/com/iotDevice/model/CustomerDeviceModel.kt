package iotdevice.com.iotDevice.model

import com.strongloop.android.loopback.Model

class CustomerDeviceModel : Model() {
    var displayName: String = ""
    var customerId: Number = 0
    var deviceId: Number = 0
    var id = 0
    var factoryCode: String =""
}