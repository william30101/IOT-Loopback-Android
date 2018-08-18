package iotdevice.com.iotDevice.repository;

import com.strongloop.android.loopback.UserRepository
import com.strongloop.android.remoting.adapters.Adapter
import iotdevice.com.iotDevice.model.CustomerModel


//class CustomerRepository(className: String?, nameForRestUrl: String?, userClass: Class<CustomerModel>) :
//        UserRepository<CustomerModel>(className, nameForRestUrl, userClass) {

class CustomerRepository: UserRepository<CustomerModel>("Customers", "Customers", CustomerModel::class.java) {
    interface LoginCallback: UserRepository.LoginCallback<CustomerModel>

    fun resetPassword(email: String, callback: Adapter.JsonCallback) {
//        val optionJson = JSONObject("""{"email" : $email}""")

        invokeStaticMethod("reset",
                mapOf("email" to email),
                callback)
    }
}