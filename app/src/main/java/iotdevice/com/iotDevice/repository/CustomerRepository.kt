package iotdevice.com.iotDevice.repository;

import com.strongloop.android.loopback.UserRepository;

import iotdevice.com.iotDevice.model.CustomerModel;



//class CustomerRepository(className: String?, nameForRestUrl: String?, userClass: Class<CustomerModel>) :
//        UserRepository<CustomerModel>(className, nameForRestUrl, userClass) {

class CustomerRepository: UserRepository<CustomerModel>("Customer", "Customers", CustomerModel::class.java) {
    interface LoginCallback: UserRepository.LoginCallback<CustomerModel>
}