package com.geochat.ui.fragments

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.geochat.Transformers.FromJsonConverter
import com.geochat.Transformers.JwtTokenDecoder
import com.geochat.location_listeners.GeoChatLocationListener
import com.geochat.model.Server
import com.geochat.model.AuthenticatedUser
import com.geochat.model.read_dtos.ChatReadDto
import com.geochat.preference_managers.PreferenceManager
import com.geochat.storages.Storage
import com.geochat.ui.activities.MainActivity
import com.microsoft.signalr.HubConnection

abstract class UtilityFragment : Fragment(), ICallbackContext {

    fun navigateTo(rId: Int) {
        NavHostFragment.findNavController(this).navigate(rId)
    }

    protected fun setLogoutVisibility(visibility: Boolean) {
        (requireActivity() as MainActivity).setLogoutVisibility(visibility)
    }

    protected fun toast(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    protected fun getAuthenticatedUser(): AuthenticatedUser {
        return FromJsonConverter.convertToUser(
            JwtTokenDecoder.getJson(
                PreferenceManager.getAuthToken(
                    requireActivity()
                )
            )
        )
    }

    private fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        }
    }

    private fun setupUI(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                activity?.let { hideSoftKeyboard(it) }
                false
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI(view)
    }

    protected fun getCurrentFragment(): UtilityFragment{
        return (requireActivity() as MainActivity).getCurrentFragment();
    }

    protected fun getGeoChatLocationListener(): GeoChatLocationListener?{
        return Storage.getGeoChatLocationListener()
    }

    protected fun setGeoChatLocationListener(geoChatLocationListener: GeoChatLocationListener) {
        Storage.setGeoChatLocationListener(geoChatLocationListener)
    }

    protected fun getHubConnection(): HubConnection?{
        return Storage.getHubConnection()
    }

    protected fun setHubConnection(hubConnection: HubConnection) {
        Storage.setHubConnection(hubConnection)
    }

    protected fun haveCurrentServer(): Boolean {
        return Storage.getCurrentServer() != null
    }

    protected fun getCurrentServerId(): Int? {
        return Storage.getCurrentServer()?.id
    }

    protected fun getCurrentServerUrl(): String? {
        return Storage.getCurrentServer()?.url
    }

    protected fun setCurrentServer(server: Server) {
        Storage.setCurrentServer(server)
    }

    protected fun setOpenChat(chat: ChatReadDto) {
        Storage.setOpenChat(chat)
    }

    protected fun getOpenChat(): ChatReadDto? {
        return Storage.getOpenChat()
    }

    protected fun enableActivityTouchInput() {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    protected fun disableActivityTouchInput() {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }
}