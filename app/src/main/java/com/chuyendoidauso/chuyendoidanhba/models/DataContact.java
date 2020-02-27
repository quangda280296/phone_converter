package com.chuyendoidauso.chuyendoidanhba.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataContact implements Serializable {
    public List<Contact> contactList;
    public String date;

    public List<Contact> getContactList() {
        if (this.contactList == null) {
            this.contactList = new ArrayList();
        }
        return this.contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }
}
