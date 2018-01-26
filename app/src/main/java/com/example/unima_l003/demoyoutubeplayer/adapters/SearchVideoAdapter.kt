package com.example.unima_l003.demoyoutubeplayer.adapters

import android.view.ViewGroup

import com.example.unima_l003.demoyoutubeplayer.abstracts.NMViewHolder
import com.example.unima_l003.demoyoutubeplayer.objects.Video


/**
 * unima
 * Created by michael on 7/4/2016.
 */
class SearchVideoAdapter(items: List<Video>) : VideoAdapter(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NMViewHolder =
            super.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: NMViewHolder, position: Int) {
        if (holder is VideoAdapter.VideoVH) {
            val positionVideo = getItemVideoPosition(position)
            super.onBindViewHolder(holder, positionVideo)
        }
    }

    override fun getItemViewType(position: Int): Int = super.getItemViewType(position)


}
