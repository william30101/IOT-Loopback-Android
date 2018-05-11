package iotdevice.com.iotDevice.member

import com.strongloop.android.loopback.AccessToken
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.model.CustomerModel
import iotdevice.com.iotDevice.repository.CustomerRepository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MemberRequestService : MemberService, AnkoLogger {

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