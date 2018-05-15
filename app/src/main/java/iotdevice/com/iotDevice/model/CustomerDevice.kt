package iotdevice.com.iotDevice.model

import com.strongloop.android.loopback.Model

class CustomerDevice : Model() {
    var status: Number = 0
    var customerId: Number = 0
    var deviceId: Number = 0
}