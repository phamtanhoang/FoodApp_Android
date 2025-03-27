package com.pth.androidapp.base.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.pth.androidapp.databinding.DialogLoadingBinding

class LoadingDialog(
    context: Context
) : Dialog(context) {
    private var _binding: DialogLoadingBinding? = null
    private val binding get() = _binding!!

    init {
        setCancelable(false)
        window?.setDimAmount(0f)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun dismiss() {
        super.dismiss()
        _binding = null
    }
}