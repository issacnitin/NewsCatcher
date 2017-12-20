package com.beerwithai.newscatcher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beerwithai.newscatcher.NewsView;
import com.beerwithai.newscatcher.R;

import org.w3c.dom.Text;

import java.net.URL;

public class SwipeDeckAdapter extends BaseAdapter {

    private Bitmap[] data;
    private URL[] urls;
    private String[] titles, artists;
    private Context context;

    public SwipeDeckAdapter(String[] titles, String[] artists, Bitmap[] data, URL[] urls,Context context) {
        this.data = data;
        this.urls = urls;
        this.context = context;
        this.titles = titles;
        this.artists = artists;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            // normally use a viewholder
            v = inflater.inflate(R.layout.cardview, parent, false);
        }
        ImageView coverImage = (ImageView) v.findViewById(R.id.card_image);
        Bitmap item = (Bitmap)getItem(position);
        coverImage.setImageBitmap(item);
        TextView musicTitle = (TextView) v.findViewById(R.id.sample_text);
        String title = titles[position];
        musicTitle.setText(title);

        TextView artist = (TextView) v.findViewById(R.id.artists);
        String artists_ = artists[position];
        artist.setText(artists_);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = context.getSharedPreferences("favorite_music", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(titles[position], urls[position].toString());
                editor.commit();
            }
        });

        return v;
    }
}