package iotdevice.com.iotDevice.common

import android.content.Context
import android.support.v7.app.AlertDialog
import iotdevice.com.iot_device.R

class DialogUtils {

    companion object {

        private const val networkError = "refused"
        private var displayMsg: String? = ""

        fun createAlertDialog(context: Context,
                              title: String,
                              msg: String? = context.getString(R.string.network_error),
                              clickFun: (() -> Unit)? = null) {

            displayMsg = if (msg?.contains(networkError) == true) {
                context.getString(R.string.network_error)
            } else if (msg?.contains("size is 0") == true) {
                context.getString(R.string.no_data_error)
            }  else if (msg?.contains("Unprocessable Entity") == true) {
                context.getString(R.string.duplicate_account)
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

        fun createConfirmDialog(context: Context,
                                title: String,
                                msg: String? = context.getString(R.string.network_error),
                                clickOKFun: (() -> Unit)? = null,
                                clickNoFun: (() -> Unit)? = null) {

            displayMsg = if (msg?.contains(networkError) == true) {
                context.getString(R.string.network_error)
            } else {
                msg
            }

            val builder = AlertDialog.Builder(context).apply {
                this.setTitle(title)
                this.setMessage(displayMsg)
                this.setPositiveButton("OK") { _, _ ->
                    if (clickOKFun != null) {
                        clickOKFun()
                    }}
                this.setNegativeButton("Cancel") { _, _ ->
                    if (clickNoFun != null) {
                        clickNoFun()
                    }
                }
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}