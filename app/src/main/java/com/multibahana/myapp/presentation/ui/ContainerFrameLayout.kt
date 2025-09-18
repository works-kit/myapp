package com.multibahana.youapp.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class ContainerFrameLayout  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}