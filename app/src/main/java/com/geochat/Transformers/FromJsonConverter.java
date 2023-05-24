package com.geochat.Transformers;

import com.geochat.model.read_dtos.ChatReadDTO;
import com.geochat.model.Message;
import com.geochat.model.Server;
import com.geochat.model.AuthenticatedUser;
import com.geochat.model.read_dtos.UserReadDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FromJsonConverter {
//    public T convert(String jsonString) {
//        Type type = new TypeToken<T>() {
//        }.getType();
//        return new Gson().fromJson(jsonString, type);
//
////        try {
////            ObjectMapper mapper = new ObjectMapper();
////            return mapper.readValue(jsonString, new TypeReference<T>(){});
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
//    }
    private FromJsonConverter(){}

    public static AuthenticatedUser convertToUser(String jsonString) {
        try {
            JSONObject payload = new JSONObject(jsonString);
            return new AuthenticatedUser(payload.getString("Id"), payload.getString("UserName"),payload.getString("Email"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public static UserReadDTO convertToUserReadDTO(String jsonString) {
        return new Gson().fromJson(jsonString, UserReadDTO.class);
    }

    public static List<UserReadDTO> convertToUserReadDTOList(@Nullable String jsonString) {
        return new Gson().fromJson(jsonString, new TypeToken<List<UserReadDTO>>() {
        }.getType());
    }

    public static Server convertToServer(String jsonString) {
        return new Gson().fromJson(jsonString, Server.class);
    }

    public static ChatReadDTO convertToChat(String jsonString) {
        return new Gson().fromJson(jsonString, ChatReadDTO.class);
    }

    public static Message convertToMessage(String jsonString) {
        return new Gson().fromJson(jsonString, Message.class);
    }

    public static List<ChatReadDTO> convertToChatList(@Nullable String jsonString) {
        return new Gson().fromJson(jsonString, new TypeToken<List<ChatReadDTO>>() {
        }.getType());
    }
}
