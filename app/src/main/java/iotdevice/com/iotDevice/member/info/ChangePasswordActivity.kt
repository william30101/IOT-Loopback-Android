package iotdevice.com.iotDevice.member.info

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.strongloop.android.remoting.adapters.Adapter
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.common.BaseActivity
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iotDevice.repository.CustomerRepository
import iotdevice.com.iot_device.R
import iotdevice.com.iot_device.databinding.ActivityChangePasswordBinding
import java.util.*
import kotlin.concurrent.schedule

class ChangePasswordActivity: BaseActivity() {

    private lateinit var binding: ActivityChangePasswordBinding


    private lateinit var customerRepository: CustomerRepository
    val adapter = App.sInstance.loopBackAdapter.apply {
        customerRepository = this.createRepository(CustomerRepository::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.change_password_title)


        binding.changePasswordBtn.setOnClickListener({
            if (binding.oldPassword.text.isNotEmpty() &&
                    (binding.newPassword.text.toString() == binding.newPasswordConfirm.text.toString())) {

                customerRepository.changePassword(binding.oldPassword.text.toString(), binding.newPassword.text.toString(), object: Adapter.JsonCallback(){

                    override fun onSuccess(response: Any?) {
                        Log.i(tag, "change success")
                        Toast.makeText(applicationContext, getString(R.string.change_success), Toast.LENGTH_SHORT).show()
                        Timer().schedule(2000) {
                            finish()
                        }
                    }

                    override fun onError(t: Throwable?) {
                        Log.i(tag, "change error")
                        DialogUtils.createAlertDialog( this@ChangePasswordActivity, getString(R.string.change_password_title), getString(R.string.change_fail))
                    }
                })
            } else {
                DialogUtils.createAlertDialog( this, getString(R.string.change_password_title), getString(R.string.password_not_match) )
            }
        })
    }

    companion object {
        const val tag = "ChangePasswordActivity"
    }
}