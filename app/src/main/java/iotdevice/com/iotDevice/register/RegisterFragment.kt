package iotdevice.com.iotDevice.register

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.fragment_register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.*
import kotlin.concurrent.schedule

class RegisterFragment: Fragment(), AnkoLogger {

    lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)

        registerViewModel.registerSuccess.observe(this, Observer<Any> {
            activity.toast(resources.getString(R.string.register_success))
            Timer().schedule(1000){
                //do something
                activity.finish()
            }
        })

        registerViewModel.registerFail.observe(this, Observer<Throwable> {
            DialogUtils.createAlertDialog(context, getString(R.string.register_title), it.toString())
        })

            registerBtn?.setOnClickListener({ _ ->
                if (checkField()) {
                    registerViewModel.registerUser(
                            emailEditText.text.toString(),
                            usernameEditText.text.toString(),
                            passwordEditText.text.toString())
                }
        })
    }

    private fun checkField(): Boolean {
        val email = emailEditText.text.toString()
        if (!email.contains("@")) {
            DialogUtils.createAlertDialog(context, resources.getString(R.string.register_title), resources.getString(R.string.email_not_filled), {
                info ( "clicked" )
            })
            return false
        }
        val username = usernameEditText.text.toString()
        if (username.isEmpty()) {
            DialogUtils.createAlertDialog(context, resources.getString(R.string.register_title), resources.getString(R.string.username_not_filled), {
                info ( "clicked" )
            })
            return false
        }
        val firstPassword = passwordEditText.text.toString()
        val secondPassword = passwordAgainEditText.text.toString()
        if (firstPassword != secondPassword) {

            DialogUtils.createAlertDialog(context, resources.getString(R.string.register_title), resources.getString(R.string.password_not_match), {
                info ( "clicked" )
            })
            return false
        }

        return true
    }
}