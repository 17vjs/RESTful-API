package com.example.restfulapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
EditText et_username,et_password;
String username,password;
RequestQueue requestQueue;
String url;
TextView errormsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    et_username=findViewById(R.id.username);
    et_password=findViewById(R.id.password);
    errormsg=findViewById(R.id.errorbox);
         requestQueue= Volley.newRequestQueue(this);
        url="http://localhost:5000/Androidlogin/";
    }
    public void login(View v) throws JSONException {

        username=et_username.getText().toString();
        password=et_password.getText().toString();
       JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
               Request.Method.POST,
               url,
               new JSONObject("{\"uname\":\""+username+"\",\"psw\":\""+password+"\"}"),
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       try {
                           if ("user" == response.getString("role")){
                               startActivity(new Intent(MainActivity.this, UserActivity.class));
                           }
                           else   if ("admin" == response.getString("role")){
                               startActivity(new Intent(MainActivity.this, AdminActivity.class));
                           }
                           else   if ("seller" == response.getString("role")){
                               startActivity(new Intent(MainActivity.this, SellerActivity.class));
                           }
                           else
                           {
                              errormsg.setText("Login failed");
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                       Log.e("Response------------>", response.toString());
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
}
