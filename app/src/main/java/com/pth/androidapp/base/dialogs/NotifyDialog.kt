package com.pth.androidapp.base.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.pth.androidapp.databinding.DialogNotifyBinding

enum class NotifyType {
    SUCCESS,
    WARNING,
    ERROR,
    INFO
}

class NotifyDialog (
    context: Context,
    private val type: NotifyType = NotifyType.INFO,
    private val title: String,
    private val message: String,
    private val textButton: String? = null,
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
            tvTitle.text = title
            tvContent.text = message
            textButton?.let {
                btnOK.text = textButton
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            btnOK.setOnClickListener {
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