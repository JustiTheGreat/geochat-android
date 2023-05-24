package com.geochat.ui.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geochat.R;

public class ChatPreviewViewHolder extends RecyclerView.ViewHolder {
    private final ImageView chatType;
    private final ImageView chatFlag;
    private final TextView chatName;
    private final TextView lastChatMessage;

    public ChatPreviewViewHolder(@NonNull View itemView) {
        super(itemView);
        chatType = itemView.findViewById(R.id.chat_type);
        chatFlag = itemView.findViewById(R.id.chat_flag);
        chatName = itemView.findViewById(R.id.chat_name);
        lastChatMessage = itemView.findViewById(R.id.last_chat_message);
    }

    public ImageView getChatType() {
        return chatType;
    }

    public ImageView getChatFlag() {
        return chatFlag;
    }

    public TextView getChatName() {
        return chatName;
    }

    public TextView getLastChatMessage() {
        return lastChatMessage;
    }
}
