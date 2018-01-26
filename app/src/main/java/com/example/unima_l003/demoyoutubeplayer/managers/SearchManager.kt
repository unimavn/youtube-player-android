package com.example.unima_l003.demoyoutubeplayer.managers

import android.os.AsyncTask

import com.example.unima_l003.demoyoutubeplayer.engines.SearchEngine
import com.example.unima_l003.demoyoutubeplayer.engines.YEngine
import com.example.unima_l003.demoyoutubeplayer.objects.Video

/**
 * Music
 * Created by vinhn_000 on 5/24/2015.
 * Copyright 2015 Nikmesoft Company, Ltd. All rights reserved.
 */
class SearchManager(private val keyword: String, private val callback: Callback?) {
    private val COUNT = 20
    private// do not create a new array here. it doesn't work
    val searchEngines = arrayOf<SearchEngine>(YEngine(10))
    private var loading: Boolean = false
    private var finished: Boolean = false

    private val next: SearchEngine?
        get() {
            for (engine in searchEngines) {
                if (!engine.hasFinish()) {
                    return engine
                }
            }
            return null
        }


    fun search() {
        if (loading || finished) {
            return
        }
        val engine = next
        loading = true
        val searchTask = object : AsyncTask<SearchEngine, Void, List<Video>>() {
            override fun doInBackground(vararg params: SearchEngine): List<Video>? =
                    params[0].search(keyword, COUNT)

            override fun onPostExecute(results: List<Video>?) {
                super.onPostExecute(results)
                finished = next == null
                val videoSize = results?.size ?: 0
                callback?.onUpdateResult(results, finished, videoSize < COUNT)
                loading = false
            }
        }

        searchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, engine)

    }


    interface Callback {
        fun onUpdateResult(videos: List<Video>?, finished: Boolean, success: Boolean)
    }
}
