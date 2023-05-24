package com.geochat.storages

import com.geochat.location_listeners.GeoChatLocationListener
import com.geochat.model.read_dtos.ChatReadDTO
import com.geochat.model.Server
import com.microsoft.signalr.HubConnection

object Storage {
    private var geoChatLocationListener: GeoChatLocationListener? = null
    private var hubConnection: HubConnection? = null
    private var currentServer: Server? = null
    private var openChatReadDTO: ChatReadDTO? = null

    fun getGeoChatLocationListener(): GeoChatLocationListener? {
        return geoChatLocationListener
    }

    fun setGeoChatLocationListener(geoChatLocationListener: GeoChatLocationListener?) {
        Storage.geoChatLocationListener = geoChatLocationListener
    }

    fun getHubConnection(): HubConnection? {
        return hubConnection
    }

    fun setHubConnection(hubConnection: HubConnection?) {
        Storage.hubConnection = hubConnection
    }

    fun getCurrentServer(): Server? {
        return currentServer
    }

    fun setCurrentServer(currentServer: Server?) {
        Storage.currentServer = currentServer
    }

    fun getOpenChat(): ChatReadDTO? {
        return openChatReadDTO
    }

    fun setOpenChat(openChatReadDTO: ChatReadDTO?) {
        Storage.openChatReadDTO = openChatReadDTO
    }
}
