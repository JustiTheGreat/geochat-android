package com.geochat.tasks;

import androidx.fragment.app.Fragment;

import com.geochat.R;
import com.geochat.ServiceURLs;
import com.geochat.Transformers.FromJsonConverter;
import com.geochat.model.Server;
import com.geochat.ui.fragments.ICallbackContext;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetServerByCoordinatesTask extends GeneralisedTask<Server> {
    private final double latitude;
    private final double longitude;

    public GetServerByCoordinatesTask(ICallbackContext callbackContext, String authToken, double latitude, double longitude) {
        super(callbackContext, authToken);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected Server doInBackground(Void... voids) {
        try {
            String url = ServiceURLs.GET_SERVER_BY_COORDINATES
                    .replace("{latitude}", ""+latitude)
                    .replace("{longitude}", ""+longitude);
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);

            switch (connection.getResponseCode()) {
                case 200:
                    return FromJsonConverter.convertToServer(readResponseBody(connection));
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
