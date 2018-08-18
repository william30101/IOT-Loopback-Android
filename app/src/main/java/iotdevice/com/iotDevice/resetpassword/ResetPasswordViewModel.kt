package iotdevice.com.iotDevice.resetpassword

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.strongloop.android.remoting.adapters.Adapter
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.repository.CustomerRepository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class ResetPasswordViewModel: ViewModel(), AnkoLogger {

    val sendResetSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val sendResetFail: MutableLiveData<Any> = MutableLiveData()

    val adapter = App.sInstance.loopBackAdapter
    val repository: CustomerRepository? = adapter.createRepository(CustomerRepository::class.java)

    fun resetPassword(email: String) {
        repository?.resetPassword(email,  object: Adapter.JsonCallback(){
            override fun onSuccess(response: Any?) {
                info("success")
                sendResetSuccess.value = true
            }

            // TODO : the API will return null even if success, need to handle this.
            override fun onError(t: Throwable?) {
                info("fail")
                sendResetSuccess.value = true
            }
        })
    }

}