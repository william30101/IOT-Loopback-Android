package iotdevice.com.iotDevice.common

import android.content.Context
import android.support.v7.app.AlertDialog

class DialogUtils {
    companion object {
        fun createAlertDialog(context: Context, title: String, msg: String, clickFun: () -> Unit) {

            val builder = AlertDialog.Builder(context).apply {
                this.setTitle(title)
                this.setMessage(msg)
                this.setPositiveButton("OK"){_, _-> clickFun() }
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}