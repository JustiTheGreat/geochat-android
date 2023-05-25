package com.geochat.ui.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geochat.R;
import com.geochat.databinding.ChatBinding;
import com.geochat.model.write_dtos.MessageWriteDTO;
import com.geochat.preference_managers.PreferenceManager;
import com.geochat.tasks.CreateMessageTask;
import com.geochat.tasks.FallibleTask;
import com.geochat.ui.adapters.MessageAdapter;

public class Chat extends UtilityFragment {
    private RecyclerView messages;
    private EditText messageEditText;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ChatBinding binding = ChatBinding.inflate(inflater, container, false);
        messages = binding.messages;
        messageEditText = binding.writeMessage;

        messages.setLayoutManager(new LinearLayoutManager(requireContext()) {{
            setStackFromEnd(false);
            setReverseLayout(true);
        }});
        messages.setAdapter(new MessageAdapter(requireActivity().getApplicationContext(),
                getOpenChat().getMessages(), getAuthenticatedUser()));
        binding.sendMessage.setOnClickListener(view -> {
            String message = messageEditText.getText().toString().trim();
            if (message.isEmpty()) return;
            MessageWriteDTO messageWriteDTO = new MessageWriteDTO(getOpenChat().getId(), getAuthenticatedUser().getId(), message);
            new CreateMessageTask(this, PreferenceManager.getAuthToken(requireActivity()), getCurrentServerUrl(), messageWriteDTO).execute();
            messageEditText.setText(getString(R.string.empty));
        });

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateChatFragment(int chatId) {
        if (chatId == getOpenChat().getId()) {
            messages.getAdapter().notifyDataSetChanged();
            messages.scrollToPosition(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setOpenChat(null);
    }

    @Override
    public void callback(@Nullable Object caller, @Nullable Object result) {
    }

    @Override
    public void timedOut(@Nullable Object caller) {
        if (caller instanceof FallibleTask) {
            toast(((FallibleTask) caller).getErrorMessage());
        }
    }
}
