package iotdevice.com.iotDevice.authenticator

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import iotdevice.com.iotDevice.login.LoginActivity
import iotdevice.com.iotDevice.login.LoginActivity.Companion.ARG_IS_ADDING_NEW_ACCOUNT
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class AccountAuthenticator(ctx: Context) : AbstractAccountAuthenticator(ctx), AnkoLogger {

    val context = ctx

    override fun getAuthTokenLabel(authTokenType: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?, options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.

        info(" enter getAuthToken")
        return Bundle()

//        val am = AccountManager.get(context)
//
//        var authToken: String? = am.peekAuthToken(account, authTokenType)
//
//        // Lets give another try to authenticate the user
//        if (null != authToken) {
//            if (authToken.isEmpty()) {
//                val password = am.getPassword(account)
//                if (password != null) {
//                    launch(CommonPool) {
//                        authToken = AuthUtil.mServerAuthenticator.signIn(account?.name?:"", password)
//
//                    }
//                }
//            }
//        }
//
//        // If we get an authToken - we return it
//        if (null != authToken) {
////            if (!authToken.isEmpty()) {
//                val result = Bundle()
//                result.putString(AccountManager.KEY_ACCOUNT_NAME, account?.name)
//                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account?.type)
//                result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
//                return result
////            }
//        }
//
//        // If we get here, then we couldn't access the user's password - so we
//        // need to re-prompt them for their credentials. We do that by creating
//        // an intent to display our AuthenticatorActivity.
//        val intent = Intent(context, LoginActivity::class.java)
//        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
//        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, account?.type)
//        intent.putExtra(LoginActivity.ARG_AUTH_TOKEN_TYPE, authTokenType)
//
//        // This is for the case multiple accounts are stored on the device
//        // and the AccountPicker dialog chooses an account without auth token.
//        // We can pass out the account name chosen to the user of write it
//        // again in the Login activity intent returned.
//        if (null != account) {
//            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name)
//        }
//
//        val bundle = Bundle()
//        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
//
//        return bundle
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle {
        return Bundle()
    }

    override fun addAccount(response: AccountAuthenticatorResponse?, accountType: String?,
                            authTokenType: String?, requiredFeatures: Array<out String>?, options: Bundle?): Bundle {
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType)
        intent.putExtra(AuthUtil.AUTH_TOKEN_TYPE_NAME, authTokenType)
        intent.putExtra(ARG_IS_ADDING_NEW_ACCOUNT, true)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)

        return bundle
    }

}