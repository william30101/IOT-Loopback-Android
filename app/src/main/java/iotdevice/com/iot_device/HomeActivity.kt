package iotdevice.com.iot_device

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.strongloop.android.loopback.callbacks.ListCallback
import iotdevice.com.iot_device.model.DeviceStatusModel
import iotdevice.com.iot_device.repository.DeviceStatusRepostory
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info




class HomeActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sendRequest.setOnClickListener({view -> sendRequest()})
    }

    private fun sendRequest() {
        // 1. Grab the shared RestAdapter instance.

        val adapter = App.sInstance.loopBackAdapter

        // 2. Instantiate our NoteRepository. For the intrepid, notice that we could create this
        //    once (say, in onCreateView) and use the same instance for every request.
        //    Additionally, the shared adapter is associated with the prototype, so we'd only
        //    have to do step 1 in onCreateView also. This more verbose version is presented
        //    as an example; making it more efficient is left as a rewarding exercise for the reader.
        val repository = adapter.createRepository(DeviceStatusRepostory::class.java)

        // 3. From that prototype, create a new NoteModel. We pass in an empty dictionary to defer
        //    setting any values.
//        val model: DeviceStatusModel = repository.createObject(null)


        // 4. Pull model values from the UI.
//        model.operationTime = factoryCode.text.toString().toLong()


        // 5. Save!
//        repository.findById(object : VoidCallback {
//            override fun onSuccess() {
//                showResult("Saved!")
//            }
//
//            override fun onError(t: Throwable) {
//                showResult("Failed.")
//            }
//        })
        info("click button")

//        repository.findById(8, object : ObjectCallback<DeviceStatusModel> {
//            override fun onSuccess(deviceStatus: DeviceStatusModel) {
//                // found!
//                info("onsuccess device status " + deviceStatus)
//
//                info("boot time is " + deviceStatus.bootTime)
//            }
//
//            override fun onError(t: Throwable) {
//                // handle the error
//                info("onError device status " + t)
//            }
//        })



        repository.filter(1, object : ListCallback<DeviceStatusModel> {

            override fun onError(t: Throwable) {
                info("Didn't work because " + t.localizedMessage.toString())
            }

            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {
                info("SUCCESS" + objects?.get(0)?.bootTime)
            }
        })
    }


}
