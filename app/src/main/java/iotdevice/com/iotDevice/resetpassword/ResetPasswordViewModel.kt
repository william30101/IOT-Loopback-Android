package iotdevice.com.iotDevice.resetpassword


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strongloop.android.remoting.adapters.Adapter
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.repository.CustomerRepository


class ResetPasswordViewModel: ViewModel() {

    val sendResetSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val sendResetFail: MutableLiveData<Boolean> = MutableLiveData()

    val adapter = App.sInstance.loopBackAdapter
    val repository: CustomerRepository? = adapter.createRepository(CustomerRepository::class.java)

    fun resetPassword(email: String) {
        repository?.resetPassword(email,  object: Adapter.JsonCallback(){
            override fun onSuccess(response: Any?) {
                Log.i(tag, "success")
                sendResetSuccess.value = true
            }

            // TODO : the API will return null even if success, need to handle this.
            override fun onError(t: Throwable?) {
                Log.i(tag, "fail")
                if (t?.message?.contains("null") == true) {
                    sendResetSuccess.value = true
                } else {
                    sendResetFail.value = true
                }
            }
        })
    }

    companion object {
        const val tag = "ResetPasswordViewModel"
    }
}