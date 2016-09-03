package com.hiralio.www;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.toolbox.StringRequest;

public class Register extends AppCompatActivity{


    EditText password_edittext,email_edittext, name_edittext,phone_edittext,con_password_edittext, address_edittext;

    Button register_button;

    ProgressDialog mProgressDialog;

    // Session  Manager Class
    SessionManager session;

    LinearLayout gotologin_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        session = new SessionManager(getApplicationContext());


        password_edittext = (EditText) findViewById(R.id.password_edittext);

        email_edittext = (EditText) findViewById(R.id.email_edittext);

        name_edittext = (EditText) findViewById(R.id.name_edittext);

        phone_edittext = (EditText) findViewById(R.id.phone_edittext);


        con_password_edittext = (EditText) findViewById(R.id.con_password_edittext);

        gotologin_layout = (LinearLayout) findViewById(R.id.gotologin_layout);

        gotologin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        register_button = (Button) findViewById(R.id.register_button);





    }




    private void sendData(final String nameStr, final String phoneStr, final String emailStr, final String passStr, final String cityStr, final String addressStr){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://dana.taksibentor.com/json/customer/saveregister.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressDialog.dismiss();

                //  Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();

                String id = null,email = null,pass = null, nama = null;

                try{

                    JSONObject object = new JSONObject(response);
                    JSONArray jobArray = object.getJSONArray("profile");
                    for (int i = 0; i < jobArray.length(); i++) {

                        JSONObject itemJobObj = jobArray.getJSONObject(i);
                        id = itemJobObj.getString("id");
                        email = itemJobObj.getString("email");
                        pass = itemJobObj.getString("pass");
                        nama = itemJobObj.getString("nama");

                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                //   Toast.makeText(Register.this,"send = id "+id+" - email "+email+" - pass "+pass+" - nama"+nama, Toast.LENGTH_SHORT).show();

                session.createLoginSession(id,email);

                session.creatFullName(nama);

                Intent intent = new Intent(getApplicationContext(), Invitation.class);
                startActivity(intent);
                finish();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("name",nameStr);
                params.put("phone",phoneStr);
                params.put("email",emailStr);
                params.put("pass",passStr);
                params.put("city",cityStr);
                params.put("address",addressStr);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



}
