package iotdevice.com.iotDevice.register

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iotDevice.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.*
import kotlin.concurrent.schedule

class RegisterFragment: Fragment(), AnkoLogger {

    lateinit var registerViewModel: RegisterViewModel
    lateinit var emailEditText: EditText
    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var passwordAgainEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater!!.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)

        emailEditText = view.findViewById(R.id.emailEditText)
        usernameEditText = view.findViewById(R.id.usernameEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        passwordAgainEditText =  view.findViewById(R.id.passwordAgainEditText)

        registerViewModel.registerSuccess.observe(this, Observer<Any> {
            activity?.toast(resources.getString(R.string.register_success))
            Timer().schedule(1000){
                //do something
                activity?.finish()
            }
        })

        registerViewModel.registerFail.observe(this, Observer<Throwable> {
            DialogUtils.createAlertDialog(context!!, getString(R.string.register_title), getString(R.string.register_fail))
        })

        view.findViewById<Button>(R.id.registerBtn)?.setOnClickListener { _ ->
            if (checkField()) {
                registerViewModel.registerUser(
                    emailEditText.text.toString(),
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString(),
                )
            }
        }
    }

    private fun checkField(): Boolean {
        val email = emailEditText.text.toString()
        if (!email.contains("@")) {
            DialogUtils.createAlertDialog(context!!, resources.getString(R.string.register_title), resources.getString(R.string.email_not_filled)) {
                info("clicked")
            }
            return false
        }
        val username = usernameEditText.text.toString()
        if (username.isEmpty()) {
            DialogUtils.createAlertDialog(context!!, resources.getString(R.string.register_title), resources.getString(R.string.username_not_filled)) {
                info("clicked")
            }
            return false
        }
        val firstPassword = passwordEditText.text.toString()
        val secondPassword = passwordAgainEditText.text.toString()
        if (firstPassword != secondPassword) {

            DialogUtils.createAlertDialog(context!!, resources.getString(R.string.register_title), resources.getString(R.string.password_not_match)) {
                info("clicked")
            }
            return false
        }

        return true
    }
}