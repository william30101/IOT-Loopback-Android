package iotdevice.com.iotDevice.repository;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.JsonArrayParser;
import com.strongloop.android.loopback.callbacks.ListCallback;

import iotdevice.com.iotDevice.model.DeviceStatusModel;

public class DeviceStatusRepository extends ModelRepository<DeviceStatusModel> {

    public DeviceStatusRepository() {
        super("DeviceStatus",  "DeviceStatuses", DeviceStatusModel.class);
    }

    public void filter(int id, final ListCallback<DeviceStatusModel> callback) {
        invokeStaticMethod("all",
                ImmutableMap.of("filter",
                        ImmutableMap.of("where",
                                ImmutableMap.of("deviceId", id))),
                new JsonArrayParser<>(this, callback));
    }


}
