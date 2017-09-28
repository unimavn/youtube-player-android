package com.example.unima_l003.demoyoutubeplayer.Utils

import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder

/**
 * VideoDownloader
 * Created by jack on 6/7/2016.
 * Copyright 2016 Nikmesoft Company, Ltd. All rights reserved.
 */
object AppUtils {

    fun load(draweeView: SimpleDraweeView, uri: Uri?, width: Int, height: Int) {
        if (uri == null) {
//            draweeView.setImageURI(null)
            return
        }
        val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(ResizeOptions(width, height))
                .build()

        val controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.controller)
                .setImageRequest(request)
                .build() as PipelineDraweeController
        draweeView.controller = controller
    }

}
