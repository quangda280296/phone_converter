package com.chuyendoidauso.chuyendoidanhba.commons;

import com.chuyendoidauso.chuyendoidanhba.models.Contact;

import java.util.ArrayList;

public class AppConstant {

    public static final String FACE_BOOK = "_FACE_BOOK";
    public static final String FACE_BOOK_NETWORK = "facebook";

    public interface ACTION {
        String ACTION_CLOSE_NOTICE = "com.chuyendoidauso.chuyendoidanhba.action.closenotice";
        String ACTION_OPEN_NOTICE = "com.chuyendoidauso.chuyendoidanhba.action.open.activity";
    }

    public static ArrayList<Contact> originContactList = new ArrayList();
    public static int COUNT_NUMBER = 0;
    public static boolean isLoadData = false;
    public static final long TIME_DELAY_SPLASH = 600L;
    public static final long TIME_DELAY_TASK = 800L;


    public static final String BD_CONTACT_LIST = "contactList";

    /**
     * old
     */
    public static final String URL_LINK_SERVER = "http://gamemobileglobal.com/api/control_s.php";
    public static final int INDEX_NUMBER_CONVERT = 100;

    public static final String CODE_APP = "41043";

    public static final String FILE_CONTROL = "control.json";
    public static final String FILE_DS = "list_dau_so.json";

    public static final String SP_POST_NUMBER_PHONE = "list_dau_so.json";
}