package com.example.unima_l003.demoyoutubeplayer.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.unima_l003.demoyoutubeplayer.R
import com.example.unima_l003.demoyoutubeplayer.abstracts.NMDialogFragment
import com.example.unima_l003.demoyoutubeplayer.objects.Video

/**
 * s
 * Created by truon on 6/3/2016.
 */

class PlayVideoFragment : NMDialogFragment() {

    private var video: Video? = null

    override fun initData(savedInstanceState: Bundle?) {
        if (arguments != null) {
            val data = arguments.getString(EXTRA_VIDEO)
            video = Video.fromJson(data)
        }
    }

    override fun initRootView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater!!.inflate(R.layout.fragment_play_video, container, false)

    override fun initUI(view: View?, savedInstanceState: Bundle?) {


        val tvVideoTitle = view!!.findViewById(R.id.tv_play_video_title) as TextView

        if (video != null) {
            val bundle = VideoYoutubeFragment.createBundle(video!!.id.toString())
            val videoYoutubeFragment = VideoYoutubeFragment.newInstance(bundle)

            childFragmentManager.beginTransaction()
                    .add(R.id.frame_youtube, videoYoutubeFragment)
                    .commit()

            tvVideoTitle.text = video!!.title
        }


    }

    override fun loadData(savedInstanceState: Bundle?) {

    }

    companion object {
        private val EXTRA_VIDEO = "PlayVideoFragment.Video"

        private fun createExtras(video: Video): Bundle {
            val bundle = Bundle()
            val data = Video.toJson(video)
            bundle.putString(EXTRA_VIDEO, data)
            return bundle
        }

        fun show(context: Context, video: Video) {
            val fragment = PlayVideoFragment()
            fragment.arguments = createExtras(video)
            if (!(context as Activity).isFinishing) {
                fragment.show(context)
            }
        }
    }

}
