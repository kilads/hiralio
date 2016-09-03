package com.hiralio.www;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.toolbox.StringRequest;

public class Login extends AppCompatActivity {


    EditText password_edittext,email_edittext;

    Button login_button;

    ProgressDialog mProgressDialog;

    // Session  Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initializing Session Manager Class
        session = new SessionManager(getApplicationContext());


        password_edittext = (EditText) findViewById(R.id.password_edittext);

        email_edittext = (EditText) findViewById(R.id.email_edittext);

        login_button = (Button) findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email_edittext.getText().toString().trim().length() > 0 || password_edittext.getText().toString().trim().length() > 0){

                    if (email_edittext.getText().toString().trim().length() > 0){

                        if (password_edittext.getText().toString().trim().length() > 0){

                            getUserData(email_edittext.getText().toString(), password_edittext.getText().toString());


                            mProgressDialog = new ProgressDialog(Login.this);
                            // Set progressdialog title
                            mProgressDialog.setTitle("Loading...");
                            // Set progressdialog message
                            mProgressDialog.setMessage("Please wait..");
                            mProgressDialog.setIndeterminate(false);
                            mProgressDialog.setCancelable(false);
                            // Show progressdialog
                            mProgressDialog.show();

                        }else {
                            Toast.makeText(Login.this, "Isi Password", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(Login.this, "Isi Email", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(Login.this, "Isi Email dan Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        LinearLayout gotoregister_layout = (LinearLayout) findViewById(R.id.gotoregister_layout);

        gotoregister_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });


    }


    private void getUserData(final String emailStr, final String passwordStr){

        String url = "http://dana.taksibentor.com/json/customer/getuser.php?email=_ID_".replace("_ID_",emailStr);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressDialog.dismiss();

                Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();

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

                // Toast.makeText(Login.this,"id "+id+" - email "+email+" - pass "+pass+" - nama"+nama, Toast.LENGTH_SHORT).show();

                if (emailStr.equals(email)){
                    if (passwordStr.equals(pass)){


                        session.createLoginSession(id,email);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(Login.this,"Password salah",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(Login.this,"Email tidak terdaftar",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgressDialog.dismiss();

                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
