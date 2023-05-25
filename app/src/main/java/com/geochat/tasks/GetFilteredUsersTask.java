package com.geochat.tasks;

import androidx.fragment.app.Fragment;

import com.geochat.R;
import com.geochat.ServiceURLs;
import com.geochat.Transformers.FromJsonConverter;
import com.geochat.model.read_dtos.UserReadDTO;
import com.geochat.ui.fragments.ICallbackContext;

import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class GetFilteredUsersTask extends GeneralisedTask<List<UserReadDTO>> {
    private final String serverUrl;
    private final String pattern;

    public GetFilteredUsersTask(ICallbackContext callbackContext, String authToken, String serverUrl, String pattern) {
        super(callbackContext, authToken);
        this.serverUrl = serverUrl;
        this.pattern = pattern;
    }

    @Override
    protected List<UserReadDTO> doInBackground(Void... voids) {
        try {
            String url = serverUrl + ServiceURLs.GET_ALL_USERS_ROUTE.replace("{pattern}", pattern);
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);

            switch (connection.getResponseCode()) {
                case 200:
                    return FromJsonConverter.convertToUserReadDTOList(readResponseBody(connection));
                case 404:
                    errorMessage = ((Fragment) callbackContext).getString(R.string.not_found);
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
