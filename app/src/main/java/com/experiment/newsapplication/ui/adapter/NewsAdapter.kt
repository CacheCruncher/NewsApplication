package com.experiment.newsapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.experiment.newsapplication.R
import com.experiment.newsapplication.data.NewsHighlight
import com.experiment.newsapplication.databinding.ItemNewsArticleBinding

class NewsAdapter : ListAdapter<NewsHighlight, NewsViewHolder>(NewsComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            ItemNewsArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}


class NewsViewHolder(
    private val itemViewBinding: ItemNewsArticleBinding
) : RecyclerView.ViewHolder(itemViewBinding.root) {

    fun bind(newsHighlight: NewsHighlight) {
        itemViewBinding.apply {
            newsHighlightTitleTv.text = newsHighlight.title ?: ""
            val icon =
                if (newsHighlight.isBookMark) R.drawable.bookmark_icon else R.drawable.unbookmarked_icon
            newsBookmarkButton.setImageResource(icon)
            Glide.with(itemView)
                .load(newsHighlight.imageUrl)
                .error(R.drawable.image_placeholder)
                .into(newsHighlightImage)

        }
    }
}

class NewsComparator : DiffUtil.ItemCallback<NewsHighlight>() {
    override fun areItemsTheSame(oldItem: NewsHighlight, newItem: NewsHighlight): Boolean {
        return oldItem.newsUrl == newItem.newsUrl
    }

    override fun areContentsTheSame(oldItem: NewsHighlight, newItem: NewsHighlight): Boolean {
        return oldItem == newItem
    }
}