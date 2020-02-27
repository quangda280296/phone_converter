package com.chuyendoidauso.chuyendoidanhba.commons;//package com.chuyendoidauso.chuyendoidanhba.commons;

import java.util.*;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.chuyendoidauso.chuyendoidanhba.models.Contact;
import com.chuyendoidauso.chuyendoidanhba.sim.ContactSim;

public class SimUtil {
    private static final String TAG = SimUtil.class.getSimpleName();

    private ContentResolver resolver;
    private Uri simUri;
    private Context context;
    private Integer maxContactNameLength; // Maximum length of contact names may
    private HashMap<String, Contact> hashMapContact = new HashMap<>();
    // differ from SIM to SIM, will be
    // detected upon load of class

    public SimUtil(ContentResolver resolver) {
        new SimUtil(null, resolver);
    }

    public SimUtil(Context context, ContentResolver resolver) {
        this.context = context;
        if (Log.isLoggable(TAG, Log.DEBUG))
            Log.d(TAG, "SimUtil(ContentResolver)");
        if (Log.isLoggable(TAG, Log.VERBOSE))
            Log.v(TAG, " Contentresolver(" + resolver + ")");

        this.resolver = resolver;

        // URI for SIM card is different on Android 1.5 and 1.6
        simUri = Uri.parse(detectSimUri());
        //maxContactNameLength = detectMaxContactNameLength();
    }

    /**
     * Detects the URI identifier for accessing the SIM card. Is different,
     * depending on Android version.
     *
     * @return Uri of the SIM card on this system
     */
    private String detectSimUri() {
        if (Log.isLoggable(TAG, Log.DEBUG))
            Log.d(TAG, "detectSimUri()");

        Uri uri15 = Uri.parse("content://sim/adn/"); // URI of Sim card on
        // Android 1.5
        // Uri uri16 = Uri.parse("content://icc/adn/"); // URI of Sim card on
        // Android 1.6

        // resolve something from Sim card
        Cursor results = resolver.query(uri15, new String[]{android.provider.BaseColumns._ID}, null, null, null);

        // if null, we can use the 1.6 URI, otherwise we're on 1.5
        System.out.println("detectSimUri" + results);
        if (null == results) {
            return "content://icc/adn";
        } else {
            return "content://sim/adn";
        }
    }

    /**
     * Detects the maximum length of a contacts name which is accepted by the
     * SIM card.
     *
     * @return Length of the longest contact name the SIM card accepts
     */

    private Integer detectMaxContactNameLength() {
        if (Log.isLoggable(TAG, Log.DEBUG))
            Log.d(TAG, "detectMaxContactNameLength()");

        String nameString = "sImSaLabiMXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"; // 51 chars
        Integer currentMax = nameString.length();

        // used for test-creation
        Uri createdUri = null;
        ContactSim testContact = null;

        // loop from longest to shortest contact name length until a contact was
        // stored successfully
        //for (currentMax = nameString.length(); ((createdUri == null) && currentMax > 0); currentMax--) {
        testContact = new ContactSim(null, "Kieu", "841664789789");
        createdUri = createContact(testContact);
        //}

        // if not stored successfully
        if ((null == createdUri) || (!createdUri.toString().contains("/adn/0"))) {
            return null;
        }

        // if stored successfully, remove contact again
        //  deleteContact(testContact);

        return currentMax;
    }

    public void addContact(Contact contact) {
        if (Log.isLoggable(TAG, Log.DEBUG))
            Log.d(TAG, "detectMaxContactNameLength()");

        // String nameString = "sImSaLabiMXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"; // 51 chars
        // Integer currentMax = nameString.length();

        // used for test-creation
        //Uri createdUri = null;

        // loop from longest to shortest contact name length until a contact was
        // stored successfully
        //for (currentMax = nameString.length(); ((createdUri == null) && currentMax > 0); currentMax--) {
        ContactSim contactSimOld = new ContactSim(null, contact.getName(), contact.getIndsOld());
        System.out.println("contactSimold:" + contactSimOld.name + contactSimOld.number);
        deleteContact(contactSimOld);
        ContactSim contactSimNew = new ContactSim(null, contact.getName(), contact.getNewIsdn());
        System.out.println("contactSimNew" + contactSimNew.name + contactSimNew.number);
        createContact(contactSimNew);
        //createdUri = createContact(testContact);
        //}

        // if not stored successfully
        //if ((null == createdUri) || (!createdUri.toString().contains("/adn/0"))) {
        //    return null;
        // }

        // if stored successfully, remove contact again
        //deleteContact(testContact);

        // return currentMax;
    }

