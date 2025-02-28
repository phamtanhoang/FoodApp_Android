package com.pth.androidapp.base.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.pth.androidapp.databinding.DialogConfirmBinding

class ConfirmDialog(
    context: Context,
    private val title: String,
    private val message: String?,
    private val positiveButtonTitle: String,
    private val negativeButtonTitle: String,
    private val callback: ConfirmCallback?,
) : Dialog(context) {

    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DialogConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        binding.apply {
            tvTitle.text = title
            message?.let {
                tvContent.visibility = View.VISIBLE
                tvContent.text = message
            }
            btnNegative.text = negativeButtonTitle
            btnPositive.text = positiveButtonTitle
        }
    }

    private fun setupListeners() {
        binding.apply {
            btnNegative.setOnClickListener {
                callback?.negativeAction()
                dismiss()
            }
            btnPositive.setOnClickListener {
                callback?.positiveAction()
                dismiss()

            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        _binding = null
    }

    interface ConfirmCallback {
        fun negativeAction()
        fun positiveAction()
    }
}