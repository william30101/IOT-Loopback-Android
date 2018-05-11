package iotdevice.com.iotDevice.authenticator

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import iotdevice.com.iotDevice.member.AuthenticationServiceLocator


class AuthUtil {
    fun getAccount(context: Context, accountName: String): Account? {
        val accountManager = AccountManager.get(context)
        val accounts = accountManager.getAccountsByType(ACCOUNT_TYPE_NAME)
        for (account in accounts) {
            if (account.name.equals(accountName, ignoreCase = true)) {
                return account
            }
        }
        return null
    }

    companion object {
        var mServerAuthenticator = AuthenticationServiceLocator.authService
        const val ACCOUNT_TYPE_NAME = "iotdevice.com.iotDevice"
        const val AUTH_TOKEN_TYPE_NAME = "com.iotDevice.token"
    }
}

