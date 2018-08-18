package iotdevice.com.iotDevice.register

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import iotdevice.com.iotDevice.member.TokenManager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class RegisterViewModel: ViewModel(), AnkoLogger,  TokenManager.RegisterListener {

    val registerSuccess: MutableLiveData<Any> = MutableLiveData()
    val registerFail: MutableLiveData<Throwable> = MutableLiveData()

    fun registerUser(email: String, userName: String, password: String) {
        val bundle = Bundle()
        TokenManager.performRegisterRequest(bundle, email,
                userName, password, this)
    }

    override fun onRegisterComplete(result: Any?) {
        info("register success : $result")
        registerSuccess.value = result
    }

    override fun onRegisterError(err: Throwable) {
        info("register fail : $err")
        registerFail.value = err
    }
}