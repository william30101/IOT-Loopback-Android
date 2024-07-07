package iotdevice.com.iotDevice.resetpassword

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iotDevice.R
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



        findViewById<Button>(R.id.reset_button).setOnClickListener { _ ->
            resetPasswordViewModel.resetPassword(findViewById<EditText>(R.id.reset_email).text.toString())
        }

        resetPasswordViewModel.sendResetSuccess.observe(this, Observer<Boolean> {
            if (it == true) {

                toast(getString(R.string.reset_word))

                Timer().schedule(500) {
                    finish()
                }
            }
        })

        resetPasswordViewModel.sendResetFail.observe(this, Observer<Boolean> {
            if (it == true) {
                DialogUtils.createAlertDialog( this, getString(R.string.reset_title))
            }
        })
    }
}