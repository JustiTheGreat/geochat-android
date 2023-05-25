package com.geochat.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.geochat.R
import com.geochat.databinding.RegisterBinding
import com.geochat.model.write_dtos.RegistrationWriteDTO
import com.geochat.tasks.FallibleTask
import com.geochat.tasks.RegisterTask

class Register : UtilityFragment() {
    private var binding: RegisterBinding? = null
    private var username: EditText? = null
    private var email: EditText? = null
    private var password: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterBinding.inflate(inflater, container, false)
        username = binding!!.registerUsernameTB
        email = binding!!.registerEmailTB
        password = binding!!.registerPasswordTB
        binding!!.registerRegisterB.setOnClickListener { register() }
        return binding!!.root
    }

    override fun onStart() {
        super.onStart()
        resetEditTexts()
    }

    private fun resetEditTexts() {
        username!!.setText(getString(R.string.empty))
        email!!.setText(getString(R.string.empty))
        password!!.setText(getString(R.string.empty))
    }

    private fun register() {
        if (username!!.text.toString().isEmpty()) {
            toast(getString(R.string.please_input_your_username))
            return
        }
        if (email!!.text.toString().isEmpty()) {
            toast(getString(R.string.please_input_your_email))
            return
        }
        if (password!!.text.toString().isEmpty()) {
            toast(getString(R.string.please_input_your_password))
            return
        }
        val registrationWriteDTO = RegistrationWriteDTO(
            email!!.text.toString().trim { it <= ' ' },
            username!!.text.toString().trim { it <= ' ' },
            password!!.text.toString().trim { it <= ' ' },
            password!!.text.toString().trim { it <= ' ' })

        disableActivityTouchInput()
        RegisterTask(this, registrationWriteDTO).execute()
    }

    override fun callback(caller: Any?, result: Any?) {
        if (caller is RegisterTask) {
            toast(getString(R.string.register_successful))
            enableActivityTouchInput()
            navigateTo(R.id.action_register_to_login)
        }
    }

    override fun timedOut(caller: Any?) {
        if (caller is FallibleTask) {
            toast(caller.errorMessage)
            enableActivityTouchInput()
        }
    }
}