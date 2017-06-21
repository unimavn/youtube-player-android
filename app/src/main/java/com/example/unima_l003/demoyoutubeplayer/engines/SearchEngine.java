package com.example.unima_l003.demoyoutubeplayer.engines;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.example.unima_l003.demoyoutubeplayer.objects.Video;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * stricter
 * Created by daniel.nikme on 5/10/16.
 */
public abstract class SearchEngine {

    private final int maximumPage;
    private int currentPage = 0;
    private int countApi = 0;

    SearchEngine(int maximumPage) {
        this.maximumPage = maximumPage;
    }

    @WorkerThread
    @Nullable
    public List<Video> search(final String keyword, int count) {
        String url = getUrl(keyword, currentPage, count);
        url = url.replaceAll(" +", "%2c");
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        request = authorization(request);
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                currentPage++;
                return parseResponse(response.body().string());
            } else {
                if (url.contains(YEngine.YOUTUBE_HOST)) {
                    countApi++;
                    if (countApi > YEngine.API_KEY.length)
                        return null;
                    return search(keyword, count);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPage = maximumPage;
        return null;
    }


    public boolean hasFinish() {
        return currentPage > maximumPage - 1;
    }



    public abstract Request authorization(Request request);

    public abstract String getUrl(String keyword, int page, int count);

    public abstract List<Video> parseResponse(String data);
}
