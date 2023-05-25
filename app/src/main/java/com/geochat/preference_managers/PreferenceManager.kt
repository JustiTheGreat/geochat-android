package com.geochat.preference_managers

import android.app.Activity
import android.content.Context
import com.geochat.R

object PreferenceManager {
    @JvmStatic
    fun getAuthToken(activity: Activity): String? {
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPreferences.getString(activity.getString(R.string.authentication_token), null)
    }

    fun putAuthToken(activity: Activity, authToken: String?) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        sharedPref.edit().putString(activity.getString(R.string.authentication_token), authToken)
            .apply()
    }

    @JvmStatic
    fun authTokenIsAvailable(activity: Activity): Boolean {
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPreferences.contains(activity.getString(R.string.authentication_token))
    }

    fun removeAuthToken(activity: Activity) {
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(activity.getString(R.string.authentication_token)).apply()
    }
}