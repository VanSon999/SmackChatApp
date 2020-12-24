package vanson.dev.smackchapapp.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import vanson.dev.smackchapapp.Model.Message
import vanson.dev.smackchapapp.R
import vanson.dev.smackchapapp.Services.UserDataService
import java.util.ArrayList

class MessageAdapter(val context: Context, val messages: ArrayList<Message>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userImage = itemView.findViewById<ImageView>(R.id.messageUserImage)
        val timeStamp = itemView.findViewById<TextView>(R.id.timeStampLabel)
        val userName = itemView.findViewById<TextView>(R.id.messageUserName)
        val messageBody = itemView.findViewById<TextView>(R.id.messageBodyLabel)

        fun bindMessage(context: Context, message: Message){
            val resourceId = context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
            userImage.setImageResource(resourceId)
            userImage.setBackgroundColor(UserDataService.returnAvatarColor(message.userAvatarColor))
            timeStamp.text = message.timeStamp
            userName.text = message.userName
            messageBody.text = message.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.messsage_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindMessage(context, messages[p1])
    }
}