    /**
     * Retrieves all contacts from the SIM card.
     *
     * @return List containing Contact objects from the stored SIM information
     */
    public List<ContactSim> retrieveSIMContacts() {
        if (Log.isLoggable(TAG, Log.DEBUG))
            Log.d(TAG, "retrieveSIMContacts()");

        // get these columns
        final String[] simProjection = new String[]{ //
                android.provider.Contacts.PeopleColumns.NAME, //
                android.provider.Contacts.PhonesColumns.NUMBER, //
                android.provider.BaseColumns._ID};

        Cursor results = resolver.query( //
                simUri, // URI of contacts on SIM card
                simProjection, // get above defined columns
                null, null, android.provider.Contacts.PeopleColumns.NAME); // order by name

        final ArrayList<ContactSim> simContacts;
        if (results != null && results.getCount() > 0) {
            simContacts = new ArrayList<ContactSim>(results.getCount());
            // create array of SIM contacts and fill it
            while (results.moveToNext()) {
                final ContactSim simContact = new ContactSim(//
                        results.getString(2), // _id
                        results.getString(0), // name
                        results.getString(1));// number

                simContacts.add(simContact);
                //Log.d(TAG, results.getString(2)+","+results.getString(0)+","+results.getString(1));
            }
        } else {
            simContacts = new ArrayList<ContactSim>(0);
        }
        return simContacts;
    }

    /**
     * Creates a contact on the SIM card.
     *
     * @param newSimContact The Contact object containing the name and number of the
     *                      contact
     * @return the Uri of the newly created contact, "AdnFull" if there was no
     * more space left on the SIM card or null if an error occured (ie.
     * the contact name was too long for the SIM).
     */
    public Uri createContact(ContactSim newSimContact) {
        if (Log.isLoggable(TAG, Log.DEBUG))
            Log.d(TAG, "createContact(Contact)");
        if (Log.isLoggable(TAG, Log.VERBOSE))
            Log.v(TAG, " Contact(" + newSimContact + ")");

        // add it on the SIM card
        ContentValues newSimValues = new ContentValues();
        newSimValues.put("tag", newSimContact.name);
        newSimValues.put("number", newSimContact.number);
        Uri newSimRow = resolver.insert(simUri, newSimValues);

        // TODO: further row values: "AdnFull", "/adn/0"
        // TODO: null could also mean that the contact name was too long?
        return newSimRow;
    }

    /**
     * Delete a contact on the SIM card. Will only be removed if identified
     * uniquely. Identification happens on the contact.name and contact.number
     * attributes.
     *
     * @param contact The contact to delete.
     * @return 0 if the contact was deleted, -1 if an error happened during
     * deletion and nothing was deleted, otherwise the number of
     * multiple contacts which were identified.
     */
    public int deleteContact(ContactSim contact) {
        if (Log.isLoggable(TAG, Log.DEBUG))
            Log.d(TAG, "deleteContact(Contact)");
        if (Log.isLoggable(TAG, Log.VERBOSE))
            Log.v(TAG, " Contact(" + contact + ")");

        // check, that only one contact with this identifiers existing
        // TODO: currently this always returns ALL contacts on the SIM. Is this
        // a bug in the content provider?
        // Cursor results = resolver.query(simUri, new
        // String[]{android.provider.BaseColumns._ID},
        // "tag='"+contact.name+"' AND number='"+contact.number+"'", null,
        // null);
        // int rowCount = results.getCount();
        // if (rowCount > 1) {
        // return rowCount;
        // }

        // TODO: Can this ever return >1 after check above?
        int deleteCount = resolver.delete(simUri, "tag='" + contact.name + "' AND number='" + contact.number + "'", null);
        if (deleteCount >= 1) {
            //if (context != null) Toast.makeText(context, "Da xoa", Toast.LENGTH_LONG).show();
            Log.e(TAG, " deleteCount=" + deleteCount + " on deletion \"tag='" + contact.name + "' AND number='" + contact.number + "'\"");
            return deleteCount;
        } else {
            //if (context != null) Toast.makeText(context, "false", Toast.LENGTH_LONG).show();
        }

        return 0;
    }

