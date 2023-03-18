package iotdevice.com.iotDevice.resetpassword


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iot_device.R
import iotdevice.com.iot_device.databinding.ActivityResetPasswordBinding


import java.util.*
import kotlin.concurrent.schedule

class ResetPasswordActivity: AppCompatActivity() {

    private lateinit var resetPasswordViewModel: ResetPasswordViewModel

    lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)

        setContentView(binding.root)

        resetPasswordViewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]


        binding.resetButton.setOnClickListener({ _ ->
            resetPasswordViewModel.resetPassword(binding.resetEmail.text.toString())
        })

        resetPasswordViewModel.sendResetSuccess.observe(this, androidx.lifecycle.Observer {
            if (it == true) {
                Toast.makeText(applicationContext, getString(R.string.reset_word), Toast.LENGTH_SHORT).show()

                Timer().schedule(500) {
                    finish()
                }
            }
        })

        resetPasswordViewModel.sendResetFail.observe(this, androidx.lifecycle.Observer {
            if (it == true) {
                DialogUtils.createAlertDialog( this, getString(R.string.reset_title))
            }
        })
    }
}