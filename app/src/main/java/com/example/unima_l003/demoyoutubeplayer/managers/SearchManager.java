package com.example.unima_l003.demoyoutubeplayer.managers;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.unima_l003.demoyoutubeplayer.engines.SearchEngine;
import com.example.unima_l003.demoyoutubeplayer.engines.YEngine;
import com.example.unima_l003.demoyoutubeplayer.objects.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Music
 * Created by vinhn_000 on 5/24/2015.
 * Copyright 2015 Nikmesoft Company, Ltd. All rights reserved.
 */
public class SearchManager {
    public final int COUNT = 20;
    private final SearchEngine[] searchEngines = {new YEngine(10)};
    private String keyword;
    private Callback callback;
    private boolean loading;
    private boolean finished;


    public SearchManager(@NonNull String keyword, @Nullable Callback callback) {
        this.keyword = keyword;
        this.callback = callback;
    }

    private SearchEngine[] getSearchEngines() {
        // do not create a new array here. it doesn't work
        ArrayList<SearchEngine> ret = new ArrayList<SearchEngine>();
        return searchEngines;
    }


    public void search() {
        if (loading || finished) {
            return;
        }
        SearchEngine engine = getNext();
        loading = true;
        AsyncTask<SearchEngine, Void, List<Video>> searchTask = new AsyncTask<SearchEngine, Void, List<Video>>() {
            @Override
            protected List<Video> doInBackground(SearchEngine... params) {
                return params[0].search(keyword, COUNT);
            }

            @Override
            protected void onPostExecute(List<Video> results) {
                super.onPostExecute(results);
                finished = getNext() == null;
                int videoSize = results == null ? 0 : results.size();
                if (callback != null) {
                    callback.onUpdateResult(results, finished, videoSize < COUNT);
                }
                loading = false;
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            searchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, engine);
        } else {
            searchTask.execute(engine);
        }

    }

    public SearchEngine getNext() {
        for (SearchEngine engine : getSearchEngines()) {
            if (!engine.hasFinish()) {
                return engine;
            }
        }
        return null;
    }



    public interface Callback {
        void onUpdateResult(List<Video> videos, boolean finished, boolean success);
    }
}
