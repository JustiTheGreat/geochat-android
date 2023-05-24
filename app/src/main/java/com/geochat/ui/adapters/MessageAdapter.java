package com.geochat.ui.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geochat.R;
import com.geochat.model.AuthenticatedUser;
import com.geochat.model.MessageReadDto;
import com.geochat.ui.view_holders.MessageViewHolder;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private final Context context;
    private final List<MessageReadDto> messages;
    private final AuthenticatedUser authenticatedUser;

    public MessageAdapter(Context context, List<MessageReadDto> messages, AuthenticatedUser authenticatedUser) {
        this.context = context;
        this.messages = messages;
        this.authenticatedUser = authenticatedUser;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder viewHolder, int position) {
        if (messages.get(position).getUserId().equals(authenticatedUser.getId())) {
            viewHolder.getMessageContainer().setGravity(Gravity.LEFT);
            viewHolder.getSenderUsernameLeft().setVisibility(View.VISIBLE);
            viewHolder.getSenderUsernameRight().setVisibility(View.GONE);
            viewHolder.getSenderUsernameLeft().setText(messages.get(position).getUsername());
        } else {
            viewHolder.getMessageContainer().setGravity(Gravity.RIGHT);
            viewHolder.getSenderUsernameLeft().setVisibility(View.GONE);
            viewHolder.getSenderUsernameRight().setVisibility(View.VISIBLE);
            viewHolder.getSenderUsernameRight().setText(messages.get(position).getUsername());
        }
        viewHolder.getContent().setText(messages.get(position).getContent());
        viewHolder.getTimeSent().setText(messages.get(position).getTimeSent());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}

