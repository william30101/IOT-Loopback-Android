package iotdevice.com.iotDevice.member

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.login.LoginActivity
import iotdevice.com.iotDevice.member.auth.AuthUtil
import iotdevice.com.iotDevice.model.CustomerModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.sql.Timestamp

object TokenManager: AuthenticationSessionFacade {

    val tag = "TokenManager"
    var passwordFromManager = ""
    var mUsername = ""           // User name for currently logged in user


    private var mAccountManager: AccountManager? = null


    private var mLoggedIn: Boolean = false          // Is a user logged in
    private var mAuthToken: String? = null          // Auth token for currently logged in user
    private var mRefreshToken: String? = null       // Refresh token for currently logged in user
    private var mExpiryTimestamp: Timestamp? = null // Expiry timestamp for auth token
    private var mTokenAvailableListener: GMTokenAvailableListener? = null

    private fun getAccount(): Account? {
        val accounts = mAccountManager?.getAccountsByType(AuthUtil.ACCOUNT_TYPE_NAME)

        return if (accounts?.size == 0) null else accounts?.get(0)
    }

    fun initialize(context: Context) {
        mTokenAvailableListener = null
        mAccountManager = AccountManager.get(context)
        mLoggedIn = false
        mExpiryTimestamp = Timestamp(System.currentTimeMillis())

        // set initial login status depending from registered account if any
        val a = getAccount()

        if (a != null) {
//            val future = mAccountManager?.getAuthToken(a, AuthUtil.AUTH_TOKEN_TYPE_NAME, null, null, null, null)

//            launch(CommonPool) {
                try {

//                    val authToken = future!!.result
                    val accountName = mAccountManager!!.accounts[0].name
                    val password = mAccountManager!!.getPassword(a)

                    // TODO: get expire time in the future, currently, we use unlimited date.
                    mExpiryTimestamp = Timestamp.valueOf("2099-12-30 23:59:59")
                    val refreshToken = ""
                    setLoginStatus(true, accountName, mExpiryTimestamp!!, refreshToken, password)

                } catch (e: Exception) {
                    Log.i(tag, "getAuthToken failed: " + e.message)
                }
//            }

        } else if (!isLoggedIn) {
            // TODO: maybe we should generate anonymous token for guest.
//            TokenManager.INSTANCE.getAnonymousToken()
        }
    }

    interface LoginListener {

        fun onLoginComplete(result: CustomerModel)

        fun onLoginError(err: Throwable)

    }

    interface RegisterListener {

        fun onRegisterComplete(result: Any?)

        fun onRegisterError(err: Throwable)

    }

    fun performLoginRequest( userName: String, password: String, loginListener: LoginListener) {

        val memberRequestService = MemberRequestService()

        MainScope().launch {
            memberRequestService.signIn(userName, password, loginListener)
        }
    }

    fun performRegisterRequest(authParams: Bundle?, email: String, userName: String, password: String, resiterListener: RegisterListener) {

        val memberRequestService = MemberRequestService()

        MainScope().launch {
            memberRequestService.register(email, password, userName, resiterListener)
        }
    }

    private fun setLoginStatus(loggedIn: Boolean, userName: String, expiry: Timestamp,
                        refreshToken: String, password: String) {
        mLoggedIn = loggedIn
        mUsername = userName
        mExpiryTimestamp = expiry
        mRefreshToken = refreshToken
        passwordFromManager = password

        Log.i(tag, "======= set Login status")
    }

    override val isLoggedIn: Boolean
        get() {
            var isLoggedIn = false
            val account = getAccount()
//            val isTokenFresh = mExpiryTimestamp.after(Timestamp(System.currentTimeMillis()))
//            var isGlobalProfile = false

//            if (account != null) {
//                val isGlobalProfileStr = mAccountManager?.getUserData(account, LoginActivity.KEY_IS_GLOBAL_PROFILE)
//                isGlobalProfile = java.lang.Boolean.valueOf(isGlobalProfileStr)
//            }

            // is user logged in, account still exists, timestamp's good & has global profile
            if (mLoggedIn &&
                    account != null
//                    isTokenFresh
                ) {

                isLoggedIn = true
                Log.i(tag, "currently logged in")
            }

            return isLoggedIn
        }


