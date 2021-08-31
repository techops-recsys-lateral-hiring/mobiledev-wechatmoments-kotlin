package com.tws.moments.adapters

import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tws.moments.R
import com.tws.moments.api.entry.CommentsBean
import com.tws.moments.utils.inflate

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentHolder>() {

    var comments: List<CommentsBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        return CommentHolder(parent.inflate(R.layout.item_comment))
    }

    override fun getItemCount(): Int {
        return comments?.size ?: 0
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.bind(comments!![position])
    }

    inner class CommentHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val commentTV: TextView = itemView.findViewById(R.id.tv_simple_comment)

        init {
            commentTV.movementMethod = LinkMovementMethod.getInstance()
        }

        fun bind(commentsBean: CommentsBean) {
//            val spannableString = commentsBean.sender?.nick?.clickableSpan {
//                Toast.makeText(it.context, "${commentsBean.sender?.nick} info.", Toast.LENGTH_SHORT).show()
//            }
//            commentTV.text = spannableString
//            commentTV.append(":" + (commentsBean.content ?: ""))
        }
    }
}
