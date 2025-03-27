package com.pth.androidapp.base.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.pth.androidapp.R
import com.pth.androidapp.databinding.DialogNotifyBinding

enum class NotifyType {
    SUCCESS,
    WARNING,
    ERROR,
    INFO
}

class NotifyDialog(
    context: Context,
    private val type: NotifyType = NotifyType.INFO,
    private val message: String,
    private val textButton: String? = null,
    private val callback: () -> Unit = {}
) : Dialog(context) {
    private var _binding: DialogNotifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DialogNotifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        binding.apply {
            tvContent.text = message
            btnOK.text = textButton ?: context.getString(R.string.ok)
            when (type) {
                NotifyType.SUCCESS -> tvTitle.text = context.getString(R.string.success)
                NotifyType.WARNING -> tvTitle.text = context.getString(R.string.warning)
                NotifyType.ERROR -> tvTitle.text = context.getString(R.string.error)
                NotifyType.INFO -> tvTitle.text = context.getString(R.string.infomation)
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            btnOK.setOnClickListener {
                callback()
                dismiss()
            }
        }
        setContentView(binding.root)
    }

    override fun dismiss() {
        super.dismiss()
        _binding = null
    }

}