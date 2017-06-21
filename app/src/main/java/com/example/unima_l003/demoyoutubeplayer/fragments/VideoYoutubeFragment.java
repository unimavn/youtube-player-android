package com.example.unima_l003.demoyoutubeplayer.fragments;

import android.os.Bundle;
import android.widget.Toast;

import com.example.unima_l003.demoyoutubeplayer.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


/**
 * afghannetwork-android
 * Created by jack on 1/28/2016.
 * Copyright 2016 Nikmesoft Company, Ltd. All rights reserved.
 */
public class VideoYoutubeFragment extends YouTubePlayerSupportFragment implements YouTubePlayer.OnInitializedListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private static final String KEY_VIDEO_ID = "KEY_VIDEO_ID";

    public static VideoYoutubeFragment newInstance(Bundle bundle) {
        final VideoYoutubeFragment youTubeFragment = new VideoYoutubeFragment();
        if (bundle != null)
            youTubeFragment.setArguments(bundle);
        return youTubeFragment;
    }

    public static Bundle createBundle(String videoId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_VIDEO_ID, videoId);
        return bundle;
    }


    private String mVideoId;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        final Bundle arguments = getArguments();

        if (bundle != null && bundle.containsKey(KEY_VIDEO_ID)) {
            mVideoId = bundle.getString(KEY_VIDEO_ID);
        } else if (arguments != null && arguments.containsKey(KEY_VIDEO_ID)) {
            mVideoId = arguments.getString(KEY_VIDEO_ID);
        }
        initialize(getString(R.string.google_api_key), this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        if (mVideoId != null) {
            if (b) {
                youTubePlayer.play();
            } else {
                youTubePlayer.loadVideo(mVideoId);
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
        } else {
            //Handle the failure
            Toast.makeText(getActivity(), "Error load video", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(KEY_VIDEO_ID, mVideoId);
    }
}
