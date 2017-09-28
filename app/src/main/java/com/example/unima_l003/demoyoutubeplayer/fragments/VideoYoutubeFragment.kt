package com.example.unima_l003.demoyoutubeplayer.fragments

import android.os.Bundle
import android.widget.Toast

import com.example.unima_l003.demoyoutubeplayer.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment


/**
 * afghannetwork-android
 * Created by jack on 1/28/2016.
 * Copyright 2016 Nikmesoft Company, Ltd. All rights reserved.
 */
class VideoYoutubeFragment : YouTubePlayerSupportFragment(), YouTubePlayer.OnInitializedListener {


    private var mVideoId: String? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        val arguments = arguments

        if (bundle != null && bundle.containsKey(KEY_VIDEO_ID)) {
            mVideoId = bundle.getString(KEY_VIDEO_ID)
        } else if (arguments != null && arguments.containsKey(KEY_VIDEO_ID)) {
            mVideoId = arguments.getString(KEY_VIDEO_ID)
        }
        initialize(getString(R.string.google_api_key), this)
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {
        youTubePlayer.fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION

        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI)

        if (mVideoId != null) {
            if (b) {
                youTubePlayer.play()
            } else {
                youTubePlayer.loadVideo(mVideoId)
            }
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError) {
            youTubeInitializationResult.getErrorDialog(activity, RECOVERY_DIALOG_REQUEST).show()
        } else {
            //Handle the failure
            Toast.makeText(activity, "Error load video", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putString(KEY_VIDEO_ID, mVideoId)
    }

    companion object {
        private val RECOVERY_DIALOG_REQUEST = 1

        private val KEY_VIDEO_ID = "KEY_VIDEO_ID"

        fun newInstance(bundle: Bundle?): VideoYoutubeFragment {
            val youTubeFragment = VideoYoutubeFragment()
            if (bundle != null)
                youTubeFragment.arguments = bundle
            return youTubeFragment
        }

        fun createBundle(videoId: String): Bundle {
            val bundle = Bundle()
            bundle.putString(KEY_VIDEO_ID, videoId)
            return bundle
        }
    }
}
