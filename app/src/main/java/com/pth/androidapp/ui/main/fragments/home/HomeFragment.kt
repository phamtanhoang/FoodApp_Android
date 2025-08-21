package com.pth.androidapp.ui.main.fragments.home

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
import com.pth.androidapp.base.fragments.BaseFragment
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.databinding.FragmentHomeBinding
import com.pth.androidapp.ui.main.fragments.home.adapter.ImageSlideAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@Suppress("DEPRECATION")
@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var handler: Handler
    private lateinit var slideAdapter: ImageSlideAdapter

    companion object {
        private const val AUTO_SLIDE_INTERVAL = 2500L
        private const val INITIAL_POSITION = Int.MAX_VALUE / 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        collectSlides()
        registerAutoSlide()
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
                val r = 1 - kotlin.math.abs(position)
                page.scaleY = 0.85f + r * 0.14f
                page.scaleX = 0.85f + r * 0.3f
            }
        }
        binding.vpSlideHome.setPageTransformer(transformer)
    }

    private fun collectSlides() {

        lifecycleScope.launchWhenStarted {
            viewModel.imageSlideState.collectLatest { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        binding.vpSlideHome.visibility = View.VISIBLE
                        binding.sliderError.root.visibility = View.GONE
                        binding.sliderLoading.visibility = View.GONE
                        slideAdapter.setData(result.data)
                        if (result.data.isNotEmpty()) {
                            binding.vpSlideHome.setCurrentItem(INITIAL_POSITION, false)
                        }
                    }
                    is NetworkResult.Error -> {
                        binding.vpSlideHome.visibility = View.GONE
                        binding.sliderError.root.visibility = View.VISIBLE
                        binding.sliderLoading.visibility = View.GONE
                    }
                    else -> {
                        binding.vpSlideHome.visibility = View.GONE
                        binding.sliderError.root.visibility = View.GONE
                        binding.sliderLoading.visibility = View.VISIBLE

                    }
                }
            }
        }
    }

    private fun registerAutoSlide() {
        binding.vpSlideHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(autoSlideRunnable)
                handler.postDelayed(autoSlideRunnable, AUTO_SLIDE_INTERVAL)
            }
        })
    }

    private val autoSlideRunnable = Runnable {
        // Infinite loop: reset to initial position if at end
        val itemCount = slideAdapter.itemCount
        if (itemCount > 0) {
            val nextItem = binding.vpSlideHome.currentItem + 1
            binding.vpSlideHome.setCurrentItem(nextItem, true)
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoSlideRunnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(autoSlideRunnable, AUTO_SLIDE_INTERVAL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
