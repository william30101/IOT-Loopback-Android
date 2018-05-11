package iotdevice.com.iotDevice.common.network

class GMServerError {

    private var mError =""

    private var mErrorDescription = ""

    private var mStatusCode = 0

    fun GMServerError(err: String, desc: String, statusCode: Int) {
        mError = err
        mErrorDescription = desc
        mStatusCode = statusCode
    }

}