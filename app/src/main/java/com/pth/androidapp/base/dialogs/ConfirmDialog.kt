package com.pth.androidapp.base.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.pth.androidapp.R
import com.pth.androidapp.databinding.DialogConfirmBinding

class ConfirmDialog(
    context: Context,
    private val title: String,
    private val message: String? = null,
    private val positiveButtonTitle: String = context.getString(R.string.ok),
    private val negativeButtonTitle: String = context.getString(R.string.cancel),
    private val callbackNegative: () -> Unit = {},
    private val callbackPositive: () -> Unit = {},
) : Dialog(context) {

    init {
        setCancelable(false)
    }

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
            if (message != null) {
                tvContent.visibility = View.VISIBLE
                tvContent.text = message
            } else {
                tvContent.visibility = View.GONE
            }
            btnNegative.text = negativeButtonTitle
            btnPositive.text = positiveButtonTitle
        }
    }

    private fun setupListeners() {
        binding.apply {
            btnNegative.setOnClickListener {
                callbackNegative()
                dismiss()
            }
            btnPositive.setOnClickListener {
                callbackPositive()
                dismiss()
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        _binding = null
    }
}