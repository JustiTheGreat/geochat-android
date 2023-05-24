package com.geochat.tasks

import androidx.fragment.app.Fragment
import com.geochat.R
import com.geochat.ServiceURLs
import com.geochat.Transformers.FromJsonConverter
import com.geochat.model.read_dtos.ChatReadDto
import com.geochat.ui.fragments.ICallbackContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class GetUserConversationsTask(callbackContext: ICallbackContext?, authToken: String?, private val serverUrl: String?)
    : GeneralisedTask<List<ChatReadDto?>?>(callbackContext, authToken) {

    override fun doInBackground(vararg p0: Void): List<ChatReadDto?>? {
        return try {
            val url = serverUrl + ServiceURLs.GET_USER_CONVERSATIONS_ROUTE
            val connection = URL(url).openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"
            connection.doOutput = false
            connection.doInput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "*/*")
            connection.setRequestProperty("Authorization", "Bearer $authToken")

            when (connection.responseCode) {
                200 -> {
                    FromJsonConverter.convertToChatList(readResponseBody(connection))
                }

                500 -> {
                    errorMessage =
                        (callbackContext as Fragment).getString(R.string.internal_server_error)
                    null
                }

                else -> {
                    errorMessage =
                        (callbackContext as Fragment).getString(R.string.unexpected_server_response)
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = (callbackContext as Fragment).getString(R.string.connection_problems)
            null
        }
    }
}