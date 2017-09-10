package com.beerwithai.newscatcher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Wrapper;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    String[] newsArray = {"mobile", "android", "job", "ios"};
    TextView txtJson;
    JSONArray jsonArray;
    ArrayList<String> titleTexts=new ArrayList<String>(), urlTexts = new ArrayList<String>();
    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url="http://starlord.hackerearth.com/newsjson";
        try {
            JSONArray t = new AsyncTaskRunner().execute(url).get();
        } catch(Exception e) {

        }
        String[] titleStringArray = titleTexts.toArray(new String[titleTexts.size()]);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.textview, titleStringArray);

        txtJson = (TextView) findViewById(R.id.label);

        ListView listView = (ListView) findViewById(R.id.newsItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlTexts.get(position)));
                startActivity(browserIntent);
            }
        });
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            try {
                jsonArray = getJSONObjectFromURL(params[0]);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String title = jsonobject.getString("TITLE");
                    String url = jsonobject.getString("URL");
                    titleTexts.add(title);
                    urlTexts.add(url);
                }
            }catch(Exception e) {

            }
            return jsonArray;
        }


        @Override
        protected void onPreExecute() {
            //progressDialog = ProgressDialog.show(MainActivity.this, "ProgressDialog", "Wait for "+time.getText().toString()+ " seconds");
        }


        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);

        }
    }
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }
    public static JSONArray getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        String jsonString="";
        if(isInternetAvailable()) {
            URL url = new URL(urlString);
            URLConnection urlConn = url.openConnection();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            InputStream in;
            if(responseCode == 200) {
                // response code is OK
                in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                jsonString = sb.toString();
            }else{
                // response code is not OK
            }
        }
        JSONArray jsonArray = new JSONArray(jsonString);

        return (jsonArray);
    }

}

