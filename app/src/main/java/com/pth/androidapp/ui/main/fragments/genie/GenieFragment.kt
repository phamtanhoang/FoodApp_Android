package com.pth.androidapp.ui.main.fragments.genie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.pth.androidapp.base.fragments.BaseFragment
import com.pth.androidapp.databinding.FragmentGenieBinding
import com.pth.androidapp.ui.fragments.home.GenieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenieFragment : BaseFragment() {
    private var _binding: FragmentGenieBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GenieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGenieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}