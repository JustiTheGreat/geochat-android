package com.geochat.ui.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.geochat.R;
import com.geochat.model.read_dtos.ChatReadDto;
import com.geochat.ui.fragments.ICallbackContext;
import com.geochat.ui.view_holders.ChatPreviewViewHolder;

import java.util.List;

public class ChatPreviewAdapter extends RecyclerView.Adapter<ChatPreviewViewHolder> {
    private final Context context;
    private final ICallbackContext callbackContext;
    private final List<ChatReadDto> chats;

    public ChatPreviewAdapter(Context context, ICallbackContext callbackContext, List<ChatReadDto> chats) {
        this.context = context;
        this.callbackContext = callbackContext;
        this.chats = chats;
    }

    @NonNull
    @Override
    public ChatPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_preview, parent, false);
        return new ChatPreviewViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onBindViewHolder(@NonNull ChatPreviewViewHolder viewHolder, int position) {
        viewHolder.getChatName().setText(chats.get(position).getChatName());
        viewHolder.getLastChatMessage().setText(chats.get(position).getLastMessageText());
        viewHolder.getChatType().setImageResource(chats.get(position).getLocationId() == null ? R.drawable.user : R.drawable.users_group);
        viewHolder.getChatFlag().setImageResource(R.drawable.romania_flag);
        viewHolder.itemView.setOnClickListener(v -> callbackContext.callback(this, position));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
