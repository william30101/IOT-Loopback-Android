package iotdevice.com.iotDevice.model

import com.strongloop.android.loopback.Model

class DeviceStatusModel: Model() {
    var bootTime: Long = 0
    var bootTimeSoFar: Long = 0
    var operationTime: Long = 0
    var timeStamp: String? = null
    var deviceTimeStamp: String? = null
    var deviceId: Long = 0
    val productivityLit: MutableList<Long> = mutableListOf()
    var productivity0: Long = 0
    var productivity1: Long = 0
    var productivity2: Long = 0
    var productivity3: Long = 0
    var productivity4: Long = 0
    var productivity5: Long = 0
    var productivity6: Long = 0
    var productivity7: Long = 0
    var productivity8: Long = 0
    var productivity9: Long = 0
    var productivity10: Long = 0
    var productivity11: Long = 0
    var productivity12: Long = 0
    var productivity13: Long = 0
    var productivity14: Long = 0
    var productivity15: Long = 0
    var productivity16: Long = 0
    var productivity17: Long = 0
    var productivity18: Long = 0
    var productivity19: Long = 0
    var productivity20: Long = 0
    var productivity21: Long = 0
    var productivity22: Long = 0
    var productivity23: Long = 0
}