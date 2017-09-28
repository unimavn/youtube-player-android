package com.example.unima_l003.demoyoutubeplayer.objects

import android.util.Log
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Created by truon on 6/1/2016.
 */
class Video {

    var id: String? = null
    var title: String? = null
    var artist: String? = null
    var image: String? = null
    var date: Date? = null
        set
    var url: String? = null
        set
    private var type: Int = 0
    private var viewCount: Int = 0

    constructor() {}

    constructor(id: String, title: String, artist: String, image: String, date: Date, url: String, type: Int, viewCount: Int) {
        this.id = id
        this.title = title
        this.artist = artist
        this.image = image
        this.date = date
        this.url = url
        this.type = type
        this.viewCount = viewCount
    }

    companion object {

        fun getDurationTime(video: Video): String {
            val now = DateTime()
            val period = Period(DateTime(video.date), now)
            val formatter = PeriodFormatterBuilder()
                    .appendYears().appendSuffix(" years ago-")
                    .appendMonths().appendSuffix(" months ago-")
                    .appendWeeks().appendSuffix(" weeks ago-")
                    .appendDays().appendSuffix(" days ago-")
                    .appendHours().appendSuffix(" hours ago-")
                    .appendMinutes().appendSuffix(" minutes ago-")
                    .appendSeconds().appendSuffix(" seconds ago")
                    .printZeroRarelyFirst()
                    .toFormatter()

            val elapsed = formatter.print(period)
            return elapsed.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        }

        fun toJson(video: Video?): String? {
            if (video != null) {
                try {
                    val json = JSONObject()
                    json.putOpt("id", video.id)
                    json.putOpt("title", video.title)
                    json.putOpt("artist", video.artist)
                    json.putOpt("image", video.image)
                    json.putOpt("date", video.date!!.time)
                    json.putOpt("url", video.url)
                    Log.e("nulldroid", "toJson: " + video.url!!)
                    json.putOpt("type", video.type)
                    json.putOpt("viewCount", video.viewCount)
                    return json.toString()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return null
        }

        fun fromJson(json: String?): Video {
            val video = Video()
            if (json != null) {
                try {
                    val js = JSONObject(json)
                    video.id = js.optString("id")
                    video.title = js.optString("title")
                    video.artist = js.optString("artist")
                    video.image = js.optString("image")
                    val time = js.getLong("date")
                    video.date = Date(time)
                    video.url = js.optString("url")
                    video.type = js.optInt("type")
                    video.viewCount = js.optInt("viewCount")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return video
        }
    }

}
