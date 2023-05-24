package com.geochat.preference_managers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.geochat.R;

public class PreferenceManager {

    public static String getAuthToken(Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString(activity.getString(R.string.authentication_token), null);
    }

    public static void putAuthToken(Activity activity, String authToken) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        sharedPref.edit().putString(activity.getString(R.string.authentication_token), authToken).apply();
    }

    public static boolean authTokenIsAvailable(Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.contains(activity.getString(R.string.authentication_token));
    }

    public static void removeAuthToken(Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(activity.getString(R.string.authentication_token)).apply();
    }
}
