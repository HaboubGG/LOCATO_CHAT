package com.example.easychat.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easychat.R
import com.example.easychat.adapter.ChatRecyclerAdapter.ChatModelViewHolder
import com.example.easychat.model.ChatMessageModel
import com.example.easychat.utils.FirebaseUtil
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatRecyclerAdapter(
    options: FirestoreRecyclerOptions<ChatMessageModel?>,
    var context: Context
) : FirestoreRecyclerAdapter<ChatMessageModel, ChatModelViewHolder>(options) {
    override fun onBindViewHolder(
        holder: ChatModelViewHolder,
        position: Int,
        model: ChatMessageModel
    ) {
        Log.i("haushd", "asjd")
        if (model.senderId == FirebaseUtil.currentUserId()) {
            holder.leftChatLayout.visibility = View.GONE
            holder.rightChatLayout.visibility = View.VISIBLE
            holder.rightChatTextview.text = model.message
        } else {
            holder.rightChatLayout.visibility = View.GONE
            holder.leftChatLayout.visibility = View.VISIBLE
            holder.leftChatTextview.text = model.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatModelViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false)
        return ChatModelViewHolder(view)
    }

    inner class ChatModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var leftChatLayout: LinearLayout
        var rightChatLayout: LinearLayout
        var leftChatTextview: TextView
        var rightChatTextview: TextView

        init {
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout)
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout)
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview)
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview)
        }
    }
}