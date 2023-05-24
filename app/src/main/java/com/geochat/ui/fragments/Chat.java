package com.geochat.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geochat.R;
import com.geochat.databinding.ChatBinding;
import com.geochat.model.MessageReadDto;
import com.geochat.model.read_dtos.ChatReadDto;
import com.geochat.model.write_dtos.MessageWriteDTO;
import com.geochat.preference_managers.PreferenceManager;
import com.geochat.tasks.CreateMessageTask;
import com.geochat.tasks.FallibleTask;
import com.geochat.ui.adapters.MessageAdapter;

public class Chat extends UtilityFragment {
    private RecyclerView messages;
    private EditText messageEditText;
    private Button sendMessage;
    private ChatReadDto chat;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ChatBinding binding = ChatBinding.inflate(inflater, container, false);

        messages = binding.messages;
        messageEditText = binding.writeMessage;
        sendMessage = binding.sendMessage;

        chat = getOpenChat();

        messages.setLayoutManager(
                new LinearLayoutManager(requireContext())
                {{
                    setStackFromEnd(false);
                    setReverseLayout(true);
                }}
        );
        messages.setAdapter(new MessageAdapter(requireActivity().getApplicationContext(), chat.getMessages(), getAuthenticatedUser()));


        messageEditText.setOnClickListener(view -> messageEditText.setText(R.string.empty));
        sendMessage.setOnClickListener(view -> {
            MessageWriteDTO messageWriteDTO = new MessageWriteDTO(
                    chat.getId(),
                    getAuthenticatedUser().getId(),
                    messageEditText.getText().toString());
            new CreateMessageTask(this, PreferenceManager.getAuthToken(requireActivity()), getCurrentServerUrl(), messageWriteDTO).execute();
            messageEditText.setText(getString(R.string.empty));
        });
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notifyDataSetChangedIfPossible(MessageReadDto message){
//        chat.addMessage(message);
        if(message.getChatId() == chat.getId())
            messages.setAdapter(new MessageAdapter(requireActivity().getApplicationContext(), chat.getMessages(), getAuthenticatedUser()));

//            messages.getAdapter().notifyItemInserted(0);
//        messages.getLayoutManager().scrollToPosition(0);
//        Collections.reverse(chat);
//        List<MessageReadDto> listOfMessages = chat.getMessages().stream().sorted((m1, m2) -> {
//            try {
//                String formattedDate1 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(
//                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
//                                .parse(m1.getTimeSent()));
//                Date date1 = new Date(formattedDate1);
//
//                String formattedDate2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(
//                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
//                                .parse(m1.getTimeSent()));
//                Date date2 = new Date(formattedDate1);
//                return date1.compareTo(date2);
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
//
//        }).collect(Collectors.toList());
//        chat.getMessages().clear();
//        chat.getMessages().addAll(listOfMessages);

//        messages.setLayoutManager(
//                new LinearLayoutManager(requireContext()){{
////                    setStackFromEnd(false);
//                    setReverseLayout(true);
//                }}
//        );
//        synchronized (messages) {
//            messages.notify();
//            messages.
//        }
        messages.setAdapter(new MessageAdapter(requireActivity().getApplicationContext(), chat.getMessages(), getAuthenticatedUser()));
    }

    @Override
    public void callback(@Nullable Object caller, @Nullable Object result) {
        if (caller instanceof CreateMessageTask) {
        }
    }

    @Override
    public void timedOut(@Nullable Object caller) {
        if (caller instanceof FallibleTask) {
            toast(((FallibleTask) caller).getErrorMessage());
        }
    }
}
