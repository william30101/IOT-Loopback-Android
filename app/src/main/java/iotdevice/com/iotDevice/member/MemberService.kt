package iotdevice.com.iotDevice.member

interface MemberService {

    /**
     * Tells the server to create the new user and return its auth token.
     * @param email
     * @param username
     * @param password
     * @return Access token
     */
    fun signUp(email: String, username: String, password: String): String

    /**
     * Logs the user in and returns its auth token.
     * @param email
     * @param password
     * @return Access token
     */
    suspend fun signIn(email: String, password: String, loginListener: TokenManager.LoginListener)

    suspend fun register(email: String, password: String, username: String,
                         registerListener: TokenManager.RegisterListener?)

}