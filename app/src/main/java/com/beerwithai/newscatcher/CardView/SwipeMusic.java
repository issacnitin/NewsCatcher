package com.beerwithai.newscatcher.CardView;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.beerwithai.newscatcher.NewsView;
import com.beerwithai.newscatcher.R;
import com.daprlabs.aaron.swipedeck.SwipeDeck;

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
import java.util.ArrayList;

public class SwipeMusic extends android.support.v4.app.Fragment {

    com.beerwithai.newscatcher.SwipeDeckAdapter swAdapter;

    Button btn, btn2;

    JSONArray jsonArray;
    ArrayList<String> titleTexts=new ArrayList<String>(), artistList = new ArrayList<String>();
    ArrayList<URL> urlTexts = new ArrayList<URL>();
    ArrayList<Bitmap> coverImages = new ArrayList<Bitmap>();
    MediaPlayer mp = new MediaPlayer();
    boolean musicPlaying = false;
    SwipeDeck cardStack;

    String[] titleStringArray, artistListArray;
    URL[] urlStringArray;
    Bitmap[] coverImageArray;
    private View view;

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String url="http://starlord.hackerearth.com/studio";
        try {
            JSONArray t = new AsyncTaskRunner().execute(url).get();
        } catch(Exception e) {

        }

        if(view == null) {
            view = inflater.inflate(R.layout.content_swipe, container, false);
        }
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicPlaying)
                    mp.stop();
                else {
                    try {
                        String dataSource = urlStringArray[(int) cardStack.getAdapterIndex()].toString();
                        mp.setDataSource(dataSource);
                        mp.prepare();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    mp.start();
                }

                musicPlaying = !musicPlaying;
            }
        });
        cardStack = (SwipeDeck) view.findViewById(R.id.swipe_deck);
        // Inflate the layout for this fragment



        titleStringArray = titleTexts.toArray(new String[titleTexts.size()]);
        urlStringArray = urlTexts.toArray(new URL[urlTexts.size()]);
        coverImageArray = coverImages.toArray(new Bitmap[coverImages.size()]);
        artistListArray = artistList.toArray(new String[artistList.size()]);

        swAdapter = new com.beerwithai.newscatcher.SwipeDeckAdapter(titleStringArray, artistListArray, coverImageArray, urlStringArray, getContext());
        cardStack.setAdapter(swAdapter);
        swAdapter.notifyDataSetChanged();
        cardStack.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long positionInAdapter) {
                Log.i("NavigationActivity", "card was swiped left, position in adapter: " + positionInAdapter);

            }

            @Override
            public void cardSwipedRight(long positoinInAdapter) {
                Log.i("NavigationActivity", "card was swiped right, position in adapter: " + positoinInAdapter);

            }


        });

        btn = (Button) view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardLeft(180);
            }
        });

        btn2 = (Button) view.findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardRight(180);
            }
        });

        return view;
    }

    class AsyncTaskRunner extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            try {
                jsonArray = getJSONObjectFromURL(params[0]);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String title = jsonobject.getString("song");
                    String artist = jsonobject.getString("artists");
                    URL musicUrl = new URL(jsonobject.getString("url"));
                    URL coverURL = new URL(jsonobject.getString("cover_image"));

                    titleTexts.add(title);

                    HttpURLConnection ucon = (HttpURLConnection) coverURL.openConnection();
                    ucon.setInstanceFollowRedirects(false);
                    URL secondURL = new URL(ucon.getHeaderField("Location"));

                    Bitmap bmp = BitmapFactory.decodeStream(secondURL.openConnection().getInputStream());

                    coverImages.add(getResizedBitmap(bmp, 400, 300));
                    ucon.disconnect();

                    ucon = (HttpURLConnection) musicUrl.openConnection();
                    ucon.setInstanceFollowRedirects(false);
                    URL musicActualURL = new URL(ucon.getHeaderField("Location"));

                    urlTexts.add(musicActualURL);
                    artistList.add(artist);

                }
            }catch(Exception e) {

            }
            return jsonArray;
        }


        @Override
        protected void onPreExecute() {

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
            connection.disconnect();
        }
        JSONArray jsonArray = new JSONArray(jsonString);

        return (jsonArray);
    }

}

