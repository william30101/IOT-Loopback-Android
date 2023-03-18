package iotdevice.com.iotDevice.register

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import iotdevice.com.iotDevice.common.BaseActivity
import iotdevice.com.iot_device.R

class RegisterActivity: BaseActivity() {

    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        val transaction = manager.beginTransaction()
        if (manager.fragments.isEmpty() || manager.findFragmentByTag("Register")?.isVisible != true) {
            title = getString(R.string.register_title)

            // Remove entire back stack when user pressed home button.
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            val registerFragment = RegisterFragment()
            transaction.replace(R.id.baseFragment, registerFragment, "Register")
            commitFragment(transaction)
        }

    }

    fun commitFragment(transaction: FragmentTransaction) {
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        if (manager.backStackEntryCount ==1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}