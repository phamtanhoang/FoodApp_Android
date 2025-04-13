package com.pth.androidapp.base.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
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
import com.pth.androidapp.common.LanguageManager
import java.util.Locale
import kotlin.text.compareTo


@Suppress("DEPRECATION")
open class BaseActivity : AppCompatActivity() {
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyLanguage()
    }

    override fun attachBaseContext(base: Context) {
        val newContext = applyLocale(base)

        super.attachBaseContext(newContext)
    }

    private fun applyLanguage() {
        val languageManager = LanguageManager(this)
        languageManager.setLanguage(languageManager.getCurrentLanguageCode())
    }

    private fun applyLocale(base: Context): Context {
        val languageManager = LanguageManager(base)
        val locale = Locale(languageManager.getCurrentLanguageCode())
        val configuration = Configuration(base.resources.configuration).apply { setLocale(locale) }
        return base.createConfigurationContext(configuration)
    }

    open fun restart() {
        val intent = intent
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    open fun setupWindowInsets(binding: ViewBinding) {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    open fun navigateToActivity(
        activityClass: Class<out Activity>,
        extras: Bundle? = null,
        flags: Int? = null,
        finishCurrent: Boolean = true
    ) {
        val intent = Intent(this, activityClass)
        extras?.let { intent.putExtras(it) }
        flags?.let { intent.flags = it }
        startActivity(intent)
        if (finishCurrent) {
            finish()
        }
    }

    /**
     * Launch an Activity with the returned result
     */
    open fun navigateToActivityForResult(
        activityClass: Class<out Activity>,
        requestCode: Int,
        extras: Bundle? = null,
        flags: Int? = null
    ) {
        val intent = Intent(this, activityClass)
        extras?.let { intent.putExtras(it) }
        flags?.let { intent.flags = it }
        startActivityForResult(intent, requestCode)
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