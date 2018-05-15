package iotdevice.com.iotDevice.member

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.login.LoginActivity
import iotdevice.com.iotDevice.member.auth.AuthUtil
import iotdevice.com.iotDevice.model.CustomerModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.sql.Timestamp

object TokenManager: AuthenticationSessionFacade, AnkoLogger {

    private var mAccountManager: AccountManager? = null

    private var mLoggedIn: Boolean = false          // Is a user logged in
    private var mUsername: String? = null           // User name for currently logged in user
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
            val future = mAccountManager?.getAuthToken(a, AuthUtil.AUTH_TOKEN_TYPE_NAME, null, null, null, null)

            launch(CommonPool) {
                try {
                    val bnd = future?.result

                    val authToken = bnd?.getString(AccountManager.KEY_AUTHTOKEN) ?: ""
                    val userName = bnd?.getString(AccountManager.KEY_ACCOUNT_NAME) ?: ""

                    // TODO: get expire time in the future, currently, we use unlimited date.
                    mExpiryTimestamp = Timestamp.valueOf("2099-12-30 23:59:59")
                    val refreshToken = ""
                    setLoginStatus(true, userName, mExpiryTimestamp!!, authToken, refreshToken)

                } catch (e: Exception) {
                    info("getAuthToken failed: " + e.message)
                }
            }

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

    fun performLoginRequest(authParams: Bundle?, userName: String, password: String, loginListener: LoginListener) {

        val memberRequestService = MemberRequestService()

        launch(UI) {
            memberRequestService.signIn(userName, password, loginListener)
        }
    }

    fun performRegisterRequest(authParams: Bundle?, email: String, userName: String, password: String, resiterListener: RegisterListener) {

        val memberRequestService = MemberRequestService()

        launch(UI) {
            memberRequestService.register(email, password, userName, resiterListener)
        }
    }

    private fun setLoginStatus(loggedIn: Boolean, userName: String, expiry: Timestamp,
                       authToken: String, refreshToken: String) {
        mLoggedIn = loggedIn
        mUsername = userName
        mExpiryTimestamp = expiry
        mAuthToken = authToken
        mRefreshToken = refreshToken
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
                info("currently logged in")
            }

            return isLoggedIn
        }


    override fun openAuthenticatedActivity(launchingActivity: Activity, destinationIntent: Intent) {
        loginIfNeeded(launchingActivity, destinationIntent, null)
    }

    private fun requestLogin(launchingActivity: Activity, destinationIntent: Intent?) {
        info("No accounts registered, requesting login")

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

        if (!isLoggedIn) {  // no accounts, request login and create new
            requestLogin(launchingActivity, destinationIntent)
            return
        }

        mAccountManager?.getAuthToken(account, AuthUtil.AUTH_TOKEN_TYPE_NAME, null, null,
                AccountManagerCallback { future ->
                    try {
                        val bnd = future.result

                        mAuthToken = bnd.getString(AccountManager.KEY_AUTHTOKEN)
                        mUsername = bnd.getString(AccountManager.KEY_ACCOUNT_NAME)

//                val expires = mAccountManager.getUserData(account, AuthenticatorActivity.KEY_TOKEN_EXPIRY_TIMESTAMP)
//                mExpiryTimestamp = Timestamp.valueOf(expires)
//
//                val refreshToken = mAccountManager.getUserData(account, AuthenticatorActivity.KEY_TOKEN_REFRESH_TOKEN)

//                if (hasValidToken()) {
                        //Log.d(TAG, "Success getting token: " + mAuthToken);
                        //                                Log.d(TAG, "    token expires " + expires);

                        // store login status
//                    setLoginStatus(true, mUsername, mExpiryTimestamp, mAuthToken, refreshToken)
                        setLoginStatus(true, mUsername!!, mExpiryTimestamp!!, mAuthToken!!, "false")


                        if (mTokenAvailableListener != null) {
                            mTokenAvailableListener?.OnLoginTokenAvailable(mAuthToken!!)
                            mTokenAvailableListener = null
                        }

                        // Launch next activity if there is one
                        if (destinationIntent != null) {
                            destinationIntent.setExtrasClassLoader(App::class.java.classLoader)
                            destinationIntent.putExtra("token", mAuthToken)
                            launchingActivity.startActivity(destinationIntent)
                        }
//                } else {
//                   info( "Token has expired, deleting account and requesting new login")
//                    logout()
//                    requestLogin(launchingActivity, destinationIntent)
//                }
                    } catch (e: Exception) {
                        info("getAuthToken failed: " + e.message)

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