package com.example.unima_l003.demoyoutubeplayer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class YoutubeFrameLayout extends FrameLayout {

    public YoutubeFrameLayout(Context context) {
        super(context);
    }

    public YoutubeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YoutubeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = width / 16 * 9;
        setMeasuredDimension(width, height);

    }
}
