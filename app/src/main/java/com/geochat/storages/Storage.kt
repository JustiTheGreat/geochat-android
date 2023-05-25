package com.geochat.storages

import com.geochat.location_listeners.GeoChatLocationListener
import com.geochat.model.read_dtos.ChatReadDto
import com.geochat.model.Server
import com.microsoft.signalr.HubConnection

object Storage {
    private var geoChatLocationListener: GeoChatLocationListener? = null
    private var currentServer: Server? = null
    private var hubConnection: HubConnection? = null
    private var userChats: List<ChatReadDto>? = null
    private var openChatReadDto: ChatReadDto? = null

    fun getGeoChatLocationListener(): GeoChatLocationListener? {
        return geoChatLocationListener
    }

    fun setGeoChatLocationListener(geoChatLocationListener: GeoChatLocationListener?) {
        Storage.geoChatLocationListener = geoChatLocationListener
    }

    fun getCurrentServer(): Server? {
        return currentServer
    }

    fun setCurrentServer(currentServer: Server?) {
        Storage.currentServer = currentServer
    }

    fun getHubConnection(): HubConnection? {
        return hubConnection
    }

    fun setHubConnection(hubConnection: HubConnection?) {
        Storage.hubConnection = hubConnection
    }

    fun getUserChats(): List<ChatReadDto>? {
        return userChats
    }

    fun setUserChats(userChats: List<ChatReadDto>?) {
        Storage.userChats = userChats
    }

    fun addUserChat(chat: ChatReadDto) {
        if (userChats != null) userChats = userChats!! + chat
    }

    fun getOpenChat(): ChatReadDto? {
        return openChatReadDto
    }

    fun setOpenChat(openChatReadDto: ChatReadDto?) {
        Storage.openChatReadDto = openChatReadDto
    }
}
