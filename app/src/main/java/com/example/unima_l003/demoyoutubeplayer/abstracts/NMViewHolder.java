package com.example.unima_l003.demoyoutubeplayer.abstracts;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Albameet
 * Created by vinhn_000 on 3/5/2015.
 * Copyright 2015 Nikmesoft Company, Ltd. All rights reserved.
 */
public abstract class NMViewHolder extends RecyclerView.ViewHolder {
    private final RecyclerView.Adapter<? extends NMViewHolder> adapter;
    private final Context context;

    private NMViewHolder(RecyclerView.Adapter<? extends NMViewHolder> adapter, View itemView) {
        super(itemView);
        this.adapter = adapter;
        this.context = itemView.getContext();
        initUI();
    }

    public NMViewHolder(RecyclerView.Adapter<? extends NMViewHolder> adapter, ViewGroup parent, @LayoutRes int layout) {
        this(adapter, LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    protected final Context getContext() {
        return context;
    }


    protected abstract void initUI();

}
