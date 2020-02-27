package com.chuyendoidauso.chuyendoidanhba.sim;

import android.util.Log;

/** small class which represents a contact on any view */
public class ContactSim {
    private static final String TAG = ContactSim.class.getSimpleName();

    public String name;
    public String number;
    public String id;

    public ContactSim(String id, String name, String number) {
        if(Log.isLoggable(TAG, Log.DEBUG))
            Log.d(TAG, "Contact()["+id+"] '"+name+"': "+number);
        this.id = id;
        this.name = name;
        this.number = number;
    }

    /**
     * @return the string for the Listview */
    @Override
    public String toString() {
        return "["+ id + "]'" + name +"': " + number;
    }

    // null-safe string compare
    public boolean compareStrings(final String one, final String two) {
        if (one == null ^ two == null) {
            return false;
        }
        if (one == null && two == null) {
            return true;
        }
        return one.compareTo(two) == 0;
    }

    @Override
    public boolean equals(Object o) {
        // if not Contact, can't be true
        if(!(o instanceof ContactSim))
            return false;
        ContactSim c = (ContactSim)o;

        // only if id's present, compare them
        if((id != null) && (id.length()) > 0 && (c.id.length() > 0))
            return c.id.compareTo(id) == 0;

        // if SimNames not equal...
        if(compareStrings(name, c.name) == false) {
            return false;
        }

        // finally if numbers not equal...
        return compareStrings(number, c.number);
    }
}