package iotdevice.com.iotDevice

import android.app.Application
import android.content.Context
import com.strongloop.android.loopback.RestAdapter
import iotdevice.com.iotDevice.member.TokenManager
import iotdevice.com.iot_device.BuildConfig
import kotlin.properties.Delegates

class App : Application() {
    internal var adapter: RestAdapter? = null

    // Instantiate the shared RestAdapter. In most circumstances,
    // you'll do this only once; putting that reference in a singleton
    // is recommended for the sake of simplicity.
    // However, some applications will need to talk to more than one
    // server - create as many Adapters as you need.
    //                    getApplicationContext(), "http://192.168.0.191:3000/api");
    //getApplicationContext(), "http://192.168.0.22:3000/api");
    //            getApplicationContext(), "http://172.20.10.3:3000/api");
    // This boilerplate is required for Lesson Three.
    //            adapter.getContract().addItem(
    //                    new RestContractItem("locations/nearby", "GET"),
    //                    "location.nearby");
    val loopBackAdapter: RestAdapter
        get() {
            if (adapter == null) {
                adapter = if (BuildConfig.BUILD_TYPE == "release" || BuildConfig.BUILD_TYPE == "debugincloud") {
                    RestAdapter(
                            applicationContext, "http://54.65.190.127:1883/api")
                } else {
                    RestAdapter(
                            applicationContext, "http://54.65.190.127:1883/api")
                }
            }
            return adapter!!
        }

    val context: Context
        get() = this.applicationContext

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        TokenManager.initialize(this)
    }

    companion object {
        var sInstance: App by Delegates.notNull()
    }


}