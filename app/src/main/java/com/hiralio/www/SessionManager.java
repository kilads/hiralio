package com.hiralio.www;

/**
 * Created by Kilat on 5/13/2016.
 */
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "BursadanaCoIdPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String IS_CITY = "IsCityIn";

    private static final String IS_PICKER = "IsPickerIn";

    private static final String IS_INVITE = "IsInviteIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "namePref";

    public static final String KEY_CITY_NAME = "cityNamePref";

    public static final String KEY_PICKER = "picker";

    public static final String KEY_FULLNAME = "fullname";

    public static final String KEY_INVITE = "invite";

    public static final String KEY_CITY_ID = "cityIdPref";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "emailPref";

    public static final String KEY_ID = "idPref";

    public static final String KEY_KREDIT1 = "kredit1Pref";

    public static final String KEY_KREDIT2 = "kredit2Pref";

    public static final String KEY_KREDIT3 = "kredit3Pref";

    public static final String KEY_USER = "userPref";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);


        // commit changes
        editor.commit();
    }


    public void createCitySession(String cityId, String cityName){
        // Storing login value as TRUE
        editor.putBoolean(IS_CITY, true);

        // Storing name in pref
        editor.putString(KEY_CITY_ID, cityId);

        // Storing email in pref
        editor.putString(KEY_CITY_NAME, cityName);


        // commit changes
        editor.commit();
    }

    public void creatPicker(String pickerStr){

        editor.putBoolean(IS_PICKER, true);
        // Storing name in pref
        editor.putString(KEY_PICKER, pickerStr);

        editor.commit();

    }

    public void creatFullName(String fmStr){

        // Storing name in pref
        editor.putString(KEY_FULLNAME, fmStr);

        editor.commit();

    }


    public void creatInvite(String inviteStr){

        editor.putBoolean(IS_INVITE, true);
        // Storing name in pref
        editor.putString(KEY_INVITE, inviteStr);

        editor.commit();

    }

    public void creatKredit2(String kredit2Str){
        // Storing name in pref
        editor.putString(KEY_KREDIT2, kredit2Str);

        editor.commit();

    }

    public void creatKredit3(String kredit3Str){
        // Storing name in pref
        editor.putString(KEY_KREDIT3, kredit3Str);

        editor.commit();

    }


    public HashMap<String, String> getPicker(){
        HashMap<String, String> picker = new HashMap<String, String>();
        // user name
        picker.put(KEY_PICKER, pref.getString(KEY_PICKER, null));

        // return user
        return picker;
    }


    public HashMap<String, String> getFm(){
        HashMap<String, String> fm = new HashMap<String, String>();
        // user name
        fm.put(KEY_FULLNAME, pref.getString(KEY_FULLNAME, null));

        // return user
        return fm;
    }

    public HashMap<String, String> getKredit2(){
        HashMap<String, String> kredit2 = new HashMap<String, String>();
        // user name
        kredit2.put(KEY_KREDIT2, pref.getString(KEY_KREDIT2, null));

        // return user
        return kredit2;
    }

    public HashMap<String, String> getKredit3(){
        HashMap<String, String> kredit3 = new HashMap<String, String>();
        // user name
        kredit3.put(KEY_KREDIT3, pref.getString(KEY_KREDIT3, null));

        // return user
        return kredit3;
    }






    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Register.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }




    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Register.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isPickerIn(){
        return pref.getBoolean(IS_PICKER, false);
    }
    public boolean isInviteIn(){
        return pref.getBoolean(IS_INVITE, false);
    }
}