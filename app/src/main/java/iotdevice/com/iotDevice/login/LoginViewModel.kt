package iotdevice.com.iotDevice.login


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strongloop.android.loopback.AccessToken
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.model.CustomerModel
import iotdevice.com.iotDevice.repository.CustomerRepository


class LoginViewModel : ViewModel() {

    val userData = MutableLiveData<CustomerModel>()

    fun  userLogin(email: String, password: String): LiveData<CustomerModel> {

        val adapter = App.sInstance.loopBackAdapter

        val customerRepo = adapter.createRepository(CustomerRepository::class.java)

        customerRepo.loginUser(email, password,
                object : CustomerRepository.LoginCallback {
                    override fun onSuccess(token: AccessToken?, currentUser: CustomerModel?) {
                        Log.i(tag, "currentUser :" + currentUser?.username + " AccessToken" + token)

                        currentUser?.id = token?.id.toString()
                        userData.value = currentUser
                    }

                    override fun onError(throwObj: Throwable) {
                        // login failed
                        Log.i(tag, "loginfail $throwObj")
                    }
                }
        )

        return userData
    }

    companion object {
        const val tag = "LoginViewModel"
    }


}