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
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.info
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
            snackbar(view!!, "Register Success")
            Timer().schedule(1000){
                //do something
                activity.finish()
            }
        })

        registerViewModel.registerFail.observe(this, Observer<Throwable> {
            DialogUtils.createAlertDialog(context, "Register", it.toString(), {
                info ( it.toString() )
            })
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

    fun checkField(): Boolean {
        val firstPassword = passwordEditText.text.toString()
        val secondPassword = passwordAgainEditText.text.toString()
        if (firstPassword != secondPassword) {

            DialogUtils.createAlertDialog(context, "Register", "Password not match", {
                info ( "clicked" )
            })
            return false
        }

        return true
    }


}