package com.geochat

interface ServiceURLs {
    companion object {
        const val LOGIN = "https://geochatidentity.azurewebsites.net/api/Auth/login"
        const val REGISTER = "https://geochatidentity.azurewebsites.net/api/Auth/register"
        const val GET_SERVER_BY_COORDINATES = "https://geochatgeolocation.azurewebsites.net/api/Server/{latitude}/{longitude}"
        const val GET_ALL_USERS_ROUTE = "api/Users/name/{pattern}"
        const val GET_USER_CONVERSATIONS_ROUTE = "api/Chat"
        const val CREATE_CHAT_ROUTE = "api/Chat"
        const val CREATE_MESSAGE_ROUTE = "api/Messages"
        const val CHAT_HUB_ROUTE = "chatHub"
    }
}