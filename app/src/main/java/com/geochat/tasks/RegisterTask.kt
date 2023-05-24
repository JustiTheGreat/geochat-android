package com.geochat.tasks

import androidx.fragment.app.Fragment
import com.geochat.R
import com.geochat.ServiceURLs
import com.geochat.Transformers.FromJsonConverter
import com.geochat.model.read_dtos.UserReadDTO
import com.geochat.model.write_dtos.RegistrationWriteDTO
import com.geochat.ui.fragments.ICallbackContext
import com.google.gson.Gson
import java.io.DataOutputStream
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RegisterTask(callbackContext: ICallbackContext?, private val registrationWriteDTO: RegistrationWriteDTO)
    : GeneralisedTask<UserReadDTO?>(callbackContext, null) {
    override fun doInBackground(vararg p0: Void): UserReadDTO? {
        try {
            val connection = URL(ServiceURLs.REGISTER).openConnection() as HttpsURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.doInput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "*/*")

            val requestBody = Gson().toJson(registrationWriteDTO)
            val dataOutputStream = DataOutputStream(connection.outputStream)
            dataOutputStream.writeBytes(requestBody)
            dataOutputStream.flush()
            dataOutputStream.close()

            when (connection.responseCode) {
                200 -> {
                    return UserReadDTO();
                }

                400 -> {
                    errorMessage =
                        (callbackContext as Fragment).getString(R.string.wrong_credentials)
                    return null
                }

                500 -> {
                    errorMessage =
                        (callbackContext as Fragment).getString(R.string.internal_server_error)
                    return null
                }

                else -> {
                    errorMessage =
                        (callbackContext as Fragment).getString(R.string.unexpected_server_response)
                    return null
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            errorMessage = (callbackContext as Fragment).getString(R.string.connection_problems)
            return null
        }
    }
}