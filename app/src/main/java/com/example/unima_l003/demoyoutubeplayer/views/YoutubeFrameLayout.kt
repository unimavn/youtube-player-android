package com.example.unima_l003.demoyoutubeplayer.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class YoutubeFrameLayout : FrameLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = width / 16 * 9
        setMeasuredDimension(width, height)

    }
}
