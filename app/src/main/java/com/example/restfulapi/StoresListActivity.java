package com.example.restfulapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class StoresListActivity extends AppCompatActivity{
    ListView listView;
    RequestQueue requestQueue;
    String url="https://localhost:5000/stores";
    double[] coordinates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_list);
        setUIViews();
        coordinates=getIntent().getDoubleArrayExtra("location");
    }
    void setUIViews()
    {
        listView=findViewById(R.id.stores_list);
        requestQueue = Volley.newRequestQueue(this);
    }
    public void getStoresList(View v){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<String> nearby_stores =   new ArrayList<>();
                            JSONArray jsonArray=response.getJSONArray("stores");
                            for(int i = 0; jsonArray.length() > i; i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                if (distance(coordinates[0],coordinates[1],jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"))<10.0){
                                    nearby_stores.add( jsonObject.getString("store_name"));
                                }}
                            String[] store_list=nearby_stores.toArray(new String[0]);
                            ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,store_list);
                            listView.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response------------>",error.toString());

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
    private static double distance(double lat1, double long1, double lat2, double long2) {
        if ((lat1 == lat2) && (long1 == long2)) {
            return 0;
        }
        else {
            double theta = long1 - long2;
            double DISTANCE = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            DISTANCE = Math.acos(DISTANCE);
            DISTANCE = Math.toDegrees(DISTANCE);
            DISTANCE = DISTANCE * 60 * 1.1515;
            DISTANCE = DISTANCE * 1.609344;
            return (DISTANCE);
        }
    }
}
