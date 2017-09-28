package com.example.unima_l003.demoyoutubeplayer.abstracts

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Albameet
 * Created by vinhn_000 on 3/5/2015.
 * Copyright 2015 Nikmesoft Company, Ltd. All rights reserved.
 */
abstract class NMViewHolder private constructor(private val adapter: RecyclerView.Adapter<out NMViewHolder>, itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected val context: Context

    init {
        this.context = itemView.context
        initUI()
    }

    constructor(adapter: RecyclerView.Adapter<out NMViewHolder>, parent: ViewGroup, @LayoutRes layout: Int) : this(adapter, LayoutInflater.from(parent.context).inflate(layout, parent, false)) {}


    protected abstract fun initUI()

}
