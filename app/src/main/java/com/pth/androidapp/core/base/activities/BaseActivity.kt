package com.pth.androidapp.core.base.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.pth.androidapp.R
import com.pth.androidapp.core.base.dialogs.ConfirmDialogFragment
import com.pth.androidapp.core.base.dialogs.LoadingDialogFragment
import com.pth.androidapp.core.base.dialogs.NotifyDialogFragment
import com.pth.androidapp.core.base.dialogs.NotifyType
import com.pth.androidapp.core.common.LanguageManager

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB
        private set

    abstract fun inflateBinding(inflater: LayoutInflater): VB

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        val languageManager = LanguageManager(this)
        languageManager.setLanguage(languageManager.getUserSelectedLanguageCode())

        super.onCreate(savedInstanceState)

        binding = inflateBinding(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
    }

    open fun restart() {
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    open fun setupWindowInsets() {
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
        finishCurrent: Boolean = false
    ) {
        val intent = Intent(this, activityClass)
        extras?.let { intent.putExtras(it) }
        flags?.let { intent.flags = it }
        startActivity(intent)
        if (finishCurrent) {
            finish()
        }
    }

    open fun launchActivityForResult(
        activityClass: Class<out Activity>,
        extras: Bundle? = null,
        flags: Int? = null
    ) {
        val intent = Intent(this, activityClass)
        extras?.let { intent.putExtras(it) }
        flags?.let { intent.flags = it }
        activityResultLauncher.launch(intent)
    }

    protected open fun handleActivityResult(result: ActivityResult) {

    }

    open fun showLoading(isShow: Boolean, message: String? = null) {
        if (isShow) {
            LoadingDialogFragment.show(supportFragmentManager, message)
        } else {
            LoadingDialogFragment.hide()
        }
    }

    open fun showNotifyDialog(
        type: NotifyType,
        message: String,
        textButton: String? = null,
        callback: () -> Unit = {}
    ) {
        NotifyDialogFragment.Builder(this)
            .type(type)
            .message(message)
            .buttonText(textButton ?: getString(R.string.ok))
            .onOk(callback)
            .build()
            .show(supportFragmentManager, NotifyDialogFragment.TAG)
    }

    open fun showConfirmDialog(
        title: String,
        message: String?,
        positiveButtonTitle: String? = null,
        negativeButtonTitle: String? = null,
        callbackNegative: () -> Unit = {},
        callbackPositive: () -> Unit = {}
    ) {
        val builder = ConfirmDialogFragment.Builder(this)
            .title(title)
            .onNegative(callbackNegative)
            .onPositive(callbackPositive)

        message?.let { builder.message(it) }
        positiveButtonTitle?.let { builder.positiveButtonTitle(it) }
        negativeButtonTitle?.let { builder.negativeButtonTitle(it) }

        builder.build().show(supportFragmentManager, ConfirmDialogFragment.TAG)
    }
}