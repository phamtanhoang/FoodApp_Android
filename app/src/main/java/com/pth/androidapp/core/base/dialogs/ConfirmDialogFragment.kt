package com.pth.androidapp.core.base.dialogs

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.pth.androidapp.R
import com.pth.androidapp.databinding.DialogConfirmBinding
import androidx.core.graphics.drawable.toDrawable

class ConfirmDialogFragment : DialogFragment() {
    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_MESSAGE = "arg_message"
        private const val ARG_POSITIVE_TITLE = "arg_positive_title"
        private const val ARG_NEGATIVE_TITLE = "arg_negative_title"

        const val TAG = "ConfirmDialogFragment"
    }
    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    private var onPositiveClick: () -> Unit = {}
    private var onNegativeClick: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogConfirmBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        binding.apply {
            val title = arguments?.getString(ARG_TITLE)
            val message = arguments?.getString(ARG_MESSAGE)
            val positiveButtonTitle = arguments?.getString(ARG_POSITIVE_TITLE)
            val negativeButtonTitle = arguments?.getString(ARG_NEGATIVE_TITLE)

            tvTitle.text = title
            tvContent.isVisible = !message.isNullOrEmpty()
            tvContent.text = message
            btnPositive.text = positiveButtonTitle
            btnNegative.text = negativeButtonTitle
        }
    }

    private fun setupListeners() {
        binding.btnPositive.setOnClickListener {
            onPositiveClick.invoke()
            dismiss()
        }
        binding.btnNegative.setOnClickListener {
            onNegativeClick.invoke()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class Builder(private val context: Context) {
        private var title: String = ""
        private var message: String? = null
        private var positiveButtonTitle: String? = null
        private var negativeButtonTitle: String? = null
        private var onPositive: () -> Unit = {}
        private var onNegative: () -> Unit = {}

        fun title(title: String) = apply { this.title = title }
        fun message(message: String) = apply { this.message = message }
        fun positiveButtonTitle(title: String) = apply { this.positiveButtonTitle = title }
        fun negativeButtonTitle(title: String) = apply { this.negativeButtonTitle = title }
        fun onPositive(action: () -> Unit) = apply { this.onPositive = action }
        fun onNegative(action: () -> Unit) = apply { this.onNegative = action }

        fun build(): ConfirmDialogFragment {
            val fragment = ConfirmDialogFragment()
            fragment.arguments = bundleOf(
                ARG_TITLE to title,
                ARG_MESSAGE to message,
                ARG_POSITIVE_TITLE to (positiveButtonTitle ?: context.getString(R.string.ok)),
                ARG_NEGATIVE_TITLE to (negativeButtonTitle ?: context.getString(R.string.cancel))
            )
            fragment.onPositiveClick = onPositive
            fragment.onNegativeClick = onNegative
            return fragment
        }
    }
}