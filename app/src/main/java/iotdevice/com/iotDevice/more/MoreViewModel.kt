package iotdevice.com.iotDevice.more

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strongloop.android.loopback.AccessToken
import com.strongloop.android.loopback.callbacks.ObjectCallback
import com.strongloop.android.remoting.adapters.Adapter
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iotDevice.login.LoginViewModel
import iotdevice.com.iotDevice.member.info.ChangePasswordActivity
import iotdevice.com.iotDevice.model.CustomerModel
import iotdevice.com.iotDevice.repository.CustomerRepository
import iotdevice.com.iot_device.R
import java.util.Timer
import kotlin.concurrent.schedule

class MoreViewModel: ViewModel() {

    val isDeleteSuccess = MutableLiveData<Boolean>()

    fun deleteAccount() {
        val adapter = App.sInstance.loopBackAdapter
        val customerRepo = adapter.createRepository(CustomerRepository::class.java)

        customerRepo.findCurrentUser(
            object : ObjectCallback<CustomerModel> {
                override fun onSuccess(customer: CustomerModel?) {
                    Log.i(tag, "Get user id success is $${customer?.getId()}" )

                    customer?.getId()?.let { id ->
                        customerRepo.deleteAccount(id.toString(),
                            object: Adapter.JsonCallback(){

                                override fun onSuccess(response: Any?) {
                                    Log.i(ChangePasswordActivity.tag, "delete account success")

                                    isDeleteSuccess.value = true

                                }

                                override fun onError(t: Throwable?) {
                                    Log.i(ChangePasswordActivity.tag, "delete account error")

                                    isDeleteSuccess.value = false
                                }
                            })
                    }

                }
                override fun onError(t: Throwable?) {
                    Log.i(tag, "Get user id onError", t)

                    isDeleteSuccess.value = false
                }
            })
        }

    companion object {
        const val tag = "MoreViewModel"
    }
}