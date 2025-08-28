package com.pth.androidapp.core.base.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.pth.androidapp.R
import com.pth.androidapp.core.base.dialogs.ConfirmDialogFragment
import com.pth.androidapp.core.base.dialogs.LoadingDialogFragment
import com.pth.androidapp.core.base.dialogs.NotifyDialogFragment
import com.pth.androidapp.core.base.dialogs.NotifyType

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun navigateToPage(actionId: Int, args: Bundle? = null) {
        findNavController().navigate(actionId, args)
    }

    protected fun navigateToPageAndClearBackStack(actionId: Int, navGraphId: Int, args: Bundle? = null) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(navGraphId, true)
            .build()
        findNavController().navigate(actionId, args, navOptions)
    }

    protected fun navigateToActivity(
        activityClass: Class<out Activity>,
        extras: Bundle? = null,
        flags: Int? = null,
        finishCurrent: Boolean = false
    ) {
        val intent = Intent(requireActivity(), activityClass)
        extras?.let { intent.putExtras(it) }
        flags?.let { intent.flags = it }
        startActivity(intent)
        if (finishCurrent) {
            requireActivity().finish()
        }
    }

    protected fun launchActivityForResult(activityClass: Class<out Activity>, extras: Bundle? = null) {
        val intent = Intent(requireActivity(), activityClass)
        extras?.let { intent.putExtras(it) }
        activityResultLauncher.launch(intent)
    }

    protected open fun handleActivityResult(result: ActivityResult) {
        // Mặc định không làm gì
    }

    protected fun navigateToLogin(loginActivityClass: Class<out Activity>) {
        val intent = Intent(requireActivity(), loginActivityClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    protected fun showLoading(isShow: Boolean, message: String? = null) {
        if (isShow) {
            LoadingDialogFragment.show(parentFragmentManager, message)
        } else {
            LoadingDialogFragment.hide()
        }
    }

    protected fun showNotifyDialog(
        type: NotifyType,
        message: String,
        textButton: String? = null,
        callback: () -> Unit = {}
    ) {
        NotifyDialogFragment.Builder(requireContext())
            .type(type)
            .message(message)
            .buttonText(textButton ?: getString(R.string.ok))
            .onOk(callback)
            .build()
            .show(parentFragmentManager, NotifyDialogFragment.TAG)
    }

    protected fun showConfirmDialog(
        title: String,
        message: String?,
        positiveButtonTitle: String? = null,
        negativeButtonTitle: String? = null,
        callbackNegative: () -> Unit = {},
        callbackPositive: () -> Unit = {}
    ) {
        val builder = ConfirmDialogFragment.Builder(requireContext())
            .title(title)
            .onNegative(callbackNegative)
            .onPositive(callbackPositive)

        message?.let { builder.message(it) }
        positiveButtonTitle?.let { builder.positiveButtonTitle(it) }
        negativeButtonTitle?.let { builder.negativeButtonTitle(it) }

        builder.build().show(parentFragmentManager, ConfirmDialogFragment.TAG)
    }
}