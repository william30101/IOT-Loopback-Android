package iotdevice.com.iotDevice.deviceAction

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.strongloop.android.loopback.callbacks.ObjectCallback
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iotDevice.model.CustomerDeviceModel
import iotdevice.com.iotDevice.repository.CustomerDeviceRepository
import iotdevice.com.iotDevice.repository.CustomerRepository
import iotdevice.com.iot_device.R
import iotdevice.com.iot_device.databinding.ActivityEditDeviceBinding
import java.util.*
import kotlin.concurrent.schedule

class EditDeviceActivity : AppCompatActivity() {

    lateinit var customerDeviceRepository: CustomerDeviceRepository
    lateinit var customerRepository: CustomerRepository

    lateinit var binding: ActivityEditDeviceBinding

    val adapter = App.sInstance.loopBackAdapter.apply {
        customerDeviceRepository = this.createRepository(CustomerDeviceRepository::class.java)
        customerRepository = this.createRepository(CustomerRepository::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val deviceId = intent.getIntExtra("deviceId", -1)
        val deviceName = intent.getStringExtra("deviceName")

        title = getString(R.string.edit_device_title)
        binding.originalDeviceName.text = deviceName

        binding.editDeviceNameBtn.setOnClickListener({ _ ->
            if (binding.nexDeviceName.text.isNotEmpty()) {
                customerDeviceRepository.editDevice(
                        customerRepository.currentUserId as Int,
                        deviceId,
                        binding.nexDeviceName.text.toString(),
                        object : ObjectCallback<CustomerDeviceModel> {
                            override fun onSuccess(retObject: CustomerDeviceModel?) {
                                Toast.makeText(applicationContext, getString(R.string.edit_device_success), Toast.LENGTH_SHORT).show()
                                Timer().schedule(2000) {
                                    finish()
                                }
                            }

                            override fun onError(t: Throwable?) {
                                DialogUtils.createAlertDialog(this@EditDeviceActivity, getString(R.string.edit_device_title),
                                        getString(R.string.edit_device_fail))
                            }
                        })
            }
        })
    }
}