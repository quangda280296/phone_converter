package com.chuyendoidauso.chuyendoidanhba.ui.change.models;

import com.chuyendoidauso.chuyendoidanhba.models.Contact;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    private List<Contact> contactList;

    public Data(List<Contact> list) {
        this.contactList = list;
    }

    public List<Contact> getContactList() {
        return this.contactList;
    }
}
