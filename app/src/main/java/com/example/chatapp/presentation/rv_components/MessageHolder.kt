package com.example.chatapp.presentation.rv_components

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chatapp.R
import com.google.android.material.textview.MaterialTextView

class MessageHolder(inflater: LayoutInflater, parent: ViewGroup)
    : ViewHolder(inflater.inflate(R.layout.item_message, parent, false)) {

    fun bind(message: String, type: Int) {
        itemView.findViewById<MaterialTextView>(R.id.tvMessage).text = message
        itemView.findViewById<ImageView>(R.id.ivAddMessage).isVisible = type == MessageViewType.RARE_MESSAGE
        if(type == MessageViewType.MY_MESSAGE) {
            itemView.findViewById<LinearLayoutCompat>(R.id.messageLl).gravity = Gravity.END
        }
    }
}