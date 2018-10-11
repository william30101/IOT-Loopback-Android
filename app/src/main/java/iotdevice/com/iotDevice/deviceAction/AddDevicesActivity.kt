package iotdevice.com.iotDevice.deviceAction

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.strongloop.android.loopback.callbacks.ListCallback
import com.strongloop.android.loopback.callbacks.ObjectCallback
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iotDevice.model.CustomerDeviceModel
import iotdevice.com.iotDevice.model.DeviceModel
import iotdevice.com.iotDevice.model.relateview.ImageModel
import iotdevice.com.iotDevice.repository.CustomerDeviceRepository
import iotdevice.com.iotDevice.repository.CustomerRepository
import iotdevice.com.iotDevice.repository.DeviceRepository
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.activity_add_device.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import java.util.*
import kotlin.concurrent.schedule


class AddDevicesActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var deviceRepository: DeviceRepository
    lateinit var customerDeviceRepository: CustomerDeviceRepository
    lateinit var customerRepository: CustomerRepository

    val adapter = App.sInstance.loopBackAdapter.apply {
        deviceRepository = this.createRepository(DeviceRepository::class.java)
        customerDeviceRepository = this.createRepository(CustomerDeviceRepository::class.java)
        customerRepository = this.createRepository(CustomerRepository::class.java)
    }

    // 2. Instantiate our NoteRepository. For the intrepid, notice that we could create this
    //    once (say, in onCreateView) and use the same instance for every request.
    //    Additionally, the shared adapter is associated with the prototype, so we'd only
    //    have to do step 1 in onCreateView also. This more verbose version is presented
    //    as an example; making it more efficient is left as a rewarding exercise for the reader.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_device)

        title = getString(R.string.add_device_title)

        val itemList = intent.getParcelableArrayListExtra<ImageModel>("itemList")

        addDeviceBtn.setOnClickListener({ _ ->

            val deviceName = deviceDisplayNameEditText.text.toString()
            val deviceCode = deviceCodeEditText.text.toString()
            val devicePassword = devicePasswordEditText.text.toString()

            if (deviceName.isNotEmpty() && deviceCode.isNotEmpty() && devicePassword.isNotEmpty()) {
                deviceRepository.filter(deviceCode.toInt(), devicePassword.toInt(),
                        object : ListCallback<DeviceModel> {
                            override fun onSuccess(objects: MutableList<DeviceModel>?) {

                                if (itemList.map { it.deviceId == objects?.get(0)?.getId() }.isNotEmpty()) {
                                    DialogUtils.createAlertDialog(this@AddDevicesActivity, getString(R.string.add_device_title), getString(R.string.add_device_duplicate))
                                    return
                                }

                                customerDeviceRepository.add(objects?.get(0)?.getId().toString(),
                                        customerRepository.currentUserId.toString(),
                                        deviceName,
                                        object : ObjectCallback<CustomerDeviceModel> {

                                            override fun onSuccess(res: CustomerDeviceModel?) {
                                                toast(getString(R.string.add_device_success))
                                                Timer().schedule(2000) {
                                                    finish()
                                                }
                                            }

                                            override fun onError(t: Throwable?) {
                                                DialogUtils.createAlertDialog(this@AddDevicesActivity, getString(R.string.add_device_title), t?.message.toString())

                                            }
                                        })
                            }

                            override fun onError(t: Throwable?) {
                                DialogUtils.createAlertDialog(this@AddDevicesActivity, getString(R.string.add_device_title), t?.message.toString())
                            }
                        })
            } else {
                DialogUtils.createAlertDialog(this, getString(R.string.add_device_title), getString(R.string.add_device_check_msg))
            }

        })

    }

}