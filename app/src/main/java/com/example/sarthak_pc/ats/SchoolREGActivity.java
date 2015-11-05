package com.example.sarthak_pc.ats;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SchoolREGActivity extends AppCompatActivity {

    String a1="";
    String a2="";
     String a3="";

    int e;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("RunPro","REG1");
        setContentView(R.layout.activity_school_reg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btn2 = (Button) findViewById(R.id.button3);
        final EditText t1= (EditText) findViewById(R.id.editText2);
        final EditText t2= (EditText) findViewById(R.id.editText3);
        final EditText t3= (EditText) findViewById(R.id.editText4);
        final TextView t4= (TextView) findViewById(R.id.textView6);
        final TextView t5= (TextView) findViewById(R.id.textView7);
        final TextView t6= (TextView) findViewById(R.id.textView8);

        a1=t1.getText().toString();
        a2=t2.getText().toString();
        a3=t3.getText().toString();

        Log.e("RunPro","REG2");

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("RunPro","NREG1");
                if((a1!=null)&&(a2!=null)&&(a3!=null)) {
                    new CreateNewProduct().execute(new String[0]);
                    Snackbar.make(v, "Call at 1234567890 to get school code\n You will be redirected to login", Snackbar.LENGTH_LONG).setDuration(10000)
                            .setAction("Action", null).show();
                }
                if(a1.equals("")){
                    t4.setText("Please your School Name");
                }
                if(a2.equals("")){
                    t5.setText("You Need a Teacher Incharge");
                }
                if(a3.equals("")){
                    t6.setText("Needed for emergencies");
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Call at 1234567890 to get school code", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    class CreateNewProduct extends AsyncTask<String, String, String> {
        CreateNewProduct() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("RunPro", "pre");
            SchoolREGActivity.this.pDialog = new ProgressDialog(SchoolREGActivity.this);
            SchoolREGActivity.this.pDialog.setMessage("Registering School..");
            SchoolREGActivity.this.pDialog.setIndeterminate(false);
            SchoolREGActivity.this.pDialog.setCancelable(true);
            SchoolREGActivity.this.pDialog.show();
        }

        protected String doInBackground(String... args) {
            Log.e("RunPro","bac");
            String name = a1;
            String price = a2;
            String description = a3;
            ArrayList params = new ArrayList();
            Log.e("RunPos",a1+"\n"+a2+"\n"+a3+"\n");
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("price", price));
            params.add(new BasicNameValuePair("description", description));
            JSONObject json = SchoolREGActivity.this.jsonParser.makeHttpRequest("http://fasnoida.duckdns.org/create_product.php", "GET", params);
            Log.d("Create Response", json.toString());

            try {
                e = json.getInt("success");
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            SchoolREGActivity.this.pDialog.dismiss();
            if(e == 1) {
                final Handler handler = new Handler();

                //handler.postDelayed(new Runnable() {
                   // @Override
                    //public void run() {
                        // Do something after 5s = 5000ms
                        startActivity(new Intent(SchoolREGActivity.this, Main2Activity.class));
                    //}
               // }, 9000);
            }
        }
    }

}
