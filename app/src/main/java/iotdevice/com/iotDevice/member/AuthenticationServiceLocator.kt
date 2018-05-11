package iotdevice.com.iotDevice.member

object AuthenticationServiceLocator {

    /**
     * Given the mall configuration, get the appropriate authentication
     * service that performs that marketplace's business logic.
     *
     * Defaults to [TokenManager] if there is no mall configuration
     * @return authentication service
     */
    val authService: AuthenticationSessionFacade
        get() = TokenManager
}
