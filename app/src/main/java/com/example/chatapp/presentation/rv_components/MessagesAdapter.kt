package com.example.chatapp.presentation.rv_components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.chatapp.R
import com.example.chatapp.domain.model.Message


class MessagesAdapter(
    private val inflater: LayoutInflater,
) : Adapter<MessageHolder>() {

    private var currentList: List<Message> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder =
        MessageHolder(inflater, parent)


    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bind(currentList[position].content, getItemViewType(position))
        animatePlacement(holder.itemView, position)
    }

    private fun animatePlacement(viewToAnimate: View, position: Int) {
        if (position > currentList.lastIndex-1) {
            val animation: Animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_in_right)
            viewToAnimate.startAnimation(animation)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when(currentList[position]) {
            is Message.CommonMessage -> MessageViewType.COMMON_MESSAGE
            is Message.RareMessage -> MessageViewType.RARE_MESSAGE
            is Message.MyMessage -> MessageViewType.MY_MESSAGE
        }

    override fun getItemCount(): Int =
        currentList.size

    fun submitList(list: List<Message>) {
        val oldSize = currentList.size
        currentList = list
        notifyItemRangeChanged(oldSize, list.size-oldSize)
    }
}