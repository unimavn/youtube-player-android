package com.example.unima_l003.demoyoutubeplayer.managers

import android.os.Handler
import android.os.Looper

/**
 * Albameet-Androidm
 * Created by vinhn_000 on 6/11/2015.
 * Copyright 2015 Nikmesoft Company, Ltd. All rights reserved.
 */
object ThreadManager {

    fun runInMainThread(runnable: () -> Unit) {
        Handler(Looper.getMainLooper()).post(runnable)
    }
}
