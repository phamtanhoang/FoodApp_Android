package com.pth.androidapp.core.base.dialogs

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.color.MaterialColors
import com.pth.androidapp.R
import com.pth.androidapp.databinding.DialogNotifyBinding
import androidx.core.graphics.drawable.toDrawable

enum class NotifyType {
    SUCCESS, WARNING, ERROR, INFO
}

@Suppress("DEPRECATION")
class NotifyDialogFragment : DialogFragment() {
    companion object {
        private const val ARG_TYPE = "arg_type"
        private const val ARG_MESSAGE = "arg_message"
        private const val ARG_BUTTON_TEXT = "arg_button_text"

        const val TAG = "NotifyDialogFragment"
    }

    private var _binding: DialogNotifyBinding? = null
    private val binding get() = _binding!!

    private var onOkClick: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNotifyBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            resources.displayMetrics.widthPixels - 2 * (16 * resources.displayMetrics.density).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        val type = arguments?.getSerializable(ARG_TYPE) as? NotifyType ?: NotifyType.INFO
        val message = arguments?.getString(ARG_MESSAGE)
        val buttonText = arguments?.getString(ARG_BUTTON_TEXT)

        binding.tvContent.text = message
        binding.btnOK.text = buttonText ?: getString(R.string.ok)

        when (type) {
            NotifyType.SUCCESS -> {
                binding.tvTitle.text = getString(R.string.success)
                binding.ivIcon.setImageResource(R.drawable.ic_success)
            }
            NotifyType.WARNING -> {
                binding.tvTitle.text = getString(R.string.warning)
                binding.ivIcon.setImageResource(R.drawable.ic_warning)
            }
            NotifyType.ERROR -> {
                binding.tvTitle.text = getString(R.string.error)
                binding.ivIcon.setImageResource(R.drawable.ic_error)
            }
            NotifyType.INFO -> {
                binding.tvTitle.text = getString(R.string.information)
                binding.ivIcon.setImageResource(R.drawable.ic_info)
            }
        }
    }

    private fun setupListeners() {
        binding.btnOK.setOnClickListener {
            onOkClick.invoke()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @ColorInt
    private fun getThemeColor(@AttrRes attrResId: Int): Int {
        return MaterialColors.getColor(requireContext(), attrResId, Color.BLACK)
    }

    class Builder(private val context: Context) {
        private var type: NotifyType = NotifyType.INFO
        private var message: String = ""
        private var buttonText: String? = null
        private var onOk: () -> Unit = {}

        fun type(type: NotifyType) = apply { this.type = type }
        fun message(message: String) = apply { this.message = message }
        fun buttonText(text: String) = apply { this.buttonText = text }
        fun onOk(action: () -> Unit) = apply { this.onOk = action }

        fun build(): NotifyDialogFragment {
            val fragment = NotifyDialogFragment()
            fragment.arguments = bundleOf(
                ARG_TYPE to type,
                ARG_MESSAGE to message,
                ARG_BUTTON_TEXT to buttonText
            )
            fragment.onOkClick = onOk
            return fragment
        }
    }

}