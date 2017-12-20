package com.beerwithai.newscatcher.CardView;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beerwithai.newscatcher.R;

import org.w3c.dom.Text;

public class SearchMusic extends android.support.v4.app.Fragment {

    private View view;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    MediaPlayer mp = new MediaPlayer();
    boolean musicPlaying = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view == null)
            view = inflater.inflate(R.layout.content_search_music, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Bitmap[] bmp = new Bitmap[2];
        String[] titles = new String[2];
        titles[0] = "abc";
        titles[1] = "def";
        String[] artists = new String[2];
        artists[0] = "Shah";
        artists[1] = "Blah";
        String[] urls = new String[2];
        urls[0] = "http://www.google.com";
        urls[1] = "http://www.amazon.in";

        // specify an adapter (see also next example)
        mAdapter = new SongsAdapter(bmp, titles, artists, urls);
        mRecyclerView.setAdapter(mAdapter);

        android.support.design.widget.TextInputLayout searchBar = view.findViewById(R.id.textInputLayout3);

        return view;
    }

    public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {
        private Bitmap[] coverImages;
        private String[] titles, artists, urls;


        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView artist, title;
            public ImageView cover;
            public ImageButton play;

            public ViewHolder(View v) {
                super(v);
                artist = (TextView) v.findViewById(R.id.artists_rec);
                title = (TextView) v.findViewById(R.id.title_rec);
                cover = (ImageView) v.findViewById(R.id.coverImageSmall);
                play = (ImageButton) v.findViewById(R.id.playButtonSmall);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public SongsAdapter(Bitmap[] coverImages, String[] titles, String[] artists, String[] urls) {
            this.coverImages = coverImages;
            this.titles = titles;
            this.artists = artists;
            this.urls = urls;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            android.support.v7.widget.CardView view = (android.support.v7.widget.CardView)LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_song, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.title.setText(titles[position]);
            holder.artist.setText(artists[position]);
            holder.cover.setImageBitmap(coverImages[position]);
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(musicPlaying)
                        mp.stop();
                    else {
                        try {
                            String dataSource = urls[position];
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
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return coverImages.length;
        }
    }


}


