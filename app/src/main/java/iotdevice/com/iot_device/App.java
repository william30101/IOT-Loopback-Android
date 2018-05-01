package iotdevice.com.iot_device;

import android.app.Application;

import com.strongloop.android.loopback.RestAdapter;

public class App extends Application {
    public static App sInstance;
    RestAdapter adapter;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public RestAdapter getLoopBackAdapter() {
        if (adapter == null) {
            // Instantiate the shared RestAdapter. In most circumstances,
            // you'll do this only once; putting that reference in a singleton
            // is recommended for the sake of simplicity.
            // However, some applications will need to talk to more than one
            // server - create as many Adapters as you need.
            adapter = new RestAdapter(
                    getApplicationContext(), "http://192.168.0.25:3000/api");
//                    getApplicationContext(), "http://35.194.235.166:3000/api");

            // This boilerplate is required for Lesson Three.
//            adapter.getContract().addItem(
//                    new RestContractItem("locations/nearby", "GET"),
//                    "location.nearby");
        }
        return adapter;
    }



}
