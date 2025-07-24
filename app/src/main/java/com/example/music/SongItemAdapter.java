package com.example.music;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SongItemAdapter extends BaseAdapter {

    private final String[] songs;
    Context context;

    public SongItemAdapter(Context context,String[] songs) {
        this.context = context;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.song_item, null);
        TextView songName = view.findViewById(R.id.song_name_text);
        songName.setSelected(true);
        songName.setText(songs[position]);

        return view;
    }
}
