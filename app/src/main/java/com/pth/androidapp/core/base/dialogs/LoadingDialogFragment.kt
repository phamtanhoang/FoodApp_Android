package com.pth.androidapp.core.base.dialogs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pth.androidapp.R
import com.pth.androidapp.databinding.DialogLoadingBinding
import androidx.core.graphics.drawable.toDrawable

class LoadingDialogFragment : DialogFragment() {
    private var _binding: DialogLoadingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLoadingBinding.inflate(inflater, container, false)
        // Đảm bảo nền của cửa sổ dialog trong suốt
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog?.window?.setDimAmount(0f) // Loại bỏ lớp nền mờ phía sau
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        val message = arguments?.getString(ARG_MESSAGE)

        // Hiển thị message nếu có, nếu không thì dùng string mặc định
        binding.tvLoadingMessage.text = message ?: getString(R.string.loading)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LoadingDialogFragment"
        private const val ARG_MESSAGE = "arg_message"

        @Volatile
        private var instance: LoadingDialogFragment? = null

        fun show(fragmentManager: FragmentManager, message: String? = null) {
            // Chỉ tạo và hiển thị nếu chưa có instance nào
            if (instance == null) {
                // Sử dụng synchronized để đảm bảo an toàn trên nhiều luồng
                synchronized(this) {
                    if (instance == null) {
                        if (fragmentManager.isStateSaved) return // Không hiển thị nếu state đã được lưu

                        instance = LoadingDialogFragment().apply {
                            arguments = bundleOf(ARG_MESSAGE to message)
                        }
                        try {
                            instance?.show(fragmentManager, TAG)
                        } catch (e: IllegalStateException) {
                            // Bỏ qua lỗi nếu không thể thêm fragment
                        }
                    }
                }
            }
        }

        fun hide() {
            try {
                instance?.dismissAllowingStateLoss()
            } catch (e: Exception) {
                // Bỏ qua lỗi
            } finally {
                instance = null
            }
        }
    }
}