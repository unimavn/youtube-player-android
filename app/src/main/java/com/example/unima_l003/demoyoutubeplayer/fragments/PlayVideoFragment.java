package com.example.unima_l003.demoyoutubeplayer.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.unima_l003.demoyoutubeplayer.R;
import com.example.unima_l003.demoyoutubeplayer.abstracts.NMDialogFragment;
import com.example.unima_l003.demoyoutubeplayer.objects.Video;

/**
 * s
 * Created by truon on 6/3/2016.
 */

public class PlayVideoFragment extends NMDialogFragment {
    private static final String EXTRA_VIDEO = "PlayVideoFragment.Video";

    private Video video;

    private static Bundle createExtras(Video video) {
        Bundle bundle = new Bundle();
        String data = Video.toJson(video);
        bundle.putString(EXTRA_VIDEO, data);
        return bundle;
    }

    public static void show(Context context, Video video) {
        PlayVideoFragment fragment = new PlayVideoFragment();
        fragment.setArguments(createExtras(video));
        if (!((Activity) context).isFinishing()) {
            fragment.show(context);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (getArguments() != null) {
            String data = getArguments().getString(EXTRA_VIDEO);
            video = Video.fromJson(data);
        }
    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_video, container, false);
    }

    @Override
    protected void initUI(View view, Bundle savedInstanceState) {


        TextView tvVideoTitle = (TextView) view.findViewById(R.id.tv_play_video_title);

        if (video != null) {
            Bundle bundle = VideoYoutubeFragment.createBundle(video.getId());
            VideoYoutubeFragment videoYoutubeFragment = VideoYoutubeFragment.newInstance(bundle);

            getChildFragmentManager().beginTransaction()
                    .add(R.id.frame_youtube, videoYoutubeFragment)
                    .commit();

            tvVideoTitle.setText(video.getTitle());
        }


    }

    @Override
    protected void loadData(Bundle savedInstanceState) {

    }

}
