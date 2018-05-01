package iotdevice.com.iot_device.repository;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.JsonArrayParser;
import com.strongloop.android.loopback.callbacks.ListCallback;

import iotdevice.com.iot_device.model.DeviceStatusModel;

public class DeviceStatusRepostory extends ModelRepository<DeviceStatusModel> {

    public DeviceStatusRepostory() {
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
