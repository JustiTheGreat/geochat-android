package com.geochat.tasks;

import androidx.fragment.app.Fragment;

import com.geochat.R;
import com.geochat.ServiceURLs;
import com.geochat.model.write_dtos.ChatWriteDTO;
import com.geochat.ui.fragments.ICallbackContext;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CreateChatTask extends GeneralisedTask<String>{
    private final String serverUrl;
    private final ChatWriteDTO chatWriteDTO;

    public CreateChatTask(ICallbackContext callbackContext, String authToken, String serverUrl, ChatWriteDTO chatWriteDTO) {
        super(callbackContext, authToken);
        this.serverUrl = serverUrl;
        this.chatWriteDTO = chatWriteDTO;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String url = serverUrl + ServiceURLs.CREATE_CHAT_ROUTE;
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(false);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);

            String requestBody = new Gson().toJson(chatWriteDTO);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(requestBody);
            dataOutputStream.flush();
            dataOutputStream.close();

            switch (connection.getResponseCode()) {
                case 200:
                    return "";
                case 401:
                    errorMessage = ((Fragment) callbackContext).getString(R.string.unauthorized_access);
                    return null;
                case 500:
                    errorMessage = ((Fragment) callbackContext).getString(R.string.internal_server_error);
                    return null;
                default:
                    errorMessage = ((Fragment) callbackContext).getString(R.string.unexpected_server_response);
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = ((Fragment) callbackContext).getString(R.string.connection_problems);
            return null;
        }
    }
}
