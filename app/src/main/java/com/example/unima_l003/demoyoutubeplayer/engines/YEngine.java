package com.example.unima_l003.demoyoutubeplayer.engines;

import android.text.TextUtils;
import android.util.Base64;

import com.example.unima_l003.demoyoutubeplayer.objects.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Request;

/**
 * stricter
 * Created by truon on 6/1/2016.
 */
public class YEngine extends SearchEngine {

    public static final String YOUTUBE_HOST = rb64("v02bj5ycpBXYlx2Zv92Zuc3d39yL6MHc0RHa"); //"https://www.googleapis.com/";
    public static final String YOUTUBE_SEARCH = YOUTUBE_HOST + rb64("0VGcwlmbz1DdyFGc/g2YyFWZz9yM29SZiVHd19We") + "&key=%s&q=%s&maxResults=%s&pageToken=%s&relevanceLanguage=%s"; // youtube/v3/search?part=snippet

    public static final String YOUTUBE_DURATION = "https://www.googleapis.com/youtube/v3/videos?id=%s&part=contentDetails&key=AIzaSyB-vE_PNo2_1o65I2etL3aITlKRYYhzeFs";

    public static final String[] API_KEY =
                    {"AIzaSyA4F93yHRFHhLAAB0V1Gq5FwMLR7gyp1vA",
                    "AIzaSyB-vE_PNo2_1o65I2etL3aITlKRYYhzeFs",
                    "AIzaSyAiHmiWsHBbzoSojnV3kkQKsh0qNvQoTHg",
                    "AIzaSyAB0-bh7U4jSH9Mknwe0ke693fNg3ZaBTc",
                    "AIzaSyBKMRMYEiUIePp2IKzBNgCaxVLgFhjMSlQ",
                    "AIzaSyDDP01Gnj3-wfoqM59xQz6pryJQhmYWCt8",
                    "AIzaSyBTYMejmwKbr1-Kv1IVZO10fxoQ17QbHGw",
                    "AIzaSyCjHL3fQcfHvny-XEnLyGJ8rrxeCtnqOew",
                    "AIzaSyB7VsXvNd0gBetxo2ruDjZ7qnMwV_TeEYo",
                    "AIzaSyCdT8c4cj20SHINyjXqSJLayDVm9S1xyA0"};

    private int keyIndex = 0;
    private List<String> listApiKeys;
    private String pageToken = "";

    public YEngine(int maximumPage) {
        super(maximumPage);
        listApiKeys = Arrays.asList(API_KEY);

        Collections.shuffle(listApiKeys);
    }

    public static String rb64(String input) {
        return new String(Base64.decode(new StringBuilder(input).reverse().toString(), 0));
    }

    @Override
    public Request authorization(Request request) {
        return request;
    }

    @Override
    public String getUrl(String keyword, int page, int count) {
        if (listApiKeys == null) return null;
        if (keyIndex == listApiKeys.size())
            keyIndex = 0;
        return String.format(YOUTUBE_SEARCH, listApiKeys.get(keyIndex++), keyword, count, pageToken, Locale.getDefault().getLanguage());
    }

    @Override
    public List<Video> parseResponse(String data) {
        ArrayList<Video> videos = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(data);
            pageToken = root.optString("nextPageToken");

            JSONArray array = root.optJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject json = array.optJSONObject(i);
                    JSONObject id = json.optJSONObject("id");
                    JSONObject snippet = json.optJSONObject("snippet");
                    JSONObject thumbnails = snippet.optJSONObject("thumbnails");
                    JSONObject medium = thumbnails.optJSONObject("medium");

                    String time = snippet.optString("publishedAt");

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
                    Date date = format.parse(time.replace("T", " "));

                    String videoId = id.optString("videoId");
                    if (TextUtils.isEmpty(videoId))
                        continue;

                    Video video = new Video();
                    video.setId(videoId);
                    video.setTitle(snippet.optString("title").trim());
                    video.setArtist(snippet.optString("channelTitle").trim());
                    video.setDate(date);
                    video.setUrl(String.format(rb64("==wcl0jd/g2Y0F2dv02bj5SZiVHd19Weuc3d39yL6MHc0RHa"), id.optString("videoId"))); // "https://www.youtube.com/watch?v=%s"
                    video.setImage(medium.optString("url").trim());
                    videos.add(video);
                } catch (java.text.ParseException | NullPointerException e) {
                    e.printStackTrace();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videos;
    }
}
