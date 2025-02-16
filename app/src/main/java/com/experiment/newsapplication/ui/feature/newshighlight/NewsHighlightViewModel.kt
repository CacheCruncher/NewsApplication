package com.experiment.newsapplication.ui.feature.newshighlight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experiment.newsapplication.data.APIResult
import com.experiment.newsapplication.repository.NewHighlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsHighlightViewModel @Inject constructor(
    private val repository: NewHighlightRepository
) : ViewModel() {

    private val refreshChannel = Channel<Unit>()

    val newsHighlight = refreshChannel.receiveAsFlow().flatMapLatest {
        repository.getNewsResult()
    }.stateIn(
        viewModelScope, SharingStarted.Lazily, APIResult.Loading()
    )


    fun onRefresh() {
        viewModelScope.launch {
            refreshChannel.send(Unit)
        }
    }
}