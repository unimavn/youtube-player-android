package com.example.unima_l003.demoyoutubeplayer.engines

import android.support.annotation.WorkerThread
import com.example.unima_l003.demoyoutubeplayer.objects.Video
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


/**
 * stricter
 * Created by daniel.nikme on 5/10/16.
 */
abstract class SearchEngine internal constructor(private val maximumPage: Int) {
    private var currentPage = 0
    private var countApi = 0

    @WorkerThread
    fun search(keyword: String, count: Int): List<Video>? {
        var url = getUrl(keyword, currentPage, count)
        url = url!!.replace(" +".toRegex(), "%2c")
        val okHttpClient = OkHttpClient()
        var request = Request.Builder().url(url).get().build()
        request = authorization(request)
        val call = okHttpClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                currentPage++
                return parseResponse(response.body().string())
            } else {
                if (url.contains(YEngine.YOUTUBE_HOST)) {
                    countApi++
                    return if (countApi > YEngine.API_KEY.size) null else search(keyword, count)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        currentPage = maximumPage
        return null
    }


    fun hasFinish(): Boolean = currentPage > maximumPage - 1


    abstract fun authorization(request: Request): Request

    abstract fun getUrl(keyword: String, page: Int, count: Int): String?

    abstract fun parseResponse(data: String): List<Video>
}
