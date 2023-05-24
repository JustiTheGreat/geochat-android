package com.geochat.ui.view_holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geochat.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private final LinearLayout messageContainer;
    private final TextView senderUsernameLeft;
    private final TextView senderUsernameRight;
    private final TextView content;
    private final TextView timeSent;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        messageContainer = itemView.findViewById(R.id.messageContainer);
        senderUsernameLeft = itemView.findViewById(R.id.usernameLeft);
        senderUsernameRight = itemView.findViewById(R.id.usernameRight);
        content = itemView.findViewById(R.id.messageContent);
        timeSent = itemView.findViewById(R.id.timeSent);
    }

    public LinearLayout getMessageContainer() {
        return messageContainer;
    }

    public TextView getSenderUsernameLeft() {
        return senderUsernameLeft;
    }

    public TextView getSenderUsernameRight() {
        return senderUsernameRight;
    }

    public TextView getContent() {
        return content;
    }

    public TextView getTimeSent() {
        return timeSent;
    }
}
