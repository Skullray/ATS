package com.example.sarthak_pc.ats;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class sreg extends AppCompatActivity {

    final TextView t1= (TextView) findViewById(R.id.textView3);
    final TextView t2= (TextView) findViewById(R.id.textView4);
    final TextView t3= (TextView) findViewById(R.id.textView5);

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sreg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btn2 = (Button) findViewById(R.id.button3);
        final TextView t4= (TextView) findViewById(R.id.textView6);
        final TextView t5= (TextView) findViewById(R.id.textView7);
        final TextView t6= (TextView) findViewById(R.id.textView8);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("RunPro", "NREG1");
                if ((t1.getText() != null) && (t2.getText() != null) && (t3.getText() != null)) {
                    Snackbar.make(v, "Call at 1234567890 to get school code\n You will be redirected to login", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    new CreateNewProduct().execute(new String[0]);
                }
                if (t1.getText() == null) {
                    t4.setText("Please your School Name");
                }
                if (t2.getText() == null) {
                    t5.setText("You Need a Teacher Incharge");
                }
                if (t3.getText() == null) {
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

        String name;
        String price;
        String description;

        CreateNewProduct() {

        }

        protected void onPreExecute() {
            super.onPreExecute();
            sreg.this.pDialog = new ProgressDialog(sreg.this);
            sreg.this.pDialog.setMessage("Creating Product..");
            sreg.this.pDialog.setIndeterminate(false);
            sreg.this.pDialog.setCancelable(true);
            name = sreg.this.t1.getText().toString();
            price = sreg.this.t2.getText().toString();
            description = sreg.this.t3.getText().toString();
            sreg.this.pDialog.show();
        }

        protected String doInBackground(String... args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("price", price));
            params.add(new BasicNameValuePair("description", description));
            JSONObject json = sreg.this.jsonParser.makeHttpRequest("http://fasnoida.duckdns.org/create_product.php", "GET", params);
            Log.d("Create Response", json.toString());

            try {
                int e = json.getInt("success");
                if(e == 1) {
                    final Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            startActivity(new Intent(sreg.this, Main2Activity.class));
                        }
                    }, 5000);
                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            sreg.this.pDialog.dismiss();
        }
    }

}
