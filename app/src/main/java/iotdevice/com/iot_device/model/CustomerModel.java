package iotdevice.com.iot_device.model;

import com.strongloop.android.loopback.User;

public class CustomerModel extends User {
        String lincenNo;
        String realm;
        String username;
        String email;
        boolean emailVerified;
        CustomerAddress[] customerAddress;
        EmailList[] emailList;

    public String getLincenNo() {
        return lincenNo;
    }

    public void setLincenNo(String lincenNo) {
        this.lincenNo = lincenNo;
    }

    @Override
    public String getRealm() {
        return realm;
    }

    @Override
    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public CustomerAddress[] getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(CustomerAddress[] customerAddress) {
        this.customerAddress = customerAddress;
    }

    public EmailList[] getEmailList() {
        return emailList;
    }

    public void setEmailList(EmailList[] emailList) {
        this.emailList = emailList;
    }
}



