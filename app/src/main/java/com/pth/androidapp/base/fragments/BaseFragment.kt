package com.pth.androidapp.base.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.pth.androidapp.base.activities.BaseActivity
import com.pth.androidapp.base.dialogs.NotifyType

@Suppress("DEPRECATION")
open class BaseFragment : Fragment() {
    /**
     * Navigate to another fragment in the same navigation graph
     */
    protected fun navigateToPage(
        actionId: Int,
        args: Bundle? = null
    ) {
        findNavController().navigate(actionId, args)
    }

    /**
     * Navigate to another fragment and clear the entire back stack
     */
    protected fun navigateToPageAndClearBackStack(
        actionId: Int,
        navGraphId: Int,
        args: Bundle? = null
    ) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(navGraphId, true)
            .build()

        findNavController().navigate(actionId, args, navOptions)
    }

    /**
     * Navigate to another fragment with NavOptions
     */
    protected fun navigateToPageWithOptions(
        actionId: Int,
        args: Bundle? = null,
        navOptions: NavOptions
    ) {
        findNavController().navigate(actionId, args, navOptions)
    }

    /**
     * Navigate to a new Activity
     */
    protected fun navigateToActivity(
        activityClass: Class<out Activity>,
        extras: Bundle? = null,
        flags: Int? = null,
        finishCurrent: Boolean = true
    ) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.navigateToActivity(activityClass, extras, flags, finishCurrent)
        } else {
            val intent = Intent(activity, activityClass)
            extras?.let { intent.putExtras(it) }
            flags?.let { intent.flags = it }
            startActivity(intent)
            if (finishCurrent) {
                activity.finish()
            }
        }
    }

    /**
     * Navigate to a new Activity and get the result back
     */
    protected fun navigateToActivityForResult(
        activityClass: Class<out Activity>,
        requestCode: Int,
        extras: Bundle? = null,
        flags: Int? = null
    ) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.navigateToActivityForResult(activityClass, requestCode, extras, flags)
        } else {
            val intent = Intent(activity, activityClass)
            extras?.let { intent.putExtras(it) }
            flags?.let { intent.flags = it }
            startActivityForResult(intent, requestCode)
        }
    }

    /**
     * Navigate to login screen and clear entire back stack
     * Useful when token expires or logs out
     */
    protected fun navigateToLogin(loginActivityClass: Class<out Activity>) {
        val intent = Intent(requireActivity(), loginActivityClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    protected fun showLoading(
        isShow: Boolean
    ) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showLoading(isShow)
        }
    }

    protected fun showNotifyDialog(
        type: NotifyType,
        message: String,
        textButton: String? = null,
        callback: () -> Unit = {}
    ) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showNotifyDialog(
                type,
                message,
                textButton,
                callback
            )
        }
    }
}