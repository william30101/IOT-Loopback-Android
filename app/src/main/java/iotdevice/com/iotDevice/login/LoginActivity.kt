package iotdevice.com.iotDevice.login

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iotDevice.common.IOTPreference
import iotdevice.com.iotDevice.home.HomeActivity
import iotdevice.com.iotDevice.member.TokenManager
import iotdevice.com.iotDevice.member.auth.AccountAuthenticatorActivity
import iotdevice.com.iotDevice.member.auth.AuthUtil
import iotdevice.com.iotDevice.model.CustomerModel
import iotdevice.com.iotDevice.register.RegisterActivity
import iotdevice.com.iotDevice.resetpassword.ResetPasswordActivity
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity


class LoginActivity : AccountAuthenticatorActivity(), AnkoLogger, TokenManager.LoginListener {

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
//        val loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        loginBtn.setOnClickListener({ _ ->
            checkUserInput()
        })

        registerTextView.setOnClickListener({ _ ->
            startActivity(RegisterActivityIntent())
        })

        forgotPasswordTextView.setOnClickListener({ _ ->
            startActivity<ResetPasswordActivity>()
        })

        val intent = Intent(this, HomeActivity::class.java)

        TokenManager.loginIfNeeded(this, intent, null)



    }

    fun Context.RegisterActivityIntent(): Intent {
        return Intent(this, RegisterActivity::class.java).apply {
            //            putExtra(INTENT_USER_ID, user.id)
        }
    }


    override fun onLoginComplete(result: CustomerModel) {
        val authToken = result.id
        val accountName = loginUserName.text.toString()
        val accountPassword = password_Edit.text.toString()
        val account = Account(accountName, AuthUtil.ACCOUNT_TYPE_NAME)

        info("authToken : $authToken")
        if (intent.getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, true)) {
            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager?.addAccountExplicitly(account, accountPassword, null)
            mAccountManager?.setAuthToken(account, AuthUtil.AUTH_TOKEN_TYPE_NAME, authToken)
            mAccountManager?.setPassword(account, accountPassword)
        } else {
            mAccountManager?.setPassword(account, accountPassword)
        }
        val intent = Intent(this, HomeActivity::class.java)
//        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName)
//        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AuthUtil.AUTH_TOKEN_TYPE_NAME)
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken)
        intent.putExtra(AccountManager.KEY_PASSWORD, accountPassword)
        setAccountAuthenticatorResult(intent.extras)
        setResult(RESULT_OK, intent)

        IOTPreference.saveUserName(accountName)
        startActivity(intent)
        finish()
    }

    override fun onLoginError(err: Throwable) {
        info("login error $err")
        DialogUtils.createAlertDialog( this, getString(R.string.login_title), msg = err.message)
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }

    private fun checkUserInput() {
        if (loginUserName.text.toString().isNotEmpty() &&
                password_Edit.text.toString().isNotEmpty()) {
            TokenManager.performLoginRequest(loginUserName.text.toString(),
                    password_Edit.text.toString(), this)
        } else {
            DialogUtils.createAlertDialog( this, getString(R.string.login_title), msg = resources.getString(R.string.loginin_field_not_completed))
        }
    }
}