    public void deleteContact(Contact contact) {
        ContactSim testContact = new ContactSim(null, contact.getName(), contact.getNewIsdn());
        deleteContact(testContact);
    }

    public void deleteContactUpdate(Contact contact) {
        ContactSim testContact = new ContactSim(null, contact.getName(), contact.getIsdn());
        System.out.println("DKM:" + contact.getIsdn() + contact.getName());
        deleteContact(testContact);
    }

    public void deleteContactRecovery(Contact contact) {
        ContactSim testContact = new ContactSim(null, contact.getName(), contact.getNewIsdn());
        deleteContact(testContact);
    }

    public void addContactUpdate(Contact contact) {
        ContactSim contactSimNew = new ContactSim(null, contact.getName(), contact.getNewIsdn());
        System.out.println("DKM:C:" + contactSimNew.name + contactSimNew.number);
        Uri createdUri = createContact(contactSimNew);
        // if not stored successfully
        if ((null == createdUri) || (!createdUri.toString().contains("/adn/0"))) {
            System.out.println("DKM: Thanh cong");
            // if (context != null)
            //    Toast.makeText(context, "Tao khonnh cong", Toast.LENGTH_LONG).show();
        } else {
            // if (context != null)
            //     Toast.makeText(context, "Tao Thanh cong", Toast.LENGTH_LONG).show();
            System.out.println("DKM: that bai");
        }
    }


