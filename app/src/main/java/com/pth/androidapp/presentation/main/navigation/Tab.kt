package com.pth.androidapp.presentation.main.navigation

import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Data class representing a navigation tab in MainActivity.
 */
data class Tab(
    val container: View,
    val indicator: View,
    val icon: ImageView,
    val text: TextView,
    val fragmentId: Int,
    val alwaysOrangeIcon: Boolean = false,
    val statusBarColorRes: Int,
    val isLightStatusBar: Boolean
)

