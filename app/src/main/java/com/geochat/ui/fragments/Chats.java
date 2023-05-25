package com.geochat.ui.fragments;

import static android.content.Context.LOCATION_SERVICE;
import static com.geochat.ServiceURLs.CHAT_HUB_ROUTE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.geochat.databinding.ConversationsBinding;
import com.geochat.location_listeners.GeoChatLocationListener;
import com.geochat.model.MessageReadDto;
import com.geochat.model.SearchItem;
import com.geochat.model.Server;
import com.geochat.model.read_dtos.ChatReadDto;
import com.geochat.model.read_dtos.UserReadDTO;
import com.geochat.model.write_dtos.ChatWriteDTO;
import com.geochat.tasks.CreateChatTask;
import com.geochat.tasks.FallibleTask;
import com.geochat.tasks.GetFilteredUsersTask;
import com.geochat.tasks.GetServerByCoordinatesTask;
import com.geochat.tasks.GetUserChatsTask;
import com.geochat.ui.activities.MainActivity;
import com.geochat.ui.adapters.ChatPreviewAdapter;
import com.geochat.ui.adapters.SearchItemAdapter;
import com.google.gson.reflect.TypeToken;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Single;
public class Chats extends UtilityFragment {

    private ConversationsBinding binding;
    private RecyclerView chats;
    private AutoCompleteTextView autoCompleteTextView;
    private SearchItem selectedSearchItem;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ConversationsBinding.inflate(inflater, container, false);
        autoCompleteTextView = binding.personSearcher;
        chats = binding.chatPreviews;

        setLogoutVisibility(true);
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
                String pattern = charSequence.toString().trim();
                if (pattern.isEmpty()) return;
                disableActivityTouchInput();
                new GetFilteredUsersTask(Chats.this, getAuthToken(), getCurrentServerUrl(), pattern).execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.joinConversationButton.setOnClickListener(v -> {
            if (selectedSearchItem != null) {
                new CreateChatTask(
                        this,
                        getAuthToken(),
                        getCurrentServerUrl(),
                        new ChatWriteDTO(getAuthenticatedUser().getId(), selectedSearchItem.getUserId())).execute();
            }
        });

        if (getUserChats() != null) {
            chats.setLayoutManager(new LinearLayoutManager(getContext()));
            chats.setAdapter(new ChatPreviewAdapter(requireActivity().getApplicationContext(), this, getUserChats()));
            binding.progressBar.setVisibility(View.GONE);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);
        setGeoChatLocationListener(new GeoChatLocationListener((MainActivity) requireActivity()));
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 1f, getGeoChatLocationListener());
    }

    @Override
    public void onStart() {
        super.onStart();
        autoCompleteTextView.clearFocus();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void callback(@Nullable Object caller, @Nullable Object result) {
        if (caller instanceof GetServerByCoordinatesTask) {
            Server server = (Server) result;
            if (haveCurrentServer() && server.getId() == getCurrentServerId())
                return;
            setCurrentServer(server);
            HubConnection hubConnection = setupHubConnection();
            setHubConnection(hubConnection);
        } else if (caller instanceof GetFilteredUsersTask) {
            List<SearchItem> searchItems = ((List<UserReadDTO>) result).stream().map(user -> new SearchItem(user, Constants.ROMANIA)).collect(Collectors.toList());
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setAdapter(new SearchItemAdapter(requireActivity(), searchItems, requireActivity()));
            enableActivityTouchInput();
        } else if (caller instanceof GetUserChatsTask) {
            binding.progressBar.setVisibility(View.GONE);
            setUserChats((List<ChatReadDto>) result);
            chats.setLayoutManager(new LinearLayoutManager(getContext()));
            chats.setAdapter(new ChatPreviewAdapter(requireActivity().getApplicationContext(), this, getUserChats()));
            enableActivityTouchInput();
        } else if (caller instanceof ChatPreviewAdapter) {
            setOpenChat(Objects.requireNonNull(getUserChats()).get((int) result));
            navigateTo(R.id.action_chats_to_chat);
        }
    }

    @Override
    public void timedOut(@Nullable Object caller) {
        if (caller instanceof FallibleTask) {
            toast(((FallibleTask) caller).getErrorMessage());
            enableActivityTouchInput();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private HubConnection setupHubConnection() {
        HubConnection hubConnection = HubConnectionBuilder.create(getCurrentServerUrl() + CHAT_HUB_ROUTE)
                .withAccessTokenProvider(Single.defer(() -> Single.just(Objects.requireNonNull(getAuthToken()))))
                .build();

        hubConnection.<MessageReadDto>on("MessageReceived", message -> requireActivity().runOnUiThread(() -> {
            if (getCurrentFragment() instanceof Chat) {
                addMessageToChat(message);
                ((Chat) getCurrentFragment()).updateChatFragment(message.getChatId());
            } else if (getCurrentFragment() instanceof Chats) {
                addMessageToChat(message);
                chats.setAdapter(new ChatPreviewAdapter(requireActivity().getApplicationContext(), this, getUserChats()));
            }
        }), new TypeToken<MessageReadDto>() {
        }.getType());


        hubConnection.<ChatReadDto>on("ChatCreated", chat -> requireActivity().runOnUiThread(() -> {
            if (getCurrentFragment() instanceof Chats || getCurrentFragment() instanceof Chat) {
                addUserChat(chat);
                chats.setAdapter(new ChatPreviewAdapter(requireActivity().getApplicationContext(), this, getUserChats()));
            }
        }), new TypeToken<ChatReadDto>() {
        }.getType());

        hubConnection.onClosed(exception -> {
                    toast(exception.toString());
                    hubConnection.start();
                }
        );

        hubConnection.start().doOnComplete(() -> new GetUserChatsTask(this,
                getAuthToken(), getCurrentServerUrl()).execute()).blockingAwait();

        return hubConnection;
    }

    private void addMessageToChat(MessageReadDto message) {
        for (ChatReadDto chat : Objects.requireNonNull(getUserChats())) {
            if (chat.getId() == message.getChatId()) {
                chat.addMessage(message);
                break;
            }
        }
    }
}
