package iotdevice.com.iotDevice.member

import android.app.Activity
import android.content.Intent

interface AuthenticationSessionFacade {

    /**
     * Check login state.
     * This method should be used for authentication flows where user has no option to authenticate if not logged-in
     * @return true if logged-in, false if not
     */
    val isLoggedIn: Boolean

    /**
     * Attempt to perform an activity that requires login depending on
     * implementing class.
     * This method should be used for authentication flow where user has
     * option to log-in.
     *
     * Implementation of this method requires UI, and must handle error
     * cases with UI prompts.
     * @param launchingActivity the originator of activity action
     * @param destinationIntent the intended activity action
     */
    fun openAuthenticatedActivity(launchingActivity: Activity, destinationIntent: Intent)

    /**
     * Clear login state
     */
    fun logout()
}
