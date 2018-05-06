package iotdevice.com.iot_device.repository;

import com.strongloop.android.loopback.UserRepository;

import iotdevice.com.iot_device.model.CustomerModel;


public class CustomerRepository extends UserRepository<CustomerModel> {
    public interface LoginCallback extends UserRepository.LoginCallback<CustomerModel> {
    }

    public CustomerRepository() {
        super("Customer", "Customers", CustomerModel.class);
    }
}
