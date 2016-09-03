package com.hiralio.www;

/**
 * Created by Kilat on 5/10/2016.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecentFragment extends Fragment{

    GridView report_listview;


    ArrayList<HashMap<String, String>> arraylist;

    MainAdapter listViewAdapter;

    JSONArray jsonArray;


    public RecentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent, container, false);


        arraylist = new ArrayList<HashMap<String, String>>();

        report_listview = (GridView) view.findViewById(R.id.recent_gridview);

        getReport();


        return view;
    }

    private void getReport(){
        StringRequest stringRequest = new StringRequest("http://hiralio.inetrip.com/hr_photo_recent.php?roomId=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              //  Toast.makeText(getActivity(), "response: "+response, Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    jsonArray = jsonObject.getJSONArray("hr_photo");

                    getReportArray(jsonArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getReportArray(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject itemJobObj = null;
            try {
                itemJobObj = jsonArray.getJSONObject(i);


                String url = itemJobObj.getString("url");




                HashMap<String, String> map = new HashMap<String, String>();

                map.put("url", url);

                arraylist.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        listViewAdapter = new MainAdapter(getActivity(),arraylist);

        report_listview.setAdapter(listViewAdapter);

        report_listview.setOnScrollListener(new SampleScrollListener(getActivity()));


    }

}