package iotdevice.com.iotDevice.resetpassword

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import java.util.*
import kotlin.concurrent.schedule

class ResetPasswordActivity: AppCompatActivity(), AnkoLogger {

    private lateinit var resetPasswordViewModel: ResetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reset_password)
        resetPasswordViewModel = ViewModelProviders.of(this).get(ResetPasswordViewModel::class.java)


        reset_button.setOnClickListener({ _ ->
            resetPasswordViewModel.resetPassword(reset_email.text.toString())
        })

        resetPasswordViewModel.sendResetSuccess.observe(this, Observer<Boolean> {
            if (it == true) {
                toast(getString(R.string.reset_word))

                Timer().schedule(500) {
                    finish()
                }
            }
        })
    }
}