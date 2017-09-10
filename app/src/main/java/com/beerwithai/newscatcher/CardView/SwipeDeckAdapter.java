package com.beerwithai.newscatcher;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beerwithai.newscatcher.MainActivity;
import com.beerwithai.newscatcher.R;

import java.util.List;

public class SwipeDeckAdapter extends BaseAdapter {

    private String[] data;
    private String[] urls;
    private Context context;

    public SwipeDeckAdapter(String[] data, String[] urls, Context context) {
        this.data = data;
        this.urls = urls;
        this.context = context;
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
        TextView textView = (TextView) v.findViewById(R.id.sample_text);
        String item = (String)getItem(position);
        textView.setText(item);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Layer type: ", Integer.toString(v.getLayerType()));
                Log.i("Hardware Accel type:", Integer.toString(View.LAYER_TYPE_HARDWARE));
                Intent intent = new Intent(context, NewsView.class);
                intent.putExtra("url", urls[position]);
                context.startActivity(intent);
            }
        });
        return v;
    }
}