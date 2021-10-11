package com.wwg.addressbookindex;

import com.wwg.addressnook_index.BaseAddressBookBean;

public class AddressBookBean extends BaseAddressBookBean {

    private String userName;
    private String userPhone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
