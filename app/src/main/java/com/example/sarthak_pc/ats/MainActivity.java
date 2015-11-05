package com.example.sarthak_pc.ats;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String url = "http://fasnoida.duckdns.org/get_all_products.php";

    // JSON Node names
    private static final String TAG_CONTACTS = "contacts";

    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList= new ArrayList<>();

    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new GetProductDetails().execute(new String[0]);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            new GetProductDetailsDay1().execute(new String[0]);
        } else if (id == R.id.nav_gallery) {
            new GetProductDetailsDay2().execute(new String[0]);
        } else if (id == R.id.nav_slideshow) {
            new GetProductDetailsDay3().execute(new String[0]);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class GetProductDetails extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Competition details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            Log.e("RunPos", "0.33");
            pDialog.show();
            Log.e("RunPos", "0.66");
        }

        protected String doInBackground(String... params) {
            try {
                ArrayList e = new ArrayList();
                Log.e("RunPos","1");
                Calendar c = Calendar.getInstance();
                Log.e("RunPos","2");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Log.e("RunPos","3");
                String formattedDate = df.format(c.getTime());
                Log.e("RunPos","4");

                e.add(new BasicNameValuePair("Date", formattedDate));
                Log.e("RunPos", "5");
                Log.e("RunPos", formattedDate);
                JSONObject json = MainActivity.this.jsonParser.makeHttpRequest("http://fasnoida.duckdns.org/get_daywise_comp.php", "GET", e);
                if(json==null){
                    Log.e("RunPos","100");
                }
                Log.e("RunPos","6");
                int success = json.getInt("success");
                Log.e("RunPos","6.33");
                if (success == 1) {
                    Log.e("RunPos","6.66");
                    JSONArray productObj = json.getJSONArray("contacts");
                    Log.e("RunPos","7");
                    if (productObj != null) {
                        Log.e("RunPos","7.33");
                        contacts = json.getJSONArray(TAG_CONTACTS);
                        for (int i = 0; i < contacts.length(); i++) {
                            Log.e("RunPos","7.66");
                            JSONObject product = productObj.getJSONObject(i);
                            Log.e("RunPos","8");

                            //TODO: Create A new PHP to retrieve all competitions on a a specific date and re wright this code to retrieve comp info

                            String Name = product.getString("Name");
                            String Location = product.getString("Location");
                            String NoPR = product.getString("NoPR");


                            HashMap<String, String> contact = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            contact.put("Name", Name);
                            contact.put("Location", Location);
                            contact.put("NoPR", NoPR);

                            Log.e("RunPos", contact.get("Name"));


                            // adding contact to contact list
                            contactList.add(contact);
                        }
                           /*JSONObject product = productObj.getJSONObject(0);
                            EditProductActivity.this.txtName = (EditText)EditProductActivity.this.findViewById(2131034112);
                            EditProductActivity.this.txtPrice = (EditText)EditProductActivity.this.findViewById(2131034113);
                            EditProductActivity.this.txtDesc = (EditText)EditProductActivity.this.findViewById(2131034114);
                            EditProductActivity.this.txtName.setText(product.getString("name"));
                            EditProductActivity.this.txtPrice.setText(product.getString("price"));
                            EditProductActivity.this.txtDesc.setText(product.getString("description"));*/
                    }
                }else{
                    Log.wtf("null", "It was null");
                }
            } catch (JSONException var6) {
                var6.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            Log.e("RunPos", "9");
            pDialog.dismiss();

            RelativeLayout abcd = (RelativeLayout) findViewById(R.id.rel_lay);
            abcd.removeAllViews();

            for(int i=0;i<contactList.size();i++){
            // create an empty array;
                    // create a new textview
                final TextView rowTextView1 = new TextView(MainActivity.this);
                final TextView rowTextView2 = new TextView(MainActivity.this);
                final TextView rowTextView3 = new TextView(MainActivity.this);



                    // set some properties of rowTextView or something
                rowTextView1.setText(contactList.get(i).get("Name"));
                rowTextView2.setText(contactList.get(i).get("Location"));
                rowTextView3.setText(contactList.get(i).get("NoPR"));

                    // add the textview to the linearlayout
                abcd.addView(rowTextView1);

            }
            /*ListAdapter adapter = new SimpleAdapter(getActivity(), contactList,
                    R.layout.list_item, new String[]{"Name", "Location",
                    "NoPR"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            setListAdapter(adapter);*/
        }
    }
    class GetProductDetailsDay1 extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Competition details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            Log.e("RunPos", "0.33");
            pDialog.show();
            Log.e("RunPos", "0.66");
        }

        protected String doInBackground(String... params) {
            try {
                ArrayList e = new ArrayList();

                e.clear();
                e.add(new BasicNameValuePair("Date", "1"));
                JSONObject json = MainActivity.this.jsonParser.makeHttpRequest("http://fasnoida.duckdns.org/get_day_comp.php", "GET", e);
                if(json==null){
                    Log.e("RunPos","100");
                }
                Log.e("RunPos","6");
                int success = json.getInt("success");
                Log.e("RunPos","6.33");
                if (success == 1) {
                    Log.e("RunPos","6.66");
                    JSONArray productObj = json.getJSONArray("contacts");
                    Log.e("RunPos","7");
                    if (productObj != null) {
                        Log.e("RunPos","7.33");
                        contacts = json.getJSONArray(TAG_CONTACTS);
                        for (int i = 0; i < contacts.length(); i++) {
                            Log.e("RunPos","7.66");
                            JSONObject product = productObj.getJSONObject(i);
                            Log.e("RunPos","8");

                            //TODO: Create A new PHP to retrieve all competitions on a a specific date and re wright this code to retrieve comp info

                            String Name = product.getString("Name");
                            String Location = product.getString("Location");
                            String NoPR = product.getString("NoPR");


                            HashMap<String, String> contact = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            contact.put("Name", Name);
                            contact.put("Location", Location);
                            contact.put("NoPR", NoPR);

                            Log.e("RunPos", contact.get("Name"));


                            // adding contact to contact list
                            contactList.add(contact);
                        }
                           /*JSONObject product = productObj.getJSONObject(0);
                            EditProductActivity.this.txtName = (EditText)EditProductActivity.this.findViewById(2131034112);
                            EditProductActivity.this.txtPrice = (EditText)EditProductActivity.this.findViewById(2131034113);
                            EditProductActivity.this.txtDesc = (EditText)EditProductActivity.this.findViewById(2131034114);
                            EditProductActivity.this.txtName.setText(product.getString("name"));
                            EditProductActivity.this.txtPrice.setText(product.getString("price"));
                            EditProductActivity.this.txtDesc.setText(product.getString("description"));*/
                    }
                }else{
                    Log.wtf("null", "It was null");
                }
            } catch (JSONException var6) {
                var6.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            Log.e("RunPos", "9");
            pDialog.dismiss();

            RelativeLayout abcd = (RelativeLayout) findViewById(R.id.rel_lay);
            abcd.removeAllViews();

            for(int i=0;i<contactList.size();i++){
                // create a new textview
                final TextView rowTextView1 = new TextView(MainActivity.this);
                final TextView rowTextView2 = new TextView(MainActivity.this);
                final TextView rowTextView3 = new TextView(MainActivity.this);



                // set some properties of rowTextView or something
                rowTextView1.setText(contactList.get(i).get("Name"));
                rowTextView2.setText(contactList.get(i).get("Location"));
                rowTextView3.setText(contactList.get(i).get("NoPR"));

                // add the textview to the linearlayout
                abcd.addView(rowTextView1);

            }
            /*ListAdapter adapter = new SimpleAdapter(getActivity(), contactList,
                    R.layout.list_item, new String[]{"Name", "Location",
                    "NoPR"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            setListAdapter(adapter);*/
        }
    }
    class GetProductDetailsDay2 extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Competition details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            Log.e("RunPos", "0.33");
            pDialog.show();
            Log.e("RunPos", "0.66");
        }

        protected String doInBackground(String... params) {
            try {
                ArrayList e = new ArrayList();

                Log.e("RunPos", "2");
                e.clear();
                e.add(new BasicNameValuePair("Date", "2"));
                JSONObject json = MainActivity.this.jsonParser.makeHttpRequest("http://fasnoida.duckdns.org/get_daywise_comp.php", "GET", e);
                if(json==null){
                    Log.e("RunPos","100");
                }
                Log.e("RunPos","6");
                int success = json.getInt("success");
                Log.e("RunPos","6.33");
                if (success == 1) {
                    Log.e("RunPos","6.66");
                    JSONArray productObj = json.getJSONArray("contacts");
                    Log.e("RunPos","7");
                    if (productObj != null) {
                        Log.e("RunPos","7.33");
                        contacts = json.getJSONArray(TAG_CONTACTS);
                        for (int i = 0; i < contacts.length(); i++) {
                            Log.e("RunPos","7.66");
                            JSONObject product = productObj.getJSONObject(i);
                            Log.e("RunPos","8");

                            //TODO: Create A new PHP to retrieve all competitions on a a specific date and re wright this code to retrieve comp info

                            String Name = product.getString("Name");
                            String Location = product.getString("Location");
                            String NoPR = product.getString("NoPR");


                            HashMap<String, String> contact = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            contact.put("Name", Name);
                            contact.put("Location", Location);
                            contact.put("NoPR", NoPR);

                            Log.e("RunPos", contact.get("Name"));


                            // adding contact to contact list
                            contactList.add(contact);
                        }
                           /*JSONObject product = productObj.getJSONObject(0);
                            EditProductActivity.this.txtName = (EditText)EditProductActivity.this.findViewById(2131034112);
                            EditProductActivity.this.txtPrice = (EditText)EditProductActivity.this.findViewById(2131034113);
                            EditProductActivity.this.txtDesc = (EditText)EditProductActivity.this.findViewById(2131034114);
                            EditProductActivity.this.txtName.setText(product.getString("name"));
                            EditProductActivity.this.txtPrice.setText(product.getString("price"));
                            EditProductActivity.this.txtDesc.setText(product.getString("description"));*/
                    }
                }else{
                    Log.wtf("null", "It was null");
                }
            } catch (JSONException var6) {
                var6.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            Log.e("RunPos", "9");
            pDialog.dismiss();

            RelativeLayout abcd = (RelativeLayout) findViewById(R.id.rel_lay);
            abcd.removeAllViews();

            for(int i=0;i<contactList.size();i++){

                // create a new textview
                final TextView rowTextView1 = new TextView(MainActivity.this);
                final TextView rowTextView2 = new TextView(MainActivity.this);
                final TextView rowTextView3 = new TextView(MainActivity.this);



                // set some properties of rowTextView or something
                rowTextView1.setText(contactList.get(i).get("Name"));
                rowTextView2.setText(contactList.get(i).get("Location"));
                rowTextView3.setText(contactList.get(i).get("NoPR"));

                // add the textview to the linearlayout
                abcd.addView(rowTextView1);

            }
            /*ListAdapter adapter = new SimpleAdapter(getActivity(), contactList,
                    R.layout.list_item, new String[]{"Name", "Location",
                    "NoPR"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            setListAdapter(adapter);*/
        }
    }
    class GetProductDetailsDay3 extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Competition details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            Log.e("RunPos", "0.33");
            pDialog.show();
            Log.e("RunPos", "0.66");
        }

        protected String doInBackground(String... params) {
            try {
                ArrayList e = new ArrayList();
                e.clear();
                e.add(new BasicNameValuePair("Date", "3"));
                JSONObject json = MainActivity.this.jsonParser.makeHttpRequest("http://fasnoida.duckdns.org/get_daywise_comp.php", "GET", e);
                if(json==null){
                    Log.e("RunPos","100");
                }
                Log.e("RunPos","6");
                int success = json.getInt("success");
                Log.e("RunPos","6.33");
                if (success == 1) {
                    Log.e("RunPos","6.66");
                    JSONArray productObj = json.getJSONArray("contacts");
                    Log.e("RunPos","7");
                    if (productObj != null) {
                        Log.e("RunPos","7.33");
                        contacts = json.getJSONArray(TAG_CONTACTS);
                        for (int i = 0; i < contacts.length(); i++) {
                            Log.e("RunPos","7.66");
                            JSONObject product = productObj.getJSONObject(i);
                            Log.e("RunPos","8");

                            //TODO: Create A new PHP to retrieve all competitions on a a specific date and re wright this code to retrieve comp info

                            String Name = product.getString("Name");
                            String Location = product.getString("Location");
                            String NoPR = product.getString("NoPR");


                            HashMap<String, String> contact = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            contact.put("Name", Name);
                            contact.put("Location", Location);
                            contact.put("NoPR", NoPR);

                            Log.e("RunPos", contact.get("Name"));


                            // adding contact to contact list
                            contactList.add(contact);
                        }
                           /*JSONObject product = productObj.getJSONObject(0);
                            EditProductActivity.this.txtName = (EditText)EditProductActivity.this.findViewById(2131034112);
                            EditProductActivity.this.txtPrice = (EditText)EditProductActivity.this.findViewById(2131034113);
                            EditProductActivity.this.txtDesc = (EditText)EditProductActivity.this.findViewById(2131034114);
                            EditProductActivity.this.txtName.setText(product.getString("name"));
                            EditProductActivity.this.txtPrice.setText(product.getString("price"));
                            EditProductActivity.this.txtDesc.setText(product.getString("description"));*/
                    }
                }else{
                    Log.wtf("null", "It was null");
                }
            } catch (JSONException var6) {
                var6.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            Log.e("RunPos", "9");
            pDialog.dismiss();

            RelativeLayout abcd = (RelativeLayout) findViewById(R.id.rel_lay);
            abcd.removeAllViews();

            for(int i=0;i<contactList.size();i++){
                // create a new textview
                final TextView rowTextView1 = new TextView(MainActivity.this);
                final TextView rowTextView2 = new TextView(MainActivity.this);
                final TextView rowTextView3 = new TextView(MainActivity.this);



                // set some properties of rowTextView or something
                rowTextView1.setText(contactList.get(i).get("Name"));
                rowTextView2.setText(contactList.get(i).get("Location"));
                rowTextView3.setText(contactList.get(i).get("NoPR"));

                // add the textview to the linearlayout
                abcd.addView(rowTextView1);

            }
            /*ListAdapter adapter = new SimpleAdapter(getActivity(), contactList,
                    R.layout.list_item, new String[]{"Name", "Location",
                    "NoPR"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            setListAdapter(adapter);*/
        }
    }

}




