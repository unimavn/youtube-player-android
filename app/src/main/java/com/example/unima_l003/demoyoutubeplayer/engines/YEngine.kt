package com.example.unima_l003.demoyoutubeplayer.engines

import android.text.TextUtils
import android.util.Base64
import com.example.unima_l003.demoyoutubeplayer.objects.Video
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * stricter
 * Created by truon on 6/1/2016.
 */
class YEngine(maximumPage: Int) : SearchEngine(maximumPage) {

    private var keyIndex = 0
    private val listApiKeys: List<String>?
    private var pageToken = ""

    init {
        listApiKeys = Arrays.asList(*API_KEY)

        Collections.shuffle(listApiKeys!!)
    }

    override fun authorization(request: Request): Request = request

    override fun getUrl(keyword: String, page: Int, count: Int): String? {
        if (listApiKeys == null) return null
        if (keyIndex == listApiKeys.size)
            keyIndex = 0
        return String.format(YOUTUBE_SEARCH, listApiKeys[keyIndex++], keyword, count, pageToken, Locale.getDefault().language)
    }

    override fun parseResponse(data: String): List<Video> {
        val videos = ArrayList<Video>()
        try {
            val root = JSONObject(data)
            pageToken = root.optString("nextPageToken")

            val array = root.optJSONArray("items")
            for (i in 0 until array.length()) {
                try {
                    val json = array.optJSONObject(i)
                    val id = json.optJSONObject("id")
                    val snippet = json.optJSONObject("snippet")
                    val thumbnails = snippet.optJSONObject("thumbnails")
                    val medium = thumbnails.optJSONObject("medium")

                    val time = snippet.optString("publishedAt")

                    val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
                    val date = format.parse(time.replace("T", " "))

                    val videoId = id.optString("videoId")
                    if (TextUtils.isEmpty(videoId))
                        continue

                    val video = Video()
                    video.id = videoId
                    video.title = snippet.optString("title").trim { it <= ' ' }
                    video.artist = snippet.optString("channelTitle").trim { it <= ' ' }
                    video.date = date
                    video.url = String.format(rb64("==wcl0jd/g2Y0F2dv02bj5SZiVHd19Weuc3d39yL6MHc0RHa"), id.optString("videoId")) // "https://www.youtube.com/watch?v=%s"
                    video.image = medium.optString("url").trim { it <= ' ' }
                    videos.add(video)
                } catch (e: java.text.ParseException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return videos
    }

    companion object {

        internal val YOUTUBE_HOST = rb64("v02bj5ycpBXYlx2Zv92Zuc3d39yL6MHc0RHa") //"https://www.googleapis.com/";
        private val YOUTUBE_SEARCH = YOUTUBE_HOST + rb64("0VGcwlmbz1DdyFGc/g2YyFWZz9yM29SZiVHd19We") + "&key=%s&q=%s&maxResults=%s&pageToken=%s&relevanceLanguage=%s" // youtube/v3/search?part=snippet

        val YOUTUBE_DURATION = "https://www.googleapis.com/youtube/v3/videos?id=%s&part=contentDetails&key=AIzaSyB-vE_PNo2_1o65I2etL3aITlKRYYhzeFs"

        internal val API_KEY = arrayOf("AIzaSyA4F93yHRFHhLAAB0V1Gq5FwMLR7gyp1vA", "AIzaSyB-vE_PNo2_1o65I2etL3aITlKRYYhzeFs", "AIzaSyAiHmiWsHBbzoSojnV3kkQKsh0qNvQoTHg", "AIzaSyAB0-bh7U4jSH9Mknwe0ke693fNg3ZaBTc", "AIzaSyBKMRMYEiUIePp2IKzBNgCaxVLgFhjMSlQ", "AIzaSyDDP01Gnj3-wfoqM59xQz6pryJQhmYWCt8", "AIzaSyBTYMejmwKbr1-Kv1IVZO10fxoQ17QbHGw", "AIzaSyCjHL3fQcfHvny-XEnLyGJ8rrxeCtnqOew", "AIzaSyB7VsXvNd0gBetxo2ruDjZ7qnMwV_TeEYo", "AIzaSyCdT8c4cj20SHINyjXqSJLayDVm9S1xyA0")

        private fun rb64(input: String): String =
                String(Base64.decode(StringBuilder(input).reverse().toString(), 0))
    }
}
