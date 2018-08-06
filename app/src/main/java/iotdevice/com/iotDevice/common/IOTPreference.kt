package iotdevice.com.iotDevice.common

import android.content.Context.MODE_PRIVATE
import iotdevice.com.iotDevice.App




class IOTPreference {

    companion object {
        private const val saveFileName = "iot-share"

        fun saveUserName(userName: String) {
            val pref = App.sInstance.getSharedPreferences(saveFileName, MODE_PRIVATE)
            pref.edit()
                    .putString("USERNAME", userName)
                    .apply()
        }

        fun getUserName(): String {
            return App.sInstance.getSharedPreferences(saveFileName, MODE_PRIVATE)
                    .getString("USERNAME", "")
        }
    }
}