package com.example.unima_l003.demoyoutubeplayer.objects;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by truon on 6/1/2016.
 */
public class Video{

    private String id;
    private String title;
    private String artist;
    private String image;
    private Date date;
    private String url;
    private int type;
    private int viewCount;

    public Video() {
    }

    public Video(String id, String title, String artist, String image, Date date, String url, int type, int viewCount) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.image = image;
        this.date = date;
        this.url = url;
        this.type = type;
        this.viewCount = viewCount;
    }

    public static String getDurationTime(Video video) {
        DateTime now = new DateTime();
        Period period = new Period(new DateTime(video.getDate()), now);
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendYears().appendSuffix(" years ago-")
                .appendMonths().appendSuffix(" months ago-")
                .appendWeeks().appendSuffix(" weeks ago-")
                .appendDays().appendSuffix(" days ago-")
                .appendHours().appendSuffix(" hours ago-")
                .appendMinutes().appendSuffix(" minutes ago-")
                .appendSeconds().appendSuffix(" seconds ago")
                .printZeroRarelyFirst()
                .toFormatter();

        String elapsed = formatter.print(period);
        return elapsed.split("-")[0];
    }

    public static String toJson(Video video) {
        if (video != null) {
            try {
                JSONObject json = new JSONObject();
                json.putOpt("id", video.getId());
                json.putOpt("title", video.getTitle());
                json.putOpt("artist", video.getArtist());
                json.putOpt("image", video.getImage());
                json.putOpt("date", video.getDate().getTime());
                json.putOpt("url", video.getUrl());
                Log.e("nulldroid", "toJson: " + video.getUrl());
                json.putOpt("type", video.getType());
                json.putOpt("viewCount", video.getViewCount());
                return json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Video fromJson(String json) {
        Video video = new Video();
        if (json != null) {
            try {
                JSONObject js = new JSONObject(json);
                video.setId(js.optString("id"));
                video.setTitle(js.optString("title"));
                video.setArtist(js.optString("artist"));
                video.setImage(js.optString("image"));
                long time = js.getLong("date");
                video.setDate(new Date(time));
                video.setUrl(js.optString("url"));
                video.setType(js.optInt("type"));
                video.setViewCount(js.optInt("viewCount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return video;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private int getType() {
        return type;
    }

    private void setType(int type) {
        this.type = type;
    }

    private int getViewCount() {
        return viewCount;
    }

    private void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

}
