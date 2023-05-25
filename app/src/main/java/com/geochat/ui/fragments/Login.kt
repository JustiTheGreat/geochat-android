package com.geochat.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.geochat.R
import com.geochat.databinding.LoginBinding
import com.geochat.model.write_dtos.AuthenticationWriteDTO
import com.geochat.tasks.FallibleTask
import com.geochat.tasks.LoginTask

class Login : UtilityFragment() {
    private var binding: LoginBinding? = null
    private var user: EditText? = null
    private var password: EditText? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setLogoutVisibility(false)

        binding = LoginBinding.inflate(inflater, container, false)
        user = binding!!.loginUserTB
        password = binding!!.loginPasswordTB
        binding!!.loginLoginB.setOnClickListener { login() }
        binding!!.loginRegisterLinkTV.setOnClickListener { v: View? -> navigateTo(R.id.action_login_to_register) }
        return binding!!.root
    }

    override fun onStart() {
        super.onStart()
        resetEditTexts()
        if (authTokenIsAvailable()) {
            navigateTo(R.id.action_login_to_chats)
        }
    }

    private fun resetEditTexts() {
        user!!.setText(getString(R.string.empty))
        password!!.setText(getString(R.string.empty))
    }

    private fun login() {
        if (user!!.text.toString().trim { it <= ' ' }.isEmpty()) {
            toast(getString(R.string.please_input_your_username_or_email))
            return
        }
        if (password!!.text.toString().trim { it <= ' ' }.isEmpty()) {
            toast(getString(R.string.please_input_your_password))
            return
        }
        val authenticationWriteDTO = AuthenticationWriteDTO(user!!.text.toString().trim { it <= ' ' },
            password!!.text.toString().trim { it <= ' ' })

        disableActivityTouchInput()
        LoginTask(this, authenticationWriteDTO).execute()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun callback(caller: Any?, result: Any?) {
        if (caller is LoginTask) {
            putAuthToken(result as String)
            enableActivityTouchInput()
            navigateTo(R.id.action_login_to_chats)
        }
    }

    override fun timedOut(caller: Any?) {
        if (caller is FallibleTask) {
            toast(caller.errorMessage)
            enableActivityTouchInput()
        }
    }
}