    public void addContactRecovery(Contact contact) {
        ContactSim contactSimNew = new ContactSim(null, contact.getName(), contact.getIsdn());
        System.out.println("dataContact:New" + contactSimNew.name + contactSimNew.number);
        Uri createdUri = createContact(contactSimNew);
        // if not stored successfully
        if ((null == createdUri) || (!createdUri.toString().contains("/adn/0"))) {
            // if (context != null)
            //Toast.makeText(context, "Tao khonnh cong", Toast.LENGTH_LONG).show();
            System.out.println("dataContact:New" + "OK");
        } else {
            //if (context != null)
            System.out.println("dataContact:New" + "FAIL");
            // Toast.makeText(context, "Tao Thanh cong", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Converts a contact to a SIM card conforming contact by stripping the name
     * to the maximum allowed length and setting ID to null.
     *
     * @param contact The contact to convert to SIM conforming values
     * @return a contact which does not contain values which exceed the SIM
     * cards limits or null if there was a problem detecting the limits
     */
    public ContactSim convertToSimContact(ContactSim contact) {
        if (Log.isLoggable(TAG, Log.DEBUG))
            Log.d(TAG, "convertToSimContact()");
        if (Log.isLoggable(TAG, Log.VERBOSE))
            Log.v(TAG, " Contact(" + contact + ")");

        // if no max length yet, try to detect once more
        if (maxContactNameLength == null) {
            maxContactNameLength = detectMaxContactNameLength();

            // if still null, give up
            if (maxContactNameLength == null) {
                Log.w(TAG, " unable to detect maximum length of SIM contact name");
                return null;
            }
        }

        // convert and return
        return new ContactSim(null, contact.name.substring(0, Math.min(contact.name.length(), maxContactNameLength)), contact.number);
    }


    public void recoveryContact(Contact contact) {
        if (Log.isLoggable(TAG, Log.DEBUG))
            Log.d(TAG, "detectMaxContactNameLength()");

        // String nameString = "sImSaLabiMXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"; // 51 chars
        // Integer currentMax = nameString.length();

        // used for test-creation
        //Uri createdUri = null;


        // loop from longest to shortest contact name length until a contact was
        // stored successfully
        //for (currentMax = nameString.length(); ((createdUri == null) && currentMax > 0); currentMax--) {
        ContactSim contactSimOld = new ContactSim(null, contact.getName(), contact.getNewIsdn());
        deleteContact(contactSimOld);
        ContactSim contactSimNew = new ContactSim(null, contact.getName(), contact.getIndsOld());
        createContact(contactSimNew);
        //createdUri = createContact(testContact);
        //}

        // if not stored successfully
        //if ((null == createdUri) || (!createdUri.toString().contains("/adn/0"))) {
        //    return null;
        // }

        // if stored successfully, remove contact again
        //deleteContact(testContact);

        // return currentMax;
    }

    //    private SharedPreferences settings;
//    public static final String PREFERENCE_TRANSLITERATE = "prefTransliterate";
//    public static final String PREFERENCE_ADD_TYPE_SUFFIX = "prefAddTypeSuffix";
//
//    public Contact convertToSimContact(Contact contact) {
//        String name = contact.getName();
//        boolean transliterate = this.settings.getBoolean(DumbphoneAssistantPreferenceActivity.PREFERENCE_TRANSLITERATE, false);
//        boolean addTypeSuffix = this.settings.getBoolean(DumbphoneAssistantPreferenceActivity.PREFERENCE_ADD_TYPE_SUFFIX, false);
//        if (transliterate) {
//            name = unidecode(name);
//        }
//        int adjustedContactNameLength = addTypeSuffix ? maxContactNameLength - 2 : maxContactNameLength;
//        name = adjustedContactNameLength > 0
//                ? name.substring(0, Math.min(name.length(), adjustedContactNameLength))
//                : name
//        ;
//        if (addTypeSuffix) {
//            String label = contact.getLabel();
//            if (transliterate) {
//                label = unidecode(label);
//            }
//            String firstLetter = label.substring(0, 1);
//            name = name + "," + firstLetter;
//        }
//        String number = contact.getNumber().replace("-", "");
//        return new Contact(null, name, number);
//
    private static final String SIM_DISPLAY_NAME = "name";
    private static final String SIM_PHONE_NUMBER = "number";
    public static final String CONTACT_TYPE_SIM = "Sim";

    private String getNumberStartIsdn(String number) {
        if (!TextUtils.isEmpty(number) && number.length() > 5) {
            String data = "";
            if (number.startsWith("84") || number.startsWith("+84")) {
                String start = number.substring(0, 4);
                String end = number.substring(4, number.length());
                data = start.replace("84", "").replace("+", "") + end;
            } else if (number.startsWith("0")) {
                data = number.substring(1, number.length()).trim();
            }
            return ReplaceUtils.replaceAll(data.trim());
        }
        return "";
    }

    public List<Contact> getAll() {
        Cursor simContacts = context.getContentResolver().query(simUri, null, null, null, null);
        List<Contact> simContactsForApp = new ArrayList<Contact>();
        // System.out.println("KIEUNN" + simContacts);
        if(simContacts==null) return simContactsForApp;
        while (simContacts.moveToNext()) {
            Contact contact = getContact(simContacts);
            if (contact != null) {
                simContactsForApp.add(contact);
            }
        }
        return simContactsForApp;
    }

    public HashMap<String, Contact> getAllContact() {
        Cursor simContacts = context.getContentResolver().query(simUri, null, null, null, null);
        while (simContacts.moveToNext()) {
            Contact contact = getContact(simContacts);
            if (contact != null) {
                String key = contact.getIndsOld().trim() + contact.getName().trim();
                synchronized (hashMapContact) {
                    if (!hashMapContact.containsKey(key)) {
                        hashMapContact.put(key, contact);
                    }
                }
            }
        }
        return hashMapContact;
    }

    public HashMap<String, Contact> getHashMapContact() {
        return hashMapContact;
    }

    public Contact getContact(Cursor simContactsCursor) {
        String name = simContactsCursor.getString(simContactsCursor.getColumnIndex(SIM_DISPLAY_NAME));
        String phoneNumbers = simContactsCursor.getString(simContactsCursor.getColumnIndex(SIM_PHONE_NUMBER));
        String fone = "";
        if (!TextUtils.isEmpty(phoneNumbers)) {
            if (phoneNumbers.startsWith("+")) {
                fone = "+" + phoneNumbers.substring(1, phoneNumbers.length()).replaceAll("[^0-9]", "");
            } else {
                fone = phoneNumbers.replaceAll("[^0-9]", "");
            }
            String phoneFix = ReplaceUtils.replaceAll(fone.trim());
            if (!TextUtils.isEmpty(getNumberStartIsdn(phoneFix)) && getNumberStartIsdn(phoneFix).length() == 10) {
                Contact contact = new Contact(name, phoneFix, phoneNumbers);
                contact.contactType = CONTACT_TYPE_SIM;
                return contact;
            }
        }
        return null;
    }


}