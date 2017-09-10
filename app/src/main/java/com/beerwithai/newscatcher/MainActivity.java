package com.beerwithai.newscatcher;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.daprlabs.aaron.swipedeck.SwipeDeck;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MainActivity extends AppCompatActivity {


    TextView txtJson;
    JSONArray jsonArray;
    ArrayList<String> titleTexts=new ArrayList<String>(), urlTexts = new ArrayList<String>();
    boolean flag = false;
    int i;
    ArrayAdapter adapter;
    String[] titleStringArray, urlStringArray;
    com.beerwithai.newscatcher.SwipeDeckAdapter swAdapter;
    SwipeDeck cardStack;
    Button btn, btn2, btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);

        String url="http://starlord.hackerearth.com/newsjson";
        try {
            JSONArray t = new AsyncTaskRunner().execute(url).get();
        } catch(Exception e) {

        }
        titleStringArray = titleTexts.toArray(new String[titleTexts.size()]);
        urlStringArray = urlTexts.toArray(new String[urlTexts.size()]);

        swAdapter = new com.beerwithai.newscatcher.SwipeDeckAdapter(titleStringArray, urlStringArray, this);
        cardStack.setAdapter(swAdapter);
        cardStack.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long positionInAdapter) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + positionInAdapter);

            }

            @Override
            public void cardSwipedRight(long positoinInAdapter) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + positoinInAdapter);
                Intent intent = new Intent(getApplicationContext(), NewsView.class);
                intent.putExtra("url", urlTexts.get((int)positoinInAdapter));
                startActivity(intent);
            }


        });

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardLeft(180);
            }
        });

        btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardRight(180);
            }
        });

        cardStack.scrollBy(0, 1);
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
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
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

