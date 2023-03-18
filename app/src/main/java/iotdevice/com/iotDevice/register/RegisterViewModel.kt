package iotdevice.com.iotDevice.register


import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import iotdevice.com.iotDevice.member.TokenManager


class RegisterViewModel: ViewModel(),  TokenManager.RegisterListener {

    val registerSuccess: MutableLiveData<Any> = MutableLiveData()
    val registerFail: MutableLiveData<Throwable> = MutableLiveData()

    fun registerUser(email: String, userName: String, password: String) {
        val bundle = Bundle()
        TokenManager.performRegisterRequest(bundle, email,
                userName, password, this)
    }

    override fun onRegisterComplete(result: Any?) {
        Log.i(tag, "register success : $result")
        registerSuccess.value = result
    }

    override fun onRegisterError(err: Throwable) {
        Log.i(tag, "register fail : $err")
        registerFail.value = err
    }

    companion object {
        const val tag = "RegisterViewModel"
    }
}