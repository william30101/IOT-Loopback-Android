package iotdevice.com.iotDevice;

import android.app.Application;
import android.content.Context;

import com.strongloop.android.loopback.RestAdapter;

import iotdevice.com.iotDevice.member.TokenManager;

public class App extends Application {
    public static App sInstance;
    RestAdapter adapter;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        TokenManager.INSTANCE.initialize(this);
    }

    public RestAdapter getLoopBackAdapter() {
        if (adapter == null) {
            // Instantiate the shared RestAdapter. In most circumstances,
            // you'll do this only once; putting that reference in a singleton
            // is recommended for the sake of simplicity.
            // However, some applications will need to talk to more than one
            // server - create as many Adapters as you need.
            adapter = new RestAdapter(
//                    getApplicationContext(), "http://192.168.0.191:3000/api");
                    //getApplicationContext(), "http://192.168.0.22:3000/api");
//            getApplicationContext(), "http://172.20.10.3:3000/api");
            getApplicationContext(), "http://192.168.0.100:3000/api");



            // This boilerplate is required for Lesson Three.
//            adapter.getContract().addItem(
//                    new RestContractItem("locations/nearby", "GET"),
//                    "location.nearby");
        }
        return adapter;
    }

    public Context getContext() {
        return this.getApplicationContext();
    }


}
