package com.chuyendoidauso.chuyendoidanhba.sim;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nagaraj on 12/01/15.
 */
public class SimContactsRepositoryImpl{
    private Activity parentActivity;

    private static final String SIM_DISPLAY_NAME = "name";
    private static final String TAG = "tag";
    private static final String SIM_PHONE_NUMBER = "number";
    private static final String SIM_CONTACT_IDENTIFIER = "9999";
    public static final String CONTACT_TYPE_SIM = "Sim";
    public static final Uri SIM_CONTENT_URI = Uri.parse("content://icc/adn");
    public static final String WHERE_TAG = TAG + "=? AND " + SIM_PHONE_NUMBER + "=?";


    public SimContactsRepositoryImpl(Activity activity){
        parentActivity = activity;
    }


    public ContactSim getById(String id) {
        Cursor simContactsCursor = parentActivity.getContentResolver().query(SIM_CONTENT_URI, null, null, null, null);
        while (simContactsCursor.moveToNext()){
            String name = simContactsCursor.getString(simContactsCursor.getColumnIndex(SIM_DISPLAY_NAME));
            if (name.equals(id)){
                return getContact(simContactsCursor);
            }
        }
        return null;
    }


    public boolean delete(ContactSim contact) {
        String whereCondition = String.format("tag='%s' AND number='%s'", contact.name, contact.number);
        int rowsDeleted = parentActivity.getContentResolver().delete(SIM_CONTENT_URI, whereCondition, null);
        return rowsDeleted >= 1;
    }


    public boolean save(ContactSim contact) {
        return false;
    }


    public List<ContactSim> getAll() {
        Cursor simContacts = parentActivity.getContentResolver().query(SIM_CONTENT_URI, null, null, null, null);
        List<ContactSim> simContactsForApp = new ArrayList<>();
        while (simContacts.moveToNext()){
            simContactsForApp.add(getContact(simContacts));
        }
        return simContactsForApp;
    }

    private ContactSim getContact(Cursor simContactsCursor) {
        ArrayList<String> phoneNumbers = new ArrayList<>();

        String name =simContactsCursor.getString(simContactsCursor.getColumnIndex(SIM_DISPLAY_NAME));
        phoneNumbers.add(simContactsCursor.getString(simContactsCursor.getColumnIndex(SIM_PHONE_NUMBER)));

        ContactSim contact = new ContactSim(SIM_CONTACT_IDENTIFIER,name,phoneNumbers.get(0));
        contact.id = SIM_CONTACT_IDENTIFIER;
        contact.name = name;
        return contact;
    }

}