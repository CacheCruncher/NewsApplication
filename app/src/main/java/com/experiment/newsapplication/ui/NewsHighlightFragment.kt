package com.experiment.newsapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.experiment.newsapplication.R
import com.experiment.newsapplication.databinding.FragmentNewsHighlightBinding
import com.experiment.newsapplication.ui.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsHighlightFragment : Fragment(R.layout.fragment_news_highlight) {
    private val viewModel: NewsHighlightViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNewsHighlightBinding.bind(view)
        val newsAdapter = NewsAdapter()

        binding.newsHighlightRv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            //animation.duration = 0

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.newsHighlightFlow.collect{


                        if(it.isNotEmpty()) {
                            for (news in it.listIterator()){
                                Log.d("NewsAPI", "ui: ${news.title}")
                            }
                            newsAdapter.submitList(it)
                            newsAdapter.notifyDataSetChanged()
                        }

                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getNewsHighlight()
    }

}