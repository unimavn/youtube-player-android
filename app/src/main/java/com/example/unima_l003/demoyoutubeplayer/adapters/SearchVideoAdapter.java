package com.example.unima_l003.demoyoutubeplayer.adapters;

import android.view.ViewGroup;

import com.example.unima_l003.demoyoutubeplayer.abstracts.NMViewHolder;
import com.example.unima_l003.demoyoutubeplayer.objects.Video;

import java.util.List;


/**
 * unima
 * Created by michael on 7/4/2016.
 */
public class SearchVideoAdapter extends VideoAdapter {

    public SearchVideoAdapter(List<Video> items) {
        super(items);
    }

    @Override
    public NMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(NMViewHolder holder, int position) {
        if (holder instanceof VideoVH) {
            int positionVideo = getItemVideoPosition(position);
            super.onBindViewHolder(holder, positionVideo);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }





}
