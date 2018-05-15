package iotdevice.com.iotDevice.member

import com.strongloop.android.loopback.AccessToken
import com.strongloop.android.remoting.adapters.Adapter
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.model.CustomerModel
import iotdevice.com.iotDevice.repository.CustomerRepository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MemberRequestService : MemberService, AnkoLogger {

    override suspend fun register(email: String, password: String, username: String,
                                  registerListener: TokenManager.RegisterListener?) {

        val adapter = App.sInstance.loopBackAdapter
        val customerRepo = adapter.createRepository(CustomerRepository::class.java)

        customerRepo.invokeStaticMethod("prototype.create",
                mapOf("email" to email, "password" to password, "username" to username),
                object : Adapter.JsonCallback() {
                    override fun onSuccess(response: Any?) {
                        info("success : " + response)
                        registerListener?.onRegisterComplete(response)
                    }

                    override fun onError(t: Throwable) {
                        info("error : " + t)
                        registerListener?.onRegisterError(t)
                    }

                })
    }

    override fun signUp(email: String, username: String, password: String): String {
        // TODO: register new user on the server and return its auth token
        return ""
    }

    override suspend fun signIn(email: String, password: String, loginListener: TokenManager.LoginListener) {

        val adapter = App.sInstance.loopBackAdapter
        val customerRepo = adapter.createRepository(CustomerRepository::class.java)

        customerRepo.loginUser(email, password,
                object : CustomerRepository.LoginCallback {
                    override fun onSuccess(token: AccessToken?, currentUser: CustomerModel?) {
                        info("currentUser :" + currentUser?.username + " AccessToken" + token)

                        currentUser?.id = token?.id.toString()
                        loginListener.onLoginComplete(currentUser!!)
                    }

                    override fun onError(trow: Throwable) {
                        // login failed
                        info("loginfail $trow")
                        loginListener.onLoginError(trow)
                    }
                })
    }
}