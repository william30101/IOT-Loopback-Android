package iotdevice.com.iotDevice.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import iotdevice.com.iotDevice.member.AuthenticationServiceLocator

abstract class BaseActivity : AppCompatActivity() {

    fun relaunchAuthenticated() {
        val authService = AuthenticationServiceLocator.authService

        authService.logout()
        authService.openAuthenticatedActivity(this, intent)
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
    }

    companion object {

        fun intentToFragmentArguments(intent: Intent?): Bundle {
            val arguments = Bundle()
            if (intent == null) {
                return arguments
            }

            val extras = intent.extras
            if (extras != null) {
                arguments.putAll(intent.extras)
            }

            return arguments
        }

        fun fragmentArgumentsToIntent(arguments: Bundle?): Intent {
            val intent = Intent()
            if (arguments == null) {
                return intent
            }

            intent.putExtras(arguments)
            intent.removeExtra("_uri")
            return intent
        }
    }


}