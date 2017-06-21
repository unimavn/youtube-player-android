package com.example.unima_l003.demoyoutubeplayer.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * VideoDownloader
 * Created by jack on 6/7/2016.
 * Copyright 2016 Nikmesoft Company, Ltd. All rights reserved.
 */
public class AppUtils {

    public static void load(@NonNull final SimpleDraweeView draweeView, final Uri uri, final int width, final int height) {
        if (uri == null) {
            draweeView.setImageURI(null);
            return;
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(request)
                .build();
        draweeView.setController(controller);
    }

    public static Activity getActivityFromContext(Context context) {
        if (context == null)
            return null;
        else if (context instanceof Activity)
            return (Activity) context;
        else if (context instanceof ContextWrapper)
            return getActivityFromContext(((ContextWrapper) context).getBaseContext());

        return null;
    }
}
