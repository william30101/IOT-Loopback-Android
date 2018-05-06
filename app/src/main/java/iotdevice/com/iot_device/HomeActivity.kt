package iotdevice.com.iot_device

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.strongloop.android.remoting.adapters.Adapter
import iotdevice.com.iot_device.draw.BarChartActivity
import iotdevice.com.iot_device.model.CustomerModel
import iotdevice.com.iot_device.repository.CustomerRepository
import iotdevice.com.iot_device.repository.DeviceStatusRepository
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class HomeActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sendRequest.setOnClickListener({ view -> sendRequest() })

        barChartBtn.setOnClickListener({ view ->
            val intent = Intent(this, BarChartActivity::class.java)
            // start your next activity
            startActivity(intent)
        })
    }

    private fun sendRequest() {
        // 1. Grab the shared RestAdapter instance.

        val adapter = App.sInstance.loopBackAdapter

        // 2. Instantiate our NoteRepository. For the intrepid, notice that we could create this
        //    once (say, in onCreateView) and use the same instance for every request.
        //    Additionally, the shared adapter is associated with the prototype, so we'd only
        //    have to do step 1 in onCreateView also. This more verbose version is presented
        //    as an example; making it more efficient is left as a rewarding exercise for the reader.
        val repository = adapter.createRepository(DeviceStatusRepository::class.java)

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


//        repository.filter(1, object : ListCallback<DeviceStatusModel> {
//
//            override fun onError(t: Throwable) {
//                info("Didn't work because " + t.localizedMessage.toString())
//            }
//
//            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {
//                info("SUCCESS  " + objects?.get(0)?.bootTime)
//            }
//        })

        getUser()


    }

    fun getUser() {

        val adapter = App.sInstance.loopBackAdapter

        // 2. Instantiate our NoteRepository. For the intrepid, notice that we could create this
        //    once (say, in onCreateView) and use the same instance for every request.
        //    Additionally, the shared adapter is associated with the prototype, so we'd only
        //    have to do step 1 in onCreateView also. This more verbose version is presented
        //    as an example; making it more efficient is left as a rewarding exercise for the reader.
        val customerRepo = adapter.createRepository(CustomerRepository::class.java)

//        customerRepo.loginUser("william30101@gmail.com", "1qaz@WSX",
//                object : CustomerRepository.LoginCallback {
//                    override fun onSuccess(token: AccessToken?, currentUser: CustomerModel?) {
//                        info("currentUser :" + currentUser?.username + " AccessToken" + token)
//                    }
//
//                    override fun onError(t: Throwable) {
//                        // login failed
//                        info("loginfail " + t)
//                    }
//                }
//        )

//        {
//            "lincenNo": "123",
//            "realm": "111",
//            "username": "William",
//            "email": "william30101@gmail.com",
//            "password" : "1qaz@WSX",
//            "emailVerified": true,
//            "customerAddress": {
//            "city": "12",
//            "state": "33"
//        },
//            "emailList": [
//            {
//                "label": "yahoo",
//                "address": "william30101@yahoo.com.tw"
//            }
//            ]
//        }
        val registerParameter = mapOf("lincenNo" to "123", "realm" to "test", "username" to "william5")
        val newuser : CustomerModel = customerRepo.createUser("william5@gmail.com", "1qaz@WSX")

        customerRepo.invokeStaticMethod("prototype.create",
                mapOf("email" to "william5@gmail.com", "password" to "1qaz@WSX"),
                object : Adapter.JsonCallback() {
                    override fun onSuccess(response: Any?) {
                        info("success : " + response)
                    }

                    override fun onError(t: Throwable?) {
                        info("error : " + t)
                    }

                })

// later in one of the Activity classes
//        val current = customerRepo.getCachedCurrentUser()
//        if (current != null) {
//            val address = current!!.getAddress()
//            // display the address
//        } else {
//            // you have to login first
//        }
    }


}
