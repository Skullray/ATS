package com.example.sarthak_pc.ats;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity {

    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String url = "http://fasnoida.duckdns.org/get_all_products.php";

    // JSON Node names
    private static final String TAG_CONTACTS = "contacts";

    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactList = new ArrayList<HashMap<String, String>>();

        SharedPreferences settings = getApplicationContext().getSharedPreferences("SchoolInfo", 0);
        SharedPreferences.Editor editor = settings.edit();

        final String homeScore = settings.getString("SchoolCode", "0");
        if(homeScore.equals("0")){

            Button btn = (Button)findViewById(R.id.button);
            Button btn2 = (Button) findViewById(R.id.button2);
            final TextView EnterSC = (TextView) findViewById(R.id.editText);

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Main2Activity.this, SchoolREGActivity.class));
                }
            });

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences settings = getApplicationContext().getSharedPreferences("SchoolInfo", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    Log.e("Var", homeScore);
                    editor.putString("SchoolCode", EnterSC.getText().toString());
                    editor.apply();
                    new GetContacts().execute();
                }
            });
        }else{
            new GetContacts().execute();
        }

// Apply the edits!
// Get from the SharedPreferences
        //SharedPreferences settings = getApplicationContext().getSharedPreferences("SchoolCode", 0);
        //int homeScore = settings.getInt("homeScore", 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        int Test;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Main2Activity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray(TAG_CONTACTS);

                    SharedPreferences settings = getApplicationContext().getSharedPreferences("SchoolInfo", 0);
                    SharedPreferences.Editor editor = settings.edit();

                    String homeScore = settings.getString("SchoolCode", "0");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String name = c.getString("SchoolCode");
                        if(name.equals(homeScore)) {
                            editor.putInt("Test", 1);
                            editor.apply();
                        }else{
                            Log.e("Login", "Bad School Code");
                            editor.putInt("Test", 0);
                            editor.apply();
                            editor.putString("SchoolCode", "0");
                            editor.apply();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Test =100;
                SharedPreferences settings = getApplicationContext().getSharedPreferences("SchoolInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("SchoolCode", "0");
                editor.apply();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            SharedPreferences settings = getApplicationContext().getSharedPreferences("SchoolInfo", 0);
            SharedPreferences.Editor editor = settings.edit();
            if(Test==100){
                TextView er = (TextView) findViewById(R.id.textView2);
                er.setText("Couldn't get any data from the Server");
            }

            Test = settings.getInt("Test", 0);
            if(Test==1) {
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
                finish();
            }else{
                TextView er = (TextView) findViewById(R.id.textView2);
                er.setText(R.string.a);
            }
            /**
             * Updating parsed JSON data into ListView
             * */
        }

    }

}




