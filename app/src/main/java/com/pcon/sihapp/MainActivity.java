 package com.pcon.sihapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

 public class MainActivity extends AppCompatActivity {

     private Context mContext;
     private Activity mActivity;

     private Button mButtonDo;
     private EditText randdText;
     private EditText adminText;
     private EditText marketText;
     private TextView predictValue;
     private RadioGroup radiog;
     private RadioButton radb;
     private String mUrlString = "http://192.168.43.3:5000/predict";
     ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        // Get the widget reference from XML layout
        mButtonDo =  findViewById(R.id.submit_login);
        randdText = findViewById(R.id.randd);
        adminText = findViewById(R.id.admin);
        marketText = findViewById(R.id.market);
        radiog=findViewById(R.id.radioGroup);
        predictValue=findViewById(R.id.predict);

        // Set a click listener for button widget
        mButtonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Empty the TextView

                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.show();

                String cal="0.0";
                String florida="0.0";
                String newyork="0.0";
                int selectId=radiog.getCheckedRadioButtonId();
                radb=findViewById(selectId);
                if(selectId==-1)
                    Toast.makeText(MainActivity.this,"no button selected",Toast.LENGTH_SHORT).show();
                else
                {
                    if((radb.getText().toString()).equalsIgnoreCase("california"))
                        cal="1.0";
                    else if((radb.getText().toString()).equalsIgnoreCase("florida"))
                        florida="1.0";
                    else
                        newyork="1.0";

                }


                final String randd=randdText.getText().toString();
                final String admin=adminText.getText().toString();
                final String market=marketText.getText().toString();
                Map<String,String> params = new HashMap<String,String>();
                params.put("california", cal);
                params.put("florida",florida);
                params.put("newyork",newyork);
                params.put("randd",randd);
                params.put("admin",admin);
                params.put("market",market);

                // Initialize a new StringRequest
                JsonObjectRequest jsonRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        mUrlString,
                        new JSONObject(params),
                        new  Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response){
                                // Do something with response string
                                pDialog.dismiss();
                                try {
                                    predictValue.setText(response.getString("predict"));
                                    Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                                }
                                catch (Exception exp)
                                {
                                    Toast.makeText(MainActivity.this,exp.toString(),Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismiss();
                                // Do something when get error
                                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }

                )
                {
/*
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("california", cal);
                        params.put("florida",florida);
                        params.put("newyork",newyork);
                        params.put("randd",randd);
                        params.put("admin",admin);
                        params.put("market",market);
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/x-www-form-urlencoded");
                        return params;
                    }

 */
                };

                // Add StringRequest to the RequestQueue
                VolleySingleton.getInstance(mContext).addToRequestQueue(jsonRequest);
            }
        });

    }
}
