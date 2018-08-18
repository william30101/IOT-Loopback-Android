package iotdevice.com.iotDevice.deviceAction

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.strongloop.android.loopback.callbacks.ListCallback
import com.strongloop.android.loopback.callbacks.ObjectCallback
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.model.CustomerDeviceModel
import iotdevice.com.iotDevice.model.DeviceModel
import iotdevice.com.iotDevice.repository.CustomerDeviceRepository
import iotdevice.com.iotDevice.repository.CustomerRepository
import iotdevice.com.iotDevice.repository.DeviceRepository
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.activity_add_device.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast


class AddDevicesActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_device)

        addDeviceBtn.setOnClickListener({ _ ->
            val adapter = App.sInstance.loopBackAdapter

            // 2. Instantiate our NoteRepository. For the intrepid, notice that we could create this
            //    once (say, in onCreateView) and use the same instance for every request.
            //    Additionally, the shared adapter is associated with the prototype, so we'd only
            //    have to do step 1 in onCreateView also. This more verbose version is presented
            //    as an example; making it more efficient is left as a rewarding exercise for the reader.
            val deviceRepository = adapter.createRepository(DeviceRepository::class.java)
            val customerDeviceRepository = adapter.createRepository(CustomerDeviceRepository::class.java)
            val customerRepository = adapter.createRepository(CustomerRepository::class.java)

            deviceRepository.filter(deviceCodeEditText.text.toString().toInt(), devicePasswordEditText.text.toString().toInt(),
                    object : ListCallback<DeviceModel> {
                        override fun onSuccess(objects: MutableList<DeviceModel>?) {
                            toast("success : $objects")

                            customerDeviceRepository.add(objects?.get(0)?.getId().toString(),
                                    customerRepository.currentUserId.toString(),
                                    deviceDisplayNameEditText.text.toString(),
                                    object : ObjectCallback<CustomerDeviceModel> {

                                        override fun onSuccess(res: CustomerDeviceModel?) {
                                            toast("success : $res")
                                        }

                                        override fun onError(t: Throwable?) {
                                            toast("err : $t")
                                        }
                                    })


                        }

                        override fun onError(t: Throwable?) {
//                           info("err : $t")
                            toast("err : $t")
                        }
                    })
        })

    }

//    fun getDeviceList: ListCallback<DeviceModel> {
//        info("test")
//    }


}