package com.geochat.Transformers;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

public class JwtTokenDecoder {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getJson(String token){
        String payload = token.split("\\.")[1];
        return new String(Base64.getDecoder().decode(payload));
    }
}
