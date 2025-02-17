package com.experiment.newsapplication.ui.feature.bookmarkednews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experiment.newsapplication.data.APIResult
import com.experiment.newsapplication.data.NewsHighlight
import com.experiment.newsapplication.repository.NewHighlightRepository
import com.experiment.newsapplication.ui.feature.newshighlight.Refresh
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkedNewsViewModel @Inject constructor(private val repository: NewHighlightRepository) : ViewModel() {

    private val newsHighlight = MutableStateFlow<List<NewsHighlight>>(emptyList())
    val newsHighlightFlow: Flow<List<NewsHighlight>> = newsHighlight

    private val newsResult = MutableStateFlow<APIResult<List<NewsHighlight>>>(APIResult.Loading())
    val newsResultFlow = newsResult.asStateFlow()
    fun getNewsHighlight(refresh: Refresh) {
        viewModelScope.launch {
            newsHighlight.value = repository.getNewsResponse()
        }
        viewModelScope.launch {
             repository.getNewsHighlight(refresh).collectLatest {
                 newsResult.value = it
            }
        }
    }
}