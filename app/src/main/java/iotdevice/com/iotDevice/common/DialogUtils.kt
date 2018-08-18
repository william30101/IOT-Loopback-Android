package iotdevice.com.iotDevice.common

import android.content.Context
import android.support.v7.app.AlertDialog
import iotdevice.com.iot_device.R

class DialogUtils {

    companion object {

        private const val networkError = "refused"
        var displayMsg: String? = ""

        fun createAlertDialog(context: Context,
                              title: String,
                              msg: String? = context.getString(R.string.network_error),
                              clickFun: (() -> Unit)? = null) {

            displayMsg = if (msg?.contains(networkError) == true) {
                context.getString(R.string.network_error)
            } else {
                msg
            }

            val builder = AlertDialog.Builder(context).apply {
                this.setTitle(title)
                this.setMessage(displayMsg)
                this.setPositiveButton("OK") { _, _ ->
                    if (clickFun != null) {
                        clickFun()
                    }}
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}