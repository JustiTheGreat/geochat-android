package com.geochat.tasks;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.geochat.Transformers.FromJsonConverter;
import com.geochat.ui.fragments.ICallbackContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

public abstract class GeneralisedTask<T> extends AsyncTask<Void, Void, T> implements FallibleTask{
    protected final ICallbackContext callbackContext;
    protected final String authToken;
    protected String errorMessage;

    protected GeneralisedTask(ICallbackContext callbackContext, String authToken) {
        this.callbackContext = callbackContext;
        this.authToken = authToken;
    }

    protected String readResponseBody(HttpsURLConnection connection) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseBody = new StringBuilder();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            responseBody.append(inputLine);
        }
        bufferedReader.close();
        return responseBody.toString();
    }

    @Override
    protected void onPostExecute(T object) {
        if(object == null) callbackContext.timedOut(this);
        else callbackContext.callback(this, object);
    }

    @Nullable
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
