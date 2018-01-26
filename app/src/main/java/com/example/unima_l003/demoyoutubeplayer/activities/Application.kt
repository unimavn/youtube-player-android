package com.example.unima_l003.demoyoutubeplayer.activities

import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig

/**
 * Odyssey
 * Created by jack on 11/1/2016.
 * Copyright 2016 Nikmesoft Company, Ltd. All rights reserved.
 */
class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        val config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build()
        Fresco.initialize(this, config)
    }
}
