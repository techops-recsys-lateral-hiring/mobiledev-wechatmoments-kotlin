package com.tws.moments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tws.moments.api.entry.TweetBean
import com.tws.moments.api.entry.UserBean
import com.tws.moments.databinding.ItemMomentHeadBinding
import com.tws.moments.databinding.LayoutBaseTweetBinding
import com.tws.moments.viewholders.HeaderViewHolder
import com.tws.moments.viewholders.TweetViewHolder


private const val TYPE_TWEET = 1
private const val TYPE_HEAD = 1000

class MomentsAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var tweets: MutableList<TweetBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var userBean: UserBean? = null
        set(value) {
            field = value
            notifyItemChanged(0)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEAD -> HeaderViewHolder(
                ItemMomentHeadBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            TYPE_TWEET -> TweetViewHolder(
                LayoutBaseTweetBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalStateException("unknown view type $viewType")
        }
    }

    override fun getItemCount(): Int {
        return (tweets?.size ?: 0) + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEAD
        else TYPE_TWEET
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            (holder as? HeaderViewHolder)?.bind(userBean)
        } else {
            (holder as? TweetViewHolder)?.bind(tweets!![tweetIndex(position)])
        }
    }

    fun addMoreTweet(tweets: List<TweetBean>?) {
        if (tweets.isNullOrEmpty() || this.tweets.isNullOrEmpty()) {
            return
        }
        val newTweetSize = tweets.size
        val oldTweetsSize = this.tweets!!.size
        this.tweets!!.addAll(tweets)
        notifyItemRangeInserted(oldTweetsSize + 1, newTweetSize)
    }

    private fun tweetIndex(position: Int) = position - 1
}
