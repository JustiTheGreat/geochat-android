package com.geochat.ui.fragments;

import static android.content.Context.LOCATION_SERVICE;

import static com.geochat.ServiceURLs.CHAT_HUB_ROUTE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geochat.Constants;
import com.geochat.R;
import com.geochat.ServiceURLs;
import com.geochat.Transformers.FromJsonConverter;
import com.geochat.databinding.ConversationsBinding;
import com.geochat.location_listeners.GeoChatLocationListener;
import com.geochat.model.Message;
import com.geochat.model.read_dtos.ChatReadDTO;
import com.geochat.model.SearchItem;
import com.geochat.model.Server;
import com.geochat.model.read_dtos.UserReadDTO;
import com.geochat.model.write_dtos.ChatWriteDTO;
import com.geochat.preference_managers.PreferenceManager;
import com.geochat.storages.Storage;
import com.geochat.tasks.CreateChatTask;
import com.geochat.tasks.FallibleTask;
import com.geochat.tasks.GetAllUsersTask;
import com.geochat.tasks.GetServerByCoordinatesTask;
import com.geochat.tasks.GetUserConversationsTask;
import com.geochat.ui.activities.MainActivity;
import com.geochat.ui.adapters.ChatPreviewAdapter;
import com.geochat.ui.adapters.SearchItemAdapter;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.OnClosedCallback;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Single;

public class Chats extends UtilityFragment {
    private static List<ChatReadDTO> userChats;
    private List<SearchItem> searchItems;
    private SearchItem selectedSearchItem;
    private RecyclerView recyclerView;
    private AutoCompleteTextView autoCompleteTextView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ConversationsBinding binding = ConversationsBinding.inflate(inflater, container, false);
        autoCompleteTextView = binding.personSearcher;
        recyclerView = binding.chatPreviews;

        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        autoCompleteTextView.setOnTouchListener((View v, MotionEvent event) -> {
            autoCompleteTextView.setText(getString(R.string.empty));
            return false;
        });

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) ->
                selectedSearchItem = (SearchItem) autoCompleteTextView.getAdapter().getItem(position));

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                disableActivityTouchInput();
                new GetAllUsersTask(Chats.this, PreferenceManager.getAuthToken(requireActivity()), getCurrentServerUrl(), charSequence.toString()).execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.joinConversationButton.setOnClickListener(v -> {
            if (selectedSearchItem != null)
                new CreateChatTask(
                        this,
                        PreferenceManager.getAuthToken(requireActivity()),
                        getCurrentServerUrl(),
                        new ChatWriteDTO(getAuthenticatedUser().getId(), selectedSearchItem.getUserId())).execute();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(userChats!=null){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new ChatPreviewAdapter(requireActivity().getApplicationContext(), this, userChats));
        }

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);
        setGeoChatLocationListener(new GeoChatLocationListener((MainActivity) requireActivity()));
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000L,
                1f,
                getGeoChatLocationListener()
        );

//        if (haveCurrentServer()) {
//            disableActivityTouchInput();
//            new GetUserConversationsTask(this, PreferenceManager.getAuthToken(requireActivity()), getCurrentServerUrl()).execute();
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void callback(@Nullable Object caller, @Nullable Object result) {
        if (caller instanceof GetServerByCoordinatesTask) {

            Server server = (Server) result;
            if (haveCurrentServer() && server.getId() == getCurrentServerId())
                return;
            setCurrentServer(server);

            HubConnection hubConnection = HubConnectionBuilder.create(getCurrentServerUrl() + CHAT_HUB_ROUTE)
                    .withAccessTokenProvider(Single.defer(() -> Single.just(PreferenceManager.getAuthToken(requireActivity()))))
                    .build();
            hubConnection.on("MessageReceived", (message) -> {
                toast(message.getContent());
                if (getCurrentFragment() instanceof Chat) {
                    ((Chat) getCurrentFragment()).updateChat(message);
                    addMessageToChat(message);
                }else if (getCurrentFragment() instanceof Chats) {
                    addMessageToChat(message);
                    setRecyclerViewAdapter();
                }

            }, Message.class);
            hubConnection.<ChatReadDTO>on("ChatCreated", (chat) -> {
                toast(chat.getChatName());
                if (getCurrentFragment() instanceof Chats) {
//                    ChatReadDTO c = FromJsonConverter.convertToChat(chat);
                    userChats.add(chat);
                    setRecyclerViewAdapter();
                } else if (getCurrentFragment() instanceof Chat) {
                    userChats.add(chat);
                    setRecyclerViewAdapter();
                }
            }, ChatReadDTO.class);
            hubConnection.onClosed(exception -> {
                toast(exception.getMessage());
                hubConnection.start();
                    }
            );
            setHubConnection(hubConnection);
            hubConnection.start().doOnComplete(()-> new GetUserConversationsTask(this, PreferenceManager.getAuthToken(requireActivity()), getCurrentServerUrl()).execute()).blockingAwait();
//            toast(((Server) result).getUrl());
        } else if (caller instanceof GetAllUsersTask) {
            searchItems = ((List<UserReadDTO>) result).stream().map(user -> new SearchItem(user, Constants.ROMANIA)).collect(Collectors.toList());
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setAdapter(new SearchItemAdapter(requireActivity(), searchItems, requireActivity()));
            enableActivityTouchInput();
        } else if (caller instanceof GetUserConversationsTask) {
            userChats = (List<ChatReadDTO>) result;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            setRecyclerViewAdapter();
            enableActivityTouchInput();
        } else if (caller instanceof ChatPreviewAdapter) {
            setOpenChat(userChats.get((int) result));
            navigateTo(R.id.action_chats_to_chat);
        }
    }

    private void addMessageToChat(Message message){
        for (ChatReadDTO chat: userChats) {
            if(chat.getId() == message.getChatId()){
                chat.addMessage(message);
                break;
            }
        }
    }

    private void setRecyclerViewAdapter() {
        recyclerView.setAdapter(new ChatPreviewAdapter(requireActivity().getApplicationContext(), this, userChats));
    }

    @Override
    public void timedOut(@Nullable Object caller) {
        if (caller instanceof FallibleTask) {
            toast(((FallibleTask) caller).getErrorMessage());
        }
    }
}
