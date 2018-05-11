package iotdevice.com.iotDevice.login

import android.accounts.Account
import android.accounts.AccountAuthenticatorActivity
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import iotdevice.com.iotDevice.authenticator.AuthUtil
import iotdevice.com.iotDevice.member.TokenManager
import iotdevice.com.iotDevice.model.CustomerModel
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class LoginActivity: AccountAuthenticatorActivity(), AnkoLogger, TokenManager.LoginListener {
    override fun onLoginComplete(result: CustomerModel) {
        val authToken = result.id

        val accountName = loginUserName.text.toString()
        val accountPassword = loginPassword.text.toString()
        val account = Account(accountName, AuthUtil.ACCOUNT_TYPE_NAME)

        info("authToken : $authToken" )
        if (intent.getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, true)) {
            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager?.addAccountExplicitly(account, accountPassword, null)
            mAccountManager?.setAuthToken(account, AuthUtil.AUTH_TOKEN_TYPE_NAME, authToken)
        } else {
            mAccountManager?.setPassword(account, accountPassword)
        }

        val intent = Intent()
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName)
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AuthUtil.AUTH_TOKEN_TYPE_NAME)
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken)
        setAccountAuthenticatorResult(intent.extras)
        setResult(AccountAuthenticatorActivity.RESULT_OK, intent)
        finish()
    }

    override fun onLoginError(err: Throwable) {
        info("login error $err")
    }

    var mAccountAuthenticatorResponse: AccountAuthenticatorResponse? = null
    var mResultBundle: Bundle? = null
    var mAccountManager: AccountManager? = null



    companion object {
        const val ARG_ACCOUNT_TYPE = "accountType"
        const val ARG_AUTH_TOKEN_TYPE = "authTokenType"
        const val ARG_IS_ADDING_NEW_ACCOUNT = "isAddingNewAccount"
        const val PARAM_USER_PASSWORD = "password"
        const val KEY_IS_GLOBAL_PROFILE = "IS_GLOBAL_PROFILE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAccountManager = AccountManager.get(this)

//        mAccountAuthenticatorResponse =
//                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
//
//        if (mAccountAuthenticatorResponse != null) {
//            mAccountAuthenticatorResponse?.onRequestContinued()
//        }


//        val loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)



        loginBtn.setOnClickListener({ _ ->
            TokenManager.performLoginRequest(intent.extras, loginUserName.text.toString(),
                    loginPassword.text.toString(), this)


//            launch(UI) {
//                val authToken = async(UI) {
//                    AuthUtil.mServerAuthenticator.signIn(loginUserName.text.toString(), loginPassword.text.toString())
//                }
//
//                val accountName = loginUserName.text.toString()
//                val accountPassword = loginPassword.text.toString()
//                val account = Account(accountName, AuthUtil.ACCOUNT_TYPE_NAME)
//
//                info("authToken : $authToken" )
//                if (intent.getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, true)) {
//                    // Creating the account on the device and setting the auth token we got
//                    // (Not setting the auth token will cause another call to the server to authenticate the user)
//                    mAccountManager?.addAccountExplicitly(account, accountPassword, null)
//                    mAccountManager?.setAuthToken(account, AuthUtil.AUTH_TOKEN_TYPE_NAME, authToken.await())
//                } else {
//                    mAccountManager?.setPassword(account, accountPassword)
//                }
//
//                val intent = Intent()
//                intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName)
//                intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AuthUtil.AUTH_TOKEN_TYPE_NAME)
//                intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken.await())
//                setAccountAuthenticatorResult(intent.extras)
//                setResult(AccountAuthenticatorActivity.RESULT_OK, intent)
//                finish()



            })





//            loginViewModel.userLogin(loginUserName.text.toString(), loginPassword.text.toString()).observe(this, Observer<CustomerModel> {
//
//                info("get data from network : " + it.toString())
//
//
//


    }


    override fun onBackPressed() {
        setResult(AccountAuthenticatorActivity.RESULT_CANCELED)
        super.onBackPressed()
    }
}