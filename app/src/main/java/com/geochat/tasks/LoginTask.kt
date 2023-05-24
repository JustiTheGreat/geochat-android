package com.geochat.tasks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.geochat.R
import com.geochat.ServiceURLs
import com.geochat.model.write_dtos.AuthenticationWriteDTO
import com.geochat.ui.fragments.ICallbackContext
import com.google.gson.Gson
import java.io.DataOutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class LoginTask(callbackContext: ICallbackContext?, private val authenticationWriteDTO: AuthenticationWriteDTO)
    : GeneralisedTask<String>(callbackContext, null) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doInBackground(vararg p0: Void?): String? {
        return try {
            val connection = URL(ServiceURLs.LOGIN).openConnection() as HttpsURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.doInput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "*/*")

            val requestBody = Gson().toJson(authenticationWriteDTO)
            val dataOutputStream = DataOutputStream(connection.outputStream)
            dataOutputStream.writeBytes(requestBody)
            dataOutputStream.flush()
            dataOutputStream.close()

            when (connection.responseCode) {
                200 -> {
                    readResponseBody(connection)
                }

                400 -> {
                    errorMessage =
                        (callbackContext as Fragment).getString(R.string.wrong_credentials)
                    null
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
            return null
        }
    }
}