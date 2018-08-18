package iotdevice.com.iotDevice.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.strongloop.android.loopback.AccessToken
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.model.CustomerModel
import iotdevice.com.iotDevice.repository.CustomerRepository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class LoginViewModel : ViewModel(), AnkoLogger {

    val userData = MutableLiveData<CustomerModel>()

    fun  userLogin(email: String, password: String): LiveData<CustomerModel> {

        val adapter = App.sInstance.loopBackAdapter

        val customerRepo = adapter.createRepository(CustomerRepository::class.java)

        customerRepo.loginUser(email, password,
                object : CustomerRepository.LoginCallback {
                    override fun onSuccess(token: AccessToken?, currentUser: CustomerModel?) {
                        info("currentUser :" + currentUser?.username + " AccessToken" + token)

                        currentUser?.id = token?.id.toString()
                        userData.value = currentUser
                    }

                    override fun onError(throwObj: Throwable) {
                        // login failed
                        info("loginfail $throwObj")
                    }
                }
        )

        return userData
    }


}