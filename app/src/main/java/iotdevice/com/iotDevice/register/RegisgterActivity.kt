package iotdevice.com.iotDevice.register

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import iotdevice.com.iotDevice.member.TokenManager
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class RegisgterActivity: AppCompatActivity(),AnkoLogger, TokenManager.RegisterListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        registerBtn?.setOnClickListener({ _ ->

                val bundle = Bundle()

                TokenManager.performRegisterRequest(bundle, emailEditText.text.toString(),
                        usernameEditText.text.toString(), passwordEditText.text.toString(), this)

        })
    }

    override fun onRegisterComplete(result: Any?) {
        info("onRegisterComplete : $result")
    }

    override fun onRegisterError(err: Throwable) {
        info("onRegisterError : $err")

    }
}