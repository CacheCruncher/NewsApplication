package com.experiment.newsapplication.ui.feature.bookmarkednews

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
import com.experiment.newsapplication.data.APIResult
import com.experiment.newsapplication.databinding.FragmentBookmarkedNewsBinding
import com.experiment.newsapplication.ui.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkedNewsFragment : Fragment(R.layout.fragment_bookmarked_news) {
    private val viewModel: BookmarkedNewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBookmarkedNewsBinding.bind(view)
        val newsAdapter = NewsAdapter()

        binding.bookmarkedNewsRv.apply {
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
                        }

                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.newsResultFlow.collectIndexed { index, value ->
                        Log.d("newsfragment ", "onViewCreated:  index : $index, data : ${value.data?.size}")

                        when(value){
                            is APIResult.Loading -> {

                            }
                            is APIResult.Success -> {

                            }
                            is APIResult.Error -> {


                            }
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