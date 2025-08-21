package com.pth.androidapp.ui.main.fragments.instamart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pth.androidapp.base.fragments.BaseFragment
import com.pth.androidapp.databinding.FragmentInstamartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstamartFragment : BaseFragment() {
    private var _binding: FragmentInstamartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInstamartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}