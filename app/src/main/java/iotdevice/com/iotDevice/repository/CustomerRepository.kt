package iotdevice.com.iotDevice.repository;

import android.util.Log
import com.strongloop.android.loopback.UserRepository
import com.strongloop.android.remoting.adapters.Adapter
import iotdevice.com.iotDevice.member.MemberRequestService
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

    fun changePassword(oldPassword: String, newPassword: String, callback: Adapter.JsonCallback) {
        invokeStaticMethod("change-password",
                mapOf("oldPassword" to oldPassword, "newPassword" to newPassword),
                callback)
    }

    fun deleteAccount(userId: String, callback: Adapter.JsonCallback) {

        invokeStaticMethod("prototype.remove",
            mapOf("id" to userId),
            callback)
    }

    companion object {
        const val tag= "CustomerRepository"
    }
}