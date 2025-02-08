package com.experiment.newsapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experiment.newsapplication.data.NewsHighlight
import com.experiment.newsapplication.repository.NewHighlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsHighlightViewModel @Inject constructor(private val repository: NewHighlightRepository) : ViewModel() {

    private val newsHighlight = MutableStateFlow<List<NewsHighlight>>(emptyList())
    val newsHighlightFlow: Flow<List<NewsHighlight>> = newsHighlight
    fun getNewsHighlight() {
        viewModelScope.launch {
            newsHighlight.value = repository.getNewsResponse()
        }
    }
}