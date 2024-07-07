package iotdevice.com.iotDevice.member.info

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.strongloop.android.remoting.adapters.Adapter
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.common.BaseActivity
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iotDevice.repository.CustomerRepository
import iotdevice.com.iotDevice.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.*
import kotlin.concurrent.schedule

class ChangePasswordActivity: BaseActivity(), AnkoLogger {

    private lateinit var customerRepository: CustomerRepository
    val adapter = App.sInstance.loopBackAdapter.apply {
        customerRepository = this.createRepository(CustomerRepository::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_change_password)

        title = getString(R.string.change_password_title)

        findViewById<Button>(R.id.changePasswordBtn).setOnClickListener {
            if (findViewById<EditText>(R.id.oldPassword).text.isNotEmpty() &&
                (findViewById<EditText>(R.id.newPassword).text.toString() == findViewById<EditText>(R.id.newPasswordConfirm).text.toString())) {

                customerRepository.changePassword(findViewById<EditText>(R.id.oldPassword).text.toString(), findViewById<EditText>(R.id.newPassword).text.toString(), object: Adapter.JsonCallback(){

                    override fun onSuccess(response: Any?) {
                        info("change success")
                        toast(getString(R.string.change_success))
                        Timer().schedule(2000) {
                            finish()
                        }
                    }

                    override fun onError(t: Throwable?) {
                        info("change error")
                        DialogUtils.createAlertDialog( this@ChangePasswordActivity, getString(R.string.change_password_title), getString(R.string.change_fail))
                    }
                })
            } else {
                DialogUtils.createAlertDialog( this, getString(R.string.change_password_title), getString(R.string.password_not_match) )
            }
        }
    }
}