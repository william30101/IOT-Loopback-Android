package iotdevice.com.iotDevice.common.network

class ServerError {

    private var mError =""

    private var mErrorDescription = ""

    private var mStatusCode = 0

    fun ServerError(err: String, desc: String, statusCode: Int) {
        mError = err
        mErrorDescription = desc
        mStatusCode = statusCode
    }

}