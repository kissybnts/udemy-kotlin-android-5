package com.kissybnts.udemykotlinandroid5.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kissybnts.udemykotlinandroid5.R
import com.kissybnts.udemykotlinandroid5.model.Message
import com.kissybnts.udemykotlinandroid5.service.UserDataService

class MessageAdapter(private val context: Context, private val messages: ArrayList<Message>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindMessage(context, messages[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val userImage = itemView?.findViewById<ImageView>(R.id.userImageMessage)
        val userName = itemView?.findViewById<TextView>(R.id.userNameLabelMessage)
        val timestamp = itemView?.findViewById<TextView>(R.id.timestampLabelMessage)
        val messageBody = itemView?.findViewById<TextView>(R.id.messageLabelMessage)

        fun bindMessage(context: Context, message: Message) {
            val resourceId = context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
            userImage?.setImageResource(resourceId)
            userImage?.setBackgroundColor(UserDataService.getAvatarColor(message.userAvatarColor))
            userName?.text = message.userName
            timestamp?.text = message.timestamp
            messageBody?.text = message.message
        }
    }
}