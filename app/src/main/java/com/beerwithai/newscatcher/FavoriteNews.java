package com.beerwithai.newscatcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.beerwithai.newscatcher.R.id.nav_view;

public class FavoriteNews extends AppCompatActivity {

    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lv = (ListView) findViewById(R.id.fav_news);
        ArrayList<String> arr = new ArrayList<String>();
        settings = getSharedPreferences("favorite_music", MODE_PRIVATE);

        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = settings.edit();

        // Reading from SharedPreferences
        Map<String,?> keys = settings.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            arr.add(entry.getKey().toString());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arr );
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), NewsView.class);
                String message = settings.getString(arrayAdapter.getItem(i), "");
                intent.putExtra("url", message);
                startActivity(intent);
            }
        });
    }

}
