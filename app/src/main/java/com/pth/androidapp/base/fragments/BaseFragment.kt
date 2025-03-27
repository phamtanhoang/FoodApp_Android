package com.pth.androidapp.base.fragments

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pth.androidapp.base.activities.BaseActivity
import com.pth.androidapp.base.dialogs.NotifyType

open class BaseFragment : Fragment() {
    protected fun navigateToPage(
        actionId: Int
    ) {
        findNavController().navigate(actionId)
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