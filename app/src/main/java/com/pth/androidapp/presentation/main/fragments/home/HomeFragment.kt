package com.pth.androidapp.presentation.main.fragments.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.pth.androidapp.core.base.fragments.BaseFragment
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.databinding.FragmentHomeBinding
import com.pth.androidapp.presentation.main.fragments.home.adapter.ImageSlideAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var handler: Handler
    private lateinit var slideAdapter: ImageSlideAdapter

    companion object {
        private const val AUTO_SLIDE_INTERVAL = 2500L
        private const val INITIAL_POSITION = Int.MAX_VALUE / 2
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupViewPager()
        observeImageSlides()
    }


    private fun setupViewPager() {
        handler = Handler(Looper.getMainLooper())
        slideAdapter = ImageSlideAdapter()
        binding.vpSlideHome.apply {
            adapter = slideAdapter
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            (getChildAt(0) as? RecyclerView)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        setUpTransformer()
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.14f
                page.scaleX = 0.85f + r * 0.3f
            }
        }
        binding.vpSlideHome.setPageTransformer(transformer)
    }

    private fun observeImageSlides() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.imageSlides.collect { uiState ->
                binding.uiState = uiState
                if (uiState is UiState.Success) {
                    slideAdapter.setData(uiState.data)
                    if (uiState.data.isNotEmpty()) {
                        binding.vpSlideHome.setCurrentItem(INITIAL_POSITION, false)
                        registerAutoSlide()
                    }
                }
            }
        }
    }

    private fun registerAutoSlide() {
        if (slideAdapter.itemCount > 0) {
            binding.vpSlideHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handler.removeCallbacks(autoSlideRunnable)
                    handler.postDelayed(autoSlideRunnable, AUTO_SLIDE_INTERVAL)
                }
            })
        }
    }

    private val autoSlideRunnable = Runnable {
        if (slideAdapter.itemCount > 0) {
            val nextItem = binding.vpSlideHome.currentItem + 1
            binding.vpSlideHome.setCurrentItem(nextItem, true)
        }
    }

    override fun onPause() {
        super.onPause()
        if (::handler.isInitialized) {
            handler.removeCallbacks(autoSlideRunnable)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::handler.isInitialized) {
            handler.postDelayed(autoSlideRunnable, AUTO_SLIDE_INTERVAL)
        }
    }
}
