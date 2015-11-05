package com.example.sarthak_pc.ats;

import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.sarthak_pc.ats.dummy.DummyContent;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends ListFragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    private static String url = "http://fasnoida.duckdns.org/get_daywise_comp.php";

    private static final String TAG_CONTACTS = "contacts";

    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    private ProgressDialog pDialog = new ProgressDialog(getActivity());
    JSONParser jsonParser = new JSONParser();

    // TODO: Rename and change types of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        new GetProductDetails().execute(new String[0]);

        // TODO: Change Adapter to display your content
        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    class GetProductDetails extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            ItemFragment.this.pDialog.setMessage("Loading Competition details. Please wait...");
            ItemFragment.this.pDialog.setIndeterminate(false);
            ItemFragment.this.pDialog.setCancelable(false);
            Log.e("RunPos", "0.33");
            ItemFragment.this.pDialog.show();
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
                JSONObject json = ItemFragment.this.jsonParser.makeHttpRequest("http://fasnoida.duckdns.org/get_daywise_comp.php", "GET", e);
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
                    Log.wtf("null","It was null");
                }
            } catch (JSONException var6) {
                var6.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            Log.e("RunPos", "9");
            ItemFragment.this.pDialog.dismiss();
            ListAdapter adapter = new SimpleAdapter(getActivity(), contactList,
                    R.layout.list_item, new String[]{"Name", "Location",
                    "NoPR"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            setListAdapter(adapter);
        }
    }
}
