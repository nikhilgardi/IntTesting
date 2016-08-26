package com.example.nikhilg.inttesting;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    public ListView listView1;
    String serviceProviderName,str_ServiceProviderName,str_ServiceProviderID;
    Map<String,Integer> mapPostpaid;
    List<String> branchList1,serviceProviderImagelist;
    Spinner spinnerSelectOperator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView1=(ListView)findViewById(R.id.listView1);
        spinnerSelectOperator=(Spinner)findViewById(R.id.spinnerSelectOperator);

        List<Employees> employees = null;
        try {
            XmlPullParserHandler parser = new XmlPullParserHandler();
            InputStream is=getAssets().open("employees.xml");
            employees = parser.parse(is);

            ArrayAdapter<Employees> adapter =new ArrayAdapter<Employees>
                    (this,android.R.layout.simple_list_item_1, employees);
            listView1.setAdapter(adapter);

        } catch (IOException e) {e.printStackTrace();}

        PostpaidOperator postpaidOperator=new PostpaidOperator();
        postpaidOperator.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    private class PostpaidOperator extends AsyncTask<String,Integer,String> {
        Mobilepayment_ServiceProviderID mobile_payment_mode=new Mobilepayment_ServiceProviderID();
        String responsePostpaidOpr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mapPostpaid= new HashMap<String, Integer>();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(String_url.GetProviderNameByProviderTypeID);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair(String_url.AuthKey, String_url.auth_key1));
                nameValuePairs.add(new BasicNameValuePair(String_url.ServiceProviderTypeID, String.valueOf(String_url.postpaid_ServiceProviderTypeID)));


                final HttpParams httpParams = httpclient.getParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 45000);
                HttpConnectionParams.setSoTimeout(httpParams, 45000);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request

                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responsePostpaidOpr= EntityUtils.toString(entity);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return responsePostpaidOpr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{
                if(s!=null)
                {
                    Gson gson   = new GsonBuilder().create();
                    Type type   = new TypeToken<ResponseBase<GetProviderNameByProviderTypeIDResponse[]>>(){}.getType();
                    ResponseBase<GetProviderNameByProviderTypeIDResponse[]> responseBase=gson.fromJson(s, type);
                    Map<String, List<String>> map1 = new HashMap<String, List<String>>();
                    branchList1  = new ArrayList<String>();
                    serviceProviderImagelist= new ArrayList<String>();

                    branchList1.add(getString(R.string.spinner_title));
                    if(responseBase.id==1)
                    {
                        for(GetProviderNameByProviderTypeIDResponse getProviderNameByProviderTypeIDResponse:responseBase.responseData)
                        {
                            serviceProviderName= getProviderNameByProviderTypeIDResponse.serviceProviderName;
                            int ServiceProviderID = getProviderNameByProviderTypeIDResponse.serviceProviderId;
                            boolean serviceProviderStatus = getProviderNameByProviderTypeIDResponse.serviceProviderStatus;
                            String serviceProviderImage=getProviderNameByProviderTypeIDResponse.serviceProviderImage;
                            if (serviceProviderStatus) {

                                List<String> valSetOne = new ArrayList<String>();
                                valSetOne.add(serviceProviderName);
                                valSetOne.add(String.valueOf(ServiceProviderID));
                                map1.put(serviceProviderName, valSetOne);
                                branchList1.add(serviceProviderName);
                                serviceProviderImagelist.add(serviceProviderImage);

                                mobile_payment_mode.storeBranchList("payment_mode", map1);
                                mobile_payment_mode.storeBranchListDetail("payment_id", branchList1);

                                mapPostpaid.put(serviceProviderName, ServiceProviderID);

                            }


                        }
                    }



                    List<String> branchNameResultList =  mobile_payment_mode.getBranchListDetail();
                    ArrayAdapter<String> dataAdapter_res = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, branchNameResultList)
                    {
                        public View getView(int position, View convertView, android.view.ViewGroup parent) {
                            //  tfavv = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Avvaiyar.ttf");
                            TextView v = (TextView) super.getView(position, convertView, parent);

                            v.setTextColor(Color.parseColor(String_url.spinnerHintColor));
                            v.setTextSize(18);
                            v.setGravity(Gravity.CENTER);

                            return v;
                        }

                        public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                            TextView v = (TextView) super.getView(position, convertView, parent);

                            v.setTextColor(Color.parseColor(String_url.spinnerDropDownTextColor));
                            v.setTextSize(18); v.setGravity(Gravity.CENTER);
                            v.setPadding(15,15,15,15);
                            return v;
                        }
                    };
                    dataAdapter_res.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    spinnerSelectOperator.setAdapter(dataAdapter_res);
                    spinnerSelectOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            try {
                                str_ServiceProviderName = mobile_payment_mode.getFavoriteName(spinnerSelectOperator.getSelectedItem().toString());
                                str_ServiceProviderID = mobile_payment_mode.getFavoriteMobileNumber(spinnerSelectOperator.getSelectedItem().toString());

                                Log.d("FavoriteName", str_ServiceProviderName);
                                Log.d("FavoriteName", str_ServiceProviderID);

//                                    macPage1.setVisibility(View.GONE);
//                                    macPage2.setVisibility(View.VISIBLE);
//                                    macPage3.setVisibility(View.VISIBLE);
//                                    macPage4.setVisibility(View.GONE);
                            } catch (NullPointerException e) {
                                System.out.println("Exception" + e);
                            }

                            // sBranchIfscCode = ifscCode;
                            //Toast.makeText(getActivity(), "payment_id"+payment_id, Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }



        }
    }
}
