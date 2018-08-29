package iotdevice.com.iotDevice.deviceAction

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.strongloop.android.loopback.callbacks.ObjectCallback
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iotDevice.model.CustomerDeviceModel
import iotdevice.com.iotDevice.repository.CustomerDeviceRepository
import iotdevice.com.iotDevice.repository.CustomerRepository
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.activity_edit_device.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import java.util.*
import kotlin.concurrent.schedule

class EditDeviceActivity : AppCompatActivity(), AnkoLogger {

    lateinit var customerDeviceRepository: CustomerDeviceRepository
    lateinit var customerRepository: CustomerRepository

    val adapter = App.sInstance.loopBackAdapter.apply {
        customerDeviceRepository = this.createRepository(CustomerDeviceRepository::class.java)
        customerRepository = this.createRepository(CustomerRepository::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_device)

        val deviceId = intent.getIntExtra("deviceId", -1)
        val deviceName = intent.getStringExtra("deviceName")

        originalDeviceName.text = deviceName

        edit_device_name_btn.setOnClickListener({ _ ->
            if (nexDeviceName.text.isNotEmpty()) {
                customerDeviceRepository.editDevice(
                        customerRepository.currentUserId as Int,
                        deviceId,
                        nexDeviceName.text.toString(),
                        object : ObjectCallback<CustomerDeviceModel> {
                            override fun onSuccess(retObject: CustomerDeviceModel?) {
                                toast(getString(R.string.edit_device_success))
                                Timer().schedule(2000) {
                                    finish()
                                }
                            }

                            override fun onError(t: Throwable?) {
                                DialogUtils.createAlertDialog(this@EditDeviceActivity, getString(R.string.edit_device_title), t?.message.toString())
                            }
                        })
            }
        })
    }
}