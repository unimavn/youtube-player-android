package com.example.unima_l003.demoyoutubeplayer.adapters

import android.net.Uri
import android.os.AsyncTask
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.unima_l003.demoyoutubeplayer.R
import com.example.unima_l003.demoyoutubeplayer.Utils.AppUtils
import com.example.unima_l003.demoyoutubeplayer.abstracts.NMAdapter
import com.example.unima_l003.demoyoutubeplayer.abstracts.NMViewHolder
import com.example.unima_l003.demoyoutubeplayer.engines.YEngine
import com.example.unima_l003.demoyoutubeplayer.interfaces.OnItemClickListener
import com.example.unima_l003.demoyoutubeplayer.objects.Video
import com.facebook.drawee.view.SimpleDraweeView
import okhttp3.OkHttpClient
import okhttp3.Request
import org.joda.time.format.ISOPeriodFormat
import org.joda.time.format.PeriodFormatterBuilder
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors

/**
 * VideoDownloader
 * Created by jack on 6/1/2016.
 * Copyright 2016 Nikmesoft Company, Ltd. All rights reserved.
 */
open class VideoAdapter internal constructor(private val items: List<Video>?) : NMAdapter<NMViewHolder>() {

    private val hsDurationTasks = HashMap<String, VideoVH.GetDurationTask>()
    private val hsDuration = HashMap<String, String>()
    private var onItemClickListener: OnItemClickListener? = null
    private val executor = Executors.newFixedThreadPool(5)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NMViewHolder =
            VideoVH(this, parent)


    override fun onBindViewHolder(holder: NMViewHolder, position: Int) {
        (holder as? VideoVH)?.setData(items!![position])

    }

    override fun getItemCount(): Int = items?.size ?: 0

    fun getItemVideoPosition(position: Int): Int = position

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    internal inner class VideoVH @JvmOverloads constructor(adapter: RecyclerView.Adapter<out NMViewHolder>, parent: ViewGroup, @LayoutRes layout: Int = R.layout.list_item_video) : NMViewHolder(adapter, parent, layout) {

        var ivVideoAvatar: SimpleDraweeView? = null
        lateinit var tvTitle: TextView
        lateinit var tvArtist: TextView
        lateinit var tvTime: TextView
        lateinit var tvDuration: TextView

        override fun initUI() {
            ivVideoAvatar = itemView.findViewById(R.id.iv_item_video_thumbnail) as SimpleDraweeView
            tvTitle = itemView.findViewById(R.id.tv_item_video_title) as TextView
            tvArtist = itemView.findViewById(R.id.tv_item_video_artist) as TextView
            tvTime = itemView.findViewById(R.id.tv_item_video_time) as TextView
            tvDuration = itemView.findViewById(R.id.tv_item_video_duration) as TextView
            itemView.setOnClickListener {
                if (onItemClickListener != null) {
                    onItemClickListener!!.onItemClick(this@VideoVH)
                }
            }
        }

        fun setData(video: Video) {
            if (ivVideoAvatar != null) {
                val size = context.resources.getDimensionPixelSize(R.dimen.item_video_avatar_width)
                if (video.image != null) {
                    val uri = Uri.parse(video.image)
                    AppUtils.load(ivVideoAvatar!!, uri, size, size / 16 * 9)
                }
            }

            tvTitle.text = video.title
            tvArtist.text = video.artist

            tvTime.text = Video.getDurationTime(video)
            tvTime.tag = video.id
            if (tvTime.text.toString().trim { it <= ' ' }.toLowerCase() == "0 seconds ago") {
                tvTime.visibility = View.INVISIBLE
            }
            val duration = hsDuration[video.id]
            if (!TextUtils.isEmpty(duration)) {
                tvDuration.visibility = View.VISIBLE
                tvDuration.text = duration
            } else {
                var task: GetDurationTask? = hsDurationTasks[video.id]
                if (task == null) {
                    task = GetDurationTask(video.id.toString())
                    task.executeOnExecutor(executor, video.id)
                    hsDurationTasks.put(video.id.toString(), task)
                }
            }
            tvDuration.tag = video.id
        }

        inner class GetDurationTask internal constructor(private val videoId: String) : AsyncTask<String, Void, String>() {

            override fun doInBackground(vararg strings: String): String? {
                var url = String.format(YEngine.YOUTUBE_DURATION, strings[0])
                url = url.replace(" +".toRegex(), "%2c")
                val okHttpClient = OkHttpClient()
                val request = Request.Builder().url(url).get().build()
                val call = okHttpClient.newCall(request)
                try {
                    val response = call.execute()
                    if (response.isSuccessful) {
                        return parseResponse(response.body().string())
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return null
            }

            override fun onPostExecute(duration: String?) {
                super.onPostExecute(duration)
                if (duration != null) {
                    val format = ISOPeriodFormat.standard()
                    val period = format.parsePeriod(duration)

                    val formatter = PeriodFormatterBuilder()
                            .appendHours().appendSuffix(":").minimumPrintedDigits(2)
                            .printZeroAlways().appendMinutes().appendSuffix(":").minimumPrintedDigits(2)
                            .printZeroAlways().appendSeconds().minimumPrintedDigits(2)
                            .toFormatter()
                    val elapsed = formatter.print(period)
                    if (tvDuration.tag == videoId) {
                        tvDuration.visibility = View.VISIBLE
                        tvDuration.text = elapsed
                        hsDuration.put(videoId, elapsed)
                    }
                }
                hsDurationTasks.remove(videoId)
            }
        }

        fun parseResponse(data: String): String? {
            try {
                val root = JSONObject(data)
                val array = root.optJSONArray("items")
                for (i in 0 until array.length()) {
                    val json = array.optJSONObject(i)
                    val contentDetails = json.optJSONObject("contentDetails")
                    return contentDetails.optString("duration")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return null
        }
    }
}
