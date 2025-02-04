package com.experiment.newsapplication.api

data class NewsResponse(val newsHiglights: List<NewsHighlightDTO>)
data class NewsHighlightDTO(
    val title: String?,
    val url: String,
    val urlToImage: String?
)