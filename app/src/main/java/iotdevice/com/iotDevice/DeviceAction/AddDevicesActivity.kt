package iotdevice.com.iotDevice.DeviceAction

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import iotdevice.com.iot_device.R
import org.jetbrains.anko.AnkoLogger

class AddDevicesActivity: AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pair_device)
    }
}