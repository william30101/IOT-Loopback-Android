package iotdevice.com.iotDevice.model

import com.strongloop.android.loopback.User

class CustomerModel : User() {
    var lincenNo: String = ""
    var username: String = ""
    var emailVerified: Boolean = false
    // TODO: fix the loopback bug, can't use array now.
//    var customerAddress: CustomerAddress  = CustomerAddress()
//    var emailList: Array<EmailList> = emptyArray()
    var id: String = ""

}