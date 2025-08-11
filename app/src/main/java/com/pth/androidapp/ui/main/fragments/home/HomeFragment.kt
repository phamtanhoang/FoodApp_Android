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

@Suppress("DEPRECATION")
@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var slideAdapter: ImageSlideAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager2 = binding.vpSlideHome
        handler = Handler(Looper.getMainLooper())

        // Initialize adapter with empty list
        slideAdapter = ImageSlideAdapter(emptyList(), viewPager2)
        viewPager2.adapter = slideAdapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        setUpTransformer()
        registerAutoSlide()
        collectSlides()
    }


    private fun collectSlides() {
        lifecycleScope.launchWhenStarted {
            viewModel.imageSlideState.collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        result.data?.let { slides ->
                            slideAdapter.setData(slides)
                        }
                    }

                    is NetworkResult.Loading -> {

                    }

                    is NetworkResult.Error -> {

                    }

                    else -> Unit
                }
            }
        }
    }

    private fun registerAutoSlide() {
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(autoSlideRunnable)
                handler.postDelayed(autoSlideRunnable, 2500)
            }
        })
    }

    private val autoSlideRunnable = Runnable {
        viewPager2.currentItem =
            (viewPager2.currentItem + 1).coerceAtMost(slideAdapter.itemCount - 1)
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.14f
            page.scaleX = 0.85f + r * 0.3f
        }
        viewPager2.setPageTransformer(transformer)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoSlideRunnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(autoSlideRunnable, 2500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
