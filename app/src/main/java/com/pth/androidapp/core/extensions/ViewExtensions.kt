package com.pth.androidapp.core.extensions

import android.view.View
import androidx.core.view.isVisible

private const val DEFAULT_ANIMATION_DURATION = 300L

fun View.show(duration: Long = DEFAULT_ANIMATION_DURATION) {
    if (!isVisible) {
        isVisible = true
        alpha = 0f
        animate()
            .setDuration(duration)
            .alpha(1f)
            .withStartAction { isVisible = true }
            .start()
    }
}

fun View.hide(duration: Long = DEFAULT_ANIMATION_DURATION) {
    if (isVisible) {
        animate()
            .setDuration(duration)
            .alpha(0f)
            .withEndAction { isVisible = false }
            .start()
    }
}

fun View.toggle(duration: Long = DEFAULT_ANIMATION_DURATION) {
    if (isVisible) hide(duration) else show(duration)
}

fun View.setOnClickListenerWithDebounce(
    debounceTime: Long = 500L,
    onClick: (View) -> Unit
) {
    var lastClickTime = 0L

    setOnClickListener { view ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= debounceTime) {
            lastClickTime = currentTime
            onClick(view)
        }
    }
}

fun View.setEnabledWithFeedback(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1.0f else 0.5f
}