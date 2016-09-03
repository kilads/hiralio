package com.hiralio.www;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Invitation extends AppCompatActivity{

    private ArrayList<Map<String, String>> contacts;

    private ListView contactsListView;


    int invitedCount = 0;

    // Session Manager Class
    SessionManager session;

    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    String fullname;

    TextView invited_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        cd = new ConnectionDetector(getApplicationContext());

        isInternetPresent = cd.isConnectingToInternet();


        // Session class instance
        session = new SessionManager(getApplicationContext());


        // get user data from session
        HashMap<String, String> fm = session.getFm();

        // name
        fullname = fm.get(SessionManager.KEY_FULLNAME);


        contactsListView = (ListView) findViewById(R.id.invitation_listview);

        invited_textview = (TextView) findViewById(R.id.invited_textview);

        // Create a progress bar to display while the list loads
        final ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        progressBar.setIndeterminate(true);
        contactsListView.setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);

        String[] from = { "name" };
        int[] to = { R.id.full_name };

        contacts = fetchWhatsAppContacts();

        SimpleAdapter adapter = new SimpleAdapter(this, contacts, R.layout.item_invite, from, to);
        contactsListView.setAdapter(adapter);

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String numberStr = contacts.get(arg2).get("number").toString();



                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(numberStr, null, fullname + " mengundang anda untuk mengunakan Aplikasi Bursadana, Klik https://play.google.com/store/apps/details?id=id.co.bursadana.bursadana", null, null);
                    Toast.makeText(getApplicationContext(), "Invite Sent!",
                            Toast.LENGTH_LONG).show();
                    invitedCount = invitedCount + 1;




                } catch (Exception e) {

                    e.printStackTrace();
                }

                invited_textview.setText(Integer.toString(invitedCount)+" orang telah anda undang");


                if (invitedCount==7){
                    session.creatInvite("1");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }



            }
        });


    }


    private HashMap<String, String> putData(String name, String number) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("name", name);
        item.put("number", number);
        return item;
    }

    private ArrayList<Map<String, String>> fetchWhatsAppContacts(){

        ArrayList<Map<String, String>> list = new ArrayList<Map<String,String>>();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID,  ContactsContract.Contacts.HAS_PHONE_NUMBER}, null, null, null);

        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                int hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                String displayName = "";
                String displayNumber = "";
                if (hasPhone>0){

                    Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

                    while (phoneCursor.moveToNext()) {

                        displayNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        displayName = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    }
                    phoneCursor.close();

                    list.add(putData(displayName, displayNumber));
                }
            }
        }

        return list;
    }

}
