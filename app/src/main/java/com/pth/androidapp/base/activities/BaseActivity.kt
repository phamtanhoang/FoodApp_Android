package com.pth.androidapp.base.activities

import android.app.Activity
import android.content.Intent
import android.view.Gravity
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.pth.androidapp.base.dialogs.ConfirmDialog
import com.pth.androidapp.base.dialogs.LoadingDialog
import com.pth.androidapp.base.dialogs.NotifyDialog
import com.pth.androidapp.base.dialogs.NotifyType


open class BaseActivity : AppCompatActivity() {
    private var loadingDialog: LoadingDialog? = null

    open fun setupWindowInsets(binding: ViewBinding) {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    open fun navigateToActivity(activityClass: Class<out Activity>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish()
    }

    open fun showLoading(isShow: Boolean) {
        if (isShow) {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog(this)
            }
            loadingDialog?.show()
        } else {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }

    open fun showNotifyDialog(
        type: NotifyType,
        message: String,
        textButton: String? = null,
        callback: () -> Unit = {}
    ) {
        val notifyDialog = NotifyDialog(
            context = this,
            type,
            message,
            textButton,
            callback
        )
        notifyDialog.show()
        notifyDialog.window?.setGravity(Gravity.CENTER)
        notifyDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    open fun showConfirmDialog(
        title: String,
        message: String?,
        positiveButtonTitle: String,
        negativeButtonTitle: String,
        textButton: String?,
        callbackNegative: () -> Unit = {},
        callbackPositive: () -> Unit = {},
    ) {
        val confirmDialog = ConfirmDialog(
            context = this,
            title,
            message,
            positiveButtonTitle,
            negativeButtonTitle,
            callbackNegative,
            callbackPositive
        )
        confirmDialog.show()
        confirmDialog.window?.setGravity(Gravity.CENTER)
        confirmDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}