package com.example.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JwtDecoder {
    public static String getSubjectFromJwt(String jwt) {
        if (jwt == null || !jwt.startsWith("Bearer")) {
            throw new IllegalStateException("Token is null");
        }

        var authHeader = jwt.substring(7);
        if (authHeader.isEmpty()) {
            throw new IllegalStateException("Token is not valid");
        }

        String[] parts = authHeader.split("\\.");
        if (parts.length != 3) {
            throw new IllegalStateException("Token is not valid");
        }

        String payload = parts[1];
        byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
        String payloadJson = new String(decodedBytes, StandardCharsets.UTF_8);

        Pattern pattern = Pattern.compile("\"sub\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(payloadJson);

        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1);
    }
}
