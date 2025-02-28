package com.pth.androidapp.ui.main.fragments.genie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pth.androidapp.base.fragments.BaseFragment
import com.pth.androidapp.databinding.FragmentGenieBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenieFragment : BaseFragment() {
    private var _binding: FragmentGenieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGenieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}