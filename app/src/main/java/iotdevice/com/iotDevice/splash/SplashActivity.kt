package iotdevice.com.iotDevice.splash

import android.content.Intent
import android.os.Bundle
import iotdevice.com.iotDevice.common.BaseActivity
import iotdevice.com.iotDevice.login.LoginActivity
import iotdevice.com.iot_device.R

import java.util.*
import kotlin.concurrent.schedule

class SplashActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        Timer().schedule(1000){
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}