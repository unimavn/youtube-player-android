package com.example.unima_l003.demoyoutubeplayer.managers;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

/**
 * Albameet-Androidm
 * Created by vinhn_000 on 6/11/2015.
 * Copyright 2015 Nikmesoft Company, Ltd. All rights reserved.
 */
public class ThreadManager {



    public static void runInMainThread(@NonNull final Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
