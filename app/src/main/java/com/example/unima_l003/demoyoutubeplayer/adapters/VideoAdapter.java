package com.example.unima_l003.demoyoutubeplayer.adapters;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.unima_l003.demoyoutubeplayer.R;
import com.example.unima_l003.demoyoutubeplayer.Utils.AppUtils;
import com.example.unima_l003.demoyoutubeplayer.abstracts.NMAdapter;
import com.example.unima_l003.demoyoutubeplayer.abstracts.NMViewHolder;
import com.example.unima_l003.demoyoutubeplayer.engines.YEngine;
import com.example.unima_l003.demoyoutubeplayer.interfaces.OnItemClickListener;
import com.example.unima_l003.demoyoutubeplayer.objects.Video;
import com.facebook.drawee.view.SimpleDraweeView;

import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * VideoDownloader
 * Created by jack on 6/1/2016.
 * Copyright 2016 Nikmesoft Company, Ltd. All rights reserved.
 */
public class VideoAdapter extends NMAdapter<NMViewHolder> {

    private HashMap<String, VideoVH.GetDurationTask> hsDurationTasks = new HashMap<>();
    private HashMap<String, String> hsDuration = new HashMap<>();
    private List<Video> items;
    private OnItemClickListener onItemClickListener;
    private Executor executor = Executors.newFixedThreadPool(5);

    VideoAdapter(List<Video> items) {
        this.items = items;
    }

    @Override
    public NMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoVH(this, parent);
    }


    @Override
    public void onBindViewHolder(NMViewHolder holder, int position) {
        if (holder instanceof VideoVH) {
            ((VideoVH) holder).setData(items.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public int getItemVideoPosition(int position) {
        return position;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class VideoVH extends NMViewHolder {

        SimpleDraweeView ivVideoAvatar;
        TextView tvTitle;
        TextView tvArtist;
        TextView tvTime;
        TextView tvDuration;

        VideoVH(RecyclerView.Adapter<? extends NMViewHolder> adapter, ViewGroup parent) {
            this(adapter, parent, R.layout.list_item_video);
        }

        VideoVH(RecyclerView.Adapter<? extends NMViewHolder> adapter, ViewGroup parent, @LayoutRes int layout) {
            super(adapter, parent, layout);
        }

        @Override
        protected void initUI() {
            ivVideoAvatar = (SimpleDraweeView) itemView.findViewById(R.id.iv_item_video_thumbnail);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_item_video_title);
            tvArtist = (TextView) itemView.findViewById(R.id.tv_item_video_artist);
            tvTime = (TextView) itemView.findViewById(R.id.tv_item_video_time);
            tvDuration = (TextView) itemView.findViewById(R.id.tv_item_video_duration);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(VideoVH.this);
                    }
                }
            });
        }

        public void setData(final Video video) {
            if (ivVideoAvatar != null) {
                int size = getContext().getResources().getDimensionPixelSize(R.dimen.item_video_avatar_width);
                if (video.getImage() != null) {
                    Uri uri = Uri.parse(video.getImage());
                    AppUtils.load(ivVideoAvatar, uri, size, size / 16 * 9);
                }
            }

            tvTitle.setText(video.getTitle());
            tvArtist.setText(video.getArtist());

            tvTime.setText(Video.getDurationTime(video));
            tvTime.setTag(video.getId());
            if (tvTime.getText().toString().trim().toLowerCase().equals("0 seconds ago")) {
                tvTime.setVisibility(View.INVISIBLE);
            }
            String duration = hsDuration.get(video.getId());
            if (!TextUtils.isEmpty(duration)) {
                tvDuration.setVisibility(View.VISIBLE);
                tvDuration.setText(duration);
            } else {
                GetDurationTask task = hsDurationTasks.get(video.getId());
                if (task == null) {
                    task = new GetDurationTask(video.getId());
                    task.executeOnExecutor(executor, video.getId());
                    hsDurationTasks.put(video.getId(), task);
                }
            }
            tvDuration.setTag(video.getId());
        }

        private class GetDurationTask extends AsyncTask<String, Void, String> {
            private String videoId;

            GetDurationTask(String videoId) {
                this.videoId = videoId;
            }

            @Override
            protected String doInBackground(String... strings) {
                String url = String.format(YEngine.YOUTUBE_DURATION, strings[0]);
                url = url.replaceAll(" +", "%2c");
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(url).get().build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        return parseResponse(response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String duration) {
                super.onPostExecute(duration);
                if (duration != null) {
                    PeriodFormatter format = ISOPeriodFormat.standard();
                    Period period = format.parsePeriod(duration);

                    PeriodFormatter formatter = new PeriodFormatterBuilder()
                            .appendHours().appendSuffix(":").minimumPrintedDigits(2)
                            .printZeroAlways().appendMinutes().appendSuffix(":").minimumPrintedDigits(2)
                            .printZeroAlways().appendSeconds().minimumPrintedDigits(2)
                            .toFormatter();
                    String elapsed = formatter.print(period);
                    if (tvDuration.getTag().equals(videoId)) {
                        tvDuration.setVisibility(View.VISIBLE);
                        tvDuration.setText(elapsed);
                        hsDuration.put(videoId, elapsed);
                    }
                }
                hsDurationTasks.remove(videoId);
            }
        }

        String parseResponse(String data) {
            try {
                JSONObject root = new JSONObject(data);
                JSONArray array = root.optJSONArray("items");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.optJSONObject(i);
                    JSONObject contentDetails = json.optJSONObject("contentDetails");
                    return contentDetails.optString("duration");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
