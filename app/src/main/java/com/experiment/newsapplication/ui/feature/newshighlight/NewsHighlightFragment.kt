package com.experiment.newsapplication.ui.feature.newshighlight

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.experiment.newsapplication.R
import com.experiment.newsapplication.data.APIResult
import com.experiment.newsapplication.databinding.FragmentNewsHighlightBinding
import com.experiment.newsapplication.ui.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsHighlightFragment : Fragment(R.layout.fragment_news_highlight) {
    private val viewModel: NewsHighlightViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNewsHighlightBinding.bind(view)
        val newsAdapter = NewsAdapter()

        binding.apply {
            newsHighlightRv.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator?.changeDuration = 0
            }


            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.newsHighlight.collectIndexed { index, newsResult ->
                        Log.d("newsfragment ", "onViewCreated:  index : $index, data : ${newsResult.data?.size}, state : is_loading : ${newsResult is APIResult.Loading}, is_error:${newsResult is APIResult.Error}, is_success: ${newsResult is APIResult.Success}")

                        when(newsResult){
                            is APIResult.Loading -> {
                                swipeRefreshLayout.isRefreshing = true
                                retryButton.isVisible = false
                                errorTv.isVisible = false
                                newsResult.data?.let {
                                    newsAdapter.submitList(newsResult.data)
                                }
                            }
                            is APIResult.Success -> {
                                swipeRefreshLayout.isRefreshing = false
                                retryButton.isVisible = false
                                errorTv.isVisible = false
                                newsAdapter.submitList(newsResult.data)
                            }
                            is APIResult.Error -> {
                                swipeRefreshLayout.isRefreshing = false
                                retryButton.isVisible = true
                                errorTv.isVisible = true
                            }
                        }

                    }
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.onRefresh(Refresh.MANUAL)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onRefresh(Refresh.AUTOMATIC)
    }

}