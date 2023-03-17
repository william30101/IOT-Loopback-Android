package iotdevice.com.iotDevice

import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.strongloop.android.loopback.AccessToken
import com.strongloop.android.loopback.callbacks.ObjectCallback
import com.strongloop.android.remoting.adapters.Adapter
import iotdevice.com.iotDevice.model.CustomerModel
import iotdevice.com.iotDevice.model.DeviceStatusModel
import iotdevice.com.iotDevice.repository.CustomerRepository
import iotdevice.com.iotDevice.repository.DeviceStatusRepository
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)
//
//        sendRequest.setOnClickListener({ view -> sendRequest() })
//
//        barChartBtn.setOnClickListener({ view ->
//            val intent = Intent(this, BarChartActivity::class.java)
//            // start your next activity
//            startActivity(intent)
//        })
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
        Log.i(tag, "click button")

        repository.findById(8, object : ObjectCallback<DeviceStatusModel> {
            override fun onSuccess(deviceStatus: DeviceStatusModel) {
                // found!
                Log.i(tag, "onsuccess device status " + deviceStatus)

                Log.i(tag, "boot time is " + deviceStatus.bootTime)
            }

            override fun onError(t: Throwable) {
                // handle the error
                Log.i(tag, "onError device status " + t)
            }
        })


//        repository.filter(1, object : ListCallback<DeviceStatusModel> {
//
//            override fun onError(t: Throwable) {
//                Log.i(tag, "Didn't work because " + t.localizedMessage.toString())
//            }
//
//            override fun onSuccess(objects: MutableList<DeviceStatusModel>?) {
//                Log.i(tag, "SUCCESS  " + objects?.get(0)?.bootTime)
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

        customerRepo.loginUser("william30101@gmail.com", "1qaz@WSX",
                object : CustomerRepository.LoginCallback {
                    override fun onSuccess(token: AccessToken?, currentUser: CustomerModel?) {
                        Log.i(tag, "currentUser :" + currentUser?.username + " AccessToken" + token)
                    }

                    override fun onError(t: Throwable) {
                        // login failed
                        Log.i(tag, "loginfail " + t)
                    }
                }
        )

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
                        Log.i(tag, "success : " + response)
                    }

                    override fun onError(t: Throwable?) {
                        Log.i(tag, "error : " + t)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // get destination intent if there is one
        val destinationIntent = intent.getParcelableExtra<Intent>(AccountManager.KEY_INTENT)

        // trigger success screen
//        val intent = Intent(this, RegisterSuccessfulActivity::class.java)
//        intent.putExtra(RegisterSuccessfulFragment.USER_NAME, name)
//        intent.putExtra(RegisterSuccessfulFragment.USER_EMAIL, email)
//        intent.putExtra(RegisterSuccessfulFragment.USER_DOB, dob)
//        intent.putExtra(RegisterSuccessfulFragment.USER_GENDER, gender)
//        intent.putExtra(RegisterSuccessfulFragment.INTENT, destinationIntent)
//        startActivity(intent)
    }

    companion object {
        const val tag = "HomeActivity"
    }
}
