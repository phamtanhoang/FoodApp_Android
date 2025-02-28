package com.pth.androidapp.ui.main.fragments.dineout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pth.androidapp.base.fragments.BaseFragment
import com.pth.androidapp.databinding.FragmentDineoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DineoutFragment : BaseFragment() {
    private var _binding: FragmentDineoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDineoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}