    override fun openAuthenticatedActivity(launchingActivity: Activity, destinationIntent: Intent) {
        loginIfNeeded(launchingActivity, destinationIntent, null)
    }

    private fun requestLogin(launchingActivity: Activity, destinationIntent: Intent?) {
        Log.i(tag, "No accounts registered, requesting login")

        val intent = Intent(App.sInstance.context, LoginActivity::class.java)

        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, AuthUtil.ACCOUNT_TYPE_NAME)
        intent.putExtra(LoginActivity.ARG_AUTH_TOKEN_TYPE, AuthUtil.AUTH_TOKEN_TYPE_NAME)

        if (destinationIntent != null)
            intent.putExtra(AccountManager.KEY_INTENT, destinationIntent)

        launchingActivity.startActivity(intent)
    }

//    @Synchronized
//    fun hasValidToken(): Boolean {
//        val t = Timestamp(System.currentTimeMillis())
//        info( "now  = $t")
//        info( "expiryTimeStamp = $mExpiryTimestamp")
//
//        return mExpiryTimestamp.after(Timestamp(System.currentTimeMillis()))
//    }

    fun loginIfNeeded(launchingActivity: Activity, destinationIntent: Intent?,
                      tokenListener: GMTokenAvailableListener?) {
        mTokenAvailableListener = tokenListener
        val account = getAccount()

        Log.i(tag, " ==== check login status")

        if (!isLoggedIn) {  // no accounts, request login and create new
//            requestLogin(launchingActivity, destinationIntent)
            return
        }

        mAccountManager?.getAuthToken(account, AuthUtil.AUTH_TOKEN_TYPE_NAME, null, null,
                { future ->
                    try {
//                        val bnd = future.result

//                        val authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN)
//                        val userName = bnd.getString(AccountManager.KEY_ACCOUNT_NAME)
//                        val password = bnd.getString(AccountManager.KEY_PASSWORD)

//                val expires = mAccountManager.getUserData(account, AuthenticatorActivity.KEY_TOKEN_EXPIRY_TIMESTAMP)
//                mExpiryTimestamp = Timestamp.valueOf(expires)
//
//                val refreshToken = mAccountManager.getUserData(account, AuthenticatorActivity.KEY_TOKEN_REFRESH_TOKEN)

//                if (hasValidToken()) {
                        //Log.d(TAG, "Success getting token: " + mAuthToken);
                        //                                Log.d(TAG, "    token expires " + expires);

                        // store login status
//                    setLoginStatus(true, mUsername, mExpiryTimestamp, mAuthToken, refreshToken)
//                        setLoginStatus(true, userName, mExpiryTimestamp!!, "false", password)

//
                        // Verify Token available later
//                        if (mTokenAvailableListener != null) {
//                            mTokenAvailableListener?.OnLoginTokenAvailable(mAuthToken!!)
//                            mTokenAvailableListener = null
//                        }

                        // Launch next activity if there is one
                        if (destinationIntent != null) {
                            destinationIntent.setExtrasClassLoader(App::class.java.classLoader)
                            destinationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            destinationIntent.putExtra("token", mAuthToken)
                            launchingActivity.startActivity(destinationIntent)
                            launchingActivity.finish()
                        }
//                } else {
//                   info( "Token has expired, deleting account and requesting new login")
//                    logout()
//                    requestLogin(launchingActivity, destinationIntent)
//                }
                    } catch (e: Exception) {
                        Log.i(tag, "getAuthToken failed: " + e.message)

                        if (mTokenAvailableListener != null) {
                            mTokenAvailableListener?.OnTokenFailed(null)
                            mTokenAvailableListener = null
                        }
                    }
                }, null)
    }

    override fun logout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    interface GMTokenAvailableListener {

        fun OnLoginTokenAvailable(token: String)

        fun OnTokenFailed(error: Throwable?)
    }

}