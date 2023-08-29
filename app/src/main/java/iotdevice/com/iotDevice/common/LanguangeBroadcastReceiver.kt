package iotdevice.com.iotDevice.common

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.HomeActivity
import iotdevice.com.iotDevice.splash.SplashActivity


class LanguageBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        StringBuilder().apply {
            append("Action: ${intent?.action}\n")
            append("URI: ${intent?.toUri(Intent.URI_INTENT_SCHEME)}\n")
            toString().also { log ->
                Log.d("William", log)
            }
        }

        context?.let { triggerRebirth(context) }
    }

    private fun triggerRebirth(context: Context) {
        val intent = Intent(context, SplashActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
        Runtime.getRuntime().exit(0)
    }
}