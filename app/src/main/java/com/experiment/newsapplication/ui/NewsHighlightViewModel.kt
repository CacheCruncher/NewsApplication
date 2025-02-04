package com.experiment.newsapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experiment.newsapplication.data.NewsHighlight
import com.experiment.newsapplication.repository.NewHighlightRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsHighlightViewModel(private val repository: NewHighlightRepository) : ViewModel() {
    private val newsHighlight = MutableStateFlow<List<NewsHighlight>>(emptyList())
    val newsHighlightFlow = newsHighlight
    fun getNewsHighlight() {
        viewModelScope.launch {
            newsHighlight.value = repository.getNewsResponse()
        }
    }
}