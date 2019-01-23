package com.example.apirest;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnHit;
    TextView txtJson;
    private ListView listView = null;
    private ArrayAdapter arrayAdapter = null;
    ProgressDialog pd;
    private String[] blogTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnHit = (Button) findViewById(R.id.btnHit);
        //txtJson = (TextView) findViewById(R.id.tvJsonItem);
        listView = (ListView) findViewById(R.id.listview);


        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonTask().execute("https://angular-workspace-cloned-hugoclavijo.c9users.io/api/employees");



            }
        });


    }


    private class JsonTask extends AsyncTask <String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                //return buffer.toString();

                String response = buffer.toString();
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

//                for(int i=0; i< jsonArray.length();i++ ){
//                    JSONObject post = jsonArray.optJSONObject(i);
//                    String title = post.optString("name");
//
//                    blogTitles[i] = title;
//                }

                //String lastName = jsonObject.getString("name") +" "+ jsonObject.getString("lastname");

                //return lastName;
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
          //  txtJson.setText(result);
//            arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, blogTitles);
//
//            listView.setAdapter(arrayAdapter);

            try
            {
                String jsonInput = "[\"one\",\"two\",\"three\",\"four\",\"five\",\"six\",\"seven\",\"eight\",\"nine\",\"ten\"]";
                JSONArray jsonArray = new JSONArray(result);
                int length = jsonArray.length();
                List<String> listContents = new ArrayList<String>(length);
                for (int i = 0; i < length; i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    listContents.add(jsonObject.getString("name"));
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listContents);
                listView.setAdapter(arrayAdapter);
                //myListView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listContents));
            }
            catch (Exception e)
            {
                // this is just an example
            }



        }

        }




}
